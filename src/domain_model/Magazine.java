package domain_model;
import java.time.LocalDate;


public class Magazine extends Item{
    String publishingHouseMagazine;

    public Magazine() {
        /*Da cancellare */
    }

    public Magazine(String code, String title, LocalDate publicationDate, String language, String category, String link, boolean isBorrowable, String publishingHouseMagazine){
        super(code, title, publicationDate, language, category, link, isBorrowable);
        this.publishingHouseMagazine = publishingHouseMagazine;
    }


    public String getPublishingHouseMagazine(){ return this.publishingHouseMagazine; }

    public void setPublishingHouseMagazine(String newPublishingHouseMagazine){ this.publishingHouseMagazine = newPublishingHouseMagazine; }


    public void updateMagazine(Magazine newMagazine){
        updateItem(newMagazine);
        this.publishingHouseMagazine = newMagazine.publishingHouseMagazine;
    }

    @Override
    public void updateItem(Item item){
        
    }

}
