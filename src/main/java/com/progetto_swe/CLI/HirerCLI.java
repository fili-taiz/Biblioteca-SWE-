package com.progetto_swe.CLI;

import com.progetto_swe.business_logic.HirerController;
import com.progetto_swe.domain_model.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class HirerCLI {/*
    private Hirer hirer;
    private Scanner scanner = new Scanner(System.in);

    public HirerCLI(Hirer hirer) {
        this.hirer = hirer;
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
                    "Esci: se vuoi uscire dall'applicazione.");
            scelta = scanner.nextLine().toUpperCase();

            switch (scelta) {
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

                case "RESOCONTO PRENOTAZIONI": {
                    paginaResocontoPrenotazioni();
                    break;
                }

                case "RESOCONTO PRESTITI": {
                    paginaResocontoPrestiti();
                    break;
                }

                case "LOGOUT": {
                    return "UTENTE ANONIMO";
                }
            }
        } while (!scelta.equals("ESCI"));
        return "ESCI";
    }

    private Item getItem(ArrayList<Item> items, int code) {
        for (Item i : items) {
            if (i.getCode() == code) {
                return i;
            }
        }
        return null; //TODO eccezione ID errato
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
        return hirerController.advanceSearchItem(keywords, category, language, borrowable, startDate, endDate);
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
        CommandLineInterface.clearScreen();

        String operazione;
        do {
            stampaArticolo(item);

            System.out.println("Inserisci l'operazione che vuoi eseguire: \n" +
                    "Prenota: se vuoi prenotare un articolo; " +
                    "Aggiungimi: se vuoi ricevere una notifica una volta l'articolo sia disponibile" +
                    "Indietro: tornare alla pagina precedente; ");
            operazione = scanner.nextLine().toUpperCase();

            switch (operazione) {
                case "PRENOTA": {
                    System.out.println("Inserisci il nome della sede in cui vuoi ritirare l'articolo: ");
                    String storagePlace = scanner.nextLine();

                    hirerController.reserveItem(item, Library.valueOf(storagePlace));
                    System.out.println("Prenotazione effettuata con successo.");
                    return "ESCI";
                }

                case "AGGIUNGIMI": {
                    System.out.println("Inserisci il nome della sede di cui ottenere notifica: ");
                    String storagePlace = scanner.nextLine();

                    hirerController.addInWaitingList(item, storagePlace);
                    return "INDIETRO";
                }

                case "INDIETRO": {
                    return "INDIETRO";
                }

                default: {
                    System.out.println("Errore: non hai inserito un operazione corretta.");
                }
            }
        } while (true);
    }

    private void paginaResocontoPrenotazioni() {
        String operazione;
        do {
            CommandLineInterface.clearScreen();
            ArrayList<Reservation> reservations = hirerController.getReservation();
            stampaPrenotazioni(reservations);
            System.out.println("\n" + //TODO dare un occhiata se sostituire con text block
                    "Inserisci l'operazione che vuoi eseguire: \n" +
                    "Cancella: se vuoi cancellare una prenotazione; \n" +
                    "Esci: tornare alla home page. ");
            operazione = scanner.nextLine().toUpperCase();
            switch (operazione) {
                case "CANCELLA": {
                    System.out.println("Inserisci [Codice articolo] dell'articolo di cui vuoi cancellare la prenotazione: ");
                    String itemCode = scanner.nextLine();

                    System.out.println("Inserisci il nome della biblioteca in cui ha effettuato la prenotazione: ");
                    String storagePlace = scanner.nextLine();
                    try {
                        hirerController.removeReservation(getReservation(reservations, Integer.parseInt(itemCode), storagePlace));
                    } catch (NumberFormatException e) {
                        CommandLineInterface.clearScreen();
                        System.out.println("Errore: non hai inserito un codice corretto.");
                    } catch (Exception e) { // TODO ipotetico eccezione oggetto inesistente
                        CommandLineInterface.clearScreen();
                        System.out.println("Errore: articolo inesistente.");
                    }
                    System.out.println("Prenotazione effettuata con successo.");
                    break;
                }

                case "ESCI": {
                    return;
                }

                default: {
                    System.out.println("Errore: non hai inserito un operazione corretta.");
                }
            }
        } while (!operazione.equals("ESCI"));
    }

    private Reservation getReservation(ArrayList<Reservation> reservations, int itemCode, String storagePlace) {
        for (Reservation r : reservations) {
            if (r.getItem().getCode() == itemCode && r.getStoragePlace().equals(Library.valueOf(storagePlace))) {
                return r;
            }
        }
        return null; //TODO eccezione ID errato
    }


    private void stampaPrenotazioni(ArrayList<Reservation> reservations) {
        String[] header = {"Titolo Articolo", "Sede", "Scadenza prenotazione"};
        ArrayList<String[]> data = new ArrayList<>();
        for (Reservation r : reservations) {
            data.add(new String[]{Integer.toString(r.getItem().getCode()), r.getItem().getTitle(), r.getStoragePlace().toString(), r.getReservationDate().plusWeeks(1).toString()});
        }
        CommandLineInterface.printTable(header, data, 0);
    }


    private void paginaResocontoPrestiti() {
        String operazione;
        do {
            CommandLineInterface.clearScreen();
            ArrayList<Lending> lendings = hirerController.getLendings();
            stampaPrestiti(lendings);
            System.out.println("\n" + //TODO dare un occhiata se sostituire con text block
                    "Inserisci l'operazione che vuoi eseguire: \n" +
                    "Esci: tornare alla home page. ");
            operazione = scanner.nextLine().toUpperCase();
            if(!operazione.equals("ESCI")) {
                System.out.println("Errore: non hai inserito un operazione valida.");
            }
        } while (!operazione.equals("ESCI"));
    }


    private void stampaPrestiti(ArrayList<Lending> lendings) {
        String[] header = {"Titolo Articolo", "Sede", "Scadenza prenotazione"};
        ArrayList<String[]> data = new ArrayList<>();
        for (Lending l : lendings) {
            data.add(new String[]{Integer.toString(l.getItem().getCode()), l.getItem().getTitle(), l.getStoragePlace().toString(), l.getMaturityDate().toString()});
        }
        CommandLineInterface.printTable(header, data, 0);
    }


    private void stampaArticoli(ArrayList<Item> items) {
        CommandLineInterface.clearScreen();
        String[] header = {"Titolo Articolo", "Autore", "Categoria", "Data Pubblicazione"}; //TODO moficare item compagnia per passare il code
        ArrayList<String[]> data = new ArrayList<>();
        for (Item item : items) {
            data.add(item.getValues());
        }
        CommandLineInterface.printTable(header, data, 0);
    }

    private void stampaArticolo(Item item) {
        CommandLineInterface.clearScreen();
        CommandLineInterface.printCard("Dati articolo", item.toStringValues(), 1);
        //TODO visualizzare che un articolo è gia stato messo in lista di attesa oppure prenotato
        System.out.println("Biblioteche:");
        String[] header = {"Sede", "Numero copie", "Stato"};
        ArrayList<String[]> data = new ArrayList<>();
        for (Library library : item.getPhysicalCopies().keySet()) {
            data.add(new String[]{library.toString(), Integer.toString(item.getLibraryPhysicalCopies(library).getNumberOfAvailableCopies()), state(item.getLibraryPhysicalCopies(library).getNumberOfAvailableCopies(), item.isBorrowable(library))});
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
