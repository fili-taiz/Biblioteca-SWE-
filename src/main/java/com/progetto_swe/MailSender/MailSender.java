package com.progetto_swe.MailSender;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.Properties;
import java.util.Scanner;

public class MailSender {
    private static final MailSender mailSender = new MailSender();
    private static Session session;
    private static String myAccountEmail = "biblioteca.SWE@gmail.com";
    private static String password = "upun dxcl xqfi rwae";
    private static boolean sendMail = false;


    private MailSender() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587");

        this.session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });
        session.setDebug(true);
    }

    private static String getHTMLmodel() {
        Scanner in = null;
        try {
            in = new Scanner(new FileReader("src/main/resources/Mail/ModelloHTML.html"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String content = "";
        while (in.hasNextLine()) {
            content = content.concat(in.nextLine());
        }
        return content;
    }

    private static String createhtml(String subject, String userCode, String content) {
        String html = getHTMLmodel();
        html = html.replace("$[TITOLO]", subject);
        html = html.replace("$[UTENTE]", userCode);
        html = html.replace("$[CONTENUTO]", content);
        return html;
    }

    public static void sendReservationSuccessMail(String recepient, String userCode, int itemCode, String title, String storagePlace, LocalDate expireDate) {
        String subject = "PRENOTAZIONE EFFETTUATA CON SUCCESSO";
        String content = "la sua prenotazione dell'articolo \"" + title + "\" con codice <strong>" + itemCode + "</strong>" +
                        " presso " + storagePlace + "  è stata effettuata con successo. <br>" +
                        "È pregata di venire nella sede \"" + storagePlace + "\" a ritirare l'articolo entro la data " + expireDate + " " +
                        "<br> per concludere l'operazione di noleggio.";
        String html = createhtml(subject, userCode, content);
        sendMail(recepient, subject, html);
    }

    //mandata da postgre
    public static void sendReservationExpired(String recepient, String userCode, int itemCode, String title, String storagePlace) {
        String subject = "CANCELLAZIONE PRENOTAZIONE";
        String content = "la sua prenotazione dell'articolo \"" + title + "\" con codice <strong>" + itemCode + "</strong>" +
                        " presso " + storagePlace + "  è stata cancellata perché non è venuta a ritirare " +
                        "<br>l'articolo entro la scadenza comunicata.";
        String html = createhtml(subject, userCode, content);
        sendMail(recepient, subject, html);
    }

    public static void sendLendingSuccessMail(String recepient, String userCode, int itemCode, String title, String storagePlace, LocalDate expireDate) {
        String subject = "NOLEGGIO EFFETTUATO CON SUCCESSO";
        String content = "il suo noleggio dell'articolo \"" + title + "\" con codice <strong>" + itemCode + "</strong>" +
                        " presso " + storagePlace + "  è stata effettuato con successo. <br>" +
                        "È pregata di restituire l'articolo alla sede \"" + storagePlace + "\" entro la data " + expireDate + ".";
        String html = createhtml(subject, userCode, content);
        sendMail(recepient, subject, html);
    }


    public static void sendWithdrawSuccessMail(String recepient, String userCode, int itemCode, String title) {
        String subject = "RESTITUZIONE ARTICOLO CON SUCCESSO";
        String content = "ha restituito con successo l'articolo \"" + title + "\" con codice <strong>" + itemCode + "</strong>.";
        String html = createhtml(subject, userCode, content);
        sendMail(recepient, subject, html);
    }

    //mandato da postgre
    public static void sendUpdateLendingDateMail(String recepient, String userCode, int itemCode, String title, String storagePlace, LocalDate expireDate) {
        String subject = "RINNOVATO NOLEGGIO";
        String content = "il suo noleggio dell'articolo \"" + title + "\" con codice <strong>" + itemCode + "</strong>" +
                        " presso " + storagePlace + "  è stato rinnovato perché non ha restituito il libro" +
                        "<br> entro la scadenza comunicata. <br>" +
                        "È pregata di restituire l'articolo alla sede \"" + storagePlace + "\" entro la data " + expireDate + ".";
        String html = createhtml(subject, userCode, content);
        sendMail(recepient, subject, html);
    }

    //mandato da postgre
    public static void sendHirerBannedMail(String recepient, String userCode, int itemCode, String title, LocalDate unbannedDate) {
        String subject = "UTENTE BANNATO";
        String content = "è stata bannata fino a data " + unbannedDate + " perché non ha restituito 'articolo \"" + title + "\" " +
                "<br>con codice <strong>" + itemCode + "</strong>" + " dopo aver il noleggio 3 volte.";
        String html = createhtml(subject, userCode, content);
        sendMail(recepient, subject, html);
    }

    //mandato da postgre
    public static void sendHirerUnbannedMail(String recepient, String userCode) {
        String subject = "UTENTE UNBANNATO";
        String content = "è stata unbannata, la pregriamo di restituire l'articolo entro le scadenze.";
        String html = createhtml(subject, userCode, content);
        sendMail(recepient, subject, html);
    }

    public static void sendAddInWaitingListSuccessMail(String recepient, String userCode, int itemCode, String title, String storagePlace) {
        String subject = "AGGIUNTO IL LISTA D'ATTESA";
        String content = "è stata aggiunta nella lista d'attesa per l'articolo \"" + title + "\" con codice <strong>" + itemCode +
                "</strong> <br> presso " + storagePlace + "." +
                "<br> Le verrà mandata una notifica appena l'articolo sarà disponibile.";
        String html = createhtml(subject, userCode, content);
        sendMail(recepient, subject, html);
    }

    //mandato da postgre
    public static void sendNotifyWaitingListMail(String recepient, String userCode, int itemCode, String title, String storagePlace) {
        String subject = "ARTICOLO DISPONIBILE";
        String content = "l'articolo \"" + title + "\" con codice <strong>" + itemCode + "</strong>" +
                        " presso " + storagePlace + " è disponibile.<br> " +
                        "Le è stata mandata questa mail perché si era aggiunta alla lista d'attesa per questo articolo" +
                        "<br> alla sede \"" + storagePlace + "\".";
        String html = createhtml(subject, userCode, content);
        sendMail(recepient, subject, html);
    }

    private static void sendMail(String recipient, String subject, String content) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setContent(content, "text/html");
            if (sendMail) {
                Transport.send(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //da cancellare
    public static void mandaMail() {
    }
}
