package ru.job4j.search;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelSearch<T> extends RecursiveTask<Integer> {
    private final T[] array;
    private final T element;
    private final int from;
    private final int to;

    public ParallelSearch(T[] array, T element, int from, int to) {
        this.array = array;
        this.element = element;
        this.from = from;
        this.to = to;
    }

    private int find(T[] array) {
        int index = -1;
        for (int i = from; to >= i; i++) {
            if (element.equals(array[i])) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    protected Integer compute() {
        if (to - from <= 10) {
            return find(array);
        }
        int mid = (from + to) / 2;
        ParallelSearch<T> psLeft = new ParallelSearch<>(array, element, from, mid);
        ParallelSearch<T> psRight = new ParallelSearch<>(array, element, mid + 1, to);
        psLeft.fork();
        psRight.fork();
        return Math.max(psLeft.join(), psRight.join());
    }

    public static Integer getIndex(Object[] array, Object element) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return forkJoinPool.invoke(new ParallelSearch<>(array, element, 0, array.length - 1));
    }

    @Override
    public String toString() {
        return "ParallelSearch{"
                + "array=" + Arrays.toString(array)
                + ", element=" + element
                + ", from=" + from
                + ", to=" + to
                + '}';
    }
}