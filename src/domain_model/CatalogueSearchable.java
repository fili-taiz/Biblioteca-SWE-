package domain_model;

import java.util.ArrayList;

public interface CatalogueSearchable {
    public ArrayList<Item> searchItem(String keyword, String category, boolean dateSort, boolean asc);

}


