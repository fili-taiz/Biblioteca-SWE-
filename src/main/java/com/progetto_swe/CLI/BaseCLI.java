package com.progetto_swe.CLI;

import com.progetto_swe.business_logic.ItemController;
import com.progetto_swe.domain_model.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class BaseCLI {

    protected Scanner scanner = new Scanner(System.in);
    protected ArrayList<String[]> menuOption;
    protected String page;
    protected HashMap<String, String> searchItemParams = new HashMap<>();
    protected ArrayList<Item> items;
    protected Item item;
    protected boolean advanceSearch = false;
    protected String itemCode;
    protected ArrayList<Reservation> reservations;
    protected ArrayList<Lending> lendings;

    BaseCLI() {
        homePageMenu();
        page = "HOMEPAGE";
        CommandLineInterface.clearScreen();
    }

    protected ArrayList<String[]> getMenu() {
        switch (page) {

            case "HOMEPAGE": {
                homePageMenu();
                break;
            }

            case "ITEMSEARCHRESULT": {
                items = searchItems();
                searchResultMenu();
                stampaArticoli(items);
                break;
            }

            case "ITEMPAGE": {
                items = searchItems();
                item = getItem(itemCode, items);
                itemMenu();
                stampaArticolo(item);
                break;
            }
        }
        return menuOption;
    }

    protected void removeOption(String option) {
        for (int i = 0; i < menuOption.size(); i++) {
            if (menuOption.get(i)[0].equals(option)) {
                menuOption.remove(i);
            }
        }
    }

    protected void homePageMenu() {
        menuOption = new ArrayList<>();
        menuOption.add(new String[]{"Ricerca", "ricercare un'articolo all'interno della biblioteca"});
        menuOption.add(new String[]{"Ricerca avanzata", "ricercare con filtri"});
        menuOption.add(new String[]{"Login", "effettuare l'accesso al sito identificandoti con le tue credenziali"});
    }

    protected void searchResultMenu() {
        menuOption = new ArrayList<>();
        if (!items.isEmpty()) {
            menuOption.add(new String[]{"Dettagli", "visualizzare i dettagli di un certo articolo"});
        }
        menuOption.add(new String[]{"Indietro", "torna alla pagina precedente"});
    }

    protected void itemMenu() {
        menuOption = new ArrayList<>();
        menuOption.add(new String[]{"Indietro", "torna alla pagina precedente"});
    }


    protected void execute(String scelta) {
        boolean optionExist = false;
        for (String[] option : menuOption) {
            if (option[0].equalsIgnoreCase(scelta)) {
                optionExist = true;
            }
        }
        if (!optionExist) {
            CommandLineInterface.clearScreen();
            CommandLineInterface.printError("Errore: non hai inserito un'operazione valida.");
            return;
        }
        switch (page) {
            case "HOMEPAGE": {
                executeHomePageOption(scelta);
                break;
            }

            case "ITEMSEARCHRESULT": {
                executeItemSearchResultOption(scelta);
                break;
            }

            case "ITEMPAGE": {
                executeItemPageOption(scelta);
            }
        }
    }


    protected void executeHomePageOption(String scelta) {
        switch (scelta) {
            case "RICERCA": {
                advanceSearch = false;
                if (searchItemParams.isEmpty()) {
                    getSearchItemParameters();
                }
                page = "ITEMSEARCHRESULT";
                CommandLineInterface.clearScreen();
                break;
            }

            case "RICERCA AVANZATA": {
                advanceSearch = true;
                if (searchItemParams.isEmpty()) {
                    getAdvanceSearchItemParameters();
                }
                page = "ITEMSEARCHRESULT";
                CommandLineInterface.clearScreen();
                break;
            }
        }
    }

    protected ArrayList<Item> searchItems() {
        ItemController itemController = new ItemController();
        ArrayList<Item> items;
        if (advanceSearch) {
            items = itemController.advanceSearchItem(
                    searchItemParams.get("KEYWORDS"),
                    searchItemParams.get("CATEGORY"),
                    searchItemParams.get("LANGUAGE"),
                    searchItemParams.get("BORROWABLE").equals("SI"),
                    searchItemParams.get("STARTDATE"),
                    searchItemParams.get("ENDDATE"));
        } else {
            items = itemController.searchItem(
                    searchItemParams.get("KEYWORDS")/*,
                    searchItemParams.get("CATEGORY")*/);
        }
        return items;
    }

    protected void executeItemSearchResultOption(String scelta) {
        switch (scelta) {
            case "DETTAGLI": {
                CommandLineInterface.printMessage("Inserisci l'ItemCode dell'articolo di cui vuoi visualizzare i dettagli:");
                itemCode = scanner.nextLine();
                //item = getItem(itemCode, items);
                page = "ITEMPAGE";
                CommandLineInterface.clearScreen();
                break;
            }

            case "INDIETRO": {
                page = "HOMEPAGE";
                CommandLineInterface.clearScreen();
                searchItemParams = new HashMap<>();
            }
        }
    }

    protected void executeItemPageOption(String scelta) {
        switch (scelta) {
            case "INDIETRO": {
                page = "ITEMSEARCHRESULT";
                CommandLineInterface.clearScreen();
            }
        }
    }

    protected Item getItem(String code, ArrayList<Item> items) {
        for (Item item : items) {
            if (Integer.toString(item.getCode()).equals(code)) {
                return item;
            }
        }
        return null;
    }

    protected void getSearchItemParameters() {
        CommandLineInterface.clearScreen();
        /*CommandLineInterface.printMessage("Inserisci a quale categoria appartiene l'articolo che stai cercando tra quelli elencati: ");
        for (Category c : Category.values()) {
            System.out.print(c + ", ");
        }
        System.out.print("\b\b;\n");
        searchItemParams.put("CATEGORY", scanner.nextLine().toUpperCase());
        do {
            try {
                Category.valueOf(searchItemParams.get("CATEGORY"));
                break;
            } catch (IllegalArgumentException e) {
                CommandLineInterface.printError("Errore: non hai inserito una categoria corretta, riprova per favore:");
                searchItemParams.put("CATEGORY", scanner.nextLine().toUpperCase());
            }
        } while (true);

        System.out.println();*/
        CommandLineInterface.printMessage("Inserisci le parole chiavi dell'articolo che vuoi cercare: ");
        searchItemParams.put("KEYWORDS", scanner.nextLine().toUpperCase());
    }

    protected void getAdvanceSearchItemParameters() {
        CommandLineInterface.clearScreen();
        CommandLineInterface.printMessage("Inserisci a quale categoria appartiene l'articolo che stai cercando tra quelli elencati: ");
        for (Category c : Category.values()) {
            System.out.print(c + ", ");
        }
        System.out.println("\b\b;");
        searchItemParams.put("CATEGORY", scanner.nextLine().toUpperCase());
        //validazione
        do {
            try {
                Category.valueOf(searchItemParams.get("CATEGORY"));
                break;
            } catch (IllegalArgumentException e) {
                CommandLineInterface.printError("Errore: non hai inserito una categoria corretta, riprova per favore:");
                searchItemParams.put("CATEGORY", scanner.nextLine().toUpperCase());
            }
        } while (true);

        CommandLineInterface.printMessage("Inserisci in quale lingua è scritto l'articolo che stai cercando tra quelli elencati: ");
        for (Language l : Language.values()) {
            System.out.print(l + ", ");
        }
        System.out.println("\b\b;");
        searchItemParams.put("LANGUAGE", scanner.nextLine().toUpperCase());
        //validazione
        do {
            try {
                Language.valueOf(searchItemParams.get("LANGUAGE"));
                break;
            } catch (IllegalArgumentException e) {
                CommandLineInterface.printError("Errore: non hai inserito una lingua corretta, riprova per favore:");
                searchItemParams.put("LANGUAGE", scanner.nextLine().toUpperCase());
            }
        } while (true);

        CommandLineInterface.printMessage("Inserisci [Si] se l'articolo deve essere noleggiabile in una nostra biblioteca: ");
        searchItemParams.put("BORROWABLE", scanner.nextLine().toUpperCase());

        CommandLineInterface.printMessage("Inserisci l'intervallo in cui è stato pubblicato l'articolo che stai cercando: ");
        CommandLineInterface.printMessage("Data inizio: [formato AAAA-MM-GG]");
        searchItemParams.put("STARTDATE", scanner.nextLine().toUpperCase());
        //validazione
        do {
            try {
                LocalDate.parse(searchItemParams.get("STARTDATE"));
                break;
            } catch (DateTimeParseException e) {
                CommandLineInterface.printError("Errore: non hai inserito una data corretta, riprova per favore:");
                searchItemParams.put("STARTDATE", scanner.nextLine().toUpperCase());
            }
        } while (true);

        CommandLineInterface.printMessage("Data fine: [formato AAAA-MM-GG]");
        searchItemParams.put("ENDDATE", scanner.nextLine().toUpperCase());
        do {
            try {
                LocalDate.parse(searchItemParams.get("ENDDATE"));
                break;
            } catch (DateTimeParseException e) {
                CommandLineInterface.printError("Errore: non hai inserito una data corretta, riprova per favore:");
                searchItemParams.put("ENDDATE", scanner.nextLine().toUpperCase());
            }
        } while (true);

        System.out.println("\n");
        CommandLineInterface.printMessage("Inserisci le parole chiavi dell'articolo che vuoi cercare: ");
        searchItemParams.put("KEYWORDS", scanner.nextLine().toUpperCase());
    }

    protected Reservation getReservationByItemCode(String code, ArrayList<Reservation> reservations) {
        for (Reservation reservation : reservations) {
            if (Integer.toString(reservation.getItem().getCode()).equals(code)) {
                return reservation;
            }
        }
        return null;
    }

    protected Lending getLendingByItemCode(String code, ArrayList<Lending> lendings) {
        for (Lending lending : lendings) {
            if (Integer.toString(lending.getItem().getCode()).equals(code)) {
                return lending;
            }
        }
        return null;
    }

    protected void stampaArticoli(ArrayList<Item> items) {
        if (items.isEmpty()) {
            CommandLineInterface.printMessage("Mi dispiace, non ci sono articoli che rispettano le tue richieste, prova a rilassare i vincoli.");
            return;
        }
        String[] header = {"ItemCode", "Titolo", "Autore", "Categoria", "Data Pubblicazione"};
        ArrayList<String[]> data = new ArrayList<>();
        for (Item item : items) {
            data.add(item.getValues());
        }
        CommandLineInterface.printTable(header, data, 1);
    }

    protected void stampaArticolo(Item item) {
        if(item == null){
            CommandLineInterface.printError("Errore: non hai inserito un ItemCode valido.");
            return;
        }
        CommandLineInterface.printCard("Dati articolo", item.toStringValues());
        if (item.getNumberOfLibraries() != 0) {
            CommandLineInterface.printMessage("Biblioteche:");
            String[] header = {"Sede", "Num. copie", "Stato"};
            CommandLineInterface.printTable(header, item.getPhysicalCopiesData(), 0);
        }
    }

    protected void stampaPrenotazioni(ArrayList<Reservation> reservations) {
        if (reservations.isEmpty()) {
            CommandLineInterface.printMessage("Non hai effettuato prenotazioni.");
            return;
        }
        String[] header = {"itemCode", "Titolo", "Sede", "Scadenza"};
        ArrayList<String[]> data = new ArrayList<>();
        for (Reservation r : reservations) {
            data.add(new String[]{Integer.toString(r.getItem().getCode()), r.getItem().getTitle(), r.getStoragePlace().toString(), r.getReservationDate().plusWeeks(1).toString()});
        }
        CommandLineInterface.printTable(header, data, 1);
    }

    protected void stampaPrestiti(ArrayList<Lending> lendings) {
        if (lendings.isEmpty()) {
            CommandLineInterface.printMessage("Non hai effettuato prestiti.");
            return;
        }
        String[] header = {"itemCode", "Titolo", "Sede", "Scadenza"};
        ArrayList<String[]> data = new ArrayList<>();
        for (Lending l : lendings) {
            data.add(new String[]{Integer.toString(l.getItem().getCode()), l.getItem().getTitle(), l.getStoragePlace().toString(), l.getLendingDate().plusMonths(1).toString()});
        }
        CommandLineInterface.printTable(header, data, 1);
    }
}
