package com.progetto_swe.CLI;

import com.progetto_swe.business_logic.HirerController;
import com.progetto_swe.domain_model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class HirerCLI {

    private HirerController hirerController;
    private Scanner scanner = new Scanner(System.in);

    public HirerCLI(HirerController hirerController) {
        this.hirerController = hirerController;
    }

    public String start() {
        String scelta;
        do {
            CommandLineInterface.clearScreen();
            //leggere input user
            System.out.println("Inserisci l'operazione che vuoi eseguire: \n" +
                    "Ricerca: ricercare un'articolo all'interno della biblioteca; \n" +
                    "Ricerca avanzata: ricercare un'articolo con filtri; \n" +
                    "Resoconto prestiti: visualizzare tutti i prestiti effettuati; \n" +
                    "Resoconto prenotazioni: visualizzare tutte le prenotazioni effettuate; \n" +
                    "Logout: uscire dal proprio profilo; \n" +
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
                                "[Codice di un articolo]: visualizzare i dettagli di un certo articolo*" +
                                "Indietro: tornare alla pagina precedente");
                        System.out.println("\u001B[31m " +
                                "[Attenzione] non hai effettuato al login nel sito, le operazioni di prestito possono solo essere effettuate se hai effettuato il login.\n" +
                                "Se effettui il login dopo una ricerca il risultato della ricerca andrà perduta e dovrai rieffettuare la ricerca da capo \u001B[0m");
                        code = scanner.nextLine().toLowerCase();
                        stampaArticolo(items.get(Integer.valueOf(code)));
                    } while (!code.equals("indietro"));
                    break;
                }

                case "resoconto prestiti": {
                    CommandLineInterface.clearScreen();
                    stampaPrestiti();
                    String operazione;
                    do {
                        System.out.println("Inserisci l'operazione che vuoi eseguire: \n" +
                                "Indietro: tornare alla pagina precedente; ");
                        operazione = scanner.nextLine().toLowerCase();
                    } while (!operazione.equals("indietro"));
                    break;
                }

                case "resoconto prenotazioni": {
                    CommandLineInterface.clearScreen();
                    String operazione;
                    do {
                        stampaPrenotazioni();
                        System.out.println("Inserisci l'operazione che vuoi eseguire: \n" +
                                "[Codice di un articolo]: cancellare la prenotazione riguardante il libro con il codice inserito \n" +
                                "Indietro: tornare alla pagina precedente; ");
                        operazione = scanner.nextLine().toLowerCase();
                    } while (!operazione.equals("indietro"));
                    break;
                }


                case "logout":
                    return "utente anonimo";
            }
        } while (!scelta.equals("esci"));
        return "esci";
    }

    private void stampaPrenotazioni() {
        /// TODO
    }

    private void stampaPrestiti() {
        //TODO
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

        return hirerController.searchItem(keywords, category);
    }

    private ArrayList<Item> ricercaAvanzata() {
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
        return hirerController.advanceSearchItem(keywords, category, language, borrowable, startDate, endDate);
    }

    private ArrayList<Lending> ottieniLending() {
        return hirerController.getLendings();
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
            ListOfLending lendings = hirerController.getListOfLending();
            ListOfReservation reservations = hirerController.getListOfReservation();
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
    }
}
