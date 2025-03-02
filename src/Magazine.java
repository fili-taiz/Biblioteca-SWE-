import java.time.LocalDate;


public class Magazine extends Item{
    String publishingHouseMagazine;

    public Magazine(String code, String title, LocalDate publicationDate, String language, String category, String link, boolean isBorrowable, String publishingHouseMagazine){
        super(code, title, publicationDate, language, category, link, isBorrowable);
        this.publishingHouseMagazine = publishingHouseMagazine;
    }


    public String get_publishing_house_magazine(){ return this.publishingHouseMagazine; }

    public void set_publishing_house_magazine(String newPublishingHouseMagazine){ this.publishingHouseMagazine = newPublishingHouseMagazine; }


    public void update_magazine(Magazine newMagazine){
        update_item(newMagazine);
        this.publishingHouseMagazine = newMagazine.publishingHouseMagazine;
    }



}
