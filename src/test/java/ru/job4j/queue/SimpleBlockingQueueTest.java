package ru.job4j.queue;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class SimpleBlockingQueueTest {
    private class Consumer extends Thread {
        private final SimpleBlockingQueue<Integer> queue;

        private Consumer(final SimpleBlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            synchronized (queue) {
                try {
                    this.queue.poll();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private class Producer extends Thread {
        private final SimpleBlockingQueue<Integer> queue;

        private Producer(final SimpleBlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                queue.offer(1);
                queue.offer(2);
                queue.offer(3);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Test
    public void poll() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(2);
        Thread consumer = new Consumer(queue);
        Thread consumer2 = new Consumer(queue);
        Thread producer = new Producer(queue);
        consumer.start();
        consumer2.start();
        producer.start();
        producer.join();
        consumer.join();
        consumer2.join();
        assertThat(queue.poll(), is(3));
    }
}