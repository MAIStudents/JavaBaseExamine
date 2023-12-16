package org.example;

public class ClientMain {
    public static void main(String[] args) {
        Client client = new Client("localhost", 8843);
        client.start();
    }
}
