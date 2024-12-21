import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FileChangeLogger {


    private static final String filePath = "/home/kostas/Desktop/java/example_file.txt";
    private static final String logFilePath = "/home/kostas/Desktop/java/logs.log";
    private static final long checkInterval = 5000;

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(new FileWatcher(filePath, logFilePath), 0, checkInterval, TimeUnit.MILLISECONDS);
    }

    static class FileWatcher implements Runnable {
        private final String filePath;
        private final String logFilePath;
        private long lastModifiedTime = -1;

        public FileWatcher(String filePath, String logFilePath) {
            this.filePath = filePath;
            this.logFilePath = logFilePath;
        }

        @Override
        public void run() {
            try {
                File file = new File(filePath);
                if (!file.exists()) {
                    System.out.println("Файл не существует: " + filePath);
                    return;
                }

                long currentModifiedTime = file.lastModified();
                if (currentModifiedTime != lastModifiedTime) {
                    logFileChange();
                    lastModifiedTime = currentModifiedTime;
                }
            } catch (IOException e) {
                System.err.println("Ошибка при чтении файла: " + e.getMessage());
            }
        }

        private void logFileChange() throws IOException {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String logMessage = "Файл был изменен: " + timestamp + "\n";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
                writer.write(logMessage);
            }
        }
    }
}
