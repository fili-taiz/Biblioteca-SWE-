package com.progetto_swe.CLI;

import com.progetto_swe.business_logic.*;
import com.progetto_swe.business_logic.business_logic_exception.ActionDeniedException;
import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.database_exception.IdAlreadyExistsException;
import com.progetto_swe.orm.database_exception.IdNotFoundException;

import java.util.ArrayList;

public class HirerCLI extends BaseCLI {
    protected Hirer hirer;
    protected ArrayList<String> addableStoragePlaces = new ArrayList<>();
    protected ArrayList<String> reservableStoragePlaces = new ArrayList<>();

    public HirerCLI(Hirer hirer) {
        this.hirer = hirer;
    }

    @Override
    protected ArrayList<String[]> getMenu() {
        switch (page) {
            case "RESERVATION": {
                ReservationController reservationController = new ReservationController();
                reservations = reservationController.getReservations(hirer.getUserCode());
                reservationMenu();
                stampaPrenotazioni(reservations);
                break;
            }

            case "LENDING": {
                LendingController lendingController = new LendingController();
                lendings = lendingController.getLendings(hirer.getUserCode());
                lendingMenu();
                stampaPrestiti(lendings);
                break;
            }

            default: {
                super.getMenu();
                break;
            }
        }
        return menuOption;
    }

    @Override
    protected void homePageMenu() {
        super.homePageMenu();
        removeOption("Login");
        menuOption.add(new String[]{"Resoconto prenotazioni", "visualizzare tutti i prestiti effettuati"});
        menuOption.add(new String[]{"Resoconto prestiti", "visualizzare tutte le prenotazioni effettuate"});
        menuOption.add( new String[]{"Logout", "uscire dall'account"});
    }

    protected void reservationMenu() {
        menuOption = new ArrayList<>();
        if (!reservations.isEmpty()) {
            menuOption.add(new String[]{"Cancella", "cancellare una prenotazione"});
        }
        menuOption.add(new String[]{"Indietro", "tornare alla pagina precedente"});
    }

    protected void lendingMenu() {
        menuOption = new ArrayList<>();
        menuOption.add(new String[]{"Indietro", "tornare alla pagina precedente"});
    }

    @Override
    protected void itemMenu() {
        super.itemMenu();
        if (item != null) {
            ArrayList<String[]> data = item.getPhysicalCopiesData();
            for (String[] row : data) {
                if (row[2].equals("Prenotabile")) {
                    reservableStoragePlaces.add(row[0]);
                }
                if (row[2].equals("Esaurito")) {
                    addableStoragePlaces.add(row[0]);
                }
            }
            if (!reservableStoragePlaces.isEmpty()) {
                menuOption.add(0, new String[]{"Prenota", "prenotare un articolo"});
            }
            if (!addableStoragePlaces.isEmpty()) {
                menuOption.add(0, new String[]{"Aggiungimi", "ricevere una notifica una volta l'articolo sia disponibile"});
            }
        }
    }

    @Override
    protected void execute(String scelta) {
        super.execute(scelta);
        switch (page) {
            case "LENDING": {
                executeLendingOption(scelta);
                break;
            }

            case "RESERVATION": {
                executeReservationOption(scelta);
                break;
            }
        }
    }

    @Override
    protected void executeHomePageOption(String scelta) {
        super.executeHomePageOption(scelta);
        switch (scelta) {
            case "RESOCONTO PRENOTAZIONI": {
                page = "RESERVATION";
                CommandLineInterface.clearScreen();
                break;
            }

            case "RESOCONTO PRESTITI": {
                page = "LENDING";
                CommandLineInterface.clearScreen();
                break;
            }
        }
    }

    @Override
    protected void executeItemPageOption(String scelta) {
        super.executeItemPageOption(scelta);
        switch (scelta) {
            case "PRENOTA": {
                reserveItem();
                break;
            }

            case "AGGIUNGIMI": {
                addWaitingList();
                break;
            }
        }
    }

    protected void executeReservationOption(String scelta) {
        switch (scelta) {
            case "CANCELLA": {
                CommandLineInterface.printMessage("Inserisci l'ItemCode dell'articolo della prenotazione che vuoi cancellare:");
                String itemCode = scanner.nextLine();
                Reservation reservation = getReservationByItemCode(itemCode, reservations);
                ReservationController reservationController = new ReservationController();
                reservationController.removeReservation(hirer, reservation.getItem(), reservation.getStoragePlace().toString(), hirer.getToken());
                CommandLineInterface.clearScreen();
                break;
            }

            case "INDIETRO": {
                page = "HOMEPAGE";
                CommandLineInterface.clearScreen();
                reservations = new ArrayList<>();
                break;
            }
        }
    }

    protected void executeLendingOption(String scelta) {
        switch (scelta) {
            case "INDIETRO": {
                page = "HOMEPAGE";
                CommandLineInterface.clearScreen();
                reservations = new ArrayList<>();
                break;
            }
        }
    }

    protected void reserveItem() {
        CommandLineInterface.printMessage("Inserisci la sede in cui vuoi prenotare l'articolo: ");
        String storagePlace = scanner.nextLine().toUpperCase();
        do {
            try {
                Library.valueOf(storagePlace);
                break;
            } catch (IllegalArgumentException e) {
                CommandLineInterface.printError("Errore: non hai inserito una sede valida, riprova per favore:");
                storagePlace = scanner.nextLine().toUpperCase();
            }
        } while (true);
        ReservationController reservationController = new ReservationController();
        try {
            reservationController.reserveItem(hirer, item, storagePlace, hirer.getToken());
            CommandLineInterface.clearScreen();
        } catch (ActionDeniedException | IdAlreadyExistsException e) {
            CommandLineInterface.clearScreen();
            CommandLineInterface.printError(e.getMessage());
        }
    }

    protected void addWaitingList() {
        CommandLineInterface.printMessage("Inserisci la sede in cui vuoi ottenere notifica una volta disponibile: ");
        HirerController hirerController = new HirerController();
        String storagePlace = scanner.nextLine().toUpperCase();
        do {
            try {
                Library.valueOf(storagePlace);
                break;
            } catch (IllegalArgumentException e) {
                CommandLineInterface.printError("Errore: non hai inserito una sede valida, riprova per favore:");
                storagePlace = scanner.nextLine().toUpperCase();
            }
        } while (true);
        try {
            hirerController.addToWaitingList(item, hirer.getEmail(), storagePlace);
        } catch (ActionDeniedException | IdAlreadyExistsException e) {
            CommandLineInterface.clearScreen();
            CommandLineInterface.printError(e.getMessage());
        }
    }
}
