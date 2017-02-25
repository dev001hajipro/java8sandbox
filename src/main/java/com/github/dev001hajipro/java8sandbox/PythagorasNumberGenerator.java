package com.github.dev001hajipro.java8sandbox;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * ピタゴラス数を生成
 * a^2 + b^2 = c^2
 * Created by dev001 on 2017/02/24.
 */
class PythagorasNumberGenerator {
    static Stream<String> generate1() {
        return num(1)
                .map(a -> String.format("(%d)", a));
    }

    static Stream<String> generate2() {
        return num(1) // 1,2,3,4,5...
                .flatMap(n -> num(1).limit(n)) // 1, 1,2, 1,2,3, 1,2,3,4,
                .map(a -> String.format("(%d)", a));
    }

    static Stream<String> generate3() {
        return num(1) // 1,2,3,4,5...
                .flatMap(c -> num(1).limit(c) // 1, 1,2, 1,2,3, 1,2,3,4,
                        .flatMap(b -> num(1).limit(b)
                                .filter(a -> isCoprime(a, b))
                                .filter(a -> isPythagorasNumber(a, b, c))
                                .map(a -> String.format("(%d,%d,%d)", a, b, c))));
    }

    /**
     * 無限数[1..n]
     *
     * @param initial 初期値
     * @return 無限のシーケンスを返す。
     */
    static Stream<Integer> num(int initial) {
        return IntStream.iterate(initial, n -> n + 1).boxed();
    }

    private static boolean isCoprime(int a, int b) {
        if (b == 0) return false;
        if (b == 1) return true;
        if (b > a) return isCoprime(b, a);
        return isCoprime(b, a % b);
    }

    private static boolean isPythagorasNumber(int a, int b, int c) {
        return c * c == a * a + b * b;
    }
}
