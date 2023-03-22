package com.zwh.thread;

public class TestNotifyAll {

    private static Object resourceA = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread threadA = new Thread(() -> {
            synchronized (resourceA) {
                System.out.println("ThreadA get resourceA lock");
                try {
                    System.out.println("ThreadA begin wait");
                    resourceA.wait();
                    System.out.println("ThreadA end wait");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread threadB = new Thread(() -> {
            synchronized (resourceA) {
                System.out.println("ThreadB get resourceA lock");
                try {
                    System.out.println("ThreadB begin wait");
                    resourceA.wait();
                    System.out.println("ThreadB end wait");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread threadC = new Thread(() -> {
            synchronized (resourceA) {
                System.out.println("ThreadC begin notifyAll");
                resourceA.notifyAll();
            }
        });

        threadA.start();
        threadB.start();

        Thread.sleep(1000);
        threadC.start();

        threadA.join();
        threadB.join();
        threadC.join();

        System.out.println("main over");
    }
}
