package org.example;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class MainServer {


    static public void doWork() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            while (true) {
                try (Socket client = serverSocket.accept()) {
                    System.out.println("Клиент подключен");
                    try (PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                         BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
                        while (true) {
                            var result = clientHandler(buf.readLine());
                            System.out.println(result);
                            out.println(result);
                            out.flush();
                        }
                    }
                }
            }
        }
    }

    public static double clientHandler(String numberString) {
        System.out.println(numberString);
        if (numberString != null && !numberString.isEmpty()) {
            List<Integer> arr = Arrays.stream(numberString.trim().split(" ")).map(Integer::parseInt).sorted().toList();
            if (arr.size() % 2 != 0) {
                return arr.get(arr.size() / 2);
            }
            else {
                return (arr.get(arr.size() / 2) + arr.get(arr.size() / 2 - 1)) / 2.0;
            }
        }
        else {
            return -1;
        }
    }

    public static void main(String[] args) throws IOException {
        MainServer.doWork();
    }
}
