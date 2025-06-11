package com.progetto_swe.CLI;

import com.progetto_swe.MailSender.MailSender;
import com.progetto_swe.business_logic.*;
import com.progetto_swe.business_logic.business_logic_exception.ActionDeniedException;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.database_exception.IdAlreadyExistsException;
import com.progetto_swe.orm.database_exception.IdNotFoundException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminCLI extends BaseCLI {
    protected Admin admin;
    protected String telNumberRegex = "^\\+?[0-9]{1,4}?[-.\\s]?\\(?[0-9]{2,4}?\\)?[-.\\s]?[0-9]{3,4}[-.\\s]?[0-9]{3,4}$";
    protected String searchHirerKeyWords = "";
    protected ArrayList<Hirer> hirers;
    protected String userCode = "";
    protected Hirer hirer;


    public AdminCLI(Admin admin) {
        this.admin = admin;
    }

    @Override
    protected ArrayList<String[]> getMenu() {
        switch (page) {
            case "HIRERSEARCHRESULT": {
                hirers = searchHirer();
                hirerSearchResultMenu(hirers);
                stampaUtenti(hirers);
                break;
            }

            case "HIRERPAGE": {
                hirer = getHirer(userCode, searchHirer());
                ReservationController reservationController = new ReservationController();
                LendingController lendingController = new LendingController();
                reservations = reservationController.getReservations(userCode);
                lendings = lendingController.getLendings(userCode);
                hirerMenu(reservations, lendings);
                stampaUtente(hirer, reservations, lendings);
                break;
            }

            default: {
                super.getMenu();
                break;
            }
        }
        return menuOption;
    }

    @Override
    protected void homePageMenu() {
        super.homePageMenu();
        removeOption("Login");
        menuOption.add(new String[]{"Registrazione utente", "registrazione di un utente esterno per prendere in prestito libri"});
        menuOption.add(new String[]{"Ricerca utente", "ricercare un utente all'interno della biblioteca"});
        menuOption.add(new String[]{"Aggiungi", "aggiungi un articolo"});
        menuOption.add(new String[]{"Registra prestito", "registra il prestito di un libro da un utente"});
        menuOption.add(new String[]{"Logout", "uscire dall'account"});
    }

    protected void hirerSearchResultMenu(ArrayList<Hirer> hirers) {
        menuOption = new ArrayList<>();
        if (!hirers.isEmpty()) {
            menuOption.add(new String[]{"Dettagli", "visualizzare i dettagli di un certo utente"});
        }
        menuOption.add(new String[]{"Indietro", "tornare alla pagina precedente"});
    }

    protected void hirerMenu(ArrayList<Reservation> reservations, ArrayList<Lending> lendings) {
        menuOption = new ArrayList<>();
        if (!reservations.isEmpty()) {
            menuOption.add(new String[]{"Ritira", "registrare il ritiro di un articolo prenotato ed il relativo prestito"});
        }
        if (!lendings.isEmpty()) {
            menuOption.add(new String[]{"Restituisci", "registrare la restituzione dell'articolo noleggiato"});
        }
        menuOption.add(new String[]{"Indietro", "tornare alla pagina precedente"});
    }

    @Override
    protected void itemMenu() {
        super.itemMenu();
        if (item != null) {
            menuOption.add(0, new String[]{"Modifica", "modifica i dati di un articolo"});
            menuOption.add(0, new String[]{"Elimina", "per eliminare un articolo dalla sede in cui lavori"});
        }
    }

    @Override
    protected void execute(String scelta) {
        super.execute(scelta);
        switch (page) {
            case "HIRERSEARCHRESULT": {
                executeHirerSearchResultOption(scelta);
                break;
            }

            case "HIRERPAGE": {
                executeHirerPageOption(scelta);
                break;
            }
        }
    }

    @Override
    protected void executeHomePageOption(String scelta) {
        super.executeHomePageOption(scelta);
        switch (scelta) {
            case "REGISTRAZIONE UTENTE": {
                registerHirer();
                CommandLineInterface.clearScreen();
                break;
            }

            case "RICERCA UTENTE": {
                if (searchHirerKeyWords.isEmpty()) {
                    getSearchHirerParameters();
                }
                page = "HIRERSEARCHRESULT";
                CommandLineInterface.clearScreen();
                break;
            }

            case "AGGIUNGI": {
                CommandLineInterface.clearScreen();
                addItem();
                break;
            }

            case "REGISTRA PRESTITO": {
                CommandLineInterface.clearScreen();
                registerLending();
                break;
            }
        }
    }

    @Override
    protected void executeItemPageOption(String scelta) {
        super.executeItemPageOption(scelta);
        switch (scelta) {
            case "MODIFICA": {
                modifyItem();
                break;
            }

            case "ELIMINA": {
                deleteItem();
                page = "ITEMSEARCHRESULT";
                break;
            }
        }
    }

    protected void registerHirer() {
        CommandLineInterface.printMessage("Inserisci il nome dell'utente: ");
        String name = scanner.nextLine().toUpperCase();

        CommandLineInterface.printMessage("Inserisci il cognome dell'utente: ");
        String surname = scanner.nextLine().toUpperCase();

        CommandLineInterface.printMessage("Inserisci il numero di telefono dell'utente: ");
        Pattern pattern = Pattern.compile(telNumberRegex);
        Matcher matcher;
        String telNumber;
        do {
            CommandLineInterface.printMessage("Utilizza i seguenti formati: ");
            CommandLineInterface.printMessage("Numeri internazionali con prefisso: +39 123-456-7890, 0039 1234567890;");
            CommandLineInterface.printMessage("Numeri nazionali italiani con prefisso: 123-456-7890, (123) 456-7890, 1234567890;");
            CommandLineInterface.printMessage("È valido come separatore anche il punto.");
            telNumber = scanner.nextLine().toUpperCase();
            matcher = pattern.matcher(telNumber);
            if (matcher.matches()) {
                break;
            }
            CommandLineInterface.printError("Errore: formato del numero di telefono incorretto");
        } while (true);

        int verificationCode = 0;
        String email = "";
        do {
            if (email.isEmpty()) {
                CommandLineInterface.printMessage("Inserisci email dell'utente: ");
                email = scanner.nextLine().toUpperCase();
                verificationCode = (int) (Math.random() * 1000000);
            }
            //mando email verifica codice
            try {
                MailSender.sendMailVerification(email, verificationCode);
                CommandLineInterface.printMessage("Inserisci il codice di verifica: ");
                String input = scanner.nextLine();
                if (input.equals(String.valueOf(verificationCode))) {
                    break;
                } else {
                    CommandLineInterface.printError("Errore: codice di verifica non corretto");
                    CommandLineInterface.printMessage("Inserisci [Si] se vuoi inserire una nuova mail");
                    if (scanner.nextLine().equalsIgnoreCase("SI")) {
                        email = "";
                    }
                }
            } catch (ActionDeniedException e) {
                CommandLineInterface.printError(e.getMessage());
                email = "";
            }
        } while (true);
        HirerController hirerController = new HirerController();
        hirerController.registerExternalHirer(name, surname, email, telNumber, admin.getToken());
    }

    protected void getSearchHirerParameters() {
        CommandLineInterface.clearScreen();
        CommandLineInterface.printMessage("Inserisci informazioni dell'utente (dividi con spazio le parole chiavi): ");
        searchHirerKeyWords = scanner.nextLine().toUpperCase();
    }

    protected ArrayList<Hirer> searchHirer() {
        HirerController hirerController = new HirerController();
        return hirerController.searchHirer(searchHirerKeyWords, admin.getToken());
    }

    protected void executeHirerSearchResultOption(String scelta) {
        switch (scelta) {
            case "DETTAGLI": {
                CommandLineInterface.printMessage("Inserisci lo userCode dell'utente di cui vuoi visualizzare i dettagli:");
                userCode = scanner.nextLine();
                page = "HIRERPAGE";
                CommandLineInterface.clearScreen();
                break;
            }

            case "INDIETRO": {
                page = "HOMEPAGE";
                CommandLineInterface.clearScreen();
                searchHirerKeyWords = "";
                break;
            }
        }
    }

    protected void executeHirerPageOption(String scelta) {
        switch (scelta) {
            case "RITIRA": {
                CommandLineInterface.printMessage("Inserisci il codice dell'articolo di cui si vuole effettuare il ritiro: ");
                String code = scanner.nextLine();
                ReservationController reservationController = new ReservationController();
                Reservation reservation = getReservationByItemCode(code, reservations);
                if (reservation == null) {
                    CommandLineInterface.printError("Errore: non hai inserito un ItemCode valido.");
                    return;
                }
                CommandLineInterface.clearScreen();
                try {
                    reservationController.confirmReservationWithdraw(hirer, reservation.getItem(), reservation.getStoragePlace().toString(), admin.getToken());
                } catch (IdAlreadyExistsException | ActionDeniedException e) {
                    CommandLineInterface.printError(e.getMessage());
                }
                break;
            }

            case "RESTITUISCI": {
                CommandLineInterface.printMessage("Inserisci il codice dell'articolo di cui si vuole registrare la restituzione: ");
                String code = scanner.nextLine();
                LendingController lendingController = new LendingController();
                Lending lending = getLendingByItemCode(code, lendings);
                CommandLineInterface.clearScreen();
                if (lending == null) {
                    CommandLineInterface.printError("Errore: non hai inserito un ItemCode valido.");
                    return;
                }
                lendingController.registerReturnOfItem(hirer, lending.getItem(), lending.getStoragePlace().toString(), admin.getToken());
                break;
            }

            case "INDIETRO": {
                page = "HIRERSEARCHRESULT";
                CommandLineInterface.clearScreen();
                userCode = "";
            }
        }
    }

    private Hirer getHirer(String code, ArrayList<Hirer> Hirer) {
        for (Hirer hirer : Hirer) {
            if (hirer.getUserCode().equals(code)) {
                return hirer;
            }
        }
        return null;
    }


    protected HashMap<String, String> getItemParameters() {
        CommandLineInterface.clearScreen();
        HashMap<String, String> parameters = new HashMap<>();
        CommandLineInterface.printMessage("Inserisci il titolo: ");
        parameters.put("TITLE", scanner.nextLine());

        CommandLineInterface.printMessage("Inserisci a quale categoria appartiene l'articolo che stai cercando tra quelli elencati: ");
        for (Category c : Category.values()) {
            System.out.print(c + ", ");
        }
        System.out.println("\b\b;");
        parameters.put("CATEGORY", scanner.nextLine().toUpperCase());
        //validazione
        do {
            try {
                Category.valueOf(parameters.get("CATEGORY"));
                break;
            } catch (IllegalArgumentException e) {
                CommandLineInterface.printError("Errore: non hai inserito una categoria corretta, riprova per favore:");
                parameters.put("CATEGORY", scanner.nextLine().toUpperCase());
            }
        } while (true);

        CommandLineInterface.printMessage("Inserisci in quale lingua è scritto l'articolo che stai cercando tra quelli elencati: ");
        for (Language l : Language.values()) {
            System.out.print(l + ", ");
        }
        System.out.println("\b\b;");
        parameters.put("LANGUAGE", scanner.nextLine().toUpperCase());
        //validazione
        do {
            try {
                Language.valueOf(parameters.get("LANGUAGE"));
                break;
            } catch (IllegalArgumentException e) {
                CommandLineInterface.printError("Errore: non hai inserito una lingua corretta, riprova per favore:");
                parameters.put("LANGUAGE", scanner.nextLine().toUpperCase());
            }
        } while (true);

        CommandLineInterface.printMessage("Inserisci il numero di pagine: ");
        parameters.put("NUMBEROFPAGES", scanner.nextLine());
        do {
            try {
                Integer.parseInt(parameters.get("NUMBEROFPAGES"));
                break;
            } catch (NumberFormatException e) {
                CommandLineInterface.printError("Errore: non hai inserito una numero, riprova per favore:");
                parameters.put("NUMBEROFPAGES", scanner.nextLine());
            }
        } while (true);

        CommandLineInterface.printMessage("Inserisci il link dell'articolo: ");
        parameters.put("LINK", scanner.nextLine());

        CommandLineInterface.printMessage("Inserisci la data in cui è stato pubblicato l'articolo [formato AAAA-MM-GG]: ");
        parameters.put("PUBLICATIONDATE", scanner.nextLine().toUpperCase());
        do {
            try {
                LocalDate.parse(parameters.get("PUBLICATIONDATE"));
                break;
            } catch (DateTimeParseException e) {
                CommandLineInterface.printError("Errore: non hai inserito una data corretta, riprova per favore:");
                parameters.put("PUBLICATIONDATE", scanner.nextLine().toUpperCase());
            }
        } while (true);
        return parameters;
    }

    private HashMap<String, String> getPhysicalCopiesParameters() {
        HashMap<String, String> parameters = new HashMap<>();

        CommandLineInterface.printMessage("Inserisci il numero di copie di quest'articolo nella sede in cui lavori: ");
        parameters.put("NUMBEROFCOPIES", scanner.nextLine());
        do {
            try {
                Integer.parseInt(parameters.get("NUMBEROFCOPIES"));
                break;
            } catch (NumberFormatException e) {
                CommandLineInterface.printError("Errore: non hai inserito una numero, riprova per favore:");
                parameters.put("NUMBEROFCOPIES", scanner.nextLine());
            }
        } while (true);

        System.out.println("Inserisci [Si] se è noleggiabile nella sede in cui lavori: ");
        parameters.put("BORROWABLE", Boolean.toString(scanner.nextLine().equalsIgnoreCase("SI")));
        return parameters;
    }

    protected HashMap<String, String> getBookParameters() {
        HashMap<String, String> parameters = new HashMap<>();
        System.out.println("Inserisci l'ISBN del libro: ");
        parameters.put("ISBN", scanner.nextLine());

        System.out.println("Inserisci la casa editrice del libro: ");
        parameters.put("PUBLISHINGHOUSE", scanner.nextLine());

        System.out.println("Inserisci gli autori del libro [suddivisi con spazi]: ");
        parameters.put("AUTHORS", scanner.nextLine().toUpperCase());
        return parameters;
    }

    protected HashMap<String, String> getMagazineParameters() {
        HashMap<String, String> parameters = new HashMap<>();
        System.out.println("Inserisci la casa editrice della rivista: ");
        parameters.put("PUBLISHINGHOUSE", scanner.nextLine().toUpperCase());
        return parameters;
    }

    protected HashMap<String, String> getThesisParameters() {
        HashMap<String, String> parameters = new HashMap<>();
        System.out.println("Inserisci l'autore della tesi: ");
        parameters.put("AUTHOR", scanner.nextLine().toUpperCase());

        System.out.println("Inserisci i supervisori della tesi [divisi con spazi]: ");
        parameters.put("SUPERVISORS", scanner.nextLine().toUpperCase());

        System.out.println("Inserisci l'università della tesi: ");
        parameters.put("UNIVERSITY", scanner.nextLine().toUpperCase());
        return parameters;
    }

    protected void addItem() {
        CommandLineInterface.printMessage("Inserisci il tipo dell'articolo che vuoi aggiungere: ");
        CommandLineInterface.printMessage("[BOOK], [MAGAZINE], [THESIS];");
        String type = scanner.nextLine().toUpperCase();
        switch (type) {
            case "BOOK": {
                addBook();
                break;
            }

            case "MAGAZINE": {
                addMagazine();
                break;
            }

            case "THESIS": {
                addThesis();
                break;
            }

            default: {
                CommandLineInterface.clearScreen();
                CommandLineInterface.printError("Errore: tipologia di articolo non valido.");
                return;
            }
        }
        CommandLineInterface.clearScreen();
    }

    protected void addBook() {
        HashMap<String, String> itemParams = getItemParameters();
        HashMap<String, String> bookParams = getBookParameters();
        HashMap<String, String> pCopiesParams = getPhysicalCopiesParameters();

        ItemController itemController = new ItemController();
        itemController.addBook(
                itemParams.get("TITLE"),
                itemParams.get("PUBLICATIONDATE"),
                itemParams.get("LANGUAGE"),
                itemParams.get("CATEGORY"),
                itemParams.get("LINK"),
                bookParams.get("ISBN"),
                bookParams.get("PUBLISHINGHOUSE"),
                Integer.parseInt(itemParams.get("NUMBEROFPAGES")),
                bookParams.get("AUTHORS"),
                Integer.parseInt(pCopiesParams.get("NUMBEROFCOPIES")),
                Boolean.parseBoolean(pCopiesParams.get("BORROWABLE")),
                admin.getToken());
    }

    protected void addMagazine() {
        HashMap<String, String> itemParams = getItemParameters();
        HashMap<String, String> magazineParams = getMagazineParameters();
        HashMap<String, String> pCopiesParams = getPhysicalCopiesParameters();

        ItemController itemController = new ItemController();
        itemController.addMagazine(
                itemParams.get("TITLE"),
                itemParams.get("PUBLICATIONDATE"),
                itemParams.get("LANGUAGE"),
                itemParams.get("CATEGORY"),
                itemParams.get("LINK"),
                Integer.parseInt(itemParams.get("NUMBEROFPAGES")),
                magazineParams.get("PUBLISHINGHOUSE"),
                Integer.parseInt(pCopiesParams.get("NUMBEROFCOPIES")),
                Boolean.parseBoolean(pCopiesParams.get("BORROWABLE")),
                admin.getToken());
    }

    protected void addThesis() {
        HashMap<String, String> itemParams = getItemParameters();
        HashMap<String, String> thesisParams = getThesisParameters();
        HashMap<String, String> pCopiesParams = getPhysicalCopiesParameters();

        ItemController itemController = new ItemController();
        itemController.addThesis(
                itemParams.get("TITLE"),
                itemParams.get("PUBLICATIONDATE"),
                itemParams.get("LANGUAGE"),
                itemParams.get("CATEGORY"),
                itemParams.get("LINK"),
                Integer.parseInt(itemParams.get("NUMBEROFPAGES")),
                thesisParams.get("AUTHOR"),
                thesisParams.get("SUPERVISORS"),
                thesisParams.get("UNIVERSITY"),
                Integer.parseInt(pCopiesParams.get("NUMBEROFCOPIES")),
                Boolean.parseBoolean(pCopiesParams.get("BORROWABLE")),
                admin.getToken());
    }

    protected void registerLending() {
        CommandLineInterface.printMessage("Inserisci lo userCode dell'utente che vuole effettuare il prestito: ");
        searchHirerKeyWords = scanner.nextLine();
        hirer = getHirer(searchHirerKeyWords, searchHirer());
        if (hirer == null) {
            CommandLineInterface.printError("Errore: userCode non esistente.");
            return;
        }
        CommandLineInterface.printMessage("Inserisci l'itemCode dell'articolo che vuole effettuare il prestito: ");
        itemCode = scanner.nextLine();
        advanceSearch = false;
        searchItemParams.put("KEYWORDS", itemCode);
        item = getItem(itemCode, searchItems());
        if (item == null) {
            CommandLineInterface.printError("Errore: itemCode non esistente.");
            return;
        }

        LendingController lendingController = new LendingController();
        try {
            lendingController.registerLending(hirer, item, admin.getToken());
            CommandLineInterface.clearScreen();
        } catch (ActionDeniedException e) {
            CommandLineInterface.clearScreen();
            CommandLineInterface.printError(e.getMessage());
        }
        searchHirerKeyWords = "";
        itemCode = "";
        hirer = null;
        item = null;
    }

    protected void modifyItem() {
        HashMap<String, String> itemParams = getItemParameters();
        ItemController itemController = new ItemController();
        try {
            if (item.getClass() == Book.class) {
                HashMap<String, String> bookParams = getBookParameters();
                HashMap<String, String> pCopiesParams = new HashMap<>();
                if(item.getNumberOfCopiesInLibrary(admin.getWorkingPlace()) > 0){
                    pCopiesParams = getPhysicalCopiesParameters();
                } else {
                    pCopiesParams.put("NUMBEROFCOPIES", "0");
                    pCopiesParams.put("BORROWABLE", "false");
                }
                itemController.updateBook(
                        item.getCode(),
                        itemParams.get("TITLE"),
                        itemParams.get("PUBLICATIONDATE"),
                        Boolean.parseBoolean(pCopiesParams.get("BORROWABLE")),
                        itemParams.get("LANGUAGE"),
                        itemParams.get("CATEGORY"),
                        itemParams.get("LINK"),
                        bookParams.get("ISBN"),
                        bookParams.get("PUBLISHINGHOUSE"),
                        Integer.parseInt(itemParams.get("NUMBEROFPAGES")),
                        bookParams.get("AUTHORS"),
                        Integer.parseInt(pCopiesParams.get("NUMBEROFCOPIES")),
                        admin.getToken());
            }
            if (item.getClass() == Magazine.class) {
                HashMap<String, String> magazineParams = getMagazineParameters();
                HashMap<String, String> pCopiesParams = new HashMap<>();
                if(item.getNumberOfCopiesInLibrary(admin.getWorkingPlace()) > 0){
                    pCopiesParams = getPhysicalCopiesParameters();
                } else {
                    pCopiesParams.put("NUMBEROFCOPIES", "0");
                    pCopiesParams.put("BORROWABLE", "false");
                }
                itemController.updateMagazine(
                        item.getCode(),
                        itemParams.get("TITLE"),
                        itemParams.get("PUBLICATIONDATE"),
                        Boolean.parseBoolean(pCopiesParams.get("BORROWABLE")),
                        itemParams.get("LANGUAGE"),
                        itemParams.get("CATEGORY"),
                        itemParams.get("LINK"),
                        magazineParams.get("PUBLISHINGHOUSE"),
                        Integer.parseInt(pCopiesParams.get("NUMBEROFCOPIES")),
                        admin.getToken(),
                        Integer.parseInt(itemParams.get("NUMBEROFPAGES")));
            }
            if (item.getClass() == Thesis.class) {
                HashMap<String, String> thesisParams = getThesisParameters();
                HashMap<String, String> pCopiesParams = new HashMap<>();
                if(item.getNumberOfCopiesInLibrary(admin.getWorkingPlace()) > 0){
                    pCopiesParams = getPhysicalCopiesParameters();
                } else {
                    pCopiesParams.put("NUMBEROFCOPIES", "0");
                    pCopiesParams.put("BORROWABLE", "false");
                }
                itemController.updateThesis(
                        item.getCode(),
                        itemParams.get("TITLE"),
                        itemParams.get("PUBLICATIONDATE"),
                        Boolean.parseBoolean(pCopiesParams.get("BORROWABLE")),
                        itemParams.get("LANGUAGE"),
                        itemParams.get("CATEGORY"),
                        itemParams.get("LINK"),
                        thesisParams.get("AUTHOR"),
                        thesisParams.get("SUPERVISORS"),
                        thesisParams.get("UNIVERSITY"),
                        Integer.parseInt(pCopiesParams.get("NUMBEROFCOPIES")),
                        admin.getToken(),
                        Integer.parseInt(itemParams.get("NUMBEROFPAGES")));
            }
            CommandLineInterface.clearScreen();
        } catch (ActionDeniedException | IdNotFoundException e) {
            CommandLineInterface.clearScreen();
            CommandLineInterface.printError(e.getMessage());
        }
    }

    protected void deleteItem() {
        ItemController itemController = new ItemController();
        try {
            if (item.getClass() == Book.class) {
                itemController.removeBook(item.getCode(), admin.getToken());
            }
            if (item.getClass() == Magazine.class) {
                itemController.removeMagazine(item.getCode(), admin.getToken());
            }
            if (item.getClass() == Thesis.class) {
                itemController.removeThesis(item.getCode(), admin.getToken());
            }
            CommandLineInterface.clearScreen();
        } catch (ActionDeniedException e) {
            CommandLineInterface.clearScreen();
            CommandLineInterface.printError(e.getMessage());
        }
    }

    protected void stampaUtenti(ArrayList<Hirer> hirers) {
        if (hirers.isEmpty()) {
            CommandLineInterface.printMessage("Mi dispiace, non ci sono utenti che rispettano le tue richieste, prova a rilassare i vincoli.");
            return;
        }
        String[] header = {"userCode", "Nome", "Cognome", "Email"};
        ArrayList<String[]> data = new ArrayList<>();
        for (Hirer h : hirers) {
            data.add(new String[]{h.getUserCode(), h.getName(), h.getSurname(), h.getEmail()});
        }
        CommandLineInterface.printTable(header, data);
    }

    protected void stampaUtente(Hirer hirer, ArrayList<Reservation> reservations, ArrayList<Lending> lendings) {
        if (hirer == null) {
            CommandLineInterface.printError("Errore: non hai inserito uno userCode valido.");
            return;
        }
        ArrayList<String[]> data = new ArrayList<>();
        data.add(new String[]{"userCode", hirer.getUserCode()});
        data.add(new String[]{"Nome", hirer.getName()});
        data.add(new String[]{"Cognome", hirer.getSurname()});
        data.add(new String[]{"Tel.", hirer.getTelephoneNumber()});
        data.add(new String[]{"Email", hirer.getEmail()});
        if (hirer.getUnbannedDate() != null) {
            data.add(new String[]{"Data sblocco", hirer.getUnbannedDate().toString()});
        }
        CommandLineInterface.printCard("Dati utente", data);

        if (!reservations.isEmpty()) {
            CommandLineInterface.printMessage("Prenotazioni:");
            stampaPrenotazioni(reservations);
        }

        if (!lendings.isEmpty()) {
            CommandLineInterface.printMessage("Prestiti:");
            stampaPrestiti(lendings);
        }
    }


}
