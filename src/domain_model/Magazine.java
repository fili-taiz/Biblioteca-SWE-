package domain_model;
import java.time.LocalDate;


public class Magazine extends Item{
    String publishing_house_magazine;



    public Magazine(String code, String title, LocalDate publicationDate, Language language, Category category, String link, boolean borrowable, String publishing_house_magazine){
        super(code, title, publicationDate, language, category, link, borrowable);
        this.publishing_house_magazine = publishing_house_magazine;
    }


    public String getPublishingHouseMagazine(){ return this.publishing_house_magazine; }

    public void setPublishingHouseMagazine(String new_publishing_house_magazine){ this.publishing_house_magazine = new_publishing_house_magazine; }


    @Override
    public boolean updateItem(Item new_item){
        if (new_item instanceof Magazine){
            super.updateItem(new_item);
            this.setPublishingHouseMagazine(((Magazine)new_item).getPublishingHouseMagazine());
            return true;
        }
        return false;
    }



    @Override
    public boolean equals(Object o){
        if(super.equals(o)){
            Magazine m = (Magazine)o;
            return this.publishing_house_magazine.equals(m.publishing_house_magazine);
        }
        return false;
    }

}
