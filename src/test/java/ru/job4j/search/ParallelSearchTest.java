package ru.job4j.search;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class ParallelSearchTest {
    private String[] getArray(int length) {
        String[] array = new String[length];
        String str = "Hello";
        for (int i = 0; i < length; i++) {
            array[i] = str + i;
        }
        return array;
    }

    @Test
    public void findIndexOfElementWhenArrayLengthIs10() {
        ParallelSearch<String> ps = new ParallelSearch<>();
        int r = ps.run(getArray(10), "Hello5");
        assertThat(r, is(5));
    }

    @Test
    public void findIndexOfElementWhenArrayLengthIs10000000() {
        ParallelSearch<String> ps = new ParallelSearch<>();
        int r = ps.run(getArray(10000000), "Hello5000000");
        assertThat(r, is(5000000));
    }
}