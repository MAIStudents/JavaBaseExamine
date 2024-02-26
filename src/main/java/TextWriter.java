
import java.util.concurrent.BlockingQueue;
import java.io.*;

public class TextWriter implements Runnable{
    private final BlockingQueue<String> queue;
    private final String outPutFile;

    public TextWriter(String filePath, BlockingQueue<String> queue){
        outPutFile = filePath;
        this.queue = queue;
    }
    @Override
    public void run() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outPutFile))) {
            while (!queue.isEmpty()) {
                String line = queue.take();
                writer.write(line);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
