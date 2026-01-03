package ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import data.Animal;
import data.Gestion;
import db.Connexion;
import metier.SpaService;
import exception.SpaException;

public class ConsoleApp {

    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        System.out.println("=== Démarrage de l'application SPA Val Minou ===");

        Connection conn = Connexion.connectR();
        if (conn == null) return;

        try {
            Gestion gestion = new Gestion(conn);
            SpaService service = new SpaService(gestion);

            // Chargement des données existantes
            try {
                service.chargerDonneesDepuisBDD();
            } catch (SQLException e) {
                System.err.println(">> Info : Base vide ou erreur de chargement (" + e.getMessage() + ")");
            }

            boolean running = true;
            while (running) {
                afficherMenu();
                String choix = lireTexte(""); // Lecture sécurisée

                try {
                    switch (choix) {
                        case "1":
                            service.afficherTousLesAnimaux();
                            break;
                        case "2":
                            ajouterAnimalInterface(service);
                            break;
                        case "3":
                            modifierAnimalInterface(service);
                            break;
                        case "4":
                            String fSave = lireTexte("Nom du fichier de sauvegarde : ");
                            service.sauvegarderCacheSurDisque(fSave);
                            break;
                        case "5":
                            String fLoad = lireTexte("Nom du fichier à charger : ");
                            service.chargerCacheDepuisDisque(fLoad);
                            break;
                        case "6":
                            System.out.println("Au revoir !");
                            running = false;
                            break;
                        default:
                            System.out.println("Choix inconnu.");
                    }
                } catch (SpaException e) {
                    System.err.println(">>> ERREUR MÉTIER : " + e.getMessage());
                } catch (SQLException e) {
                    System.err.println(">>> ERREUR SQL : " + e.getMessage());
                } catch (Exception e) {
                    System.err.println(">>> ERREUR : " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } finally {
            Connexion.close();
        }
    }

    private static void afficherMenu() {
        System.out.println("\n---------------- MENU PRINCIPAL ----------------");
        System.out.println("1. Afficher les animaux (BDD)");
        System.out.println("2. Ajouter un nouvel animal (Complet)");
        System.out.println("3. Modifier un animal");
        System.out.println("4. SAUVEGARDER");
        System.out.println("5. CHARGER");
        System.out.println("6. Quitter");
        System.out.print("Votre choix > ");
    }

    // --- INTERFACE AJOUT ---
    private static void ajouterAnimalInterface(SpaService service) throws SpaException, SQLException, IOException {
        System.out.println("\n--- Ajout d'un Animal ---");
        System.out.println("(Appuyez sur Entrée pour ignorer un champ facultatif)");

        String nom = lireTexte("Nom : ");
        String type = lireTexte("Type (Chien/Chat) : ");
        String num = lireTexte("Numéro d'identification : ");
        String race = lireTexte("Race : ");

        // Si vide, renvoie 0
        int annee = lireEntier("Année de naissance (YYYY) : ");

        // Si vide, renvoie null
        Date dateArrivee = lireDate("Date d'arrivée (YYYY-MM-DD) : ");

        String statut = lireTexte("Statut (En attente/Adopte...) : ");

        // Création de l'objet de base
        Animal a = new Animal(nom, type, num, race, annee, dateArrivee, statut);

        // Ajout des informations complémentaires (Booléens)
        a.setOkHumains(lireBooleen("OK Humains ? (O/N) : "));
        a.setOkChiens(lireBooleen("OK Chiens ? (O/N) : "));
        a.setOkChats(lireBooleen("OK Chats ? (O/N) : "));
        a.setOkBebes(lireBooleen("OK Bébés ? (O/N) : "));

        // ID Famille (FK)
        Integer idFam = lireEntierObjet("ID Famille d'origine (si connue) : ");
        a.setIdFamilleOrigine(idFam);

        service.ajouterAnimal(a);
    }

    // --- INTERFACE MODIFICATION ---
    private static void modifierAnimalInterface(SpaService service) throws SpaException, SQLException, IOException {
        System.out.println("\n--- Modification d'un Animal ---");

        int id = lireEntier("ID de l'animal à modifier : ");
        Animal a = service.recupererAnimal(id); // Récupère l'existant

        System.out.println("Modification de : " + a.getNom());
        System.out.println("(Laissez vide pour ne pas changer la valeur actuelle)");

        // On propose chaque champ. Si l'utilisateur tape quelque chose, on modifie.

        String saisie = lireTexte("Nom [" + a.getNom() + "] : ");
        if (!saisie.isEmpty()) a.setNom(saisie);

        saisie = lireTexte("Type [" + a.getTypeAnimal() + "] : ");
        if (!saisie.isEmpty()) a.setTypeAnimal(saisie);

        saisie = lireTexte("Race [" + a.getRace() + "] : ");
        if (!saisie.isEmpty()) a.setRace(saisie);

        // Pour les entiers, c'est plus délicat, il faut vérifier si la saisie n'est pas vide
        Integer nouvelleAnnee = lireEntierObjet("Année Naissance [" + a.getAnneeNaissance() + "] : ");
        if (nouvelleAnnee != null) a.setAnneeNaissance(nouvelleAnnee);

        Date nouvelleDate = lireDate("Date Arrivée [" + a.getDateArrivee() + "] : ");
        if (nouvelleDate != null) a.setDateArrivee(nouvelleDate);

        // Modification des booléens
        // Astuce : On affiche la valeur actuelle (true/false/null)
        Boolean okH = lireBooleen("OK Humains [" + a.getOkHumains() + "] (O/N) : ");
        if (okH != null) a.setOkHumains(okH); // Note : ici on ne peut pas remettre à null facilement avec cette logique simple, mais c'est acceptable.

        Boolean okC = lireBooleen("OK Chiens [" + a.getOkChiens() + "] (O/N) : ");
        if (okC != null) a.setOkChiens(okC);

        Boolean okCh = lireBooleen("OK Chats [" + a.getOkChats() + "] (O/N) : ");
        if (okCh != null) a.setOkChats(okCh);

        service.modifierAnimal(a);
    }

    // ==========================================================
    // MÉTHODES UTILITAIRES (Pour ne pas répéter les try/catch)
    // ==========================================================

    /** Lit une chaine de caractères. */
    private static String lireTexte(String prompt) {
        System.out.print(prompt);
        try {
            return br.readLine().trim();
        } catch (IOException e) {
            return "";
        }
    }

    /** Lit un int primitif (renvoie 0 si vide). */
    private static int lireEntier(String prompt) {
        String s = lireTexte(prompt);
        if (s.isEmpty()) return 0;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println(">> Ce n'est pas un nombre valide. Valeur 0 utilisée.");
            return 0;
        }
    }

    /** Lit un Integer objet (renvoie null si vide). */
    private static Integer lireEntierObjet(String prompt) {
        String s = lireTexte(prompt);
        if (s.isEmpty()) return null;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** Lit une Date SQL (renvoie null si vide ou invalide). */
    private static Date lireDate(String prompt) {
        String s = lireTexte(prompt);
        if (s.isEmpty()) return null;
        try {
            return Date.valueOf(s); // Format YYYY-MM-DD
        } catch (IllegalArgumentException e) {
            System.out.println(">> Date invalide. Ignorée.");
            return null;
        }
    }

    /** Lit un Oui/Non et renvoie un Boolean (null si vide). */
    private static Boolean lireBooleen(String prompt) {
        String s = lireTexte(prompt).toLowerCase();
        if (s.isEmpty()) return null; // Inconnu
        if (s.startsWith("o") || s.startsWith("y")) return true; // Oui / Yes
        if (s.startsWith("n")) return false; // Non
        return null; // Autre chose -> Inconnu
    }
}