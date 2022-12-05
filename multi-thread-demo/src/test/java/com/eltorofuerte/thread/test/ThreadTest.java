package com.eltorofuerte.thread.test;

import junit.framework.TestCase;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * join : 指定线程结束后，其他线程才能开始抢占cpu执行权
 * Priority : 线程优先级
 * setDaemon:  不存在非守护线程时，jvm退出
 * interrupt : 抛出异常，终止线程，继续执行之后的代码
 * stop：终止程序
 * yield: 让步，让出cpu执行权
 * ThreadGroup: 组
 *
 * @author xinyu.zhang
 * @since 2022/12/5 14:19
 */
public class ThreadTest extends TestCase {

    public static void main(String[] args) throws InterruptedException {
//        testJoin();
        testStop();
    }

    public static void testJoin() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 100; i++) {
                    System.out.println(Thread.currentThread().getName() + "计数" + i);
                }
            }
        };

        Thread t1 = new Thread(runnable, "001");
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
        }

        new Thread(runnable, "002").start();
        new Thread(runnable, "003").start();
        System.out.println(Thread.currentThread().getName() + "主线程结束");
    }

    /*
        junit执行，发现执行了main线程。线程001，002，003并未执行

        原因：test方法的代码运行在main中，执行完成后会调用system.exit退出虚拟机，thread还没执行完就被销毁了 详情： https://blog.csdn.net/anthony_ju/article/details/113851557

        有以下3种方式：
            1. main中运行
            2. join：指定线程结束后，其他线程才能开始抢占cpu执行权
            3. countDownLatch
     */
    public void testResolveExecute() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3 * 1000L);
                    System.out.println(Thread.currentThread().getName() + "执行结束");
                    countDownLatch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t1 = new Thread(runnable, "001");
        t1.start();
        //        t1.join();
        Thread t2 = new Thread(runnable, "002");
        t2.start();
        //        t2.join();
        Thread t3 = new Thread(runnable, "003");
        t3.start();
        //        t3.join();
        System.out.println(Thread.currentThread().getName() + "主线程结束");
        countDownLatch.await();
    }

    /*
     * https://blog.csdn.net/weixin_44688301/article/details/115554321
     * 通过一个计数器来实现,初始值为线程的数量,如果多个线程并行执行一个任务，则初始count值必须与启动线程数保持一致，即count为10，则必须开启10 个异步线程
     *  countDown(): 每调用一次计数器值-1，直到count被减为0，代表所有线程全部执行完毕
     *  getCount()：获取当前计数器的值
     *  await(): 等待计数器变为0，即等待所有异步线程执行完毕
     *  boolean await(long timeout, TimeUnit unit)： 此方法与await()区别：
     *      ①此方法至多会等待指定的时间，超时后会自动唤醒，若 timeout 小于等于零，则不会等待
     *      ②boolean 类型返回值：若计数器变为零了，则返回 true；若指定的等待时间过去了，则返回 false
     */
    public void testCountDownLatch() throws InterruptedException {
        List list = new ArrayList();
        for (int i = 1; i <= 100; i++) {
            list.add(i);
        }
        long start = System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {
            Thread.sleep(100);
        }
        System.out.println("=====同步执行：耗时" + (System.currentTimeMillis() - start) + "毫秒======");
        long start1 = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < latch.getCount(); i++) {
            new Thread(new Test(latch, i, list)).start();
        }
        latch.await();
        System.out.println("=====异步执行：耗时" + (System.currentTimeMillis() - start1) + "毫秒======");
    }

    static class Test implements Runnable {
        private CountDownLatch latch;
        private int i;
        private List list;

        Test(CountDownLatch latch, int i, List list) {
            this.latch = latch;
            this.i = i;
            this.list = list;
        }

        @SneakyThrows
        @Override
        public void run() {
            for (int a = i * 10; a < (i + 1) * 10; a++) {
                // 执行任务逻辑
                Thread.sleep(100);
            }
            latch.countDown();
        }
    }

    /**
     * 线程优先级Priority 默认5，范围1-10，数值越大，优先级越高，但不一定是优先级高的先执行
     */
    public void testThreadPriority() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "开始执行");
                countDownLatch.countDown();
            }
        };

        Thread t1 = new Thread(runnable, "t1");
        t1.start();

        Thread t2 = new Thread(runnable, "t2");
        t2.setPriority(10);
        t2.start();

        Thread t3 = new Thread(runnable, "t3");
        t3.start();

        System.out.println("t1 priority = " + t1.getPriority());
        System.out.println("t2 priority = " + t2.getPriority());
        System.out.println("t3 priority = " + t3.getPriority());
        System.out.println("main thread priority = " + Thread.currentThread().getPriority());
        countDownLatch.await();
    }

    /**
     * https://www.jianshu.com/p/53adfc81e9c3
     * 把一个线程标记为“守护线程”，就是当他是一个“后台线程”or“内部线程”，类似于管理内存垃圾回收的线程一样。
     * Java程序入口就是由JVM启动main线程，main线程又可以启动其他线程。当所有线程都运行结束时，JVM退出，进程结束。如果有一个线程没有退出，JVM进程就不会退出。所以，必须保证所有线程都能及时结束。
     * 由谁负责结束这个线程？ 守护线程是指为其他线程服务的线程。在JVM中，所有非守护线程都执行完毕后，无论有没有守护线程，虚拟机都会自动退出。很好理解，没有被守护的对象了，也不需要守护线程了
     *
     * 在守护线程中，编写代码要注意：守护线程不能持有任何需要关闭的资源，例如打开文件等，因为虚拟机退出时，守护线程没有任何机会来关闭文件，这会导致数据丢失。
     *
     */
    public void testSetDaemon() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 50; i++) {
                    System.out.println(Thread.currentThread().getName() + " 守护" + i);
                }
            }
        };
        Thread zhangfei = new Thread(runnable, "张飞");
        zhangfei.setDaemon(true);
        Thread guanyu = new Thread(runnable, "关羽");
        guanyu.setDaemon(true);

        zhangfei.start();
        guanyu.start();

        Thread.currentThread().setName("刘备");
        for (int i = 1; i <= 50; i++) {
            System.out.println(Thread.currentThread().getName() + " 老大被守护" + i);
        }

    }

    /**
     * main 调用
     * @throws InterruptedException
     */
    public static void testStop() throws InterruptedException {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName() + "线程开始");
                    Thread.sleep(3 * 1000L);
                    System.out.println(Thread.currentThread().getName() + "线程结束");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t1 = new Thread(runnable);
        t1.start();
        t1.interrupt();

        Thread.sleep(2 * 1000L);
        System.out.println(Thread.currentThread().getName() + "主线程结束");
    }

    /**
     * 线程的状态： 抢占执行权 -> 就绪 - > 运行
     *
     * 译为线程让步： 调用该方法的线程，会将自己的状态重新变为就绪（让掉自己抢占到cpu执行权后开始执行的时间），从而让其它具有相同优先级的等待线程获取执行权，
     * 注意是让自己或者其他线程运行，并不是单纯的让给其他线程。
     * 并不能保证在当前线程调用yield()之后，其它具有相同优先级的线程就一定能获得执行权；也有可能是当前线程又进入到“运行状态”继续运行！
     */
    public void testYield() {

    }

    public void testThreadGroup() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getThreadGroup().getName() + "=" + Thread.currentThread().getName() + ": 天下风云出我辈");
            }
        };

        // 开启两个线程
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);

        ThreadGroup g1 = t1.getThreadGroup();
        ThreadGroup g2 = t2.getThreadGroup();

//       默认线程都是main一组
        System.out.println(g1.getName());
        System.out.println(g2.getName());
        System.out.println(Thread.currentThread().getThreadGroup().getName());

        System.out.println("-----------------------------");
        // 加入线程组的方法
        ThreadGroup tg = new ThreadGroup("武林老前辈");

        Thread t3 = new Thread(tg, runnable, "东方不败");
        Thread t4 = new Thread(tg, runnable, "任我行");

        t3.start();
        t4.start();
    }
}
