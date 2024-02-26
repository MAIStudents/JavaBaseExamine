import java.io.*;
import java.util.concurrent.BlockingQueue;

public class TextReader implements Runnable {
    private final BlockingQueue<String> queue;
    private final String filename;
    public TextReader(String filePath, BlockingQueue<String> queue){
        filename = filePath;
        this.queue = queue;

    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                queue.put(line);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
