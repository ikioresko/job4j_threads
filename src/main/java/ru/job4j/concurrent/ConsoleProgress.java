package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {
    @Override
    public void run() {
        Character[] charArray = {'-', '\\', '|', '/'};
        while (!Thread.currentThread().isInterrupted()) {
            try {
                for (Character ch : charArray) {
                    System.out.print("\r Loading: " + ch);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println(" Completed");
    }

    public static void main(String[] args) throws InterruptedException {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        Thread.sleep(5000);
        progress.interrupt();
    }
}
