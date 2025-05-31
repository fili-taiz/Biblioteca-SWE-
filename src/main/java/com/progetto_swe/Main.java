package com.progetto_swe;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import com.progetto_swe.CLI.CommandLineInterface;


public class Main {
    public static void main(String[] args) {
        CommandLineInterface cli = new CommandLineInterface();
        cli.start();
    }

    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }  
}