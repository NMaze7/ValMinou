package db;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;

import data.Produit;
import data.Gestion;
import db.Connexion;

public class Test {

    public static void main(String[] args) {

        try {
            Connection conn = Connexion.connectR();
            Gestion g = new Gestion(conn);

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("=== Distributeur de produits ===");
            System.out.println("Commandes disponibles :");
            System.out.println(" CREATE   : créer la table produit");
            System.out.println(" INSERT   : insérer un nouveau produit");
            System.out.println(" DISPLAY  : afficher la table produit");
            System.out.println(" REMOVE   : supprimer une ligne (par id)");
            System.out.println(" STRUCT   : afficher la structure de la table");
            System.out.println(" DROP     : supprimer la table");
            System.out.println(" EXIT     : quitter\n");

            String cmd;

            while (true) {

                System.out.print("\n> ");
                cmd = br.readLine().trim().toUpperCase();

                switch (cmd) {

                    case "CREATE":
                        g.createTableProduct();
                        System.out.println("Table produit créée (si inexistante).");
                        break;

                    case "INSERT":
                        System.out.println("Création d’un nouveau produit :");

                        System.out.print("  Lot : ");
                        int lot = Integer.parseInt(br.readLine());

                        System.out.print("  Nom : ");
                        String nom = br.readLine();

                        System.out.print("  Description : ");
                        String description = br.readLine();

                        System.out.print("  Catégorie : ");
                        String categorie = br.readLine();

                        System.out.print("  Prix : ");
                        double prix = Double.parseDouble(br.readLine());

                        Produit p = new Produit(lot, nom, description, categorie, prix);
                        g.insert(p, "produit");

                        System.out.println("Produit inséré : " + p);
                        break;

                    case "DISPLAY":
                        System.out.println("Contenu de la table produit :");
                        g.displayTable("produit");
                        break;

                    case "REMOVE":
                        System.out.print("ID du produit à supprimer : ");
                        int id = Integer.parseInt(br.readLine());
                        g.execute("DELETE FROM produit WHERE id=" + id);
                        System.out.println("Ligne supprimée si elle existait.");
                        break;

                    case "STRUCT":
                        System.out.println("Structure de la table produit :");
                        g.structTable("produit", true);
                        break;

                    case "DROP":
                        g.execute("DROP TABLE IF EXISTS produit");
                        System.out.println("Table supprimée.");
                        break;

                    case "EXIT":
                        Connexion.close();
                        System.out.println("Fin du programme.");
                        return;

                    default:
                        System.out.println("Commande inconnue.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
