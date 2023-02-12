package com.br.zz.thread.test;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 模拟卖票窗口引入同步：
 * synchronized 关键词
 * jdk1.5 lock
 *
 *
 * @author xinyu.zhang
 * @since 2022/12/5 16:24
 */
public class SynchronizedThreadTest {

    /*
     * 线程抢占带来的问题：
     *      负数票：ticket=1的时候，3个线程经过cpu的抢占，都已经进入ticketNum>0了 所以出现负数票
     *      同数票：在对ticketNum--操作时，线程1还没有--完成，就被线程2夺取了cpu执行权
     *
     * 什么时候发生： A：多线程 B：有共享资源 C：多条语句操作共享资源，不是原子性的操作
     *
     */
    public static void main(String[] args) {
//        simulatedSellingTickets();
//        simulatedSellingTicketsSynchronized(new SynchronizedSaleTicketTask());
//        simulatedSellingTicketsSynchronized(new StaticSynchronizedSaleTicketTask());
        simulatedSellingTicketsSynchronized(new SyncByLockSaleTicketTask());

    }

    public static void simulatedSellingTickets() {
        SaleTicketThread t1 = new SaleTicketThread();
        t1.setName("窗口1");
        SaleTicketThread t2 = new SaleTicketThread();
        t2.setName("窗口2");
        SaleTicketThread t3 = new SaleTicketThread();
        t3.setName("窗口3");

        t1.start();
        t2.start();
        t3.start();
    }

    public static void simulatedSellingTicketsSynchronized(Runnable runnable) {
        new Thread(runnable, "窗口1").start();
        new Thread(runnable, "窗口2").start();
        new Thread(runnable, "窗口3").start();
    }


    static class SaleTicketThread extends Thread {
        /**
         * 售卖的票是所有窗口共享的,static避免每个窗口都有一个ticketNum=100
         */
        private static int ticketNum = 100;

        @Override
        public void run() {
            while (ticketNum > 0) {
                try {
                    // 出票没那么快，增加延迟
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(this.getName() + "正在售卖第" + (ticketNum--) + "张票");
            }
        }
    }

    static class SynchronizedSaleTicketTask implements Runnable {
        /**
         * 只创建了一个SaleTicket对象，ticketNum的数据是共享的
         */
        private int ticketNum = 100;

        @Override
        public void run() {
            // this就是SynchronizedSaleTicketThread对象，当线程进入时，就会加锁，执行完毕后，释放锁
            synchronized (this) {
                while (ticketNum > 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "正在售卖第" + (ticketNum--) + "张票");
                }
            }
        }
    }

    static class StaticSynchronizedSaleTicketTask implements Runnable {
        /**
         * 只创建了一个SaleTicket对象，ticketNum的数据是共享的
         */
        private int ticketNum = 100;
        private int num = 0;

        @Override
        public void run() {
            if (num % 2 == 0) {
                // this就是SynchronizedSaleTicketThread对象，当线程进入时，就会加锁，执行完毕后，释放锁
                synchronized (this) {
                    while (ticketNum > 0) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName() + "正在售卖第" + (ticketNum--) + "张票");
                    }
                }
            } else {
                saleTicket();
            }
            num++;
        }

        public synchronized void saleTicket() { // 如果是静态同步方法，锁对象是StaticSynchronizedSaleTicketTask.class
            while (ticketNum > 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "正在售卖第" + (ticketNum--) + "张票");
            }
        }
    }

    static class SyncByLockSaleTicketTask implements Runnable {
        // jdk1.5新特性
        private final ReentrantLock lock = new ReentrantLock();
        private int tickets = 100;

        @Override
        public void run() {
            try {
                lock.lock();// 加锁
                while (tickets > 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "正在售卖第" + (tickets--) + "张票");
                }
            } finally {
                lock.unlock(); // 释放锁
            }
        }
    }

}
