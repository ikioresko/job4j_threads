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
        int r = ParallelSearch.getIndex(getArray(10), "Hello5");
        assertThat(r, is(5));
    }

    @Test
    public void findIndexOfElementWhenArrayLengthIs10000000() {
        int r = ParallelSearch.getIndex(getArray(10000000), "Hello5000000");
        assertThat(r, is(5000000));
    }

    @Test
    public void findIndexOfElementWhenElementIsAbsent() {
        int r = ParallelSearch.getIndex(getArray(10), "Hello15");
        assertThat(r, is(-1));
    }

    @Test
    public void findIndexOfElementWhenElementIsAbsents() {
        int r = ParallelSearch.getIndex(getArray(100), "Hello150");
        assertThat(r, is(-1));
    }

    @Test
    public void findIndexOfElementWhenArrayLengthIs1000() {
        int r = ParallelSearch.getIndex(getArray(1000), "Hello322");
        assertThat(r, is(322));
    }
}