package com.progetto_swe;


import java.util.Properties;
import java.util.Scanner;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * Demo app that shows how to construct and send a single part html
 * message.  Note that the same basic technique can be used to send
 * data of any type.
 */

public class CommandLineInterface {
    int screenWidth = 90;

    public void start() {
        clearScreen();
        do {
            String scelta;
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object
            System.out.println("Enter username");

            String userName = myObj.nextLine();  // Read user input
            System.out.println("Username is: " + userName);  // Output user input
            switch (scelta){
                case "Cerca":
                    break;

                case "Login":
                    break;
            }

        } while (true);

    }


    public void printBiblioteca() {
        String biblioteca = "Benvenuto nella Biblioteca";
        for (int i = 0; i <= (this.screenWidth - biblioteca.length()) / 2; i++) {
            System.out.print("─");
        }
        System.out.println(biblioteca);
        for (int i = 0; i < (this.screenWidth - biblioteca.length()) / 2; i++) {
            System.out.print("─");
        }
        System.out.println("\n\n\n");
    }

    public void title(String title) {
        System.out.println(title);
        for (int i = 0; i < this.screenWidth - title.length(); i++) {
            System.out.print("─");
        }
    }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        printBiblioteca();
    }

}
