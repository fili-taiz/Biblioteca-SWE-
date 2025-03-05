
import orm.*;

public class Main {
    public static void main(String[] args) {
        clearScreen();
        ConnectionManager cm = new ConnectionManager();
        cm.query("Select * From Hirer");
        System.out.println("oiuoiu");
        
    }

    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }  
}