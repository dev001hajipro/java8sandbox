package com.github.dev001hajipro.java8sandbox;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by dev001 on 2017/02/23.
 *
 * @link http://enterprisegeeks.hatenablog.com/entry/2014/04/08/193834
 */
class HelloStream {
    static void sample01() {
        IntStream.range(0, 20).boxed() // 1.stream
                .filter(n -> n % 2 == 0) // intermediate
                .map(x -> x + ",")
                .forEach(System.out::print); // terminal
        System.out.println();
    }

    static void sample02() {
        List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
        data.stream()
                .map(n -> n * 2)
                .map(n -> "answer = " + n + ",")
                .forEach(System.out::print);
        System.out.println();
    }

    static void sample03() {
        IntStream.range(0, 20).boxed()
                .filter(n -> n % 2 == 0)
                .map(n -> n * 2 + ",")
                .forEach(System.out::print);
        System.out.println();
    }

    static void sample04() {
        System.out.println("sample04=====1");
        Function<Integer, Stream<Integer>> f1 = n -> Stream.generate(() -> n).limit(n);
        f1.apply(1).forEach(System.out::print);
        System.out.println();
        f1.apply(2).forEach(System.out::print);
        System.out.println();
        f1.apply(3).forEach(System.out::print);
        System.out.println();
        System.out.println("sample04=====2");
        List<Integer> list = Arrays.asList(0, 1, 2, 3);
        list.stream()
                .flatMap(f1)
                .flatMap(f1)
                .map(x -> x + ",")
                .forEach(System.out::print);
    }

    // flatMapで二重ループ
    static void sample05() {
        System.out.println("sample05");
        IntStream.range(0, 4).boxed()
                .flatMap(i -> IntStream.range(0, 4).boxed().map(j -> i + ":" + j + ", "))
                .forEach(System.out::print);
    }
}
