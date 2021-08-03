package ru.job4j;

import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class CountBarrier {
    private final Object monitor = this;

    private final int total;

    private int count = 0;

    public CountBarrier(final int total) {
        this.total = total;
    }

    public void count() {
        synchronized (monitor) {
            while (count != total) {
                System.out.println("count " + Thread.currentThread().getName());
                count++;
                monitor.notifyAll();
            }
        }
    }

    public void await() {
        synchronized (monitor) {
            while (count < total) {
                try {
                    System.out.println("await " + Thread.currentThread().getName());
                    monitor.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) {
        CountBarrier b = new CountBarrier(5);
        Thread master = new Thread(() -> {
            b.count();
            System.out.println(Thread.currentThread().getName() + " work");
        }, "master");
        Thread slave = new Thread(() -> {
            b.await();
            System.out.println(Thread.currentThread().getName() + " work");
        }, "slave");
        master.start();
        slave.start();
    }
}
