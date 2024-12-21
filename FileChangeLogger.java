import java.io.*;
import java.text.SimpleDateFormat;
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
        private String previousFileContent = "";

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
                    logFileChange(file);
                    lastModifiedTime = currentModifiedTime;
                }
            } catch (IOException e) {
                System.err.println("Ошибка при чтении файла: " + e.getMessage());
            }
        }

        private void logFileChange(File file) throws IOException {
            StringBuilder currentFileContent = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    currentFileContent.append(line);
                }
            }

            String oldContent = previousFileContent;
            String newContent = currentFileContent.toString();
            StringBuilder addedContent = new StringBuilder();
            StringBuilder removedContent = new StringBuilder();

            int i = 0, j = 0;
            while (i < oldContent.length() || j < newContent.length()) {
                if (i < oldContent.length() && j < newContent.length() && oldContent.charAt(i) == newContent.charAt(j)) {
                    i++;
                    j++;
                } else if (i < oldContent.length()) {
                    removedContent.append(oldContent.charAt(i));
                    i++;
                } else if (j < newContent.length()) {
                    addedContent.append(newContent.charAt(j));
                    j++;
                }
            }

            if (addedContent.length() > 0 || removedContent.length() > 0) {
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
                StringBuilder logMessage = new StringBuilder();
                logMessage.append("Файл был изменен: ").append(timestamp).append("\n");

                if (addedContent.length() > 0) {
                    logMessage.append("Добавленные символы: ").append(addedContent.toString()).append("\n");
                }

                if (removedContent.length() > 0) {
                    logMessage.append("Удалённые символы: ").append(removedContent.toString()).append("\n");
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
                    writer.write(logMessage.toString());
                }

                previousFileContent = currentFileContent.toString();
            }
        }
    }
}
