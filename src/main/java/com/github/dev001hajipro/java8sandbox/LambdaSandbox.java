package com.github.dev001hajipro.java8sandbox;

import java.util.stream.IntStream;

/**
 * Runnableインタフェースのλ式作成。
 * Created by dev001 on 2017/02/23.
 *
 * @link http://enterprisegeeks.hatenablog.com/entry/2014/04/10/163618
 */
class LambdaSandbox {
    /**
     * Runnableインタフェースのラムダ式はすべてハッシュが違う。
     */
    static void showIdentityHash() {
        IntStream.range(0, 3).boxed()
                .map(n -> (Runnable) () -> System.out.println(n))
                .map(System::identityHashCode)
                .map(id -> " " + id + ",")
                .forEach(System.out::print);
    }

    /**
     * ラムダ式で引数を渡していない場合は、最適化されてハッシュ値は同一になる。
     */
    static void showRunnableHash() {
        IntStream.range(0, 3).boxed()
                .map(n -> (Runnable) () -> {
                })
                .map(System::identityHashCode)
                .map(id -> " " + id + ",")
                .forEach(System.out::print);
    }
}
