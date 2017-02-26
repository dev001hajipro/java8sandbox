package com.github.dev001hajipro.java8sandbox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Java8の勉強
 * Created by dev001 on 2017/02/23.
 *
 * @link http://enterprisegeeks.hatenablog.com/archive/category/Java8
 */
public class App {
    public static void main(String[] args) {
        //testHelloStream();
        System.out.println("hasOverAge:" + hasOverAge(5));
        System.out.println("hasOverAge:" + hasOverAge(15));
        printFemaleCount();

        getNameStartsWishA().forEach(System.out::print);

        getPersons().forEach(System.out::println);

        useFunction();
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
