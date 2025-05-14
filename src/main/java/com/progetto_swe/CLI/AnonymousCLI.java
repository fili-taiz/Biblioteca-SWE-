package com.progetto_swe.CLI;

import java.util.Scanner;

public class AnonymousCLI {

    /*private AnonymousUserController anonymousUserController = new AnonymousUserController();
    private Scanner scanner = new Scanner(System.in);
/*
    public String start() {
        CommandLineInterface.clearScreen();
        String scelta;
        do {
            //leggere input user
            System.out.println("Inserisci l'operazione che vuoi eseguire: \n" +
                    "Ricerca: ricercare un'articolo all'interno della biblioteca; \n" +
                    "Ricerca avanzata: ricercare con filtri; \n" +
                    "Login: effettuare l'accesso al sito identificandoti con le tue credenziali; \n" +
                    "Esci: se vuoi uscire dall'applicazione.");
            scelta = scanner.nextLine().toUpperCase();

            switch (scelta) {

                case "RICERCA": {
                    try {
                        String operazione;
                        do {
                            ArrayList<Item> items = ricerca();
                            operazione = paginaArticoli(items);
                        } while (!operazione.equals("ESCI"));
                    } catch (Exception e) { //TODO eccezione Categoria inesistente
                        CommandLineInterface.clearScreen();
                        System.out.println("Errore: non hai inserito una categoria corretta.");
                    }
                    break;
                }

                case "RICERCA AVANZATA": {
                    try {
                        String operazione;
                        do {
                            ArrayList<Item> items = ricercaAvanzata();
                            operazione = paginaArticoli(items);
                        } while (!operazione.equals("ESCI"));
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

                case "LOGIN": {
                    System.out.println("Inserisci il ruolo con cui vuoi effettuare l'accesso al sito tra quelli seguenti: \n" +
                            "[NOLEGGIATORE ESTERNO], [NOLEGGIATORE UNIVERSITARIO], [AMMINISTRATORE BIBLIOTECARIO]; ");
                    return scanner.nextLine().toUpperCase();
                }
            }
        } while (!scelta.equals("ESCI"));
        return "ESCI";
    }

    private int getPosition(ArrayList<Item> items, int code ){
        for(int i = 0 ; i < items.size(); i++){
            if(items.get(i).getCode() == code){
                return i;
            }
        }
        return -1; //TODO eccezione ID errato
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

        System.out.println("Inserisci [Si] se l'articolo deve essere noleggiabile in una nostra biblioteca: ");
        boolean borrowable = scanner.nextLine().toUpperCase().equals("SI");

        System.out.println("Inserisci l'intervallo in cui è stato pubblicato l'articolo che stai cercando: \n" +
                "Data inizio: [formato GG/MM/AAAA]");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());
        System.out.println("Data fine: [formato GG/MM/AAAA]");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());

        System.out.println("\n\nInserisci le parole chiavi dell'articolo che vuoi cercare: ");
        String keywords = scanner.nextLine().toUpperCase();
        return anonymousUserController.advanceSearchItem(keywords, category, language, borrowable, startDate, endDate);
    }

    private String paginaArticoli(ArrayList<Item> items) {
        CommandLineInterface.clearScreen();
        String code;
        do {
            stampaArticoli(items);

            System.out.println("Inserisci l'operazione che vuoi eseguire: \n" +
                    "[Un codice di un articolo]: visualizzare i dettagli di un certo articolo; " +
                    "Indietro: tornare alla pagina precedente; " +
                    "Esci: tornare alla home page. ");
            code = scanner.nextLine().toUpperCase();
            try{
                int itemCode = Integer.parseInt(code);
                paginaArticolo(items.get(getPosition(items, itemCode)));
            } catch (NumberFormatException e) {
                CommandLineInterface.clearScreen();
                System.out.println("Errore: non hai inserito un codice corretto.");
            } // catch id inserito non presente
        } while (!code.equals("INDIETRO") && !code.equals("ESCI"));
        return code;
    }

    private void paginaArticolo(Item item) {
        CommandLineInterface.clearScreen();

        String operazione;
        do {
            stampaArticolo(item);
            System.out.println("Inserisci l'operazione che vuoi eseguire: \n" +
                    "Indietro: tornare alla pagina precedente; ");
            operazione = scanner.nextLine().toUpperCase();
        } while (!operazione.equals("INDIETRO"));
    }

    private void stampaArticoli(ArrayList<Item> items) {
        CommandLineInterface.clearScreen();
        String[] header = {"Titolo Articolo", "Autore", "Categoria", "Data Pubblicazione"};
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
            data.add(new String[]{library.toString(), Integer.toString(item.getNumberOfAvailableCopiesInLibrary(library)), state(item.getNumberOfAvailableCopiesInLibrary(library), item.isBorrowable(library))});
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
