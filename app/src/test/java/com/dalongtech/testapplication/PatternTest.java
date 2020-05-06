package com.dalongtech.testapplication;

import org.junit.Test;

/**
 * Author:xianglei
 * Date: 2020-04-16 13:54
 * Description:正则测试
 *
 * 1. 任意一个字符表示匹配任意对应的字符，如a匹配a，7匹配7，-匹配-。
 *
 * 2. []代表匹配中括号中其中任一个字符，如[abc]匹配a或b或c。
 *
 * 3. -在中括号里面和外面代表含义不同，如在外时，就匹配-，如果在中括号内[a-b]表示匹配26个小写字母中的任一个；[a-zA-Z]匹配大小写共52个字母中任一个；[0-9]匹配十个数字中任一个。
 *
 * 4. ^在中括号里面和外面含义不同，如在外时，就表示开头，如^7[0-9]表示匹配开头是7的，且第二位是任一数字的字符串；如果在中括号里面，表示除了这个字符之外的任意字符(包括数字，特殊字符)，如[^abc]表示匹配出去abc之外的其他任一字符。
 *
 * 5. .表示匹配任意的字符，\n除外。包括空格可以是(.|\n)
 *
 * 6. \d表示数字。
 *
 * 7. \D表示非数字。
 *
 * 8. \s表示由空字符组成，[ \t\n\r\x\f]。
 *
 * 9. \S表示由非空字符组成，[^\s]。
 *
 * 10. \w表示字母、数字、下划线，[a-zA-Z0-9_]。
 *
 * 11. \W表示不是由字母、数字、下划线组成。
 *
 * 12. ?: 表示出现0次或1次。
 *
 * 13. +表示出现1次或多次。
 *
 * 14. *表示出现0次、1次或多次。
 *
 * 15. {n}表示出现n次。
 *
 * 16. {n,m}表示出现n~m次。
 *
 * 17. {n,}表示出现n次或n次以上。
 *
 * 18. XY表示X后面跟着Y，这里X和Y分别是正则表达式的一部分。
 *
 * 19. X|Y表示X或Y，比如"food|f"匹配的是foo（d或f），而"(food)|f"匹配的是food或f。
 *
 * 20. (X)子表达式，将X看做是一个整体。
 */
public class PatternTest {

    @Test
    public void baseTest() {
        //中括号里可以是0-9可以是a-z可以是abcd，但不能是.（表示所有非空格字符）或\n(表示所有空格),*表示0次一次或多次，.表示\n外所有字符start$表示start结尾
        String regex = "[0-9]*.*start$";
        String str = "8888sdfsdfdf88start";
        println(str.matches(regex));

        String notSpace = "[^\\s]*";
        String str1 = "342342 34";
        println(str1.matches(notSpace));

    }

    //不能所有字符全数字的匹配
    @Test
    public void notDigit() {
        //(?!pattern)表示正向否定预查，((?=pattern))表示正向肯定预查
        String regex = "^(?![\\d]+$).*";
        String str = "888889898";
        println(str.matches(regex));
    }

    //不能全字母
    @Test
    public void notAllLetter() {
        String regex = "^(?![a-zA-Z]+$).*";
        String str = "asdfsdfs";
        println(str.matches(regex));
    }

    //普通密码检测
    @Test
    public void passwordPattern() {
        //^表示从开头检测，(?![\d]+$)表示到结尾全是数字就false,(?![a-zA-Z]+$)表示到结尾全是字母就false,(?![^\da-zA-Z]+$)表示到结尾全是非数字与字母就false,前3项否定预查都过了，就看最后，.{6,20}$表示所有字符出现6次到20次结尾
        String regex = "^(?![\\d]+$)(?![a-zA-Z]+$)(?![^\\da-zA-Z]+$).{6,20}$";
        String str = "*&*&*&*&*&*&a";
        println(str.matches(regex));
    }

    //密码检测还不能有空格
    @Test
    public void passwordNotSpace() {
        String regex = "^(?![\\d]+$)(?![a-zA-Z]+$)(?![^\\da-zA-Z]+$)[\\S]{6,20}$";
        String str = "*&*&*&*&*&*&a";
        println(str.matches(regex));
    }

    @Test
    public void test() {
        String regex = "^[\\dabcdef]{4}(.|\\n)*";
        String str = "123fsdflkjsdlfkj\n";
        println(str.matches(regex));
    }



    private void println(Object s) {
        System.out.println(s);
    }
}
