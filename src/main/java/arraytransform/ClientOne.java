package arraytransform;

public class ClientOne {
    public static void main(String[] args) {
        Client client = new Client("localhost", 8843, "-3,-2,-1,0,2");
        client.start();
    }
}
