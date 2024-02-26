import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.*;

    public class ThreadsExample {

        private static Data data;

        public static void main(String[] args) throws IOException, InterruptedException {
            BlockingQueue<Data> queue = new LinkedBlockingQueue<>();

            Thread reader = new Thread(() -> {
                try {
                    File file = new File("input.txt");
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        Data data = new Data(line);
                        queue.offer(data);
                    }
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            Thread transformer = new Thread(() -> {
                try {
                    Data data;
                    while ((data = queue.take()) != null) {
                        String reversed = new StringBuilder(data.getValue()).reverse().toString();
                        data.setValue(reversed);
                        queue.offer(data);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            Thread writer;
            writer = new Thread(() -> {
                try {
                    FileWriter fileWriter = new FileWriter("output.txt");
                    while ((data = queue.take()) != null) {
                        fileWriter.write(data.getValue().toUpperCase() + "\n");
                    }
                    fileWriter.close();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });

            reader.start();
            transformer.start();
            writer.start();

            reader.join();
            transformer.join();
            writer.join();
        }
    }
