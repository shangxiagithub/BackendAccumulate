package com.zwh.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CallerTask implements Callable<String> {

    @Override
    public String call() {
        return "hello";
    }

    public static void main(String[] args) {
        FutureTask<String> task = new FutureTask<>(new CallerTask());
        new Thread(task).start();
        String result;
        try {
            result = task.get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
