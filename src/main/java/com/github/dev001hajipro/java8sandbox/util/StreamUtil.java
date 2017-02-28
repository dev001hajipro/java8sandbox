package com.github.dev001hajipro.java8sandbox.util;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * 一般的な関数型プログラミング言語にある便利な関数。zip, takeWhile, dropWhile
 * Created by dev001 on 2017/02/27.
 *
 * @link http://enterprisegeeks.hatenablog.com/entry/2014/09/08/091000
 */
public class StreamUtil {


    /**
     * 手続き型プログラミング言語のif(test) break;と同じ。
     * filter関数は、一致する要素をフィルターするだけで、停止する機能はない。
     * 関数型プログラミングでは、無限の値を扱える。そしてJava8でも無限ストリームを
     * Stream.iterator(0, n->n+1)のように作れるので、このようなストリームで
     * .limitで上限を決められない場合に使う。
     *
     * @param s   入力ストリーム
     * @param p   テスト
     * @param <T> 任意の型
     * @return 一致しない場合は停止するストリーム
     */
    public static <T> Stream<T> createTakeWhile(Stream<T> s, Predicate<T> p) {
        /*
         * Streamは内部にSpliterator(Split-Iteratorの造語)を持ち、ここに情報がある
         */

        Spliterator<T> splt = s.spliterator();
        int characteristics = splt.characteristics();
        Iterator<T> baseIt = Spliterators.iterator(splt);
        Iterator<T> it = new Iterator<T>() {
            T element;
            boolean end;

            @Override
            public boolean hasNext() {
                if (!baseIt.hasNext() || end) {
                    return false;
                }
                element = baseIt.next();
                if (p.test(element)) {
                    return true;
                }
                // テストに一致しなかったら次は読み込まないようにフラグを立てる
                end = true;
                return true;
            }

            @Override
            public T next() {
                return element;
            }
        };

        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, characteristics), false);
    }

    /**
     * 述語(Predicate)がtrueの間は、要素を読み飛ばす
     * 手続き型プログラミング言語のif (Predicate()==true) continue;と同じ。
     *
     * @param s   入力ストリーム
     * @param p   テスト
     * @param <T> 任意の型
     * @return 一致しない場合は停止するストリーム
     */
    public static <T> Stream<T> createDropWhile(Stream<T> s, Predicate<T> p) {
        /*
         * Streamは内部にSpliterator(Split-Iteratorの造語)を持ち、ここに情報がある
         */

        Spliterator<T> splt = s.spliterator();
        int characteristics = splt.characteristics();
        Iterator<T> baseIt = Spliterators.iterator(splt);
        Iterator<T> it = new Iterator<T>() {
            T element;
            boolean start;

            @Override
            public boolean hasNext() {
                if (!baseIt.hasNext()) {
                    return false;
                }
                element = baseIt.next();

                // 初回は述語を評価して、それがfalseになるまで要素を読み飛ばす。
                // 述語が成立しなくなった時(=false)まだ、ストリームに要素がある場合は
                // 通常のhasNextと同じ動きになる。
                // 述語が成立しなくなるまで読み飛ばす。
                if (!start) {
                    while (p.test(element) && baseIt.hasNext()) {
                        element = baseIt.next();
                    }
                    if (baseIt.hasNext()) {
                        start = true;
                    } else {
                        return false;
                    }
                }

                return true;
            }

            @Override
            public T next() {
                return element;
            }
        };

        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, characteristics), false);
    }

    /**
     * 2つのストリームの各要素をペアにして１つのストリームを返す。
     *
     * @param s1   ストリーム
     * @param t1   ストリーム
     * @param size サイズ
     * @param <T>  任意の型
     * @param <U>  任意の型
     * @return Pairを要素にしたストリーム
     */
    public static <T, U> Stream<Pair<T, U>> zip(Stream<T> s1, Stream<U> t1, int size) {
        /* Java8で独自Streamの作り方
         * 1.Iteratorを継承したものを用意する
         * 2.Spliterator.spliteratorと用意したIteratorで、Spliteratorクラスを作成
         * 3.StreamSupport#streamとSpliteratorクラスで、Streamを作成
         */

        /*
         * Pair p = new Pair(1, 2);で、インスタンス生成ができる。つまりPair::newは
         * 型引数T=Integer=1,型引数U=Integer=2をとり、
         * 戻り値R=Pairオブジェクト
         * のBiFunction(T,U,R)と同等と考えることができる。
         */
        PairIterator<T, U, Pair<T, U>> it = new PairIterator<>(s1.iterator(), t1.iterator(), Pair::new);
        int characteristics = Spliterator.IMMUTABLE | Spliterator.NONNULL;

        if (size < 0) {
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, characteristics), false);
        }
        return StreamSupport.stream(Spliterators.spliterator(it, size, characteristics), false);
    }

    public static <T, U> Stream<Pair<T, U>> zip(Stream<T> s1, Stream<U> t1) {
        return zip(s1, t1, -1);
    }

    private static class PairIterator<T, U, R> implements Iterator<R> {
        private final Iterator<T> it1;
        private final Iterator<U> it2;
        // TとUを引数に、Rを返す関数。
        private final BiFunction<T, U, R> mapper;

        PairIterator(Iterator<T> it1, Iterator<U> it2, BiFunction<T, U, R> mapper) {
            this.it1 = it1;
            this.it2 = it2;
            this.mapper = mapper;
        }

        @Override
        public boolean hasNext() {
            return it1.hasNext() && it2.hasNext();
        }

        @Override
        public R next() {
            return mapper.apply(it1.next(), it2.next());
        }
    }
}
