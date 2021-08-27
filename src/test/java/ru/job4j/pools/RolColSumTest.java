package ru.job4j.pools;

import org.junit.Test;
import ru.job4j.synch.SingleLockList;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static ru.job4j.pools.RolColSum.asyncSum;

public class RolColSumTest {
    @Test
    public void synchro() throws InterruptedException {
        SingleLockList<RolColSum.Sums[]> list = new SingleLockList<>(new ArrayList<>());
        int[][] array = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        Thread t1 = new Thread(() -> list.add(RolColSum.sum(array, 0, 1)));
        Thread t2 = new Thread(() -> list.add(RolColSum.sum(array, 1, 2)));
        Thread t3 = new Thread(() -> list.add(RolColSum.sum(array, 2, 3)));
        t1.start();
        t1.join();
        t2.start();
        t2.join();
        t3.start();
        t3.join();
        List<Integer> rsl = new ArrayList<>();
        rsl.add(list.get(0)[0].getColSum());
        rsl.add(list.get(0)[0].getRowSum());
        rsl.add(list.get(1)[1].getColSum());
        rsl.add(list.get(1)[1].getRowSum());
        rsl.add(list.get(2)[2].getColSum());
        rsl.add(list.get(2)[2].getRowSum());
        assertThat(rsl, is(List.of(12, 6, 15, 15, 18, 24)));
    }

    @Test
    public void async() throws InterruptedException, ExecutionException {
        int[][] array = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        List<Integer> rsl = new ArrayList<>();
        for (RolColSum.Sums sums : asyncSum(array)) {
            rsl.add(sums.getColSum());
            rsl.add((sums.getRowSum()));
        }
        assertThat(rsl, is(List.of(12, 6, 15, 15, 18, 24)));
    }
}
