import java.time.LocalDate;
import java.util.ArrayList;

public class Thesis extends Item {
    String author;
    String supervisors;
    String university;

    public Thesis(String code, String title, LocalDate publicationDate, String language, String category, String link, boolean isBorrowable, String author, String supervisors, String university){
        super(code, title, publicationDate, language, category, link, isBorrowable);
        this.author = author;
        this.supervisors = supervisors;
        this.university = university;
    }


    public String get_author(){ return this.author; }
    public String get_supervisors(){ return this.supervisors; }
    public String get_university(){ return this.university; }

    public void set_author(String newAuthor){ this.author = newAuthor; }
    public void set_supervisors(String newSupervisors){ this.supervisors = newSupervisors; }
    public void set_university(String newUniversity){ this.university = newUniversity; }

    public void update_thesis(Thesis newThesis){
        update_item(newThesis);
        this.author = newThesis.author;
        this.supervisors = newThesis.supervisors;
        this.university = newThesis.university;
    }



}
