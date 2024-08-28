
public record ClientRecord(String name,
                           String email) {

    public static final String MESSAGE = "PONG";

    public static void printMessage(){
        System.out.println("PING: " + MESSAGE);
    }

    public void printName(){
        System.out.println("Name: " + name);
    }
}
