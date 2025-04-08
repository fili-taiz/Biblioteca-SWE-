package com.progetto_swe;


import java.time.LocalDate;

import com.progetto_swe.MailSender.MailSender;
import com.progetto_swe.domain_model.Category;
import com.progetto_swe.domain_model.Item;
import com.progetto_swe.domain_model.Language;
import com.progetto_swe.domain_model.Magazine;
import com.progetto_swe.orm.ConnectionManager;
import com.progetto_swe.orm.HirerDAO;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;


public class Main {
   /* public static void main(String[] args) {
        clearScreen();
        //ConnectionManager.query("Select * From Hirer");
        System.out.println(Math.round((Math.random()*1000000)));
/*ArrayList<String> a = new ArrayList<>();
a.add("C");
a.add("c");
a.add("a");
a.add("A");

        Item i1 = new Item(1, "null", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "null", false);
        Item i2 = new Item(1, "null", LocalDate.now(), Language.LANGUAGE_1, Category.CATEGORY_1, "null", false);

        Integer i = 1;
        System.out.println(i-8);
        HirerDAO dao = new HirerDAO();
        //System.out.println(dao.getHirer("1")==null);

        String input = "as";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        MailSender mailSender = new MailSender();
        mailSender.mandaMail();

        md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }  */
}