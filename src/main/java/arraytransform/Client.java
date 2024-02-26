package arraytransform;

//import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class Client {

//    private static final Logger logger = Logger.getLogger(Client.class.getName());

    private String host = "localhost";
    private Integer port = 8843;

    private String array = "";

    public Client() {
    }

    public Client(String host, Integer port, String array) {
        this.host = host;
        this.port = port;
        this.array = array;
    }

    public void start() {
        try {
//            logger.info("Клиент инициализирован");
            System.out.println("client initialized");
            Socket server = new Socket(host, port);
            PrintWriter outputStream = new PrintWriter(server.getOutputStream());
//            outputStream.println("Привет! Я новый клиент! ###" + name);
//            outputStream.flush();
            outputStream.println(array);
            outputStream.flush();

            new Thread(() -> {
                Scanner inputStream = null;
                try {
                    inputStream = new Scanner(server.getInputStream());

                    while (inputStream.hasNext()) {
                        String text = inputStream.nextLine();
                        System.out.println(text);
                    }
                } catch (IOException e) {
//                    logger.error("Проблема при чтении сервера клиентом", e);
                    System.out.println("Problem when the client reads the server " + e);
                } finally {
                    if (inputStream != null) inputStream.close();
                    try {
                        server.close();
                    } catch (IOException ex) {
//                        logger.error("Problem when closing the connection to the server ", ex);
                        System.out.println("Problem when closing the connection to the server  " + ex);
                    }
                }
            }).start();

            Scanner inputMessage = new Scanner(System.in);
            while (inputMessage.hasNext()) {
                outputStream.println(inputMessage.nextLine());
                outputStream.flush();
            }
            outputStream.close();
        } catch (IOException e) {
//            logger.error("Error when connecting to the server", e);
            System.out.println("Error when connecting to the server " + e);
        }

    }
}
