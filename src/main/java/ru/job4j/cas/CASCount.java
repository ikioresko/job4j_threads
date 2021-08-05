package ru.job4j.cas;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public class CASCount {
    private final AtomicReference<Integer> count = new AtomicReference<>(0);

    public void increment() {
        int current;
        int next;
        do {
            current = count.get();
            next = current + 1;
        }
        while (!count.compareAndSet(current, next));
    }

    public int get() {
        int current;
        do {
            current = count.get();
        }
        while (!count.compareAndSet(current, current));
        return current;
    }
}
