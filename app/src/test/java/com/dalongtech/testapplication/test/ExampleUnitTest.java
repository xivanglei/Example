package com.dalongtech.testapplication.test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {



    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test() {
        int a = 0;
        int b = 10;
        int c = 20;
        a = b = c = 30;
        int d = (c = 40);
        System.out.println("d:" + d);
    }

}