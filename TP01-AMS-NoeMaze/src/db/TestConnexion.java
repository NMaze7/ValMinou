package db;

import java.sql.Connection;
import data.Produit;
import data.Gestion;
import db.Connexion;

public class TestConnexion {
    public static void main(String[] args) {
        Connection conn = Connexion.connectR();
        if (conn != null) {
            System.out.println("Connexion réussie !");
        } else {
            System.out.println("Échec de la connexion.");
            return;
        }

        try {
            Gestion g = new Gestion(conn);
            g.execute("DROP TABLE IF EXISTS produit");
            g.createTableProduct();
            Produit p1 = new Produit(5, "Table", "Meuble bois", "Maison", 49.99);
            Produit p2 = new Produit(3, "Chaise", "Assise bois", "Maison", 19.99);
            Produit p3 = new Produit(5, "Table", "Table en bois", "Maison", 30.0);
            g.insert(p1, "produit");
            g.insert(p2, "produit");
            g.insert(p3, "produit");
            System.out.println("Produits insérés avec succès.");
            System.out.println("\nContenu de la table produit :");
            g.displayTable("produit");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connexion.close();
        }
    }
}

