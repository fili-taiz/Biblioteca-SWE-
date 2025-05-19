package com.progetto_swe.CLI;

import com.progetto_swe.business_logic.AdminController;
import com.progetto_swe.business_logic.*;
import com.progetto_swe.domain_model.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
//TODO decidere se admin deve leggere tutte le prenotazioni o solametne quelle dello storagePlace in cui lavora
public class AdminCLI {/*
    private AdminController adminController;
    Library workingPlace;
    private Scanner scanner = new Scanner(System.in);

    public AdminCLI(AdminController adminController, Library workingPlace) {
        this.adminController = adminController;
        this.workingPlace = workingPlace;
    }

    public String start() {
        String scelta;
        do {
            CommandLineInterface.clearScreen();
            //leggere input user
            System.out.println("Inserisci l'operazione che vuoi eseguire: \n" +
                    "Registrazione utente: registrazione di un utente esterno per prendere in prestito libri;" +
                    "Ricerca utente: ricercare un utente all'interno della biblioteca; \n" +
                    "Ricerca avanzata: ricercare un'articolo con filtri; \n" +
                    "Aggiungi: aggiungi un articolo; \n" +
                    "Registra prestito: registra il prestito di un libro da un utente; \n" +
                    "Logout: uscire dal proprio profilo; \n" +
                    "Esci: se vuoi uscire dall'applicazione");
            scelta = scanner.nextLine().toUpperCase();

            switch (scelta) {
                case "REGISTRAZIONE UTENTE": {
                    try{//TODO ipotetica mail per autenticazione mail ed invio di email e password
                        registrazioneUtenteEsterno();
                    } catch (Exception e){//problema connessione server,

                    }
                    break;
                }

                case "RICERCA UTENTE": {
                        try {
                            CommandLineInterface.clearScreen();
                            ArrayList<Hirer> hirers = ricercaUtente();
                            String usercode;
                            stampaUtenti(hirers);

                            System.out.println("Inserisci l'operazione che vuoi eseguire: \n" +
                                    "[Un codice di un utente]: visualizzare i dettagli di un certo utente" +
                                    "Indietro: tornare alla pagina precedente");
                            usercode = scanner.nextLine().toUpperCase();
                            stampaUtente(getHirer(hirers, usercode));
                        } catch (NumberFormatException e) {
                            CommandLineInterface.clearScreen();
                            System.out.println("Errore: non hai inserito un codice corretto.");
                        } catch (Exception e) { // TODO ipotetico eccezione oggetto inesistente
                            CommandLineInterface.clearScreen();
                            System.out.println("Errore: non hai inserito una categoria corretta.");
                        }
                    break;
                }

                case "RICERCA": {
                    try {
                        ArrayList<Item> items = ricerca();
                        paginaArticoli(items);
                    } catch (Exception e) { //TODO eccezione Categoria inesistente
                        CommandLineInterface.clearScreen();
                        System.out.println("Errore: non hai inserito una categoria corretta.");
                    }
                    break;
                }

                case "RICERCA AVANZATA": {
                    try {
                        ArrayList<Item> items = ricercaAvanzata();
                        paginaArticoli(items); //TODO controllare contenuto eccezione per gestire messaggio di lingua o categoria errata
                    } catch (NumberFormatException e) { //TODO eccezione Categoria inesistente
                        CommandLineInterface.clearScreen();
                        System.out.println("Errore: non hai inserito una categoria corretta.");
                        break;
                    } catch (Exception e) { //TODO eccezione Lingua inesistente
                        CommandLineInterface.clearScreen();
                        System.out.println("Errore: non hai inserito una lingua corretta.");
                    }
                    break;
                }

                case "AGGIUNGI": {
                    System.out.println("Inserisci che tipo di articolo vuoi aggiungere: \n" +
                            "Libro, Rivista, Tesi");
                    String operazione = scanner.nextLine().toUpperCase();
                    ArrayList<Item> param;
                    switch (operazione) {
                        case "LIBRO": {
                            ArrayList<String> params = getItemParameters();
                            params.addAll(getBookParameters());
                            params.addAll(getPhysicalCopiesParameters());
                            adminController.addBook( //TODO riordinare ordine di tutti i parametri
                                    params.get(0),
                                    params.get(1),
                                    params.get(3),
                                    params.get(4),
                                    params.get(5),
                                    params.get(6),
                                    params.get(7),
                                    Integer.parseInt(params.get(8)),
                                    params.get(9),
                                    Integer.parseInt(params.get(10)),
                                    params.get(2).equals("SI")
                            );
                        }

                        case "RIVISTA": {
                            ArrayList<String> params = getItemParameters();
                            params.addAll(getMagazineParameters());
                            params.addAll(getPhysicalCopiesParameters());
                            adminController.addMagazine( //TODO riordinare ordine di tutti i parametri
                                    params.get(0),
                                    params.get(1),
                                    params.get(3),
                                    params.get(4),
                                    params.get(5),
                                    Integer.parseInt(params.get(8)),
                                    params.get(7),
                                    Integer.parseInt(params.get(8)),
                                    params.get(2).equals("SI")
                            );
                            break;
                        }

                        case "TESI": {
                            ArrayList<String> params = getItemParameters();
                            params.addAll(getThesisParameters());
                            params.addAll(getPhysicalCopiesParameters());
                            adminController.addThesis( //TODO riordinare ordine di tutti i parametri
                                    params.get(0),
                                    params.get(1),
                                    params.get(3),
                                    params.get(4),
                                    params.get(5),
                                    Integer.parseInt(params.get(9)),
                                    params.get(7),
                                    params.get(8),
                                    params.get(8),
                                    Integer.parseInt(params.get(9)),
                                    params.get(2).equals("SI")
                            );
                            break;
                        }
                    }
                    break;
                }


                //TODO modificare MockUPS registrazione prestito
                case "REGISTRA PRESTITI" : {
                    CommandLineInterface.clearScreen();
                    System.out.println("Inserisci lo userCode dell'utente che vuole effettuare il prestito: ");
                    String usercode = scanner.nextLine().toUpperCase();

                    System.out.println("Inserisci l'itemCode dell'articolo che l'utente vuole prendere in prestito: ");
                    String itemCode = scanner.nextLine().toUpperCase();

                    adminController.registerLending(usercode, itemCode);
                }

                case "logout": {
                    return "utente anonimo";
                }
            }
        } while (!scelta.equals("esci"));
        return "esci";
    }

    private Item getItem(ArrayList<Item> items, int code){
        for(Item i : items){
            if(i.getCode() == code){
                return i;
            }
        }
        return null; //TODO eccezione ID errato
    }

    private Hirer getHirer(ArrayList<Hirer> hirers, String userCode){
        for(Hirer h : hirers){
            if(h.getUserCode().equals(userCode)){
                return h;
            }
        }
        return null; //TODO eccezione ID errato
    }

    private Reservation getReservation(ArrayList<Reservation> reservations, int itemCode, String storagePlace) {
        for (Reservation r : reservations) {
            if (r.getItem().getCode() == itemCode && r.getStoragePlace().equals(Library.valueOf(storagePlace))) {
                return r;
            }
        }
        return null; //TODO eccezione ID errato
    }

    private Lending getLending(ArrayList<Lending> lendings, int itemCode, String StoragePlace) {
        for(Lending l : lendings){
            if(l.getItem().getCode() == itemCode && l.getStoragePlace().equals(Library.valueOf(StoragePlace))) {
                return l;
            }
        }
        return null; //TODO eccezione ID errato
    }

    private void registrazioneUtenteEsterno(){
        CommandLineInterface.clearScreen();
        System.out.println("Inserisci il nome dell'utente: ");
        String name = scanner.nextLine().toUpperCase();

        System.out.println("Inserisci il cognome dell'utente: ");
        String surname = scanner.nextLine().toUpperCase();

        System.out.println("Inserisci il numero di telefono dell'utente: ");
        String telNumber = scanner.nextLine().toUpperCase();

        String email;
        do {
            System.out.println("Inserisci email dell'utente: ");
            email = scanner.nextLine().toUpperCase();

            //mando email verifica codice
            int checkNumber = (int) Math.floor(Math.random()*100000);
            int input;
            do {
                System.out.println("Inserisci il codice di verifica: ");
                try {
                    input = Integer.parseInt(scanner.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Errore: non hai inserito un codice corretto (valori solo numerici).");
                }
            } while(true);
            if(input == checkNumber){
                System.out.println("Codice di verifica corretto.");
                break;
            }
            System.out.println("Errore: codice di verifica errato.");
        }while(true);
        System.out.println("Inserisci la password dell'utente: "); //TODO modifica registrazione utente esterno, password generata random
        String password = scanner.nextLine();
        adminController.registerExternalHirer(password, name, surname, email, telNumber);
    }

    private ArrayList<Hirer> ricercaUtente() {
        System.out.println("Inserisci informazioni dell'utente (dividi con spazio le parole chiavi): ");
        String keywords = scanner.nextLine().toUpperCase();

        return adminController.searchHirer(keywords);
    }

    private void stampaUtenti(ArrayList<Hirer> hirers) {
        CommandLineInterface.clearScreen();
        String[] header = {"Codice utente", "Nome", "Cognome", "Numero di telefono", "Email"};
        ArrayList<String[]> data = new ArrayList<>();
        for (Hirer h : hirers) {
            data.add(new String[] {h.getUserCode(), h.getName(), h.getSurname(), h.getTelephoneNumber(), h.getEmail()});
        }
        CommandLineInterface.printTable(header, data, 1);
    }

    private void stampaUtente(Hirer hirer, ArrayList<Reservation> reservations, ArrayList<Lending> lendings) {
        //TODO aggiungi come metodo in Hirer
        ArrayList<String[]> data = new ArrayList<>();
        data.add(new String[]{"Codice utente: ", hirer.getUserCode()});
        data.add(new String[]{"Nome: ", hirer.getName()});
        data.add(new String[]{"Cognome: ", hirer.getSurname()});
        data.add(new String[]{"Num. di telefono: ", hirer.getTelephoneNumber()});
        data.add(new String[]{"Email: ", hirer.getEmail()});
        if(hirer.getUnbannedDate() == null){
            data.add(new String[]{"Data di unban: ", "Regolare"});
        } else {
            data.add(new String[]{"Data di unban: ", hirer.getUnbannedDate().toString()});
        }

        System.out.println("Prenotazioni:");
        stampaPrenotazioni(reservations);
        //TODO stampaPrenotazioni(adminController.getLendings().serchbyHirer(id));

        System.out.println("Prestiti:");
        stampaPrestiti(lendings);
        //TODO stampaPrenotazioni(adminController.getLendings().serchbyHirer(id));
    }

    private void stampaPrenotazioni(ArrayList<Reservation> reservations) { //TODO riguardare il contenuto
        CommandLineInterface.clearScreen();
        String[] header = {"Titolo Articolo", "Sede", "Scadenza prenotazione"};
        ArrayList<String[]> data = new ArrayList<>();
        for (Reservation r : reservations) {
            data.add(new String[] {r.getItem().getTitle(), r.getStoragePlace().toString(), r.getReservationDate().plusWeeks(1).toString()});
        }
        CommandLineInterface.printTable(header, data, 0);
    }

    private void stampaPrestiti(ArrayList<Lending> lendings) {//TODO riguardare il contenuto
        String[] header = {"Titolo Articolo", "Sede", "Scadenza prenotazione"};
        ArrayList<String[]> data = new ArrayList<>();
        for (Lending l : lendings) {
            data.add(new String[] {l.getItem().getTitle(), l.getStoragePlace().toString(), l.getLendingDate().plusMonths(1).toString()});
        }
        CommandLineInterface.printTable(header, data, 0);
    }

    private ArrayList<Item> ricerca() {
        System.out.println("Inserisci a quale categoria appartiene l'articolo che stai cercando tra quelli elencati: ");
        for (Category c : Category.values()) {
            System.out.print(c + ", ");
        }
        System.out.print("\b;");

        Category category = Category.valueOf(scanner.nextLine().toUpperCase());

        System.out.println("\n\nInserisci le parole chiavi dell'articolo che vuoi cercare: ");
        String keywords = scanner.nextLine().toUpperCase();

        return adminController.searchItem(keywords, category.toString());
    } //TODO mettere ciclo while che esegue fino a quando i valori sono corretti esegui di continuo in base all'eccezione modifichi il paramtero errato

    private ArrayList<Item> ricercaAvanzata() {
        CommandLineInterface.clearScreen();

        System.out.println("Inserisci a quale categoria appartiene l'articolo che stai cercando tra quelli elencati: ");
        for (Category c : Category.values()) {
            System.out.print(c + ", ");
        }
        System.out.println("\b;");
        Category category = Category.valueOf(scanner.nextLine());

        System.out.println("Inserisci in quale lingua è scritto l'articolo che stai cercando tra quelli elencati: ");
        for (Language l : Language.values()) {
            System.out.print(l + ", ");
        }
        System.out.println("\b;");
        Language language = Language.valueOf(scanner.nextLine());

        System.out.println("Inserisci [Si] se l'articolo deve essere noleggiabile in una nostra biblioteca: ");
        boolean borrowable = scanner.nextLine().toUpperCase().equals("SI");

        System.out.println("Inserisci l'intervallo in cui è stato pubblicato l'articolo che stai cercando: \n" +
                "Data inizio: [formato GG/MM/AAAA]");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());
        System.out.println("Data fine: [formato GG/MM/AAAA]");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());

        System.out.println("\n\nInserisci le parole chiavi dell'articolo che vuoi cercare: ");
        String keywords = scanner.nextLine().toUpperCase();
        return adminController.advancedSearchItem(keywords, category.toString(), language.toString(), borrowable, startDate, endDate);
    }

    private void paginaUtenti(ArrayList<Hirer> hirers) {
        CommandLineInterface.clearScreen();
        String code;
        do {
            stampaUtenti(hirers);

            System.out.println("Inserisci l'operazione che vuoi eseguire: \n" +
                    "[UserCode di un utente]: visualizzare i dettagli di un utente; \n" +
                    "Esci: tornare alla home page. ");
            code = scanner.nextLine().toUpperCase();
            if (!code.equals("ESCI")) {
                try {
                    String userCode = scanner.nextLine().toUpperCase();
                    code = paginaUtente(getHirer(hirers, userCode));
                } catch (NumberFormatException e) {
                    CommandLineInterface.clearScreen();
                    System.out.println("Errore: non hai inserito un codice corretto.");
                } // catch id inserito non presente
            }
        } while (!code.equals("ESCI"));
    }

    private String paginaUtente(Hirer hirer) {
        String operazione;
        do {
            ArrayList<Reservation> reservations = adminController.getReservations();
            ArrayList<Lending> lendings = adminController.getLendings();
            stampaUtente(hirer, reservations, lendings);

            System.out.println("Inserisci l'operazione che vuoi eseguire: \n" +
                    "Ritira: per registrare il ritiro di un articolo di una prenotazione ed il relativo prestito; \n" +
                    "Restituisci: per registrare la restituzione dell'articolo presente nel prestito; \n" +
                    "Indietro: tornare alla pagina precedente"); //TODO controllare il codice ESCI/INDIETRO

            operazione = scanner.nextLine().toUpperCase();
            switch (operazione) {
                case "RITIRA": {
                    System.out.println("Inserisci il codice dell'articolo di cui si vuole effettuare il ritiro: ");
                    String code = scanner.nextLine();
                    try {
                        int itemCode = Integer.parseInt(code);
                        adminController.confirmReservationWithdraw(getReservation(reservations, itemCode, this.workingPlace.toString()));
                    } catch (NumberFormatException e) {
                        CommandLineInterface.clearScreen();
                        System.out.println("Errore: non hai inserito un codice corretto.");
                    } catch (Exception e) { // TODO ipotetico eccezione oggetto inesistente
                        CommandLineInterface.clearScreen();
                        System.out.println("Errore: articolo inesistente.");
                    }
                    break;
                }

                case "RESTITUISCI": {
                    System.out.println("Inserisci il codice dell'articolo di cui si vuole registrare la restituzione: ");
                    String code = scanner.nextLine();
                    try {
                        int itemCode = Integer.parseInt(code);
                        adminController.registerReturnOfItem(getLending(lendings, itemCode, this.workingPlace.toString()));
                    } catch (NumberFormatException e) {
                        CommandLineInterface.clearScreen();
                        System.out.println("Errore: non hai inserito un codice corretto.");
                    } catch (Exception e) { // TODO ipotetico eccezione oggetto inesistente
                        CommandLineInterface.clearScreen();
                        System.out.println("Errore: articolo inesistente.");
                    }
                    break;
                }

                case "INDIETRO": {
                    return "INDIETRO";
                }

                default:{
                    System.out.println("Errore: non hai inserito un operazione corretta.");
                }
            }
        } while (true);
    }

    private void paginaArticoli(ArrayList<Item> items) {
        CommandLineInterface.clearScreen();
        String code;
        do {
            stampaArticoli(items);

            System.out.println("Inserisci l'operazione che vuoi eseguire: \n" +
                    "[Un codice di un articolo]: visualizzare i dettagli di un certo articolo; " +
                    "Esci: tornare alla home page. ");
            code = scanner.nextLine().toUpperCase();
            if (!code.equals("ESCI")) {
                try {
                    int itemCode = Integer.parseInt(code);
                    code = paginaArticolo(getItem(items, itemCode));
                } catch (NumberFormatException e) {
                    CommandLineInterface.clearScreen();
                    System.out.println("Errore: non hai inserito un codice corretto.");
                } // catch id inserito non presente
            }
        } while (!code.equals("ESCI"));
    }

    private String paginaArticolo(Item item) {
        String operazione;
        do {
            System.out.println("Inserisci l'operazione che vuoi eseguire: \n" +
                    "[Modifica]: per modificare i dati di un articolo; \n" +
                    "[Elimina]: per eliminare un articolo dalla sede in cui lavori; \n" +
                    "Indietro: tornare alla pagina precedente");
            operazione = scanner.nextLine().toUpperCase();
            switch (operazione) {
                case "MODIFICA": {
                    String code = scanner.nextLine();
                    try {
                        ArrayList<String> params = getItemParameters();
                        if(item.getClass() == Book.class) {
                            params.addAll(getBookParameters());
                            params.addAll(getPhysicalCopiesParameters());
                            adminController.updateBook( //TODO riordinare ordine di tutti i parametri
                                    item.getCode(),
                                    params.get(0),
                                    params.get(1),
                                    params.get(2).equals("SI"),
                                    params.get(3),
                                    params.get(4),
                                    params.get(5),
                                    params.get(6),
                                    params.get(7),
                                    Integer.parseInt(params.get(8)),
                                    params.get(9),
                                    Integer.parseInt(params.get(10))
                            );
                        }
                        if (item.getClass() == Magazine.class) {
                            params.addAll(getMagazineParameters());
                            params.addAll(getPhysicalCopiesParameters());
                            adminController.updateMagazine( //TODO riordinare ordine di tutti i parametri
                                    item.getCode(),
                                    params.get(0),
                                    params.get(1),
                                    params.get(2).equals("SI"),
                                    params.get(3),
                                    params.get(4),
                                    params.get(5),
                                    params.get(6),
                                    params.get(7),
                                    Integer.parseInt(params.get(8))
                            );
                        }
                        if (item.getClass() == Thesis.class) {
                            params.addAll(getThesisParameters());
                            params.addAll(getPhysicalCopiesParameters());
                            adminController.updateThesis( //TODO riordinare ordine di tutti i parametri
                                    item.getCode(),
                                    params.get(0),
                                    params.get(1),
                                    params.get(2).equals("SI"),
                                    params.get(3),
                                    params.get(4),
                                    params.get(5),
                                    params.get(6),
                                    params.get(7),
                                    params.get(8),
                                    Integer.parseInt(params.get(9))
                            );
                        }
                    } catch (NumberFormatException e) {
                        CommandLineInterface.clearScreen();
                        System.out.println("Errore: non hai inserito un codice corretto.");
                    } catch (Exception e) { // TODO ipotetico eccezione oggetto inesistente
                        CommandLineInterface.clearScreen();
                        System.out.println("Errore: articolo inesistente.");
                    }
                    break;
                }

                case "ELIMINA": {
                    String code = scanner.nextLine();
                    try {
                        int itemCode = Integer.parseInt(code);
                        if (item.getClass() == Book.class) {
                            adminController.removeBook(itemCode);
                        }
                        if (item.getClass() == Magazine.class) {
                            adminController.removeMagazine(itemCode);
                        }
                        if (item.getClass() == Thesis.class) {
                            adminController.removeThesis(itemCode);
                        }
                    } catch (NumberFormatException e) {
                        CommandLineInterface.clearScreen();
                        System.out.println("Errore: non hai inserito un codice corretto.");
                    } catch (Exception e) { // TODO ipotetico eccezione articoli inesistente
                        CommandLineInterface.clearScreen();
                        System.out.println("Errore: articolo inesistente.");
                    }
                    break;

                }case "INDIETRO": {
                    return "INDIETRO";
                }

                default: {
                    System.out.println("Errore: non hai inserito un operazione corretta.");
                }
            }
        } while (true);
    }

    private void stampaArticoli(ArrayList<Item> items) {
        String[] header = {"Titolo Articolo", "Autore", "Categoria", "Data Pubblicazione"}; //TODO moficare item compagnia per passare il code
        ArrayList<String[]> data = new ArrayList<>();
        for (Item item : items) {
            data.add(item.getValues());
        }
        CommandLineInterface.printTable(header, data, 0);
    }

    private void stampaArticolo(Item item) {
        ArrayList<String[]> data = new ArrayList<>();
        data = item.toStringValues();//TODO gestire il numero di copie rimaste e numero di copie totali
        data.add(new String[]{"Num. copie totali: ", Integer.toString(item.getLibraryPhysicalCopies(workingPlace).getNumberOfPhysicalCopies())});
        data.add(new String[]{"Num. copie disponibili: ", Integer.toString(item.getLibraryPhysicalCopies(workingPlace).getNumberOfAvailableCopies())});
        data.add(new String[]{"Stato: ", state(item.getLibraryPhysicalCopies(workingPlace).getNumberOfAvailableCopies(), item.isBorrowable())});
        CommandLineInterface.printCard("Dati articolo", data, 1);

        System.out.println("Prenotazioni:");
        ArrayList<Reservation> reservations;
        //TODO Aggiunta in admin controller del get di lendings e reservations.
        //TODO stampaPrenotazioni(adminController.getLendings().serchbyHirer(id));


        System.out.println("Prestiti:");
        ArrayList<Lending> lendings;
        //TODO Aggiunta in admin controller del get di lendings e reservations.
        //TODO stampaPrenotazioni(adminController.getLendings().serchbyHirer(id));

        }


    private ArrayList<String> getItemParameters(){
        ArrayList<String> parameters = new ArrayList<>();
        System.out.println("Inserisci il titolo: ");
        parameters.add(scanner.nextLine());

        System.out.println("Inserisci la categoria tra quelli elencati: ");
        for (Category c : Category.values()) {
            System.out.print(c + ", ");
        }
        System.out.println("\b;");
        parameters.add(scanner.nextLine());

        System.out.println("Inserisci in quale lingua è scritto l'articolo che stai cercando tra quelli elencati: ");
        for (Language l : Language.values()) {
            System.out.print(l + ", ");
        }
        System.out.println("\b;");
        parameters.add(scanner.nextLine());

        System.out.println("Inserisci il numero di pagine: ");
        parameters.add(scanner.nextLine());

        System.out.println("Inserisci il link dell'articolo: ");
        parameters.add(scanner.nextLine());

        System.out.println("Inserisci l'intervallo in cui è stato pubblicato l'articolo che stai cercando [formato GG/MM/AAAA]: ");
        parameters.add(scanner.nextLine());
        return parameters;
    }


    private ArrayList<String> getBookParameters(){
        ArrayList<String> parameters = new ArrayList<>();
        System.out.println("Inserisci l'ISBN del libro: ");

        parameters.add(scanner.nextLine());

        System.out.println("Inserisci la casa editrice del libro: ");
        parameters.add(scanner.nextLine());

        System.out.println("Inserisci gli autori del libro [suddivisi con spazi]: ");
        parameters.add(scanner.nextLine());

        return parameters;
    }


    private ArrayList<String> getMagazineParameters(){
        ArrayList<String> parameters = new ArrayList<>();

        System.out.println("Inserisci la casa editrice della rivista: ");
        parameters.add(scanner.nextLine());

        return parameters;
    }


    private ArrayList<String> getThesisParameters(){
        ArrayList<String> parameters = new ArrayList<>();

        System.out.println("Inserisci l'autore della tesi: ");
        parameters.add(scanner.nextLine());

        System.out.println("Inserisci i supervisori della tesi [divisi con spazi]: ");
        parameters.add(scanner.nextLine());

        System.out.println("Inserisci l'università della tesi': ");
        parameters.add(scanner.nextLine());

        return parameters;
    }


    private ArrayList<String> getPhysicalCopiesParameters(){
        ArrayList<String> parameters = new ArrayList<>();

        System.out.println("Inserisci il numero di copie nella sede in cui lavori: ");
        parameters.add(scanner.nextLine());

        System.out.println("Inserisci [Si] se è noleggiabile nella sede in cui lavori: ");
        parameters.add(scanner.nextLine());

        return parameters;
    }

    private String state(int numberOfCopies, boolean borrowable) {
        if (!borrowable) {
            return "Non noleggiabile";
        }
        if (numberOfCopies == 0) {
            return "Esaurito";
        }
        return "Prenotabile";
    }*/
}
