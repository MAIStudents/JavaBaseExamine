
import java.io.*;
import java.util.concurrent.*;

public class MultiThreadedProcessing {
    public static void main(String inputFilePath, String outputFilePath) {
        // To avoid tracking string source inside Data class
        BlockingQueue<Data> queue1 = new LinkedBlockingQueue<>();
        BlockingQueue<Data> queue2 = new LinkedBlockingQueue<>();

        // Fixed 
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Read-reverse-add
        executor.submit(() -> {
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Data data = new Data(line);
                    data.reverseString();
                    queue1.put(data);
                }
                queue1.put(new Data(null));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Read-toUpper-add
        executor.submit(() -> {
            try {
                while (true) {
                    Data data = queue1.take();
                    if (data.getString() == null) {
                        queue2.put(data);
                        break;
                    }
                    data.toUpperCase();
                    queue2.put(data);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Read-lgo
        executor.submit(() -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
                while (true) {
                    Data data = queue2.take();
                    if (data.getString() == null) {
                        break;
                    }
                    writer.write(data.getString() + "\n");
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        executor.shutdown();
    }
}

class Data {
    private String string;

    public Data(String string) {
        this.string = string;
    }

    public synchronized void reverseString() {
        if (string != null) {
            string = new StringBuilder(string).reverse().toString();
        }
    }

    public synchronized void toUpperCase() {
        if (string != null) {
            string = string.toUpperCase();
        }
    }

    public synchronized String getString() {
        return string;
    }
}
