package org.example;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final Logger logger = Logger.getLogger(Client.class.getName());

    private String host = "localhost";
    private Integer port = 8843;

    public Client() {
    }

    public Client(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        try {
            logger.info("Клиент инициализирован");
            Socket server = new Socket(host, port);
            PrintWriter outputStream = new PrintWriter(server.getOutputStream());

            new Thread(() -> {
                try (Scanner inputStream = new Scanner(server.getInputStream())) {

                    while (inputStream.hasNext()) {
                        String text = inputStream.nextLine();
                        if (text.equals("###exit###")) {
                            break;
                        }
                        System.out.println(text);
                    }
                } catch (IOException e) {
                    logger.error("Проблема при чтении сервера клиентом", e);
                }
            }).start();

            Scanner inputMessage = new Scanner(System.in);
            boolean isExited = false;
            while (inputMessage.hasNext() && !isExited) {
                String messageFromUser = inputMessage.nextLine();
                outputStream.println(messageFromUser);
                outputStream.flush();
                if (messageFromUser.equals("exit")) {
                    break;
                }
            }
            outputStream.close();
        } catch (IOException e) {
            logger.error("Cannot connect to server");
        }
    }
}