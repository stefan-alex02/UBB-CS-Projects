package org.example;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
public class ConcurrentTaskRunner {
    ReentrantLock locker = new ReentrantLock();
    private final Condition notEmptyOrFinished = locker.newCondition();
    static Semaphore queueSem = new Semaphore(1);
    private Boolean isFinished = false;
    int workerCount = 0;
    ExecutorService workers;
    private final ExecutorService loopThread;
    Queue<Runnable> itemQ = new LinkedList<>();
    public ConcurrentTaskRunner(int workerCount) throws InterruptedException {
        //creates workerCount workers
        // Create and start a separate thread for each worker via metoda Consume
        this.workerCount = workerCount;
        workers = Executors.newFixedThreadPool(workerCount);

        loopThread = Executors.newSingleThreadExecutor();
        loopThread.execute(()->
        {
            try {
                consume();
                loopThread.shutdown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Producer: add an action to be added to the Queue
     * @param item - delegate method to be consumed
     */
    public void enqueueItem(Runnable item) throws InterruptedException {
        queueSem.acquire();
        itemQ.add(item);
        queueSem.release();

        locker.lock();
        notEmptyOrFinished.signal();
        locker.unlock();
    }

    /**
     * Consume an action from the queue
     */
    void consume() throws InterruptedException {
        boolean isEmpty;
        while (true) { // Keep consuming until told otherwise.

            Runnable task = null;

            queueSem.acquire();
            locker.lock();
            while (itemQ.isEmpty()) {
                queueSem.release();
                notEmptyOrFinished.await();

                if (isFinished) {
                    locker.unlock();
                    return;
                }

                queueSem.acquire();
            }
            locker.unlock();
            task = itemQ.poll();
            queueSem.release();

            if (task != null) {
                workers.execute(task);
            }
        }
    }

    /**
     * Stop the process of task execution and wait till all already existing task
     * in the threadpool are done in case of waitForWorkers param is true
     * @param waitForWorkers
     * @throws InterruptedException
     */
    public void shutdown(boolean waitForWorkers) throws InterruptedException {
        // Enqueue one null item per worker to make each exit.
        // Wait for workers to finish if waitForWorkers is true
        if (waitForWorkers) {
            workers.shutdown();
        }
        else {
            workers.shutdownNow();
        }

        locker.lock();
        isFinished = true;
        notEmptyOrFinished.signal();
        locker.unlock();
    }
}
