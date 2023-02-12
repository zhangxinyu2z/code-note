package com.br.zz.demo.basic;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * 线程通信
 *
 * @author xinyu.zhang
 * @since 2022/12/7 17:00
 */
public class ThreadCommunicationTest {

    public static void main(String[] args) {
        v1();
    }

    /**
     * 队列  轮询
     */
    public static void v1() {
        // 队列
        WhileQueue<String> queue = new WhileQueue<>();

        // 生产者
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        queue.put("消息" + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        // 消费者
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        queue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 轮询版本
     */
    public static class WhileQueue<T> {
        // 容器，用来装东西
        private final LinkedList<T> queue = new LinkedList<>();

        public void put(T resource) throws InterruptedException {
            while (queue.size() >= 1) {
                // 队列满了，不能再塞东西了，轮询等待消费者取出数据
                System.out.println("生产者：队列已满，无法插入...");
                TimeUnit.MILLISECONDS.sleep(1000);
            }
            System.out.println("生产者：插入" + resource + "!!!");
            queue.addFirst(resource);
        }

        public void take() throws InterruptedException {
            while (queue.size() <= 0) {
                // 队列空了，不能再取东西，轮询等待生产者插入数据
                System.out.println("消费者：队列为空，无法取出...");
                TimeUnit.MILLISECONDS.sleep(1000);
            }
            System.out.println("消费者：取出消息!!!");
            queue.removeLast();
            TimeUnit.MILLISECONDS.sleep(5000);
        }

    }
}
