package ru.job4j.concurrent;

public class ThreadState {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName());
        Thread first = new Thread(() -> System.out.println(Thread.currentThread().getName()));
        Thread second = new Thread(() -> System.out.println(Thread.currentThread().getName()));
        first.start();
        second.start();
        while (first.getState() != Thread.State.TERMINATED
                && second.getState() != Thread.State.TERMINATED) {
            System.out.println(first.getState());
            System.out.println(second.getState());
        }
        System.out.println(first.getName() + ": " + first.getState()
                + System.lineSeparator()
                + second.getName() + ": " + second.getState()
                + System.lineSeparator()
                + "Work completed");
    }
}