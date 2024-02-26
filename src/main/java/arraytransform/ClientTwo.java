package arraytransform;

public class ClientTwo {
    public static void main(String[] args) {
        Client client =  new Client("localhost", 8843, "-5,-4,-3,-2,-1,0");
        client.start();
    }
}
