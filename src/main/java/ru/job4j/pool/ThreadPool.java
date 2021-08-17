package ru.job4j.pool;

import net.jcip.annotations.ThreadSafe;
import ru.job4j.queue.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;

@ThreadSafe
public class ThreadPool {
    private final int size = Runtime.getRuntime().availableProcessors();
    private final SimpleBlockingQueue<Runnable> tasks;
    private final List<Thread> threads = new LinkedList<>();

    public ThreadPool(int taskQueueSize) {
        tasks = new SimpleBlockingQueue<>(taskQueueSize);
    }

    public void initPool() throws InterruptedException {
        for (int i = 0; i < size; i++) {
            Thread task = (Thread) tasks.poll();
            if (task.isInterrupted()) {
                break;
            }
            task.start();
            threads.add(task);
        }
    }

    public void work(Runnable job) throws InterruptedException {
        tasks.offer(job);
    }

    public synchronized void shutdown() {
        for (int i = 0; i < size; i++) {
            threads.get(i).interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadPool tp = new ThreadPool(10);
        Thread cons = new Thread(() -> {
            try {
                tp.initPool();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "consumer");

        Thread prod = new Thread(() -> {
            for (int i = 0; i < 15; i++) {
                try {
                    tp.work(new Thread());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "producer");
        cons.start();
        prod.start();
        prod.join();
        cons.join();
        for (Thread t : tp.threads) {
            System.out.println(t.toString());
        }
    }
}
