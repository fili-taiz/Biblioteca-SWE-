package com.progetto_swe.orm;

import com.progetto_swe.domain_model.Category;
import com.progetto_swe.domain_model.Language;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ItemDAO {
    private Connection connection;

    public ItemDAO(){
        this.connection = ConnectionManager.getConnection();
    }

    public int addItem(String title, String publicationDate, String language, String category, String link, String borrowable) {
        connection = ConnectionManager.getConnection();
        try {
            String query = "INSERT INTO Item (title, publication_date, language, category, link, borrowable)"
                    + "VALUES ('" + title + "', '" + publicationDate + "', '" + language + "', '" + category + "', '"
                    + link + "', '" + borrowable + "')" +
                    "RETURNING code;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                int code = resultSet.getInt("code");
            };
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return -1;
    }
}
