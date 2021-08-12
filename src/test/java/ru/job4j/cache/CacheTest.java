package ru.job4j.cache;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class CacheTest {
    @Test
    public void serialTest() throws InterruptedException {
        Cache cache = new Cache();
        cache.add(new Base(1, 1));
        cache.add(new Base(2, 1));
        Base changed = new Base(1, 1);
        changed.setName("John");
        Thread t1 = new Thread(() -> cache.update(changed));
        t1.start();
        t1.join();
        Base changed2 = new Base(1, 2);
        changed2.setName("Tom");
        Thread t2 = new Thread(() -> cache.update(changed2));
        t2.start();
        t2.join();
        assertThat(cache.get(1).getVersion(), is(3));
        assertThat(cache.get(1).getName(), is("Tom"));
    }

    @Test
    public void deleteTest() throws InterruptedException {
        Cache cache = new Cache();
        cache.add(new Base(1, 1));
        cache.add(new Base(2, 1));
        Base changed = new Base(1, 1);
        changed.setName("John");
        Thread t1 = new Thread(() -> cache.delete(changed));
        t1.start();
        t1.join();
        assertNull(cache.get(1));
    }
}