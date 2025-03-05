package business_logic;

import domain_model.*;
import java.time.LocalDate;
import java.util.ArrayList;
import orm.BookDAO;
import orm.HirerDAO;
import orm.LendingDAO;
import orm.MagazineDAO;
import orm.ReservationDAO;
import orm.ThesisDAO;

public class AdminController {
    Admin admin;

    public AdminController(Admin admin){
        this.admin = admin;
    }
    /*da riguardare i parametri */
    public boolean registerExternalHirer(String name, String surname, String IDcode, String eMail, String telephoneNumber){
        HirerDAO hirerDAO = new HirerDAO();
        if(hirerDAO.addHirer(name, surname, IDcode, eMail, telephoneNumber)){
            admin.addHirer(new Hirer());
        }
        return false;
    }

    public boolean addBook(String parametriAggiuntaBook){
        BookDAO bookDAO = new BookDAO();
        if(bookDAO.addBook(parametriAggiuntaBook)){
            admin.addItem(new Book());
        }
        return false;
    }

    public boolean addMagazine(String parametriAggiuntaMagazine){
        MagazineDAO magazineDAO = new MagazineDAO();
        if(magazineDAO.addMagazine(parametriAggiuntaMagazine)){
            admin.addItem(new Magazine());
        }
        return false;
    }

    public boolean addThesis(String parametriAggiuntaThesis){
        ThesisDAO thesisDAO = new ThesisDAO();
        if(thesisDAO.addThesis(parametriAggiuntaThesis)){
            admin.addItem(new Thesis());
        }
        return false;
    }

    public boolean removeBook(String parametriRimozioneBook){
        BookDAO bookDAO = new BookDAO();
        if(bookDAO.removeBook(parametriRimozioneBook)){
            admin.removeItem(new Book());
        }
        return false;
    }

    public boolean removeMagazine(String parametriRimozioneMagazine){
        MagazineDAO magazineDAO = new MagazineDAO();
        if(magazineDAO.removeMagazine(parametriRimozioneMagazine)){
            admin.removeItem(new Magazine());
        }
        return false;
    }

    public boolean removeThesis(String parametriRimozioneThesis){
        ThesisDAO thesisDAO = new ThesisDAO();
        if(thesisDAO.removeThesis(parametriRimozioneThesis)){
            admin.removeItem(new Thesis());
        }
        return false;
    }

    public boolean updateBook(Item originaleItem, String parametriAggiornamentoBook){
        BookDAO bookDAO = new BookDAO();
        if(bookDAO.updateBook(parametriAggiornamentoBook)){
            /* */
            admin.updateItem(originaleItem, new Book());
        }
        return false;
    }

    public boolean updateMagazine(Item originaleItem, String parametriAggiornamentoMagazine){
        MagazineDAO magazineDAO = new MagazineDAO();
        if(magazineDAO.updateMagazine(parametriAggiornamentoMagazine)){
            /* */
            admin.updateItem(originaleItem, new Magazine());
        }
        return false;
    }

    public boolean updateThesis(Item originaleItem, String parametriAggiornamentoThesis){
        ThesisDAO thesisDAO = new ThesisDAO();
        if(thesisDAO.updateThesis(parametriAggiornamentoThesis)){
            /* */
            admin.updateItem(originaleItem, new Thesis());
        }
        return false;
    }

    public boolean registerLending(Hirer hirer, Item item){
        LendingDAO lendingDAO = new LendingDAO();
        if(lendingDAO.addLending("parametriAggiuntaLending")){
            admin.registerLending(hirer, item, LocalDate.now());
        }
        return false;
    }

    public boolean confirmReservationWithdraw(Hirer hirer, Item item){
        LendingDAO lendingDAO = new LendingDAO();
        ReservationDAO reservationDAO = new ReservationDAO();
        /*cancella reservation */
        admin.confirmReservationWithdraw(hirer, item, LocalDate.now());
        if(lendingDAO.addLending("parametriAggiuntaLending")){
            admin.registerLending(hirer, item, LocalDate.now());
        }
        return false;
    }

    public boolean registerItemReturn(Lending lending){
        LendingDAO lendingDAO = new LendingDAO();
        /*cancella reservation */
        if(lendingDAO.removeLending("coeLending")){
            admin.registerItemReturn(lending);
        }
        return false;
    }

     public ArrayList<Item> searchItem(String keyWord){
        return admin.searchItem(keyWord, "ALL", false, false);
    }

    public ArrayList<Item> searchItem(String keyWord, String category, boolean dateSort, boolean asc){
        return admin.searchItem(keyWord, category, dateSort, asc);
    }

}
