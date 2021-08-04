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

    private int getSize() {
        synchronized (queue) {
            return queue.size();
        }
    }

    public void offer(T value) {
        synchronized (queue) {
            System.out.println(Thread.currentThread().getName() + " offer producer start");
            while (getSize() == size) {
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            queue.offer(value);
            queue.notifyAll();
            System.out.println(Thread.currentThread().getName() + " offer producer fin");
        }
    }

    public T poll() {
        synchronized (queue) {
            System.out.println(Thread.currentThread().getName() + " consumer poll start");
            try {
                while (getSize() < 1) {
                    queue.wait();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            T t = queue.poll();
            queue.notifyAll();
            System.out.println(Thread.currentThread().getName() + " consumer poll fin " + t);
            return t;
        }
    }
}