package com.github.dev001hajipro.java8sandbox;

import org.junit.Test;

/**
 * ピタゴラス数のテスト
 * Created by dev001 on 2017/02/25.
 */
public class PythagorasNumberGeneratorTest {
    @Test
    public void generate1() throws Exception {
        PythagorasNumberGenerator.generate1()
                .skip(10)
                .limit(5)
                .forEach(System.out::print);
    }

    @Test
    public void generate2() throws Exception {
        PythagorasNumberGenerator.generate2()
                .limit(10)
                .forEach(System.out::print);
    }

    @Test
    public void generate3() throws Exception {
        PythagorasNumberGenerator.generate3()
                .limit(3)
                .forEach(System.out::print);
    }

    @Test
    public void num() throws Exception {
        PythagorasNumberGenerator.num(0).limit(10).forEach(System.out::print);
        PythagorasNumberGenerator.num(1).limit(3).forEach(System.out::print);
        PythagorasNumberGenerator.num(5).limit(3).forEach(System.out::print);

    }

}