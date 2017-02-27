package com.github.dev001hajipro.java8sandbox.util;

/**
 * Created by dev001 on 2017/02/27.
 *
 * @link http://enterprisegeeks.hatenablog.com/entry/2014/05/19/100422
 */
public class Pair<T, U> {
    public final T _1;
    public final U _2;

    public Pair(T t, U u) {
        _1 = t;
        _2 = u;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair) {
            Pair pair = (Pair) obj;
            return _1.equals(pair._1) && _2.equals(pair._2);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + _1.hashCode();
        hash = hash * 31 + _2.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return "(" + _1 + "," + _2 + ")";
    }
}
