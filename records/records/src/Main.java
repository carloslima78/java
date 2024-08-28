import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        var clientClass  = new ClientClass("Carlos", "carlos@email.com");
        System.out.println(clientClass);
        
        var clientRecord = new ClientRecord("Kelli", "kelli@email.com");
        System.out.println(clientRecord);
        clientRecord.printName();
        ClientRecord.printMessage();

        List<ClientRecord> clients=  new ArrayList<>();
        clients.add(new ClientRecord("João", "joao@email.com"));
        clients.add(new ClientRecord("Maria", "maria@email.com"));
        clients.forEach(client -> System.out.println(client.name()));

        var address1 = new AddressRecord("123 Main St", "Springfield", "IL", "62704");
        var address2 = new AddressRecord("1234 Main St", "Springfield", "IL", "62704");
        System.out.println("Address 1: " + address1);
        System.out.println("Address 2: " + address2);
        System.out.println(address2.toString());
        System.out.println(address1.toString());
        System.out.println("Addresses are equal: " + address1.equals(address2));
    }
}