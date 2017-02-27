package com.github.dev001hajipro.java8sandbox;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.*;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Java8の勉強
 * Created by dev001 on 2017/02/23.
 *
 * @link http://enterprisegeeks.hatenablog.com/archive/category/Java8
 */
public class App {
    public static void main(String[] args) {
        // class.getResourceAsStreamの場合は/が必要
        try (InputStream resource = App.class.getResourceAsStream("/logging.properties")) {
            LogManager.getLogManager().readConfiguration(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //testHelloStream();
        System.out.println("hasOverAge:" + hasOverAge(5));
        System.out.println("hasOverAge:" + hasOverAge(15));
        printFemaleCount();

        getNameStartsWishA().forEach(System.out::print);

        getPersons().forEach(System.out::println);

        useFunction();

        double avg = getPersons().stream().reduce(0.0, (acc, p) -> acc + p.getAge(), (r1, r2) -> r1 + r2 / getPersons().size());
        System.out.println("avg=" + avg);

        double avg3 = getPersons().stream().mapToInt(Person::getAge).average().orElse(0);
        System.out.println("avg3=" + avg3);

        // プリミティブStreamからStreamの変換は、boxedを使う。
        IntStream.range(0, 100).boxed()
                .map(n -> String.format("n=%d,", n)).collect(Collectors.toList())
                .forEach(System.out::print);

        System.out.println("xxxxxxoptional\n");
        Optional<Person> optional = getPersons().stream().findFirst();
        optional.ifPresent(person -> System.out.println(person.getName()));


        //testParallel();
        //testParallel2();
        //testParallel3();

        List<String> r1 = Stream.of(1, 2, 3, 4, 5)
                .map(n -> "*" + n)
                .parallel()
                .collect(Collectors.toList());
        System.out.println(r1);

        t1();

        t2();

        t3();

        // Spliterator
        // Split + iterator
        System.out.println("");
        testSpliterator();
    }

    private static void testSpliterator() {
        Logger.getGlobal().info("aaa");
        System.out.println("testSpliterator");
        List<Integer> list = Arrays.asList(1, 2, 2, 5, 4, 3, 9, 8, 7, 7, 6);
        list.stream()
                .sorted()
                .distinct()
                .forEach(System.out::print);

        System.out.println();

        // Streamのうごきを調べるコード
        // あえて、SORT済み、要素が一意という性質にすると
        // .sorted().distinct()が正常に動作しない。
        Stream<Integer> stream1 = StreamSupport.stream(
                Spliterators.spliterator(list.iterator(), list.size(), Spliterator.SORTED | Spliterator.DISTINCT), false);
        stream1.sorted()
                .distinct()
                .forEach(System.out::print);
        System.out.println();
        // Listインタフェースに合わせて、Spliterator.SIZED | Spliterator.ORDEREDを指定
        // これは正しく動作する。
        Stream<Integer> stream2 = StreamSupport.stream(
                Spliterators.spliterator(list.iterator(), list.size(), Spliterator.SIZED | Spliterator.ORDERED), false);
        stream2.sorted()
                .distinct()
                .forEach(System.out::print);
        System.out.println();

    }

    private static void t3() {
        System.out.println("stream of");
        Stream.of(1, 2, 3, 4, 5).forEach(System.out::print);
        System.out.println();

        System.out.println("arrays as list");
        Arrays.asList(1, 2, 3, 4, 5).forEach(System.out::print);
        System.out.println();

        System.out.println("infinite");
        UnaryOperator<Integer> inc = i -> i + 1;
        Stream.iterate(0, inc).limit(5).forEach(System.out::print);
        System.out.println();

        System.out.println("generate");
        Supplier<String> sup = () -> ":";
        Stream.generate(sup).limit(10).forEach(System.out::print);
        System.out.println();

        System.out.println("range");
        IntStream.range(0, 100).skip(1).limit(89).forEach(System.out::print);
        System.out.println();
    }

    private static void t2() {
        System.out.println("t2==================");
        Function<String, Integer> f1 = Integer::parseInt;

        Consumer<Integer> c1 = i -> {
            System.out.print("value=");
            System.out.println(i);
        };

        List<Integer> list = Stream.of("5", "3", "9", "4")
                .map(f1)
                .collect(Collectors.toList());

        list.forEach(c1);

        // Filter
        Predicate<String> filter1 = s -> !s.contains("a");
        Stream.of("apple", "orange", "banana", "melon", "blackberry", "peach", "kiwifruit", "pear")
                .filter(filter1)
                .forEach(s -> {
                    System.out.println("fruit=");
                    System.out.println("fruit=" + s);
                });
    }

    /**
     * やってはいけない例
     * リストの各要素を手続き型プログラミングのようにまとめる。
     * この場合、.sequentialの場合は、
     */
    private static void t1() {
        // リストは順序を持っている。
        List<String> firstNames = Arrays.asList("taro", "jiro", "saburo");
        List<String> lastNames = Arrays.asList("yamada", "tanaka", "kato");

        Iterator<String> iterator = firstNames.iterator();

        lastNames.stream().map(l -> iterator.next() + " " + l)
                .parallel() // 並列化すると、結果が不定
                .forEach(System.out::println);
    }

    private static void testParallel3() {
        List<Integer> finiteList = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            finiteList.add(i);
        }
        showSequential(finiteList.stream());

        Stream<Integer> infiniteStream = Stream.iterate(0, n -> n + 1);
        // 無限ストリームの場合値が定まらない。
        showSequential(infiniteStream);
    }

    private static void showSequential(Stream<Integer> stream) {
        List<Integer> result = stream.parallel()
                //.unordered()
                .map(n -> n + 1)
                .peek(n -> System.out.println(n + ":" + Thread.currentThread()))
                .skip(1000)
                .limit(300)
                .collect(Collectors.toList());
        System.out.printf("from %d to %d\n", result.get(0), result.get(result.size() - 1));
        System.out.println(result);

    }

    private static void testParallel2() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }

        List<Integer> result =
                list.stream().parallel()
                        .filter(x -> x % 2 == 0)
                        .peek(n -> System.out.println(n + ":" + Thread.currentThread()))
                        .skip(2000)
                        .limit(2000)
                        .collect(Collectors.toList());

        System.out.printf("from %d to %d\n", result.get(0), result.get(1999));
        int start = result.get(0);
        for (int i : result) {
            if (i != start) {
                System.out.println("exception");
                throw new RuntimeException("xxx" + i);
            }
            start += 2;
        }
        System.out.println("end");
    }


    private static void testParallel() {
        // 順序
        List<Integer> data1 = new ArrayList<>();
        for (int i = 0; i < 10000000; i++) {
            data1.add(i);
        }

        // Stream APIは簡単に並列処理ができるが、単純な処理だとparallelは遅くなるので
        // 注意が必要
        long start = System.currentTimeMillis();
        List<Integer> result = data1.stream().parallel()
                .filter(x -> x % 2 == 0)
                .peek(s -> System.out.println(s + ":" + Thread.currentThread()))
                .skip(1000)
                .limit(1000000)
                .collect(Collectors.toList());
        System.out.println((System.currentTimeMillis() - start) + "ms");
    }

    private static void useFunction() {
        Function<Person, String> f1 = p -> "name=" + p.getName();

        getPersons().stream()
                .map(f1)
                .collect(Collectors.toList())
                .forEach(System.out::println);
    }

    private static List<String> getNameStartsWishA() {
        return getPersons().stream()
                .map(Person::getName)
                .filter(s -> s.toUpperCase().startsWith("A"))
                .collect(Collectors.toList());
    }

    private static void printFemaleCount() {
        long FemaleCount = getPersons().stream()
                .filter(o -> !o.isMale() && o.getAge() <= 20)
                .count();
        System.out.println("FemaleCount:" + FemaleCount);
    }

    private static boolean hasOverAge(int n) {
        return getPersons().stream()
                .anyMatch(p -> p.getAge() > n); // any:任意の、いづれか
    }

    private static List<Person> getPersons() {
        List<Person> personList = new ArrayList<>();
        personList.add(new Person("john", 10, true));
        personList.add(new Person("maria", 31, false));
        personList.add(new Person("tom", 10, true));
        personList.add(new Person("ada", 31, false));
        personList.add(new Person("rita", 18, false));
        personList.add(new Person("ellis", 13, false));
        personList.add(new Person("alia", 9, false));
        return personList;
    }

    private static void testHelloStream() {
        HelloStream.sample01();
        HelloStream.sample02();
        HelloStream.sample03();
        HelloStream.sample04();
        HelloStream.sample05();
    }
}
