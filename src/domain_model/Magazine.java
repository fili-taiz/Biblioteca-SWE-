package domain_model;
import java.time.LocalDate;
import java.util.Objects;


public class Magazine extends Item{
    String publishing_house_magazine;

    public Magazine(){

    }


    public Magazine(String code, String title, LocalDate publicationDate, Language language, Category category, String link, boolean isBorrowable, String publishing_house_magazine){
        super(code, title, publicationDate, language, category, link, isBorrowable);
        this.publishing_house_magazine = publishing_house_magazine;
    }


    public String getPublishingHouseMagazine(){ return this.publishing_house_magazine; }

    public void setPublishingHouseMagazine(String new_publishing_house_magazine){ this.publishing_house_magazine = new_publishing_house_magazine; }


    @Override
    public boolean updateItem(Item newItem){
        if (newItem instanceof Magazine){
            super.updateItem(newItem);
            this.setPublishingHouseMagazine(((Magazine)newItem).getPublishingHouseMagazine());
            return true;
        }
        return false;
    }

    @Override
    public void gettt(){
        super.gettt();
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
