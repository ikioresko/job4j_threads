package ru.job4j.concurrent;

public class Wget {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            try {
                for (int i = 0; i < 101; i++) {
                    System.out.print("\rLoading : " + i + "%");
                    Thread.sleep(500);
                }
                System.out.println(" Completed");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        );
        thread.start();
    }
}
