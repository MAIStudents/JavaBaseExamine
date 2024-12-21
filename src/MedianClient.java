import java.io.*;
import java.net.*;
import java.util.Scanner;

public class MedianClient {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 12345;

        try (Socket socket = new Socket(host, port);
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {
                System.out.println("Подключено к серверу на " + host + ":" + port);

                while (true) {
                    System.out.println("Введите числа через пробел (или exit для выхода): ");
                    String line = scanner.nextLine();
                    if (line.equalsIgnoreCase("exit")) {
                        output.writeObject(new int[0]);
                        break;
                    }

                    String[] parts = line.split("\\s+");
                    int[] numbers = new int[parts.length];
                    for (int i = 0; i < parts.length; i++) {
                        numbers[i] = Integer.parseInt(parts[i]);
                    }

                    output.writeObject(numbers);

                    double median = (double) input.readObject();
                    System.out.println("Медиана: " + median);
                }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка клиента: " + e.getMessage());
        }
    }
}