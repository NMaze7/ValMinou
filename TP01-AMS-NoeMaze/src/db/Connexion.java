package db;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Properties;

public class Connexion {

    private static Connection connection = null;

    public static Connection connectR() {
        try {
            String dataBase = "etd";
            String url = "jdbc:postgresql://pedago.univ-avignon.fr:5432/" + dataBase;
            Properties props = new Properties();
            props.setProperty("user", "uapv2501373");
            props.setProperty("password", "Yq5v6D");
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, props);
            System.out.println("connexion reussi ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void executeUpdate(String query) {
        if (connection == null) {
            System.out.println("Erreur : aucune connexion à la base.");
            return;
        }
        try {
            Statement stmt = connection.createStatement();
            System.out.println("Exécution de la requête : " + query);
            stmt.executeUpdate(query);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connexion fermée.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
