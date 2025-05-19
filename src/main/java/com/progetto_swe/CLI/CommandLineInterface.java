package com.progetto_swe.CLI;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.progetto_swe.business_logic.*;
import com.progetto_swe.domain_model.*;

public class CommandLineInterface {/*
    static int screenWidth = 90;
    String role = "UTENTE ANONIMO";
    static Scanner scanner = new Scanner(System.in);

    public void start() {

        do {
            switch (role) {
                case "UTENTE ANONIMO": {
                    AnonymousCLI anonymousCLI = new AnonymousCLI();
                    role = anonymousCLI.start();
                    break;
                }

                case "NOLEGGIATORE ESTERNO": {
                    clearScreen();
                    HirerController hirerController = new HirerController();

                    //ottiene le credenziali
                    System.out.println("Inserisci il tuo codice utente: ");
                    String userCode = scanner.nextLine();
                    System.out.println("Inserisci la tua password: ");
                    String password = scanner.nextLine();

                    Hirer hirer = hirerController.loginExternalHirer(userCode, password);
                    if (hirer == null) {
                        System.out.println("Errore: nome utente o password errati.");
                    } else {
                        HirerCLI hirerCLI = new HirerCLI(hirerController);
                        role = hirerCLI.start();
                    }
                    break;
                }

                case "NOLEGGIATORE UNIVERSITARIO": {
                    clearScreen();
                    LoginUniversityHirerController loginUniversityHirer = new LoginUniversityHirerController();

                    //ottiene le credenziali
                    System.out.println("Inserisci il tuo codice utente: ");
                    String userCode = scanner.nextLine();
                    System.out.println("Inserisci la tua password: ");
                    String password = scanner.nextLine();

                    Hirer hirer = loginUniversityHirer.loginUniversityHirer(userCode, password);
                    if(hirer == null) {
                        System.out.println("Errore: nome utente o password errati.");
                    } else {
                        HirerCLI hirerCLI = new HirerCLI(new HirerController(hirer));
                        role = hirerCLI.start();
                    }
                    break;
                }

                case "AMMINISTRATORE BIBLIOTECARIO": {
                    clearScreen();
                    LoginAdminController loginAdminController = new LoginAdminController();
                    System.out.println("Inserisci il tuo codice utente: ");
                    String userCode = scanner.nextLine();
                    System.out.println("Inserisci la tua password: ");
                    String password = scanner.nextLine();

                    Admin admin = loginAdminController.loginAdmin(userCode, password);
                    if(admin == null) {
                        System.out.println("Errore: nome utente o password errati.");
                    } else {
                        AdminCLI adminCLI = new AdminCLI(new AdminController(admin), admin.getWorkingPlace());
                        role = adminCLI.start();
                    }
                    break;
                }

                case "LOGIN" : {
                    System.out.println("Inserisci il ruolo con cui vuoi effettuare l'accesso al sito tra quelli seguenti: \n" +
                            "[NOLEGGIATORE ESTERNO], [NOLEGGIATORE UNIVERSITARIO], [AMMINISTRATORE BIBLIOTECARIO]; ");
                    role = scanner.nextLine().toUpperCase();
                    break;
                }

                default: {
                    clearScreen();
                    System.out.println("Errore: non hai inserito un ruolo corretto.");
                    role = "LOGIN";
                    break;
                }
            }
        } while (!role.equals("ESCI"));
    }

    //evitare codice duplicato
    public static HashMap<String, String> getRicercaParam() {
        HashMap<String, String> ricercaParam = new HashMap<>();
        System.out.println("Inserisci a quale categoria appartiene l'articolo che stai cercando tra quelli elencati: ");
        for (Category c : Category.values()) {
            System.out.print(c + ", ");
        }
        System.out.print("\b;");
        ricercaParam.put("Category", scanner.nextLine());

        System.out.println("\n\nInserisci le parole chiavi dell'articolo che vuoi cercare: ");
        ricercaParam.put("Keywords", scanner.nextLine());

        return ricercaParam;
    }


    private ArrayList<Item> getRicercaAvanzataParam() {
        CommandLineInterface.clearScreen();
        HashMap<String, String> ricercaParam = new HashMap<>();
        System.out.println("Inserisci a quale categoria appartiene l'articolo che stai cercando tra quelli elencati: ");
        for (Category c : Category.values()) {
            System.out.print(c + ", ");
        }
        System.out.println("\b;");
        ricercaParam.put("Category", scanner.nextLine());

        System.out.println("Inserisci in quale lingua è scritto l'articolo che stai cercando tra quelli elencati: ");
        for (Language l : Language.values()) {
            System.out.print(l + ", ");
        }
        System.out.println("\b;");
        ricercaParam.put("Language", scanner.nextLine());

        System.out.println("Inserisci [Si] se l'articolo deve essere noleggiabile in una nostra biblioteca: ");
        ricercaParam.put("Borrowable", scanner.nextLine());

        System.out.println("Inserisci l'intervallo in cui è stato pubblicato l'articolo che stai cercando: \n" +
                "Data inizio: [formato GG/MM/AAAA]");
        ricercaParam.put("StartDate", scanner.nextLine());
        System.out.println("Data fine: [formato GG/MM/AAAA]");
        ricercaParam.put("EndDate", scanner.nextLine());

        System.out.println("\n\nInserisci le parole chiavi dell'articolo che vuoi cercare: ");
        ricercaParam.put("Keywords", scanner.nextLine());
        ItemController itemController = new ItemController();
        itemController.advanceSearchItem();
    }


    public static String state(int numberOfCopies, boolean borrowable) {
        if (!borrowable) {
            return "Non noleggiabile";
        }
        if (numberOfCopies == 0) {
            return "Esaurito";
        }
        return "Prenotabile";
    }



    //funzioni d stampa

    public static void printBiblioteca() {
        String biblioteca = "Un uomo entra in una Biblioteca, SPLASH!";
        System.out.print("─".repeat((int) Math.floor((screenWidth - biblioteca.length()) / 2.0)));
        System.out.println(biblioteca);
        System.out.print("─".repeat((int) Math.ceil((screenWidth - biblioteca.length()) / 2.0)));
        System.out.println("\n\n\n");
    }

    public void title(String title) {
        System.out.println(title);
        System.out.print("─".repeat(screenWidth - title.length()));
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        printBiblioteca();
    }




    //funzioni di Layout
    public static void printCard(String title, ArrayList<String[]> rows, int colToTruncate) {
        int[] columnWidths = calculateColumnWidths(rows);

        int maxWidth = calculateRowWidth(columnWidths);
        if (screenWidth - 2 < maxWidth) {
            maxWidth = lengthOfLongestStringToTruncate(rows, colToTruncate) - (screenWidth - 2 - maxWidth);
        }

        System.out.println("┌" + title + "─".repeat(maxWidth - title.length() - 2) + "┐\n");
        for (String[] row : rows) {
            System.out.print("|");
            for (int i = 0; i < row.length; i++) {
                System.out.printf("%-" + columnWidths[i] + "s ", truncate(row[i], maxWidth));
            }
            System.out.println("|");
        }
        System.out.println("└" + "─".repeat(maxWidth - 2) + "┘\n");
    }

    public static void printTable(String[] header, ArrayList<String[]> rows, int colToTruncate) {
        rows.add(0, header);

        int[] columnWidths = calculateColumnWidths(rows);

        int maxWidth = calculateRowWidth(columnWidths);
        if (screenWidth < maxWidth) {
            maxWidth = lengthOfLongestStringToTruncate(rows, colToTruncate) - (screenWidth - maxWidth);
        }

        for (int i = 0; i < rows.get(0).length; i++) {
            System.out.printf("%-" + columnWidths[i] + "s ", truncate(rows.get(0)[i], maxWidth));
        }

        System.out.println("-".repeat(maxWidth));
        for (String[] row : rows) {
            for (int i = 1; i < row.length; i++) {
                System.out.printf("%-" + columnWidths[i] + "s ", truncate(row[i], maxWidth));
            }
            System.out.println();
        }
    }

    private static int calculateRowWidth(int[] columnWidths) {
        int totalLength = 0;
        for (int width : columnWidths) {
            totalLength += width; // Somma la larghezza della colonna
            totalLength += 1; // Aggiunge uno spazio tra le colonne
        }
        return totalLength - 1; // Rimuove lo spazio extra alla fine
    }

    private static int[] calculateColumnWidths(ArrayList<String[]> rows) {
        int columns = rows.get(0).length;
        int[] columnWidths = new int[columns];

        for (String[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                columnWidths[i] = Math.max(columnWidths[i], row[i].length());
            }
        }
        return columnWidths;
    }

    private static String truncate(String value, int maxWidth) {
        if (value.length() > maxWidth) {
            return value.substring(0, maxWidth - 1) + ".";
        }
        return value;
    }

    private static int lengthOfLongestStringToTruncate(ArrayList<String[]> rows, int colToTruncate) {
        int maxLength = 0;
        for (String[] row : rows) {
            if (row[colToTruncate].length() > maxLength) {
                maxLength = row[colToTruncate].length();
            }
        }
        return maxLength;
    }*/
}
