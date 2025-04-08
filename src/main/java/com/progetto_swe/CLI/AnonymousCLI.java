package com.progetto_swe.CLI;

import com.progetto_swe.business_logic.AnonymousUserController;
import com.progetto_swe.domain_model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class AnonymousCLI {

   /* private AnonymousUserController anonymousUserController = new AnonymousUserController();
    private Scanner scanner = new Scanner(System.in);

    public String start() {
        String scelta;
        do {
            CommandLineInterface.clearScreen();
            //leggere input user
            System.out.println("Inserisci l'operazione che vuoi eseguire: \n" +
                    "Ricerca: ricercare un'articolo all'interno della biblioteca; \n" +
                    "Ricerca avanzata: ricercare con filtri; \n" +
                    "Login: effettuare l'accesso al sito identificandoti con le tue credenziali; \n" +
                    "Esci: se vuoi uscire dall'applicazione");
            scelta = scanner.nextLine().toLowerCase();

            switch (scelta) {
                case "ricerca": {
                    ArrayList<Item> items = ricerca();

                    String code;
                    do {
                        CommandLineInterface.clearScreen();
                        stampaArticoli(items);

                        System.out.println("Inserisci l'operazione che vuoi eseguire: \n" +
                                "[Un codice di un articolo]: visualizzare i dettagli di un certo articolo*" +
                                "Indietro: tornare alla pagina precedente");
                        System.out.println("\u001B[31m " +
                                "[Attenzione] non hai effettuato al login nel sito, le operazioni di prestito possono solo essere effettuate se hai effettuato il login.\n" +
                                "Se effettui il login dopo una ricerca il risultato della ricerca andrà perduta e dovrai rieffettuare la ricerca da capo \u001B[0m");
                        code = scanner.nextLine().toLowerCase();
                        stampaArticolo(items.get(Integer.valueOf(code)));
                    } while (!code.equals("indietro"));
                    break;
                }

                case "ricerca avanzata": {
                    ArrayList<Item> items = ricercaAvanzata();
                    String code;
                    do {
                        stampaArticoli(items);
                        System.out.println("Inserisci l'operazione che vuoi eseguire: \n" +
                                "[Un codice di un articolo]: visualizzare i dettagli di un certo articolo*" +
                                "Indietro: tornare alla pagina precedente");
                        System.out.println("\u001B[31m " +
                                "[Attenzione] non hai effettuato al login nel sito, le operazioni di prestito possono solo essere effettuate se hai effettuato il login.\n" +
                                "Se effettui il login dopo una ricerca il risultato della ricerca andrà perduta e dovrai rieffettuare la ricerca da capo \u001B[0m");
                        code = scanner.nextLine().toLowerCase();
                        stampaArticolo(items.get(Integer.valueOf(code)));
                    } while (!code.equals("indietro"));
                    break;
                }

                case "login": {
                    System.out.println("Inserisci il ruolo con cui vuoi effettuare l'accesso al sito");
                    return scanner.nextLine().toLowerCase();
                }
            }
        } while (!scelta.equals("esci"));
        return "esci";
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

        return anonymousUserController.searchItem(keywords, category);
    }

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

        System.out.println("Inserisci: " +
                "Si: se l'articolo deve essere noleggiabile in una nostra biblioteca" +
                "No: se l'articolo può non essere noleggiabile");
        System.out.print("\b;");
        boolean borrowable = scanner.nextLine().equals("Si");

        System.out.println("Inserisci l'intervallo in cui è stato pubblicato l'articolo che stai cercando: \n" +
                "Data inizio: [formato GG/MM/AAAA]");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());
        System.out.println("Data fine: [formato GG/MM/AAAA]");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());

        System.out.println("\n\nInserisci le parole chiavi dell'articolo che vuoi cercare: ");
        String keywords = scanner.nextLine();
        return anonymousUserController.advanceSearchItem(keywords, category, language, borrowable, startDate, endDate);
    }

    private void stampaArticoli(ArrayList<Item> items) {
        CommandLineInterface.clearScreen();
        String[] header = {"Nome Articolo", "Autore", "Categoria", "Data Pubblicazione"};
        ArrayList<String[]> data = new ArrayList<>();
        for (Item item : items) {
            data.add(item.getValues());
        }
        CommandLineInterface.printTable(header, data, 0);
    }

    private void stampaArticolo(Item item) {
        CommandLineInterface.clearScreen();
        CommandLineInterface.printCard("Dati articolo", item.toStringValues(), 1);

        System.out.println("Biblioteche:");
        String[] header = {"Sede", "Numero copie", "Stato"};
        ArrayList<String[]> data = new ArrayList<>();
        for (Library library : item.getPhysicalCopies().keySet()) {
            ListOfLendings lendings = anonymousUserController.getListOfLending();
            ListOfReservations reservations = anonymousUserController.getListOfReservation();
            data.add(new String[]{library.toString(), Integer.toString(item.getNumberOfAvailableCopiesInLibrary(lendings, reservations, library)), state(item.getNumberOfAvailableCopiesInLibrary(lendings, reservations, library), item.isBorrowable(library))});
        }
        CommandLineInterface.printTable(header, data, 0);
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
