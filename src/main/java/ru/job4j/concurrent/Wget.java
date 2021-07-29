package ru.job4j.concurrent;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

/**
 * Класс осуществляет скачивание файла по ссылке с возможностью ввода ограничения по скорости скачивания
 *
 * @author Igor Kioresko
 * @version 0.1
 */
public class Wget implements Runnable {
    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    /**
     * Метод осуществляет скачивание файла путем чтения и записи
     */
    private void read() {
        Date timeStart = new Date();
        Date timeEnd;
        int pause = 0;
        boolean pauseChecked = false;
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream("pom_tmp.xml")) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                timeEnd = new Date();
                if (!pauseChecked) {
                    pause = getPause(timeStart, timeEnd);
                    pauseChecked = true;
                }
                Thread.sleep(pause);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод вычисляет текущую скорость скачивания 1024 байт
     *
     * @param time Время затраченое на скачивание 1024 байт
     * @return Текущая скорость скачивания 1024 байт
     */
    public double getSpeed(long time) {
        double timeSec = (double) time / 1000;
        return 1024 / timeSec;
    }

    /**
     * Метод вычисляет время паузы для ограничения скорости скачивания
     *
     * @param timeStart Время начала работы метода скачивания
     * @param timeEnd   Время завершения скачивания первых 1024 байт
     * @return Возвращает время в миллисекундах необходимое для паузы
     */
    private int getPause(Date timeStart, Date timeEnd) {
        int pause;
        long time = timeEnd.getTime() - timeStart.getTime();
        double currentSpeed = getSpeed(time);
        if (speed < currentSpeed) {
            pause = (int) ((currentSpeed / speed) * 1000 - time);
        } else {
            pause = 0;
        }
        return pause;
    }

    /**
     * Метод осуществляет валидацию входящих параметров
     *
     * @param args Ссылка на файл и заданная скорость
     * @return boolean значение валидации
     */
    public static boolean check(String[] args) {
        return args.length == 2;
    }

    @Override
    public void run() {
        read();
    }

    public static void main(String[] args) throws InterruptedException {
        if (check(args)) {
            String url = args[0];
            int speed = Integer.parseInt(args[1]);
            Thread wget = new Thread(new Wget(url, speed));
            wget.start();
            wget.join();
        } else {
            System.out.println("Check your args. Args must be: LINK_TO_FILE DOWNLOAD_SPEED");
        }
    }
}