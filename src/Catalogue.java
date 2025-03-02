import java.util.ArrayList;
import java.util.Comparator;

public class Catalogue implements CatalogueSearchable{
    ArrayList<Item> items;
    @Override
    public void search_item(String keyword, String category, boolean dateSort, boolean asc){
        ArrayList<Item> result = new ArrayList<>();
        for(Item i: this.items){
            if(i.get_title().equals(keyword) && i.get_category().equals(category)){
                result.add(i);
            }
        }
        if(dateSort){
            items.sort(Comparator.comparing(Item::get_publication_date));
        }

        if(asc){
            items.sort(Comparator.comparing(Item::get_title));
        } else{
            items.sort(Comparator.comparing(Item::get_title).reversed());
        }
    }

    public void add_item(Item item){
        items.add(item);
    }

    public void remove_item(Item item){
        items.remove(item);
    }

    public Item get_item(Item item){
        return items.get(items.indexOf(item));
    }



}
