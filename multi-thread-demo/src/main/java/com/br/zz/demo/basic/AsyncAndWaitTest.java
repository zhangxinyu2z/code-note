package com.br.zz.demo.basic;

import java.util.concurrent.*;

/**
 * 创建线程的只有thread, runnable和callable只是任务类
 */
public class AsyncAndWaitTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //        createThread();
        System.out.println(Thread.currentThread().getName() + " 主线程开始");
        Worker worker = new Worker();
        worker.begin();
        System.out.println(Thread.currentThread().getName() + " 主线程结束");
        Thread.sleep(4 * 1000L);
    }

    static class Worker implements Runnable {
        public void begin() {
            new Thread(this).start();
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "执行Worker#run() begin");
            try {
                Thread.sleep(3 * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "执行Worker#run() end");
        }
    }

    public static void createThread() throws ExecutionException, InterruptedException {
        // 方式1：重写Thread#run()
        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "========>正在执行");
            }
        };
        thread.start();

        // 方式2：构造方法传入Runnable实例
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "========>正在执行");
        }).start();

        // 方式3：线程池 + Callable/Runnable，这里以Callable为例
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> submit = executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName() + "========>正在执行");
            Thread.sleep(3 * 1000L);
            return "success";
        });
        String result = submit.get();
        System.out.println("result=======>" + result);
        // 关闭线程池
        executorService.shutdown();
    }
}