package org.example;
import java.io.*;
import java.util.concurrent.*;

class SharedData {
    private String data;

    public SharedData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static SharedData createEndOfData() {
        return new SharedData(null);
    }

    // Метод для проверки, является ли объект концом данных
    public boolean isEndOfData() {
        return getData() == null;
    }
}


class FileProcessor implements Runnable {
    private BlockingQueue<SharedData> inputQueue;
    private BlockingQueue<SharedData> outputQueue;
    private volatile boolean isReading = true;

    public FileProcessor(BlockingQueue<SharedData> inputQueue, BlockingQueue<SharedData> outputQueue) {
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Anna\\IdeaProjects\\untitled\\src\\main\\java\\org\\example\\test1.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                SharedData sharedData = new SharedData(line);

                // Reverse строки
                sharedData.setData(new StringBuilder(sharedData.getData()).reverse().toString());

                // Помещаем данные в очередь для второго потока
                outputQueue.put(sharedData);
            }
            // Посылаем специальный объект, чтобы сообщить о завершении
            outputQueue.put(SharedData.createEndOfData());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}



class DataTransformer implements Runnable {
    private BlockingQueue<SharedData> inputQueue;
    private BlockingQueue<SharedData> outputQueue;

    public DataTransformer(BlockingQueue<SharedData> inputQueue, BlockingQueue<SharedData> outputQueue) {
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                SharedData sharedData = inputQueue.take();

                if (sharedData.isEndOfData()) {
                    // Завершаем работу после получения специального объекта
                    break;
                }

                // Перевод в верхний регистр
                sharedData.setData(sharedData.getData().toUpperCase());

                // Помещаем данные в очередь для третьего потока
                outputQueue.put(sharedData);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class FileWriterThread implements Runnable {
    private BlockingQueue<SharedData> inputQueue;

    public FileWriterThread(BlockingQueue<SharedData> inputQueue) {
        this.inputQueue = inputQueue;
    }

    @Override
    public void run() {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("output.txt")))) {
            while (true) {
                SharedData sharedData = inputQueue.take();

                if (sharedData.isEndOfData()) {
                    // Завершаем работу после получения специального объекта
                    break;
                }

                // Запись данных в отдельный файл
                writer.println(sharedData.getData());
                writer.flush();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        BlockingQueue<SharedData> queue1 = new LinkedBlockingQueue<>();
        BlockingQueue<SharedData> queue2 = new LinkedBlockingQueue<>();

        // Запуск потоков
        FileProcessor fileProcessorThread = new FileProcessor(queue1, queue2);
        DataTransformer dataTransformerThread = new DataTransformer(queue2, queue1);
        FileWriterThread fileWriterThread = new FileWriterThread(queue1);

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(fileProcessorThread);
        executorService.submit(dataTransformerThread);
        executorService.submit(fileWriterThread);

        // Ожидание завершения работы потоков
        try {
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}