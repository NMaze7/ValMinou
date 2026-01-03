package ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;

import data.*;
import db.Connexion;
import metier.SpaService;
import exception.SpaException;

public class ConsoleApp {

    // Gestionnaire de lecture (remplace Scanner)
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        System.out.println("=== Démarrage de l'application SPA Val Minou ===");

        // 1. Connexion BDD
        Connection conn = Connexion.connectR();
        if (conn == null) {
            System.err.println("Echec de connexion. Fin.");
            return;
        }

        try {
            // 2. Initialisation des services
            Gestion gestion = new Gestion(conn);
            SpaService service = new SpaService(gestion);

            // 3. Chargement des données au démarrage
            try {
                service.chargerDonneesDepuisBDD();
            } catch (SQLException e) {
                System.err.println(">> Info : Base vide ou erreur de chargement (" + e.getMessage() + ")");
            }

            // 4. Boucle Principale
            boolean running = true;
            while (running) {
                afficherMenu();
                String choix = lireTexte("");

                try {
                    switch (choix) {
                        // --- GESTION ANIMAUX ---
                        case "1": service.afficherTousLesAnimaux(); break;
                        case "2": ajouterAnimalInterface(service); break;
                        case "3": modifierAnimalInterface(service); break;

                        // --- INFRASTRUCTURE (Famille / Box) ---
                        case "4": service.afficherToutesLesFamilles(); break;
                        case "5": ajouterFamilleInterface(service); break;
                        case "6": service.afficherTousLesBoxes(); break;
                        case "7": ajouterBoxInterface(service); break;

                        // --- STAFF & SUIVI ---
                        case "8": service.afficherTousLesBenevoles(); break;
                        case "9": ajouterBenevoleInterface(service); break;
                        case "10":
                            int idAnim = lireEntier("ID de l'animal : ");
                            service.afficherHistoriqueAnimal(idAnim);
                            break;
                        case "11": ajouterHistoriqueInterface(service); break;

                        // --- VETERINAIRE ---
                        case "12":
                            int idSoinAnim = lireEntier("ID de l'animal : ");
                            service.afficherSoinsAnimal(idSoinAnim);
                            break;
                        case "13": ajouterSoinInterface(service); break;

                        // --- PLANNING & ACTIVITES ---
                        case "14": service.afficherTypesActivite(); break;
                        case "15": ajouterTypeActiviteInterface(service); break;
                        case "16": ajouterCreneauInterface(service); break;
                        case "17": planifierActiviteInterface(service); break;
                        case "18": service.afficherPlanningComplet(); break;
                        case "19": ajouterAnimalAuPlanningInterface(service); break;

                        // --- SYSTÈME ---
                        case "90":
                            String fSave = lireTexte("Nom fichier sauvegarde (ex: save.ser) : ");
                            service.sauvegarderCacheSurDisque(fSave);
                            break;
                        case "91":
                            String fLoad = lireTexte("Nom fichier à charger : ");
                            service.chargerCacheDepuisDisque(fLoad);
                            break;
                        case "99":
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
        System.out.println("\n================ MENU PRINCIPAL ================");
        System.out.println("--- GESTION ANIMAUX ---");
        System.out.println(" 1. Afficher Animaux       2. Ajouter Animal");
        System.out.println(" 3. Modifier Animal");
        System.out.println("--- INFRASTRUCTURE ---");
        System.out.println(" 4. Afficher Familles      5. Ajouter Famille");
        System.out.println(" 6. Afficher Boxes         7. Ajouter Box");
        System.out.println("--- STAFF & SUIVI ---");
        System.out.println(" 8. Afficher Bénévoles     9. Ajouter Bénévole");
        System.out.println("10. Historique Animal     11. Déplacer Animal (Mvt)");
        System.out.println("12. Carnet de Santé       13. Ajouter Soin");
        System.out.println("--- PLANNING & ACTIVITÉS ---");
        System.out.println("14. Voir Types Activité   15. Créer Type Activité");
        System.out.println("16. Créer Créneau (Date)  17. Planifier Activité");
        System.out.println("18. Voir Planning         19. Inscrire Animal");
        System.out.println("--- SYSTÈME ---");
        System.out.println("90. SAUVEGARDER           91. CHARGER");
        System.out.println("99. Quitter");
        System.out.print("Votre choix > ");
    }

    // ==========================================================
    // MÉTHODES D'INTERFACE : ANIMAUX
    // ==========================================================

    private static void ajouterAnimalInterface(SpaService service) throws SpaException, SQLException, IOException {
        System.out.println("\n--- Ajout d'un Animal ---");
        String nom = lireTexte("Nom : ");
        String type = lireTexte("Type (Chien/Chat) : ");
        String num = lireTexte("Numéro d'identification : ");
        String race = lireTexte("Race : ");
        int annee = lireEntier("Année de naissance (YYYY) : ");
        Date dateArrivee = lireDate("Date d'arrivée (YYYY-MM-DD) : ");
        String statut = lireTexte("Statut (En attente/Adopte...) : ");

        Animal a = new Animal(nom, type, num, race, annee, dateArrivee, statut);

        // Booléens
        a.setOkHumains(lireBooleen("OK Humains ? (O/N) : "));
        a.setOkChiens(lireBooleen("OK Chiens ? (O/N) : "));
        a.setOkChats(lireBooleen("OK Chats ? (O/N) : "));
        a.setOkBebes(lireBooleen("OK Bébés ? (O/N) : "));

        // Clé étrangère Famille
        Integer idFam = lireEntierObjet("ID Famille d'origine (si connue) : ");
        a.setIdFamilleOrigine(idFam);

        service.ajouterAnimal(a);
    }

    private static void modifierAnimalInterface(SpaService service) throws SpaException, SQLException, IOException {
        System.out.println("\n--- Modification d'un Animal ---");
        int id = lireEntier("ID de l'animal à modifier : ");
        Animal a = service.recupererAnimal(id);

        System.out.println("Modification de : " + a.getNom());
        System.out.println("(Laissez vide pour ne pas changer la valeur actuelle)");

        String saisie = lireTexte("Nom [" + a.getNom() + "] : ");
        if (!saisie.isEmpty()) a.setNom(saisie);

        saisie = lireTexte("Type [" + a.getTypeAnimal() + "] : ");
        if (!saisie.isEmpty()) a.setTypeAnimal(saisie);

        saisie = lireTexte("Statut [" + a.getStatut() + "] : ");
        if (!saisie.isEmpty()) a.setStatut(saisie);

        Boolean okH = lireBooleen("OK Humains [" + a.getOkHumains() + "] : ");
        if (okH != null) a.setOkHumains(okH);

        service.modifierAnimal(a);
    }

    // ==========================================================
    // MÉTHODES D'INTERFACE : INFRASTRUCTURE
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

    private static void ajouterBoxInterface(SpaService service) throws SpaException, SQLException, IOException {
        System.out.println("\n--- Ajout d'un Box ---");
        String ref = lireTexte("Référence (Nom du box) : ");
        String loc = lireTexte("Localisation (Aile A, Extérieur...) : ");
        String type = lireTexte("Type animal (Chien/Chat) : ");
        int cap = lireEntier("Capacité Max : ");

        Box b = new Box(ref, loc, type, cap);
        service.ajouterBox(b);
    }

    // ==========================================================
    // MÉTHODES D'INTERFACE : STAFF & HISTORIQUE & SOINS
    // ==========================================================

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
        Animal a = service.recupererAnimal(idAnimal);
        System.out.println("Déplacement de : " + a.getNom());

        System.out.println("Destination ? 1. Box | 2. Famille");
        String choixDest = lireTexte("Choix : ");

        String typeEmplacement = "";
        Integer idBox = null;
        Integer idFam = null;

        if (choixDest.equals("1")) {
            typeEmplacement = "Box";
            idBox = lireEntier("ID du Box de destination : ");
        } else if (choixDest.equals("2")) {
            typeEmplacement = "Famille";
            idFam = lireEntier("ID de la Famille : ");
        } else {
            System.out.println("Annulation.");
            return;
        }

        Date dateDebut = lireDate("Date de début (YYYY-MM-DD) : ");
        if (dateDebut == null) { System.out.println("Date obligatoire !"); return; }
        String comm = lireTexte("Commentaire : ");

        HistoriqueEmplacement h = new HistoriqueEmplacement(idAnimal, typeEmplacement, idBox, idFam, dateDebut, null, comm);
        service.ajouterHistorique(h);
    }

    private static void ajouterSoinInterface(SpaService service) throws SpaException, SQLException, IOException {
        System.out.println("\n--- Nouvelle Prescription ---");
        int idAnimal = lireEntier("ID de l'animal : ");
        Animal a = service.recupererAnimal(idAnimal);
        System.out.println("Soin pour : " + a.getNom());

        String desc = lireTexte("Description du soin : ");
        String freq = lireTexte("Fréquence : ");
        Date dateFin = lireDate("Date fin (Laisser vide si permanent) : ");

        PrescriptionSoin soin = new PrescriptionSoin(idAnimal, desc, freq, dateFin);
        service.ajouterSoin(soin);
    }

    // ==========================================================
    // MÉTHODES D'INTERFACE : PLANNING (NOUVEAU)
    // ==========================================================

    private static void ajouterTypeActiviteInterface(SpaService service) throws Exception {
        System.out.println("\n--- Nouveau Type d'Activité ---");
        String lib = lireTexte("Libellé (ex: Promenade) : ");
        if (lib.isEmpty()) return;

        TypeActivite t = new TypeActivite(lib);
        service.ajouterTypeActivite(t);
    }

    private static void ajouterCreneauInterface(SpaService service) throws Exception {
        System.out.println("\n--- Nouveau Créneau ---");
        Date date = lireDate("Date (YYYY-MM-DD) : ");
        if (date == null) { System.out.println("Date obligatoire."); return; }

        Time debut = lireHeure("Heure début");
        Time fin = lireHeure("Heure fin");
        if (debut == null || fin == null) return;

        int nbMin = lireEntier("Nb Bénévoles min (defaut 1) : ");
        if (nbMin == 0) nbMin = 1;

        Creneau c = new Creneau(date, debut, fin, nbMin, false);
        service.ajouterCreneau(c);
    }

    private static void planifierActiviteInterface(SpaService service) throws Exception {
        System.out.println("\n--- Organiser une Activité ---");
        int idC = lireEntier("ID du Créneau : ");

        System.out.println("Types disponibles :");
        service.afficherTypesActivite();
        int idT = lireEntier("ID du Type d'Activité : ");
        Integer idB = lireEntierObjet("ID Bénévole (facultatif) : ");

        PlanningActivite p = new PlanningActivite(idC, idB, idT);
        service.planifierActivite(p);
    }

    private static void ajouterAnimalAuPlanningInterface(SpaService service) throws Exception {
        System.out.println("\n--- Inscrire un Animal à une Activité ---");
        service.afficherPlanningComplet();
        System.out.println("--------------------------------");

        int idPlan = lireEntier("ID du Planning : ");
        int idAnim = lireEntier("ID de l'Animal : ");

        service.ajouterAnimalAuPlanning(idPlan, idAnim);
    }

    // ==========================================================
    // UTILITAIRES DE LECTURE (ROBUSTESSE)
    // ==========================================================

    private static String lireTexte(String prompt) {
        System.out.print(prompt);
        try { return br.readLine().trim(); } catch (IOException e) { return ""; }
    }

    private static int lireEntier(String prompt) {
        String s = lireTexte(prompt);
        if (s.isEmpty()) return 0;
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return 0; }
    }

    private static Integer lireEntierObjet(String prompt) {
        String s = lireTexte(prompt);
        if (s.isEmpty()) return null;
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return null; }
    }

    private static double lireDouble(String prompt) {
        String s = lireTexte(prompt);
        if (s.isEmpty()) return 0.0;
        try { return Double.parseDouble(s); } catch (NumberFormatException e) { return 0.0; }
    }

    private static Date lireDate(String prompt) {
        String s = lireTexte(prompt);
        if (s.isEmpty()) return null;
        try { return Date.valueOf(s); } catch (IllegalArgumentException e) { return null; }
    }

    private static Time lireHeure(String prompt) {
        String s = lireTexte(prompt + " (HH:MM) : ");
        if (s.isEmpty()) return null;
        try {
            if (s.length() == 5) s += ":00";
            return Time.valueOf(s);
        } catch (IllegalArgumentException e) {
            System.out.println(">> Format d'heure invalide.");
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