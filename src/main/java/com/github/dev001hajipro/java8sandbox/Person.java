package com.github.dev001hajipro.java8sandbox;

/**
 * Created by dev001 on 2017/02/25.
 *
 * @link http://enterprisegeeks.hatenablog.com/entry/2014/10/20/085500
 */
public class Person {
    private final String name;
    private final int age;
    private final boolean maleFlag;

    Person(String name, int age, boolean male) {
        this.name = name;
        this.age = age;
        this.maleFlag = male;
    }

    String getName() {
        return name;
    }

    int getAge() {
        return age;
    }

    boolean isMale() {
        return maleFlag;
    }

    @Override
    public String toString() {
        return name;
    }
}
