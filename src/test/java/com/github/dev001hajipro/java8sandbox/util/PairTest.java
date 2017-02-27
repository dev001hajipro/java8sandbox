package com.github.dev001hajipro.java8sandbox.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Pairのテスト
 * Created by dev001 on 2017/02/27.
 */
public class PairTest {
    @Test
    public void testEquals() throws Exception {
        Pair<Integer, Integer> p1 = new Pair<>(1, 1);
        Pair<Integer, Integer> p2 = new Pair<>(1, 1);

        if (p1.equals(p2)) {
            return;
        }
        fail();
    }

    @Test
    public void testHashCode() throws Exception {
        Pair<Integer, Integer> p1 = new Pair<>(5, 5);
        if (p1.hashCode() != 0) {
            return;
        }
        fail();
    }

    @Test
    public void testToString() throws Exception {
        Pair<Integer, Integer> p1 = new Pair<>(1, 1);
        assertEquals(p1.toString(), "(1,1)");
    }

}