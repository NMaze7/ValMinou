package db;

import java.sql.Connection;
import java.sql.Date;
import data.Animal;
import data.Gestion;

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
            System.out.println("Tentative d'insertion d'un animal de test...");
            Animal a = new Animal("TestRex", "Chien", "TEST1234", "Labrador", 2020, Date.valueOf("2024-01-01"), "En attente");
            g.insert(a, "animal");
            System.out.println("Animal inséré avec succès ! ID généré = " + a.getId());
            System.out.println("\nContenu de la table animal :");
            g.displayTable("animal");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connexion.close();
        }
    }
}