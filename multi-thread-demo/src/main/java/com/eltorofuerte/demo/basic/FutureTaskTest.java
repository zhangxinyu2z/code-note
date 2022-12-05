package com.eltorofuerte.demo.basic;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author xinyu.zhang
 * @since 2022/12/5 13:36
 */
public class FutureTaskTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 第一次看到FutureTask时，相信大家会震惊：啥玩意，怎么把Callable往FutureTask里塞呢？！
        FutureTask<String> futureTask =
            // 传入callable
//            new FutureTask<>(new Callable<String>() {
//                @Override
//                public String call() throws Exception {
//                    System.out.println(Thread.currentThread().getName() + "========>正在执行");
//                    try {
//                        Thread.sleep(3 * 1000L);
//                        for (int i = 3; i > 0; i--) {
//                            System.out.println(Thread.currentThread().getName() + "倒计时： " + i);
//                        }
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    return "success";
//                }
//            });
            // 传入runnable
            new FutureTask<>(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "========>正在执行");
                    try {
                        Thread.sleep(3 * 1000L);
                        for (int i = 3; i > 0; i--) {
                            System.out.println(Thread.currentThread().getName() + "倒计时： " + i);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "success");


        // 看到这，你再次震惊：啥玩意，怎么又把FutureTask塞到Thread里了呢？！
        new Thread(futureTask).start();
        System.out.println(Thread.currentThread().getName() + "========>启动任务");

        // 可以获取异步结果（会阻塞3秒）
        String result = futureTask.get();
        System.out.println("任务执行结束，result====>" + result);
    }
}
