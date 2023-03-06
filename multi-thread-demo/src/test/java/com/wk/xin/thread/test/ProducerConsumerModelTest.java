package com.wk.xin.thread.test;

import lombok.Data;

/**
 * 控制生产者生产后，消费者进行消费后，生产者再继续生产<br/>
 * 问题1：数据发生了错乱，消费者和生产者在抢夺cpu执行权的延迟和随机性导致<br/>
 * 解决：因为是多线程操作同一个资源，用同步解决<br/>
 * 问题2：消费者不顾是否有生产的数据，还是会获取没有生产的数据(默认值，name=null,age=0)，另外生产者可能一直生产，消费者一直消费<br/>
 * 解决：使用wait和notify,结合标记判断（有数据让消费者消费完后再生产），生产者和消费者来你来我往的操作<br/>
 *
 * @author xinyu.zhang
 * @since 2022/12/5 17:27
 */
public class ProducerConsumerModelTest {
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
                synchronized (stu) { // 生产者消费者操作同一资源，延迟性和随机性的问题
                    if (stu.flag) { // 如果生产者已经生产了数据
                        try {
                            stu.wait(); // 让生产者等待，释放锁
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (num % 2 == 0) {
                        stu.setName("东方不败");
                        stu.setAge(28);
                    } else {
                        stu.setName("任我行");
                        stu.setAge(42);
                    }

                    stu.flag = true; // 标记生产者已经生产了数据
                    stu.notify(); // 唤醒消费者，消费者还需要抢夺cpu执行权，如果还是被生产者抢到了，生产者wait等待
                    // 被唤醒的线程从wait处开始醒来执行，下面的代码
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
                synchronized (stu) {
                    if (!stu.flag) { // 如果生产者没有生产数据，让消费者等待，等着生产者生产数据，并释放锁
                        try {
                            stu.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(stu.getName() + ": " + stu.getAge());
                    stu.flag = false; // 标记消费者使用完数据
                    stu.notify(); // 唤醒生产者，生产者同样需要抢夺cpu执行权，即便暂时没有抢到，让消费者得去，但是消费者会wait
                }
            }
        }
    }

    @Data
    static class Student {
        private String name;
        private int age;
        boolean flag;
    }
}
