package arraytransform;

//import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

//    private static final Logger logger = Logger.getLogger(Server.class.getName());

    private String host = "localhost"; // 127.0.0.1
    private Integer port = 8843;

    private List<ClientHandler> clients = new ArrayList<>();

    public Server() {
    }

    public Server(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
//        logger.info("Server Initialisation");
        System.out.println("Server Initialisation");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
//            logger.info("Server have started and waiting for client");
            System.out.println("Server have started and waiting for a client");
            while (true) {
                Socket client = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(client, this);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
//            logger.error("Server error ", e);
            System.out.println("Server error " + e);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException ex) {
//                    logger.error("Error when closing a server ", ex);
                    System.out.println("Error when closing a server " + ex);
                }
            }
        }
    }
}
