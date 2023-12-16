import java.io.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TaskSolution {
    private static final String END_OF_STREAM = "EOF";

    private static void startFileReaderThread(String fileName, BlockingQueue<String> queue) {
        Thread fileReader = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    queue.put(line);
                }
                queue.put(END_OF_STREAM);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        fileReader.start();
    }

    public void solution(String input1, String input2, String output) {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(1000);
        startFileReaderThread(input1, queue);
        startFileReaderThread(input2, queue);

        Thread fileWriter = new Thread(() -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(output, true))) {
                String line;
                while (!(line = queue.take()).equals(END_OF_STREAM)) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        fileWriter.start();

        try {
            fileWriter.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TaskSolution taskSolution = new TaskSolution();
        taskSolution.solution("/Users/rudolfbrajninger/Documents/GitHub/JavaBaseExamine/zadacha/src/main/resources/input1.txt",
                "/Users/rudolfbrajninger/Documents/GitHub/JavaBaseExamine/zadacha/src/main/resources/input2.txt",
                "/Users/rudolfbrajninger/Documents/GitHub/JavaBaseExamine/zadacha/src/main/resources/output.txt");
    }
}
