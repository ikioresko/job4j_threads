package ru.job4j.cas;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CASCountTest {
    @Test
    public void when2ThreadIncrementAndGet() throws InterruptedException {
        CASCount cas = new CASCount();
        final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
        Thread threadInc1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                cas.increment();
                buffer.add(cas.get());
            }
        });
        Thread threadInc2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                cas.increment();
                buffer.add(cas.get());
            }
        });
        threadInc1.start();
        threadInc1.join();
        threadInc2.start();
        threadInc2.join();
        assertThat(buffer, is(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)));
    }
}