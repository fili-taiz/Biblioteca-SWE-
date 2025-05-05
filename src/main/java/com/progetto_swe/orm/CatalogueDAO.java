package com.progetto_swe.orm;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import com.progetto_swe.domain_model.*;
import com.progetto_swe.orm.database_exception.DataAccessException;
import com.progetto_swe.orm.database_exception.DatabaseConnectionException;

/* probabile che non possa servire */
public class CatalogueDAO {
    private Connection connection;

    public CatalogueDAO(){
        this.connection = ConnectionManager.getConnection();
    }

    public Catalogue getCatalogue(){
        ArrayList<Item> items = new ArrayList<>();
        HashMap<Library, PhysicalCopies> physicalCopies;
        connection = ConnectionManager.getConnection();
        try {
            //tutti i book
            String query = "SELECT * FROM Item I JOIN Book B ON I.code = B.code";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Item book = new Book(resultSet.getInt("code"), resultSet.getString("title"), LocalDate.parse(resultSet.getString("publication_date")), Language.valueOf(resultSet.getString("language")),
                        Category.valueOf(resultSet.getString("category")), resultSet.getString("link"), resultSet.getString("isbn"),
                        resultSet.getString("publishing_house"), resultSet.getInt("number_of_pages"), resultSet.getString("authors"));
                items.add(book);
            }

            //tutti i thesis
            query
                    = "SELECT * FROM Item I JOIN Thesis T ON I.code = T.code";
            ps = connection.prepareStatement(query);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Item thesis = new Thesis(resultSet.getInt("code"), resultSet.getString("title"), LocalDate.parse(resultSet.getString("publication_date")),
                        Language.valueOf(resultSet.getString("language")), Category.valueOf(resultSet.getString("category")), resultSet.getString("link"), resultSet.getInt("number_of_pages"), resultSet.getString("author"),
                        resultSet.getString("supervisors"), resultSet.getString("university"));
                items.add(thesis);
            }


            //tutti i magazine
            query
                    = "SELECT * FROM Item I JOIN Magazine M ON I.code = M.code";
            ps = connection.prepareStatement(query);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Item magazine = new Magazine(resultSet.getInt("code"), resultSet.getString("title"), LocalDate.parse(resultSet.getString("publication_date")), Language.valueOf(resultSet.getString("language")),
                        Category.valueOf(resultSet.getString("category")), resultSet.getString("link"), resultSet.getInt("number_of_pages"),
                        resultSet.getString("publishing_house"));
                items.add(magazine);
            }

            if(items.isEmpty()){
                return null;
            }

            for(Item item : items){
                String query_2 = "SELECT * FROM physical_copies P WHERE P.code = ?;";
                ps = connection.prepareStatement(query_2);
                ps.setInt(1, item.getCode());
                ResultSet copiesSet = ps.executeQuery();
                physicalCopies = new HashMap<>();
                while (copiesSet.next()) {
                    physicalCopies.put(Library.valueOf(copiesSet.getString("storage_place")), new PhysicalCopies(copiesSet.getInt("number_of_copies"), copiesSet.getBoolean("borrowable")));
                }
                item.setPhysicalCopies(physicalCopies);
            }
            return new Catalogue(items);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }
}
