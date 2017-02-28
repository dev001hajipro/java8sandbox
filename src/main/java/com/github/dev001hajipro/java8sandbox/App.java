package com.github.dev001hajipro.java8sandbox;

import com.github.dev001hajipro.java8sandbox.util.Pair;
import com.github.dev001hajipro.java8sandbox.util.StreamUtil;

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

import static com.github.dev001hajipro.java8sandbox.util.StreamUtil.zip;

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

        System.out.println("test optional\n");
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

        // testStreamUtil
        testStreamUtil();

        // findFirst
        testFindFirst();

        t4();

        t5();

        t6();

        useTakeWhile();

        useDropWhile();
    }

    private static void useTakeWhile() {
        Stream<Integer> squareNumbers = Stream.iterate(1, n -> n + 1).map(n -> n * n);
        List<Integer> less100000 = StreamUtil.createTakeWhile(squareNumbers, n -> n <= 100000)
                .collect(Collectors.toList());
        System.out.println(less100000.size());

        Stream<Integer> numbers = Stream.iterate(1, n -> n + 1);
        String str = StreamUtil.createTakeWhile(numbers, n -> n < 15)
                .map(n -> "" + n)
                .collect(Collectors.joining(","));
        System.out.println("n<15 = " + str);
    }

    private static void useDropWhile() {
        Stream<Integer> numbers = Stream.iterate(1, n -> n + 1);
        Optional<Integer> o1 = StreamUtil.createDropWhile(numbers, n -> n < 15).findFirst();
        System.out.println(o1.orElse(-1));

        System.out.println("filter?");
        int v1 = Stream.iterate(1, n -> n + 1).filter(n -> n > 15).findFirst().orElse(-1);
        System.out.println(v1);

        Stream<Integer> squareNumbers = Stream.iterate(1, n -> n + 1).map(n -> n * n);
        int over100000 = StreamUtil.createDropWhile(squareNumbers, n -> n < 100000)
                .findFirst().orElse(-1);
        System.out.println("over100000=" + over100000);

        // 無限ループ?
        Stream<Integer> squareNumbers2 = Stream.iterate(1, n -> n + 1).map(n -> n * n);
        int v2 = squareNumbers2.filter(n -> n > 100000).findFirst().orElse(-1);
        System.out.println("over100000=" + v2);


    }

    private static void t6() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        Map ret1 =
                list.stream()
                        .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        System.out.println(ret1.get(Boolean.TRUE));

        System.out.println(
                list.stream().collect(
                        Collectors.groupingBy(n -> n % 3, Collectors.averagingDouble(n -> n + 0.0)))
        );

        // 要素の登場順にグループ分け
        List<String> members = Arrays.asList("b0001", "a0001", "d0002", "c0004", "a0001");
        Map<Character, List<String>> group = members.stream()
                .collect(Collectors.groupingBy(s -> s.charAt(0), LinkedHashMap::new, Collectors.toList()));
        group.forEach((k, v) -> System.out.println(k + " = " + v));
    }

    private static void t5() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("aaa", 50));
        userList.add(new User("bbb", 49));
        userList.add(new User("ccc", 20));

        // 平均
        long sum = userList.stream().mapToInt(User::getPoint).sum();
        System.out.println("sum=" + sum);
        // JavaのStream#reduceは面倒
        // combinerはparallel対応?
        long sum2 = userList.stream()
                .parallel()
                .reduce(0, (acc, u) -> acc + u.getPoint(), (a1, a2) -> a1 + a2);
        System.out.println("sum2=" + sum2);

        // 合計
        //

        // 統計

    }

    private static void t4() {
        System.out.println("t4=========");
        Stream.of(1, 2, 3, 4, 5).collect(Collectors.toList()).forEach(System.out::print);
        System.out.println();

        System.out.println(String.join("*", "a", "b", "c"));

        String s1 = Stream.of("1", "2", "3")
                .collect(Collectors.joining("-", "[", "]"));
        System.out.println(s1);
    }

    private static void testFindFirst() {
        // findFirst
        int v1 = Stream.of(1, 2, 3, 4, 5)
                .findFirst().orElse(-1);
        System.out.println(v1);

        // findAny
        int v2 = Stream.iterate(0, n -> n + 1).limit(1000).parallel()
                .findAny().orElse(-1);
        System.out.println(v2);

        // count
        long v3 = IntStream.iterate(0, n -> n + 1)
                .limit(100)
                .count();
        System.out.println("v3=" + v3);

        // and
        boolean and = Stream.iterate(1, n -> n + 1)
                .limit(500)
                .allMatch(n -> n < 501);
        System.out.println("and=" + and);
        // or
        boolean or = Stream.iterate(1, n -> n + 1)
                .limit(500)
                .anyMatch(n -> n == 5);
        System.out.println("or=" + or);
        // not
        boolean not = Stream.iterate(1, n -> n + 1)
                .limit(500)
                .noneMatch(n -> n == 5);
        System.out.println("not=" + not);

        // reduce
        System.out.println(
                Stream.of("ab", "cd", "ef")
                        .reduce("", (acc, e) -> {
                            System.out.println("acc=" + acc + ",e=" + e);
                            return e + acc;
                        })
        );
    }

    private static void testStreamUtil() {
        List<Integer> list1 = Arrays.asList(0, 0, 1, 0, 1, 1);
        List<Integer> list2 = Arrays.asList(0, 1, 2, 3, 4, 1);
        zip(list1.stream(), list2.stream())
                .forEach(System.out::print);
        System.out.println();
        zip(list1.stream(), list2.stream(), 3)
                .forEach(System.out::print);

        System.out.println();

        System.out.println("両方1を見つける");
        Optional<Pair<Integer, Integer>> pair = zip(list1.stream(), list2.stream())
                .filter(p -> p._1.equals(1))
                .filter(p -> p._1.equals(p._2))
                .findAny();
        System.out.println("isPresent=" + pair.isPresent());
        System.out.println(pair.orElse(new Pair<>(0, 0)));

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
        List<String> firstNames = Arrays.asList("maria", "risa", "alice");
        List<String> lastNames = Arrays.asList("davis", "johnson", "williams");

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
