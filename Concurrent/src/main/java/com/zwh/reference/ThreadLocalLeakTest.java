package com.zwh.reference;



import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * Excuse the shitty sleeps :)
 */
public class ThreadLocalLeakTest {

    @Test
    public void testLeakThreadStart() throws Exception {

        // Object that will be reference from possibly "leaky" ThreadLocal
        Object object = new Object();

        // Weak reference for testing if it's been GC'ed
        final WeakReference<Object> weakReference = new WeakReference<>(object);

        // Offending thread
        new Thread(() -> {
            ThreadLocal<Object> threadLocal = new ThreadLocal<>();
            threadLocal.set(weakReference.get());
            threadLocal = null;
            // keep thread from ending for 2000 ms
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        // Wait for thread to reach sleep
        Thread.sleep(1000);

        // Remove strong reference
        object = null;

        // Force GC, no strong refs in thread to threadLocal nor object
        System.gc();

        // object was not garbage-collected (thread still running)
        // threadLocal should actually be GC'ed, but its entry in the ThreadLocalMap remains (null key, object value)
        assertNotNull(weakReference.get());

        // Wait for thread to actually end
        Thread.sleep(2000);

        // Force GC
        System.gc();

        // Thread instance is GC'ed, together with its ThreadLocalMap, which had a strong reference (value of a pseudo-WeakHashMap) to object
        assertNull(weakReference.get());
    }

    @Test
    public void testLeakSingleThreadExecutor() throws Exception {

        // Object that will be reference from possibly "leaky" ThreadLocal
        Object object = new Object();

        // Weak reference for testing if it's been GC'ed
        final WeakReference<Object> weakReference = new WeakReference<>(object);

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Offending code
        executorService.submit(() -> {
            ThreadLocal<Object> threadLocal = new ThreadLocal<>();
            threadLocal.set(weakReference.get());
        });

        // Wait for the runnable to end (the thread will still exist in the executor)
        Thread.sleep(1000);

        // Remove strong reference
        object = null;

        // Force GC, no strong refs in thread to threadLocal nor object
        System.gc();

        // object was not garbage-collected (thread still exists)
        // threadLocal should have been GC'ed, but its entry in the ThreadLocalMap remains (null key, object value)
        assertNotNull(weakReference.get());

        // Dispose of the thread (async)
        executorService.shutdownNow();
        Thread.sleep(1000);

        // Force GC
        System.gc();

        // Thread instance is GC'ed, together with its ThreadLocalMap,
        // which had a strong reference (value of a pseudo-WeakHashMap) to object
        assertNull(weakReference.get());
    }

    @Test
    public void testNoLeakNoCachingExecutor() throws Exception {

        // Object that will be reference from possibly "leaky" ThreadLocal
        Object object = new Object();

        // Weak reference for testing if it's been GC'ed
        final WeakReference<Object> weakReference = new WeakReference<>(object);

        // Create a no-cache executor that will dispose of the thread after it's done
        final ThreadPoolExecutor executor = new ThreadPoolExecutor(0, 1, 0, TimeUnit.MICROSECONDS,
                new LinkedBlockingQueue<>());

        // Offending code
        executor.submit(() -> {
            ThreadLocal<Object> threadLocal = new ThreadLocal<>();
            threadLocal.set(weakReference.get());
        });

        // Wait for the runnable to end (the thread will be disposed of as there´s no keep-alive time)
        Thread.sleep(1000);

        // Remove strong reference
        object = null;

        // Force GC
        System.gc();

        // object is garbage collected as the thread was disposed, there is no leak
        assertNull(weakReference.get());
    }
}