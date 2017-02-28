package com.github.dev001hajipro.java8sandbox;

import com.sun.istack.internal.NotNull;

/**
 * ユーザー
 * Created by dev001 on 2017/02/27.
 */
public class User {

    private final String name;
    private final int point;

    User(String name, int point) {
        this.name = name;
        this.point = point;
    }

    @NotNull
    public String getName() {
        return name;
    }

    int getPoint() {
        return point;
    }
}
