import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args){
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
        String threadTask1 = "src/main/resources/file1.txt", threadTask2 = "src/main/resources/file2.txt", threadTask3 = "src/main/resources/file3.txt";
        ExecutorService exec = Executors.newFixedThreadPool(3);
        try {
            exec.execute(new TextReader(threadTask1, queue));
            exec.execute(new TextReader(threadTask2, queue));
            exec.execute(new TextWriter(threadTask3, queue));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        exec.shutdown();
    }
}
