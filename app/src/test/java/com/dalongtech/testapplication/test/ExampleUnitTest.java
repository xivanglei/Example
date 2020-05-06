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
        String code = "{\"success\":false,\"msg\":\"\\u672a\\u767b\\u5f55\"}";
        try {
            println(unicodeToCn(code));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void println(Object s) {
        System.out.println(s);
    }

    private static String unicodeToCn(String unicode) {
        /** 以 \ u 分割，因为java注释也能识别unicode，因此中间加了一个空格*/
        String[] strs = unicode.split("\\\\u");
        StringBuilder builder = new StringBuilder();
        // 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
        for (int i = 0; i < strs.length; i++) {
            String str = strs[i];
            String regex = "^[\\dabcdef]{4}(.|\\n)*";
            if(str.matches(regex)) {
                try {
                    builder.append((char) Integer.valueOf(str.substring(0, 4), 16).intValue());
                } catch (Exception e) {
                    builder.append(str.substring(0, 4));
                }
                if(str.length() > 4) builder.append(str.substring(4));
            } else {
                builder.append(str);
            }
        }
        return builder.toString();
    }

}