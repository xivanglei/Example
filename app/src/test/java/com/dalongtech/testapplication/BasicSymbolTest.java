package com.dalongtech.testapplication;

import org.junit.Test;

/**
 * Author:xianglei
 * Date: 2020-04-03 07:58
 * Description:
 */
public class BasicSymbolTest {

    @Test
    public void baseInteger() {
        //32为全满就是 -1  满32位的第一位如果是1表示负数,0表示整数,如果第一位是1，后面的可以理解为相反，把后面31位1全变为0，但结果不是0，如果是0，就跟0冲突了，可以理解为最小的负数，就是-1
        println(0b11111111111111111111111111111111);
        //后面第四位是0，可以理解为1，其他都是0，就是1000，就是-8，但负数的表达形式需要再-1 就是-9
        println(0b11111111111111111111111111110111);
        //可以理解为0111 1111 1111 1111 1111 1111 1111 1111，第一位是0，所以是正数，就不用相反，刚好是最大的正数2147483647
        println(0b1111111111111111111111111111111);
        //第一位是1 就是负数，就需要把后面的数字相反，相当于31位全是1 -2147483647 负数结果需要再-1 这里就是最小值-2147483648
        println(0b10000000000000000000000000000000);

        //最大值111 1111 1111 1111 1111 1111 1111 1111，结果是2147483647
        println(Integer.MAX_VALUE);
        //最大+1 就成最小值了1000 0000 0000 0000 0000 0000 0000 0000 结果是 -2147483648
        int maxIncrement = Integer.MAX_VALUE + 1;
        println(maxIncrement);
        //同上最小值，-2147483648
        println(Integer.MIN_VALUE);
        //最小值减1 就是1000 0000 0000 0000 0000 0000 0000 0000 变为 111 1111 1111 1111 1111 1111 1111 1111，就是最大值了2147483647
        println(Integer.MIN_VALUE - 1);

    }

    @Test
    public void carry() {
        //0进位任何数都是0
        println(0 << 29);
        //Integer一共32位 1000 0000 0000 0000 0000 0000 0000 0000 结果是-2147483648
        println(1 << 31);
        //后面本来是全1的....1111 进以为，后面补0，就是最后以为变0了。结果跟1 << 1 加上负数一样
        println(-1 << 1);
    }

    @Test
    public void testOr() {
        //这部分是 线程池ThreadPoolExecutor中的源码
        int COUNT_BITS = Integer.SIZE - 3;
        int RUNNING    = -1 << COUNT_BITS;
        //-536870912
        println(RUNNING);
        int runningOrZero = RUNNING | 0;
        //任何数与0或 都是原来的数
        println(runningOrZero);

        //10 0000 0000 0000 0000 0000 0000 0000 减1 就是 1 1111 1111 1111 1111 1111 1111 1111
        int CAPACITY   = (1 << COUNT_BITS) - 1;
        // 536870911
        println(CAPACITY);
        //加上非就是 0001 1111 1111 1111 1111 1111 1111 1111 变为 1110 0000 0000 0000 0000 0000 0000 0000,第一位是1负数，计算方法 后面1与0互换
        //就是1 1111 1111 1111 1111 1111 1111 1111结果加负数 再 -1，就是-536870912
        println(~CAPACITY);
    }

    //ThreadPoolExecutor中记录运行状态，只记录8中状态
    private int runStateOf(int c)     {
        int CAPACITY   = (1 << 29) - 1;
        //就是其他数& 1110 0000 0000 0000 0000 0000 0000 0000，相当于只记录前3位，后面全抹掉
        return c & ~CAPACITY;
    }

    //ThreadPoolExecutor中记录有效线程数量
    private int workerCountOf(int c)  {
        int CAPACITY   = (1 << 29) - 1;
        // 0001 1111 1111 1111 1111 1111 1111 1111,只记录后29位，前面全抹掉
        return c & CAPACITY;
    }

    //① RUNNING (运行状态): 能接受新提交的任务, 并且也能处理阻塞队列中的任务.
    //② SHUTDOWN (关闭状态): 不再接受新提交的任务, 但却可以继续处理阻塞队列中已保存的任务. 在线程池处于 RUNNING 状态时, 调用 shutdown()方法会使线程池进入到该状态. 当然, finalize() 方法在执行过程中或许也会隐式地进入该状态.
    //③ STOP : 不能接受新提交的任务, 也不能处理阻塞队列中已保存的任务, 并且会中断正在处理中的任务. 在线程池处于 RUNNING 或 SHUTDOWN 状态时, 调用 shutdownNow() 方法会使线程池进入到该状态.
    //④ TIDYING (清理状态): 所有的任务都已终止了, workerCount (有效线程数) 为0, 线程池进入该状态后会调用 terminated() 方法以让该线程池进入TERMINATED 状态. 当线程池处于 SHUTDOWN 状态时, 如果此后线程池内没有线程了并且阻塞队列内也没有待执行的任务了 (即: 二者都为空), 线程池就会进入到该状态. 当线程池处于 STOP 状态时, 如果此后线程池内没有线程了, 线程池就会进入到该状态.
    //⑤ TERMINATED : terminated() 方法执行完后就进入该状态.




    private void print(Object o) {
        System.out.print(o);
    }

    private void println(Object o) {
        System.out.println(o);
    }
}
