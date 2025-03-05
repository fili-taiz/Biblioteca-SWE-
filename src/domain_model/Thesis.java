package domain_model;
import java.time.LocalDate;

public class Thesis extends Item {
    String author;
    String supervisors;
    String university;

    public Thesis() {
        /*Da cancellare */
    }

    public Thesis(String code, String title, LocalDate publicationDate, String language, String category, String link, boolean isBorrowable, String author, String supervisors, String university){
        super(code, title, publicationDate, language, category, link, isBorrowable);
        this.author = author;
        this.supervisors = supervisors;
        this.university = university;
    }


    public String getAuthor(){ return this.author; }
    public String getSupervisors(){ return this.supervisors; }
    public String getUniversity(){ return this.university; }

    public void setAuthor(String newAuthor){ this.author = newAuthor; }
    public void setSupervisors(String newSupervisors){ this.supervisors = newSupervisors; }
    public void setUniversity(String newUniversity){ this.university = newUniversity; }

    public void updateThesis(Thesis newThesis){
        updateItem(newThesis);
        this.author = newThesis.author;
        this.supervisors = newThesis.supervisors;
        this.university = newThesis.university;
    }

    @Override
    public void updateItem(Item item){
        
    }

}
