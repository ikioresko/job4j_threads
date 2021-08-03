package ru.job4j;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UserStorageTest {
    private final User user1 = new User(1, 200);
    private final User user2 = new User(2, 100);

    private class StorageTest extends Thread {
        private final UserStorage storage;
        private final int amount;

        private StorageTest(final UserStorage storage, int amount) {
            this.storage = storage;
            this.amount = amount;
        }

        @Override
        public void run() {
            this.storage.add(user1);
            this.storage.add(user2);
            this.storage.transfer(1, 2, amount);
        }
    }

    @Test
    public void when2ThreadTransfer50And70Amount() throws InterruptedException {
        final UserStorage storage = new UserStorage();
        Thread first = new StorageTest(storage, 50);
        Thread second = new StorageTest(storage, 70);
        first.start();
        second.start();
        first.join();
        second.join();
        assertThat(storage.getBalance(1), is(80));
        assertThat(storage.getBalance(2), is(220));
    }

    @Test
    public void when2ThreadTransferZeroAmount() throws InterruptedException {
        final UserStorage storage = new UserStorage();
        Thread first = new StorageTest(storage, 0);
        Thread second = new StorageTest(storage, 0);
        first.start();
        second.start();
        first.join();
        second.join();
        assertThat(storage.getBalance(1), is(200));
        assertThat(storage.getBalance(2), is(100));
    }

    @Test
    public void when2ThreadTransferNegativeAmount() throws InterruptedException {
        final UserStorage storage = new UserStorage();
        Thread first = new StorageTest(storage, -50);
        Thread second = new StorageTest(storage, -50);
        first.start();
        second.start();
        first.join();
        second.join();
        assertThat(storage.getBalance(1), is(200));
        assertThat(storage.getBalance(2), is(100));
    }
}