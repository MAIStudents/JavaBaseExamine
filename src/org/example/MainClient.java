package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MainClient {

    public static void clientConnect() throws IOException {
        try (Socket socket = new Socket("localhost", 1234)) {
            try (PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true)) {
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNextLine()) {
                    printWriter.println(scanner.nextLine());
                    printWriter.flush();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    var message = bufferedReader.readLine();
                    if (message != null) {
                        System.out.println(message);
                    }
                }

            }
        }
    }

    public static void main(String[] args) throws IOException {
        clientConnect();
    }
}
