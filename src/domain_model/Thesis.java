package domain_model;
import java.time.LocalDate;

public class Thesis extends Item {
    String author;
    String supervisors;
    String university;

    public Thesis(){}

    public Thesis(String code, String title, LocalDate publicationDate, Language language, Category category, String link, String author, String supervisors, String university){
        super(code, title, publicationDate, language, category, link, false);
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

    @Override
    public boolean updateItem(Item newItem){
        if (newItem instanceof Thesis){
            super.updateItem(newItem);
            this.setAuthor(((Thesis) newItem).getAuthor());
            this.setSupervisors(((Thesis) newItem).getSupervisors());
            this.setUniversity(((Thesis) newItem).getUniversity());
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o){
        if(super.equals(o)){
            Thesis t = (Thesis) o;
            return this.author.equals(t.author) && this.supervisors.equals(t.supervisors) && this.university.equals(t.university);
        }
        return false;
    }

}
