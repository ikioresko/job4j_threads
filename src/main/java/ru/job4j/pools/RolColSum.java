package ru.job4j.pools;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RolColSum {
    static class Sums {
        private int rowSum;
        private int colSum;

        public int getRowSum() {
            return rowSum;
        }

        public void setRowSum(int rowSum) {
            this.rowSum = rowSum;
        }

        public int getColSum() {
            return colSum;
        }

        public void setColSum(int colSum) {
            this.colSum = colSum;
        }

        @Override
        public String toString() {
            return "Sums{"
                    + "rowSum=" + rowSum
                    + ", colSum=" + colSum
                    + '}';
        }
    }

    public static Sums[] sum(int[][] matrix, int startRow, int endRow) {
        Sums[] sums = new Sums[matrix.length];
        for (int i = startRow; i < endRow; i++) {
            int sumColumn = 0;
            int sumRow = 0;
            for (int x = 0; x < matrix[i].length; x++) {
                sumRow += matrix[i][x];
                sumColumn += matrix[x][i];
            }
            Sums s = new Sums();
            s.setColSum(sumColumn);
            s.setRowSum(sumRow);
            sums[i] = s;
        }
        return sums;
    }

    public static Sums[] asyncSum(int[][] matrix) throws ExecutionException, InterruptedException {
        int n = matrix.length;
        Sums[] sums = new Sums[n];
        Map<Integer, CompletableFuture<Sums>> futures = new HashMap<>();
        for (int x = 0; x < n; x++) {
            futures.put(x, getTask(matrix, x));
        }
        for (Integer key : futures.keySet()) {
            sums[key] = futures.get(key).get();
        }
        return sums;
    }

    public static CompletableFuture<Sums> getTask(int[][] matrix, int startRow) {
        return CompletableFuture.supplyAsync(() -> {
            Sums s = new Sums();
            for (int i = startRow; i < startRow + 1; i++) {
                int sumColumn = 0;
                int sumRow = 0;
                for (int x = 0; x < matrix.length; x++) {
                    sumRow += matrix[i][x];
                    sumColumn += matrix[x][i];
                }
                s.setColSum(sumColumn);
                s.setRowSum(sumRow);
            }
            return s;
        });
    }
}