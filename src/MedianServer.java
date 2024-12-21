import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MedianServer {
    public static void main(String[] args) {
        int port = 12345;
        ExecutorService executor = Executors.newFixedThreadPool(4);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.submit(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            while (true) {
                int[] numbers = (int[]) input.readObject();

                if (numbers.length == 0) {
                    System.out.println("Клиент завершил соединение.");
                    break;
                }

                System.out.println("Получен набор чисел: " + Arrays.toString(numbers));

                double median = calculateMedian(numbers);
                System.out.println("Вычисленная медиана: " + median);

                output.writeObject(median);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка при обработке клиента: " + e.getMessage());
        }
    }

    private double calculateMedian(int[] numbers) {
        Arrays.sort(numbers);
        int n = numbers.length;
        if (n % 2 == 0) {
            return (numbers[n / 2 - 1] + numbers[n / 2]) / 2.0;
        } else {
            return numbers[n / 2];
        }
    }
}