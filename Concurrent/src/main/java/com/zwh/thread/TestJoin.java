package com.zwh.thread;

public class TestJoin {

    public static void main(String[] args) {
        Thread threadA = new Thread(() -> {
            System.out.println("threadA begin run!");
            for (; ; ) {

            }
        });

        Thread mainThread = Thread.currentThread();

        Thread threadB = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            mainThread.interrupt();
        });


        threadA.setDaemon(true);
        threadA.start();
        threadB.start();
        try {
            threadA.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
