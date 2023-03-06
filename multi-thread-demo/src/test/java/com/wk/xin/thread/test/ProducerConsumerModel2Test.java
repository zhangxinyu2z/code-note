package com.wk.xin.thread.test;

import lombok.Data;

/**
 *
 * @author xinyu.zhang
 * @since 2022/12/5 17:27
 */
public class ProducerConsumerModel2Test {
    public static void main(String[] args) {
        Student s = new Student();
        Thread producer = new Thread(new Producer(s));
        Thread consumer = new Thread(new Consumer(s));

        producer.start();
        consumer.start();
    }

    static class Producer implements Runnable {
        private Student stu;
        private int num = 0;

        public Producer(Student stu) {
            this.stu = stu;
        }

        @Override
        public void run() {
            while (true) {
                if (num % 2 == 0) {
                    stu.set("东方不败", 45);
                } else {
                    stu.set("任我行", 48);
                }
                num++;
            }
        }
    }

    static class Consumer implements Runnable {
        private Student stu;

        public Consumer(Student stu) {
            this.stu = stu;
        }

        @Override
        public void run() {
            while (true) {
                stu.get();
            }
        }
    }


    @Data
    static class Student {
        private String name;
        private int age;
        private boolean flag;

        public Student() {
            super();
        }

        public Student(String name, int age) {
            super();
            this.name = name;
            this.age = age;
        }

        public synchronized void set(String name, int age) {
            if (flag) { // true:生产者生产了数据
                try {
                    this.wait(); // 让生产者线程等待，释放锁
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 否则就是没有数据
            this.name = name;
            this.age = age;
            this.flag = true; // 生产好了
            this.notify(); // 唤醒消费者，去消费，消费者需要抢夺cpu执行权
        }

        public synchronized void get() {
            if (!flag) {
                try {
                    this.wait(); // 没有数据，让消费者等待，释放锁，等着生产者生产数据
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 有数据
            System.out.println(this.name + ": " + this.age);
            this.flag = false; // 消费完了
            this.notify(); // 唤醒生产者，去生产数据，生产者需要抢夺cpu执行权
        }
    }
}
