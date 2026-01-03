package ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import data.*;
import db.Connexion;
import metier.SpaService;
import exception.SpaException;

public class ConsoleApp {

    // Lecture sécurisée sans Scanner
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        System.out.println("=== Démarrage de l'application SPA Val Minou ===");

        Connection conn = Connexion.connectR();
        if (conn == null) return;

        try {
            Gestion gestion = new Gestion(conn);
            SpaService service = new SpaService(gestion);

            // Chargement initial des données (Animaux + Familles + Box)
            try {
                service.chargerDonneesDepuisBDD();
            } catch (SQLException e) {
                System.err.println(">> Info : Base vide ou erreur de chargement (" + e.getMessage() + ")");
            }

            boolean running = true;
            while (running) {
                afficherMenu();
                String choix = lireTexte("");

                try {
                    switch (choix) {
                        // --- ANIMAUX ---
                        case "1": service.afficherTousLesAnimaux(); break;
                        case "2": ajouterAnimalInterface(service); break;
                        case "3": modifierAnimalInterface(service); break;

                        // --- FAMILLES ---
                        case "4": service.afficherToutesLesFamilles(); break;
                        case "5": ajouterFamilleInterface(service); break;

                        // --- BOXES ---
                        case "6": service.afficherTousLesBoxes(); break;
                        case "7": ajouterBoxInterface(service); break;

                        //benevoles
                        case "8": service.afficherTousLesBenevoles(); break;
                        case "9": ajouterBenevoleInterface(service); break;

                        // --- MOUVEMENTS ---
                        case "10":
                            int idAnim = lireEntier("ID de l'animal pour voir son historique : ");
                            service.afficherHistoriqueAnimal(idAnim);
                            break;
                        case "11": ajouterHistoriqueInterface(service); break;

                        // --- SYSTÈME ---
                        case "12":
                            String fSave = lireTexte("Nom du fichier de sauvegarde : ");
                            service.sauvegarderCacheSurDisque(fSave);
                            break;
                        case "13":
                            String fLoad = lireTexte("Nom du fichier à charger : ");
                            service.chargerCacheDepuisDisque(fLoad);
                            break;
                        case "14":
                        case "q":
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
        System.out.println("--- ANIMAUX ---");
        System.out.println("1. Afficher les animaux");
        System.out.println("2. Ajouter un animal");
        System.out.println("3. Modifier un animal");
        System.out.println("--- FAMILLES ---");
        System.out.println("4. Afficher les familles");
        System.out.println("5. Ajouter une famille");
        System.out.println("--- BOXES ---");
        System.out.println("6. Afficher les boxes");
        System.out.println("7. Ajouter un box");
        System.out.println("--- BENEVOLES ---");
        System.out.println("8. Afficher les bénévoles");
        System.out.println("9. Ajouter un bénévole");
        System.out.println("--- Historiques ---");
        System.out.println("10. historique animal");
        System.out.println("11. ajouter une historique animal");
        System.out.println("--- SYSTEME ---");
        System.out.println("12. SAUVEGARDER session");
        System.out.println("13. CHARGER session");
        System.out.println("14. Quitter");
        System.out.print("Votre choix > ");
    }

    // ==========================================================
    // MÉTHODES D'INTERFACE (Animaux)
    // ==========================================================

    private static void ajouterAnimalInterface(SpaService service) throws SpaException, SQLException, IOException {
        System.out.println("\n--- Ajout d'un Animal ---");

        String nom = lireTexte("Nom : ");
        String type = lireTexte("Type (Chien/Chat) : ");
        String num = lireTexte("Numéro d'identification : ");
        String race = lireTexte("Race : ");
        int annee = lireEntier("Année de naissance (YYYY) : ");
        Date dateArrivee = lireDate("Date d'arrivée (YYYY-MM-DD) : ");
        String statut = lireTexte("Statut (En attente/Adopte) : ");

        Animal a = new Animal(nom, type, num, race, annee, dateArrivee, statut);

        // Options
        a.setOkHumains(lireBooleen("OK Humains ? (O/N) : "));
        a.setOkChiens(lireBooleen("OK Chiens ? (O/N) : "));
        a.setOkChats(lireBooleen("OK Chats ? (O/N) : "));
        a.setOkBebes(lireBooleen("OK Bébés ? (O/N) : "));

        // Clé étrangère
        Integer idFam = lireEntierObjet("ID Famille d'origine (si connue) : ");
        a.setIdFamilleOrigine(idFam);

        service.ajouterAnimal(a);
    }

    private static void modifierAnimalInterface(SpaService service) throws SpaException, SQLException, IOException {
        System.out.println("\n--- Modification d'un Animal ---");
        int id = lireEntier("ID de l'animal à modifier : ");
        Animal a = service.recupererAnimal(id);

        System.out.println("Modification de : " + a.getNom());
        System.out.println("(Entrée pour ignorer)");

        String saisie = lireTexte("Nom [" + a.getNom() + "] : ");
        if (!saisie.isEmpty()) a.setNom(saisie);

        saisie = lireTexte("Type [" + a.getTypeAnimal() + "] : ");
        if (!saisie.isEmpty()) a.setTypeAnimal(saisie);

        saisie = lireTexte("Statut [" + a.getStatut() + "] : ");
        if (!saisie.isEmpty()) a.setStatut(saisie);

        // Booléens
        Boolean okH = lireBooleen("OK Humains [" + a.getOkHumains() + "] : ");
        if (okH != null) a.setOkHumains(okH);

        service.modifierAnimal(a);
    }

    // ==========================================================
    // MÉTHODES D'INTERFACE (Famille)
    // ==========================================================

    private static void ajouterFamilleInterface(SpaService service) throws SpaException, SQLException, IOException {
        System.out.println("\n--- Ajout d'une Famille ---");

        String nom = lireTexte("Nom : ");
        String prenom = lireTexte("Prénom : ");
        String adresse = lireTexte("Adresse : ");
        String tel = lireTexte("Téléphone : ");
        String type = lireTexte("Type (Accueil/Adoptante...) : ");

        Famille f = new Famille(nom, prenom, adresse, tel, type);
        service.ajouterFamille(f);
    }

    // ==========================================================
    // MÉTHODES D'INTERFACE (Box)
    // ==========================================================

    private static void ajouterBoxInterface(SpaService service) throws SpaException, SQLException, IOException {
        System.out.println("\n--- Ajout d'un Box ---");

        String ref = lireTexte("Référence (Nom du box) : ");
        String loc = lireTexte("Localisation (Aile A, Extérieur...) : ");
        String type = lireTexte("Type animal (Chien/Chat) : ");
        int cap = lireEntier("Capacité Max : ");

        Box b = new Box(ref, loc, type, cap);
        service.ajouterBox(b);
    }


    private static void ajouterBenevoleInterface(SpaService service) throws SpaException, SQLException, IOException {
        System.out.println("\n--- Ajout d'un Bénévole ---");

        String nom = lireTexte("Nom : ");
        String prenom = lireTexte("Prénom : ");
        String tel = lireTexte("Téléphone : ");
        String adresse = lireTexte("Adresse : ");
        String role = lireTexte("Rôle (laisser vide pour 'Benevole') : ");

        Benevole b = new Benevole(nom, prenom, tel, adresse, role);
        service.ajouterBenevole(b);
    }



    private static void ajouterHistoriqueInterface(SpaService service) throws SpaException, SQLException, IOException {
        System.out.println("\n--- Nouveau Mouvement (Déplacement) ---");

        int idAnimal = lireEntier("ID de l'animal à déplacer : ");
        // Vérif rapide si l'animal existe
        Animal a = service.recupererAnimal(idAnimal);
        System.out.println("Déplacement de : " + a.getNom());

        System.out.println("Destination ?");
        System.out.println("1. Box");
        System.out.println("2. Famille d'accueil");
        String choixDest = lireTexte("Choix (1 ou 2) : ");

        String typeEmplacement = "";
        Integer idBox = null;
        Integer idFam = null;

        if (choixDest.equals("1")) {
            typeEmplacement = "Box";
            idBox = lireEntier("ID du Box de destination : ");
            // On pourrait vérifier si le box existe ici via service.recupererBox(idBox)
        } else if (choixDest.equals("2")) {
            typeEmplacement = "Famille";
            idFam = lireEntier("ID de la Famille : ");
        } else {
            System.out.println("Choix invalide. Annulation.");
            return;
        }

        Date dateDebut = lireDate("Date de début (YYYY-MM-DD) : ");
        if (dateDebut == null) {
            System.out.println("Date obligatoire !");
            return;
        }

        String comm = lireTexte("Commentaire (facultatif) : ");

        // Par défaut date_fin est null (mouvement en cours)
        HistoriqueEmplacement h = new HistoriqueEmplacement(
                idAnimal, typeEmplacement, idBox, idFam, dateDebut, null, comm
        );

        service.ajouterHistorique(h);
    }

    // ==========================================================
    // UTILITAIRES DE SAISIE ROBUSTE
    // ==========================================================

    private static String lireTexte(String prompt) {
        System.out.print(prompt);
        try {
            return br.readLine().trim();
        } catch (IOException e) {
            return "";
        }
    }

    private static int lireEntier(String prompt) {
        String s = lireTexte(prompt);
        if (s.isEmpty()) return 0;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static double lireDouble(String prompt) {
        String s = lireTexte(prompt);
        if (s.isEmpty()) return 0.0;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private static Integer lireEntierObjet(String prompt) {
        String s = lireTexte(prompt);
        if (s.isEmpty()) return null;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Date lireDate(String prompt) {
        String s = lireTexte(prompt);
        if (s.isEmpty()) return null;
        try {
            return Date.valueOf(s);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static Boolean lireBooleen(String prompt) {
        String s = lireTexte(prompt).toLowerCase();
        if (s.isEmpty()) return null;
        if (s.startsWith("o") || s.startsWith("y") || s.equals("true")) return true;
        if (s.startsWith("n") || s.equals("false")) return false;
        return null;
    }
}