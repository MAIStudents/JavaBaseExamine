package org.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public class Main {
    private static final Object lock = new Object();
    private static final Queue<String> queue = new ArrayDeque<>();

    public static void main(String[] args) {
        // Создаём потоки
        Thread producerThread = new Thread(new Producer());
        Thread consumerThread = new Thread(new Consumer());

        // Запускаем потоки
        producerThread.start();
        consumerThread.start();
    }

    static class Producer implements Runnable {
        @Override
        public void run() {
            Random random = new Random();
            try {
                while (true) {
                    Thread.sleep(random.nextInt(1000)); // Случайный момент времени
                    synchronized (lock) {
                        String data = "Producer: " + System.currentTimeMillis();
                        queue.offer(data); // Добавляем строку в очередь
                        System.out.println("Первый поток отправил: " + data);
                        lock.notify(); // Уведомляем ожидающий поток (Consumer), что появились новые данные
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Except with Producer");
            }
        }
    }

    static class Consumer implements Runnable {
        @Override
        public void run() {
            Random random = new Random();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
                while (true) {
                    synchronized (lock) {
                        while (queue.isEmpty()) {
                            try {
                                Thread.sleep(random.nextInt(1000)); // Случайный момент времени
                                lock.wait(); // Ждем, пока не появятся новые данные
                            } catch (InterruptedException e) {
                                System.out.println("Except with wait");
                            }
                        }
                        String data = queue.poll(); // Забираем строку из очереди
                        System.out.println("Второй поток получил: " + data);
                        writer.write(data + "\n");
                        writer.flush();
                    }
                }
            } catch (IOException e) {
                System.out.println("Except with Producer");
            }
        }
    }
}