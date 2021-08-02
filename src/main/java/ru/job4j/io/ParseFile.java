package ru.job4j.io;

import java.io.*;
import java.util.function.Predicate;

public class ParseFile {
    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    public synchronized String getContent(Predicate<Character> filter) throws IOException {
        StringBuilder output = new StringBuilder();
        try (InputStream i = new FileInputStream(file)) {
            int data;
            while ((data = i.read()) > 0) {
                if (filter.test((char) data)) {
                    output.append((char) data);
                }
            }
        }
        return output.toString();
    }

    public static void main(String[] args) throws IOException {
        ParseFile parse = new ParseFile(new File("C:\\test2\\stats.txt"));
        String str = parse.getContent(x -> x < 0x80);
        System.out.println(str);
        str = parse.getContent(x -> x > 0);
        System.out.println(str);
        SaveFile save = new SaveFile(new File("C:\\test2\\result.txt"));
        save.saveContent(str);
    }
}