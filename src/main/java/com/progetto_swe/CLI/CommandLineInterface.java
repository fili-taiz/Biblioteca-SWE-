package com.progetto_swe.CLI;


import java.util.ArrayList;
import java.util.Scanner;

import com.progetto_swe.business_logic.*;
import com.progetto_swe.business_logic.business_logic_exception.AccessDeniedException;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.database_exception.IdNotFoundException;

public class CommandLineInterface {
    private static int screenWidth = 90;
    private String role = "UTENTE ANONIMO";
    private static Scanner scanner = new Scanner(System.in);
    private BaseCLI CLI = new BaseCLI();
    private static final String punctuation = " ,;.:-_\\|!\"£$%&/()=?'^€+*[]{}§@#°<>«»";

    public void start() {
        ArrayList<String[]> menu;
        String scelta;
        do {
            menu = CLI.getMenu();
            menu.add(new String[]{"Esci", "uscire dall'applicazione"});
            printMenu(menu);
            scelta = scanner.nextLine().toUpperCase();
            switch (scelta) {
                case "LOGIN": {
                    clearScreen();
                    printMessage("Inserisci il ruolo con cui vuoi effettuare l'accesso al sito tra quelli seguenti:");
                    printMessage("[NOLEGGIATORE ESTERNO], [NOLEGGIATORE UNIVERSITARIO], [AMMINISTRATORE BIBLIOTECARIO]; ");
                    role = scanner.nextLine().toUpperCase();
                    switch (role) {
                        case "NOLEGGIATORE ESTERNO": {
                            HirerController hirerController = new HirerController();
                            System.out.println("Inserisci il tuo codice utente: ");
                            String userCode = scanner.nextLine();
                            System.out.println("Inserisci la tua password: ");
                            String password = scanner.nextLine();

                            try {
                                Hirer hirer = hirerController.loginExternalHirer(userCode, password);
                                CLI = new HirerCLI(hirer);
                            } catch (AccessDeniedException | IdNotFoundException e) {
                                clearScreen();
                                printError(e.getMessage());
                            }
                            scelta = "OPERATION";
                            break;
                        }

                        case "NOLEGGIATORE UNIVERSITARIO": {
                            HirerController hirerController = new HirerController();
                            System.out.println("Inserisci il tuo codice utente: ");
                            String userCode = scanner.nextLine();
                            System.out.println("Inserisci la tua password: ");
                            String password = scanner.nextLine();
                            try {
                                Hirer hirer = hirerController.loginUniversityHirer(userCode, password);
                                CLI = new HirerCLI(hirer);
                                scelta = "OPERATION";
                            } catch (AccessDeniedException e) {
                                clearScreen();
                                printError(e.getMessage());
                            }
                            break;
                        }

                        case "AMMINISTRATORE BIBLIOTECARIO": {
                            AdminController adminController = new AdminController();
                            System.out.println("Inserisci il tuo codice utente: ");
                            String userCode = scanner.nextLine();
                            System.out.println("Inserisci la tua password: ");
                            String password = scanner.nextLine();
                            try {
                                Admin admin = adminController.loginAdmin(userCode, password);
                                CLI = new AdminCLI(admin);
                                scelta = "OPERATION";
                            } catch (AccessDeniedException e) {
                                clearScreen();
                                printError(e.getMessage());
                            }
                            break;
                        }

                        default: {
                            clearScreen();
                            System.out.println("Errore: non hai inserito un ruolo corretto.");
                            CLI = new BaseCLI();
                            break;
                        }
                    }
                    break;
                }

                case "LOGOUT": {
                    CLI = new BaseCLI();
                    break;
                }

                default: {
                    try {
                        CLI.execute(scelta);
                    } catch (Exception e) {
                        printError(e.getMessage());
                        scelta = "ESCI";
                    }
                    break;
                }
            }
        } while (!scelta.equals("ESCI"));
    }


    //funzioni d stampa
    private static void printBiblioteca() {
        String biblioteca = "Benvenuto in Biblioteca!";
        System.out.print("─".repeat((int) Math.floor((screenWidth - biblioteca.length()) / 2.0)));
        System.out.print(biblioteca);
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

    private static void printMenu(ArrayList<String[]> rows) {
        int[] columnWidths = calculateColumnWidths(rows);
        int maxCellWidth = screenWidth - columnWidths[0] - 2; //": " e ";"
        for (int i = 0; i < rows.size(); i++) {
            rows.get(i)[1] += ";";
            ArrayList<String> splittedString = splitString(rows.get(i)[1], maxCellWidth);
            rows.get(i)[1] = splittedString.get(0);
            for (int j = 1; j < splittedString.size(); j++) {
                rows.add(i + 1, new String[]{"", splittedString.get(j)});
                i++;
            }
        }
        for (String[] row : rows) {
            System.out.printf("%" + (columnWidths[0]) + "s: ", row[0]);
            System.out.printf("%-" + (columnWidths[1]) + "s\n", row[1]);
        }
        System.out.println();
    }

    //funzioni di Layout
    public static void printCard(String title, ArrayList<String[]> rows) {
        int[] columnWidths = calculateColumnWidths(rows);

        int maxWidth = calculateRowWidth(columnWidths, 1, 1, 3);
        if (screenWidth < maxWidth) {
            rows = truncate(rows, columnWidths[1] - (maxWidth - screenWidth), 1);
            columnWidths[1] = columnWidths[1] - (maxWidth - screenWidth);
            maxWidth = screenWidth;
        }

        System.out.println("┌" + title + "─".repeat(maxWidth - title.length() - 2) + "┐");
        for (String[] row : rows) {
            System.out.print("|");
            System.out.printf("%-" + (columnWidths[0] + 2) + "s", row[0] + ": ");
            System.out.printf("%-" + (columnWidths[1]) + "s |\n", row[1]);
        }
        System.out.println("└" + "─".repeat(maxWidth - 2) + "┘\n");
    }

    public static void printTable(String[] header, ArrayList<String[]> rows) {
        rows.add(0, header);

        //calcolo lunghezza massima ogni colonna
        int[] columnWidths = calculateColumnWidths(rows);

        //calcolo lunghezza finale per riga contenente simboli per tabella
        int maxWidth = calculateRowWidth(columnWidths, 1, 1, 3);
        while (screenWidth < maxWidth) {
            int colToTruncate = 0;
            for(int i = 0 ; i < columnWidths.length ; i++) {
                if(columnWidths[i] > columnWidths[colToTruncate]) {
                    colToTruncate = i;
                }
            }
            int lengthToCut = (int) Math.ceil((maxWidth - screenWidth)/3.0);
            //se lunghezza massima > lunghezza schermo taglio contenuto della colonna colToTruncate
            rows = truncate(rows, columnWidths[colToTruncate] - lengthToCut, colToTruncate);
            columnWidths[colToTruncate] = columnWidths[colToTruncate] - lengthToCut;
            maxWidth -= lengthToCut;
        }

        for (int i = 0; i < rows.get(0).length; i++) {
            System.out.printf(" %-" + columnWidths[i] + "s |", header[i]);
        }
        System.out.println("\b");
        System.out.println("-".repeat(maxWidth));
        for (int i = 1; i < rows.size(); i++) {
            for (int j = 0; j < rows.get(i).length; j++) {
                System.out.printf(" %-" + columnWidths[j] + "s |", rows.get(i)[j]);
            }
            System.out.println("\b");
        }
        System.out.println("\n\n");
    }

    public static void printMessage(String message) {
        ArrayList<String> rows = splitString(message, screenWidth);
        for (String row : rows) {
            System.out.println(row);
        }
    }

    public static void printError(String message) {
        ArrayList<String> rows = splitString(message, screenWidth);
        for (String row : rows) {
            System.out.println("\u001B[31m" + row + "\u001B[0m");
        }
    }

    private static ArrayList<String> splitString(String message, int maxLength) {
        int i = 0;
        ArrayList<String> rows = new ArrayList<>();
        while (message.length() > maxLength + i) {
            if (isPunctuation(message.charAt(maxLength + i))) {
                rows.add(message.substring(i, maxLength + i));
            } else {
                rows.add(message.substring(i, maxLength + i - 1) + "-");
                i--;
            }
            i = i + maxLength;
        }
        rows.add(message.substring(i));
        return rows;
    }

    private static int calculateRowWidth(int[] columnWidths, int startSpace, int endSpace, int span) {
        int totalLength = startSpace + endSpace;
        for (int width : columnWidths) {
            totalLength += width; // Somma la larghezza della colonna
            totalLength += span; // Aggiunge | e uno spazio tra le colonne
        }
        return totalLength - span; // Rimuove lo spazio extra alla fine
    }


    //calcolo della larghezza più lunga in ogni colonna
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

    private static ArrayList<String[]> truncate(ArrayList<String[]> rows, int maxWidth, int colToTruncate) {
        for (String[] row : rows) {
            if (row[colToTruncate].length() > maxWidth) {
                row[colToTruncate] = row[colToTruncate].substring(0, maxWidth - 1) + ".";
            }
        }
        return rows;
    }

    private static boolean isPunctuation(char c) {
        return punctuation.contains(String.valueOf(c));
    }
}
