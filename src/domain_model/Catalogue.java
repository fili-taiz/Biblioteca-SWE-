package domain_model;
import java.util.ArrayList;
import java.util.Comparator;

public class Catalogue{
    ArrayList<Item> items;

    public ArrayList<Item> searchItem(String keyword, Category category) {
        ArrayList<Item> result = new ArrayList<>();
        for (Item i : this.items) {
            if (i.getTitle().equals(keyword) && i.getCategory().equals(category)) {
                result.add(i);
            }
        }
        return result;
    }


    public void addItem(Item item){
        items.add(item);
    }

    public void removeItem(Item item){
        items.remove(item);
    }

    public Item getItem(Item item){
        return items.get(items.indexOf(item));
    }



}
