import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        var fileWatcherThread = new Thread(new FileMonitor());
        fileWatcherThread.start();
    }
}

/**
 * Идея заключается в сл.: переодически проверять файл на изменение (с помощью MessageDigest получаем хэш файла).
 * Если измененем, то залогировать это, иначе поток засыпает на еще один интервал.
 */

class FileMonitor implements Runnable {
    private static final String CHECKED_FILE = "input.txt";
    private static final String LOG_FILE = "changes.log";
    private static final long CHECK_INTERVAL_IN_SECONDS = 5;
    private String lastFileHash = "";

    @Override
    public void run() {
        while (true) {
            try {
                checkFileForChanges();
                TimeUnit.SECONDS.sleep(CHECK_INTERVAL_IN_SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void checkFileForChanges() {
        try {
            String currentFileHash = getFileHash(CHECKED_FILE);
            if (!currentFileHash.equals(lastFileHash)) {
                lastFileHash = currentFileHash;
                logFileChange();
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private String getFileHash(String fileName) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] fileBytes = Files.readAllBytes(Paths.get(fileName));
        byte[] hashBytes = digest.digest(fileBytes);
        StringBuilder hexString = new StringBuilder();

        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
    }

    private void logFileChange() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write("Файл: " + CHECKED_FILE + " модифицирован в " + java.time.LocalDate.now());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}