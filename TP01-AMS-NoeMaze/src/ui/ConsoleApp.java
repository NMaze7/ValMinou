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

    // On utilise BufferedReader au lieu de Scanner pour une lecture plus stable
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        System.out.println("=== Démarrage de l'application SPA Val Minou ===");

        // 1. Connexion à la base
        Connection conn = Connexion.connectR();
        if (conn == null) {
            System.err.println("Impossible de se connecter à la base de données. Fin du programme.");
            return;
        }

        try {
            // 2. Initialisation des services
            Gestion gestion = new Gestion(conn);
            SpaService service = new SpaService(gestion);

            // 3. Chargement initial des données depuis la BDD (Correction de l'erreur ici)
            try {
                service.chargerDonneesDepuisBDD();
            } catch (SQLException e) {
                System.err.println(">> Attention : Impossible de charger les animaux existants (" + e.getMessage() + ")");
            }

            // 4. Boucle principale
            boolean running = true;
            while (running) {
                afficherMenu();

                String choix = "";
                try {
                    choix = br.readLine();
                } catch (IOException e) {
                    System.err.println("Erreur de lecture : " + e.getMessage());
                    continue;
                }

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
                            System.out.print("Nom du fichier de sauvegarde (ex: backup.ser) : ");
                            String fSave = br.readLine();
                            service.sauvegarderCacheSurDisque(fSave);
                            break;
                        case "5":
                            System.out.print("Nom du fichier à charger : ");
                            String fLoad = br.readLine();
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
                } catch (IOException e) {
                    System.err.println(">>> ERREUR E/S : " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.err.println(">>> ERREUR FORMAT : Veuillez entrer un nombre valide.");
                } catch (Exception e) {
                    System.err.println(">>> ERREUR INATTENDUE : " + e.getMessage());
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
        System.out.println("2. Ajouter un nouvel animal");
        System.out.println("3. Modifier un animal (Update)");
        System.out.println("4. SAUVEGARDER la session (Sérialisation)");
        System.out.println("5. CHARGER une session");
        System.out.println("6. Quitter");
        System.out.print("Votre choix > ");
    }

    private static void ajouterAnimalInterface(SpaService service) throws SpaException, SQLException, IOException {
        System.out.println("\n--- Ajout d'un Animal ---");

        System.out.print("Nom : ");
        String nom = br.readLine();

        System.out.print("Type (Chien/Chat) : ");
        String type = br.readLine();

        System.out.print("Numéro d'identification (Puce/Tatouage) : ");
        String num = br.readLine();

        System.out.print("Race : ");
        String race = br.readLine();

        System.out.print("Année de naissance (YYYY) : ");
        int annee = Integer.parseInt(br.readLine());

        System.out.print("Date d'arrivée (format YYYY-MM-DD) : ");
        String dateStr = br.readLine();
        Date dateArrivee;
        try {
            dateArrivee = Date.valueOf(dateStr);
        } catch (IllegalArgumentException e) {
            throw new SpaException("Format de date invalide. Utilisez YYYY-MM-DD.") {};
        }

        System.out.print("Statut (En attente/Adopte/Non adoptable) : ");
        String statut = br.readLine();

        // Création et ajout
        Animal nouvelAnimal = new Animal(nom, type, num, race, annee, dateArrivee, statut);
        service.ajouterAnimal(nouvelAnimal);
    }

    private static void modifierAnimalInterface(SpaService service) throws SpaException, SQLException, IOException {
        System.out.println("\n--- Modification d'un Animal ---");

        System.out.print("Entrez l'ID de l'animal à modifier : ");
        String idStr = br.readLine();
        if (idStr.isEmpty()) return;

        int id = Integer.parseInt(idStr);

        // On récupère l'animal (si pas trouvé, une Exception sera levée par le service)
        Animal animal = service.recupererAnimal(id);

        System.out.println("Modification de : " + animal.getNom() + " (" + animal.getTypeAnimal() + ")");
        System.out.println("(Laissez vide et appuyez sur Entrée pour ne pas changer la valeur)");

        // --- Début des modifications ---

        System.out.print("Nom [" + animal.getNom() + "] : ");
        String saisie = br.readLine();
        if (!saisie.isEmpty()) animal.setNom(saisie);

        System.out.print("Type [" + animal.getTypeAnimal() + "] : ");
        saisie = br.readLine();
        if (!saisie.isEmpty()) animal.setTypeAnimal(saisie);

        System.out.print("Race [" + animal.getRace() + "] : ");
        saisie = br.readLine();
        if (!saisie.isEmpty()) animal.setRace(saisie);

        System.out.print("Statut [" + animal.getStatut() + "] : ");
        saisie = br.readLine();
        if (!saisie.isEmpty()) animal.setStatut(saisie);

        System.out.print("Année Naissance [" + animal.getAnneeNaissance() + "] : ");
        saisie = br.readLine();
        if (!saisie.isEmpty()) {
            animal.setAnneeNaissance(Integer.parseInt(saisie));
        }

        // --- Application de la mise à jour ---
        service.modifierAnimal(animal);
    }
}