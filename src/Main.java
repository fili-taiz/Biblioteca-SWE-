
import domain_model.Category;
import domain_model.Item;
import domain_model.Language;
import domain_model.Magazine;
import orm.*;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        /*clearScreen();
        ConnectionManager cm = new ConnectionManager();
        cm.query("Select * From Hirer");
        System.out.println("oiuoiu");
*/
        Object o = new Object();

        //Item magazine = new Magazine("code", "title", LocalDate.of(2012, 2, 20), Language.LANGUAGE_1, Category.CATEGORY_1, "link", true, "publishing_house_magazine");

        Magazine newMagazine = new Magazine("code_1", "title_1", LocalDate.of(2012, 2, 19), Language.LANGUAGE_1, Category.CATEGORY_1, "link_1", false, "publishing_house_magazine_1");

        newMagazine.gettt();
    }

    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }  
}