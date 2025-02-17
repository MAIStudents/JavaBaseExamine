package org.exam;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.*;

class Main {

  private static final long threadsCount = Runtime.getRuntime().availableProcessors();
  private static final int threadBuffer = 1024;

  private static final Map<String, Integer> map = new ConcurrentHashMap<>();

  public static void find(String inputFilename) {
    try (RandomAccessFile file = new RandomAccessFile(new File(inputFilename), "r")) {
      long size = file.length();
      ExecutorService executorService = Executors.newFixedThreadPool((int) threadsCount);
      for (long i = 0; i < size; i += threadBuffer) {
        final long startPos = i;
        final long endPos = Math.min((i + threadBuffer), size);
        executorService.submit(() -> {
          try (RandomAccessFile file1 = new RandomAccessFile(new File(inputFilename), "r")) {
            for (long j = startPos; j < endPos; j++) {
              byte[] bytes = new byte[3];
              file1.seek(j);
              file1.read(bytes, 0, 3);
              String currentLine = (new String(bytes, StandardCharsets.UTF_8)).toLowerCase();
              if (currentLine.isEmpty() ||
                      currentLine.contains(" ") ||
                      currentLine.contains("\t") ||
                      currentLine.contains("\n") ||
                      currentLine.contains("\r")) {
                continue;
              }
              map.putIfAbsent(currentLine, 0);
              map.put(currentLine, map.get(currentLine) + 1);
            }
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });
      }
      executorService.shutdown();
      if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
        executorService.shutdownNow();
        throw new InterruptedException();
      }
      var res = map.entrySet().stream()
              .sorted((a, b) -> b.getValue() - a.getValue())
              .limit(10)
              .toList();
      for (var entry : res) {
        System.out.println(entry.getKey() + " " + entry.getValue());
      }

    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public static void main(String[] args) throws IOException {
    find("src/main/java/org/example/abc.txt");
  }

}