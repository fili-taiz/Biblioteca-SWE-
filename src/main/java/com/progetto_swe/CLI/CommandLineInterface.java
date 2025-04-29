package com.progetto_swe.CLI;


import java.util.ArrayList;
import java.util.Scanner;

import com.progetto_swe.business_logic.*;
import com.progetto_swe.domain_model.Hirer;

public class CommandLineInterface {
    static int screenWidth = 90;
    String role = "utente anonimo";

    public void start() {
        Scanner scanner = new Scanner(System.in);
        do {
            role = role.toLowerCase();
            switch (role) {
                case "utente anonimo": {
                    AnonymousCLI anonymousCLI = new AnonymousCLI();
                    role = anonymousCLI.start();
                    break;
                }

                case "noleggiatore esterno": {
                    clearScreen();
                    LoginExternalHirerController loginExternalHirerController = new LoginExternalHirerController();
                    System.out.println("Inserisci il tuo codice utente: ");
                    String userCode = scanner.nextLine();
                    System.out.println("Inserisci la tua password: ");
                    String password = scanner.nextLine();
                    Hirer hirer = loginExternalHirerController.loginExternalHirer(userCode, password);
                    HirerController hirerController = new HirerController(hirer);
                    HirerCLI hirerCLI = new HirerCLI(hirerController);
                    role = hirerCLI.start();
                    break;
                }

                case "noleggiatore universitario": {
                    clearScreen();
                    LoginUniversityHirerController loginUniversityHirer = new LoginUniversityHirerController();
                    System.out.println("Inserisci il tuo codice utente: ");
                    String userCode = scanner.nextLine();
                    System.out.println("Inserisci la tua password: ");
                    String password = scanner.nextLine();
                    Hirer hirer = loginUniversityHirer.loginUniversityHirer(userCode, password);
                    HirerCLI hirerCLI = new HirerCLI(new HirerController(hirer));
                    role = hirerCLI.start();
                    break;
                }

                case "amministratore bibliotecario": {
                    clearScreen();
                    LoginAdminController loginAdminController = new LoginAdminController();
                    System.out.println("Inserisci il tuo codice utente: ");
                    String userCode = scanner.nextLine();
                    System.out.println("Inserisci la tua password: ");
                    String password = scanner.nextLine();
                    AdminController adminController = new AdminController(loginAdminController.loginAdmin(userCode, password));
                    AdminCLI adminCLI = new AdminCLI();
                    role = adminCLI.start();
                    break;
                }
            }
        } while (!role.equals("Esci"));
    }


    public static void printBiblioteca() {
        String biblioteca = "Un uomo entra in una Biblioteca, SPLASH! ";
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

    public static void printCard(String title, ArrayList<String[]> rows, int colToTruncate){
        int[] columnWidths = calculateColumnWidths(rows);

        int maxWidth = calculateRowWidth(columnWidths);
        if(screenWidth - 2 < maxWidth){
            maxWidth = lengthOfLongestStringToTruncate(rows, colToTruncate) - (screenWidth - 2 - maxWidth);
        }

        System.out.println("┌" + title + "─".repeat(maxWidth - title.length() - 2) + "┐\n");
        for (String[] row : rows) {
            System.out.print("|");
            for (int i = 0; i < row.length ; i++) {
                System.out.printf("%-" + columnWidths[i] + "s ", truncate(row[i], maxWidth));
            }
            System.out.println("|");
        }
        System.out.println("└" + "─".repeat(maxWidth - 2) + "┘\n");
    }

    public static void printTable(String[] header, ArrayList<String[]> rows, int colToTruncate){
        rows.add(0, header);

        int[] columnWidths = calculateColumnWidths(rows);

        int maxWidth = calculateRowWidth(columnWidths);
        if(screenWidth < maxWidth){
            maxWidth = lengthOfLongestStringToTruncate(rows, colToTruncate) - (screenWidth - maxWidth);
        }

        for (int i = 0; i < rows.get(0).length ; i++) {
            System.out.printf("%-" + columnWidths[i] + "s ", truncate(rows.get(0)[i], maxWidth));
        }

        System.out.println("-".repeat(maxWidth));
        for (String[] row : rows) {
            for (int i = 1; i < row.length ; i++) {
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
    }

}
