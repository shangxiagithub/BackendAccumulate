package com.zwh.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestSleep {

    private static final Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        Thread threadA = new Thread(() -> {
            lock.lock();
            try{
                System.out.println("child threadA is in sleep");
                Thread.sleep(10000);
                System.out.println("child threadA is in awake");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally{
                lock.unlock();
            }
        });

        Thread threadB = new Thread(() -> {
            lock.lock();
            try{
                System.out.println("child threadB is in sleep");
                Thread.sleep(10000);
                System.out.println("child threadB is in awake");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally{
                lock.unlock();
            }
        });

        threadA.start();
        threadB.start();
    }
}
