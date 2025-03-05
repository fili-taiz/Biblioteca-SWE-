package domain_model;
import java.util.ArrayList;
import java.util.Comparator;

public class Catalogue implements CatalogueSearchable{
    ArrayList<Item> items;
    @Override
    public ArrayList<Item> searchItem(String keyword, String category, boolean dateSort, boolean asc){
        ArrayList<Item> result = new ArrayList<>();
        for(Item i: this.items){
            if(i.getTitle().equals(keyword) && i.getCategory().equals(category)){
                result.add(i);
            }
        }
        if(dateSort){
            items.sort(Comparator.comparing(Item::getPublicationDate));
        }

        if(asc){
            items.sort(Comparator.comparing(Item::getTitle));
        } else{
            items.sort(Comparator.comparing(Item::getTitle).reversed());
        }
        /*messo per non avere Error */
        return null;
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
