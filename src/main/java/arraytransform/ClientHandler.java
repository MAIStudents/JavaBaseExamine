package arraytransform;

//import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class ClientHandler implements Runnable {

//    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());

    private Socket client;
    private Server server;

    public ClientHandler(Socket client, Server server) {
        this.client = client;
        this.server = server;
    }

    @Override
    public void run() {
        Scanner inputStream = null;
        try {
            inputStream = new Scanner(client.getInputStream());

            while (inputStream.hasNext()) {
                String text = inputStream.nextLine();
//                logger.info("Message from the client: " + text);
                System.out.println("Message from the client: " + text);

                    sendMessage("Array " + ProcessArray(text));

            }
        } catch (IOException e) {
//            logger.error("Error when working with a client ", e);
            System.out.println("Error when working with a client " + e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }

            if (client != null) {
                try {
                    client.close();
                } catch (IOException ex) {
//                    logger.error("Error when closing client ", ex);
                    System.out.println("Error when closing client " + ex);
                }
            }
        }
    }

    public void sendMessage(String message) {
        PrintWriter outputStream = null;
        try {
            outputStream = new PrintWriter(client.getOutputStream());
            outputStream.println(message);
            outputStream.flush();
        } catch (IOException e) {
//            logger.error("Problem while writing in clients stream " + client.toString(), e);
            System.out.println("Problem while writing in clients thread " + client.toString() + " " + e);
        }
    }

    private String ProcessArray(String arr) {

        String[] stringArray = arr.split(",");

        int[] intArray = new int[stringArray.length];

        int left = 0;
        int right = stringArray.length - 1;
        int leftNum = Integer.parseInt(stringArray[left]);
        int rightNum = Integer.parseInt(stringArray[right]);
        int ptr = 0;

        while (left < right) {

            if (Abs(leftNum) < Abs(rightNum)) {
                intArray[ptr] = rightNum * rightNum;
                right--;
                rightNum = Integer.parseInt(stringArray[right]);
            } else {
                intArray[ptr] = leftNum * leftNum;
                left++;
                leftNum = Integer.parseInt(stringArray[left]);
            }
            ptr++;
        }

        intArray[ptr] = Integer.parseInt(stringArray[left]);

        return Arrays.toString(intArray);
    }

    private int Abs(int x) {
        if (x > 0) {
            return x;
        }
        return -x;
    }
}
