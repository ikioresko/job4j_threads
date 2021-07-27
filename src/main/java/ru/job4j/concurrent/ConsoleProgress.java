package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {
    private final Character[] charList = {'-', '\\', '|', '/'};

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                for (Character ch : charList) {
                    System.out.print("\r Loading: " + ch);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        Thread.sleep(5000);
        progress.interrupt();
        System.out.println(" Completed");
    }
}
