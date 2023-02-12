package com.br.zz.thread.test;

/**
 * @author xinyu.zhang
 * @since 2022/12/5 17:07
 */
public class ThreadDeadLockTest {

    public static void main(String[] args) {
        new MockThrad(true).start();
        new MockThrad(false).start();
    }

    static class MockThrad extends Thread {
        private boolean flag;

        public MockThrad(boolean flag) {
            super();
            this.flag = flag;
        }

        public void run() {
            if (flag) {
                synchronized (MyLock.objA) { // 线程A已经进入
                    System.err.println("if objA");
                    synchronized (MyLock.objB) { // 需要锁B，但是锁b正在被线程B使用锁住
                        System.err.println("if objB");
                    }
                }
            } else {
                synchronized (MyLock.objB) { // 线程B已进入
                    System.err.println("else objB");
                    synchronized (MyLock.objA) { // 需要锁A，但是锁A正在被线程A使用锁住
                        System.out.println("else objA");
                    }
                }
            }
        }
    }
    static class MyLock {
        public static final Object objA = new Object();
        public static final Object objB = new Object();
    }
}
