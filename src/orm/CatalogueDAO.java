package orm;

import domain_model.*;
import java.sql.*;

/* probabile che non possa servire */
public class CatalogueDAO {
    private Connection connection;

    public CatalogueDAO(){
        this.connection = ConnectionManager.getConnection();
    }

    public Catalogue getCatalogue(){
        try {
            String query = "query";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();

        /**/

        
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return new Catalogue();
    }

    public Hirer updatCatalogue(Catalogue Catalogue){
        try {
            String query = "query";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();

        /**/

        
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        }
        return new Hirer();
    }
}
