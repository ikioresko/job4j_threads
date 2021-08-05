package ru.job4j.queue;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {
    @GuardedBy("queue")
    private final Queue<T> queue = new LinkedList<>();
    private final int size;

    public SimpleBlockingQueue(int size) {
        this.size = size;
    }

    public void offer(T value) throws InterruptedException {
        synchronized (queue) {
            System.out.println(Thread.currentThread().getName() + " offer producer start");
            while (queue.size() == size) {
                    queue.wait();
            }
            queue.offer(value);
            queue.notifyAll();
            System.out.println(Thread.currentThread().getName() + " offer producer fin");
        }
    }

    public T poll() throws InterruptedException {
        synchronized (queue) {
            System.out.println(Thread.currentThread().getName() + " consumer poll start");
                while (queue.size() < 1) {
                    queue.wait();
                }
            T t = queue.poll();
            queue.notifyAll();
            System.out.println(Thread.currentThread().getName() + " consumer poll fin " + t);
            return t;
        }
    }
}