package com.github.dev001hajipro.java8sandbox.util;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * ZIP関数作成
 * Created by dev001 on 2017/02/27.
 */
public class StreamUtil {
    public static <T, U> Stream<Pair<T, U>> zip(Stream<T> s1, Stream<U> t1, int size) {

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
