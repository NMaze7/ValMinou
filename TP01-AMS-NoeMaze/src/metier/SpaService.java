package metier;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import data.Animal;
import data.Gestion;
import exception.DonneeInvalideExceptions;
import exception.DonneeInvalideExceptions;
import exception.ElementIntrouvableException;
import exception.SpaException;

public class SpaService {

    private Gestion gestion;
    // Exigence : "Utilisation d'au moins une collection"
    private List<Animal> cacheAnimaux;

    public SpaService(Gestion gestion) {
        this.gestion = gestion;
        this.cacheAnimaux = new ArrayList<>();
    }

    /**
     * NOUVEAU : Charge les animaux de la BDD vers le cache mémoire au démarrage.
     * Indispensable pour pouvoir modifier des animaux existants après un redémarrage.
     */
    public void chargerDonneesDepuisBDD() throws SQLException {
        cacheAnimaux.clear(); // Vide le cache pour éviter les doublons

        // Utilise la méthode select() que tu as ajoutée dans Gestion.java
        ResultSet rs = gestion.select("SELECT * FROM animal");

        while (rs.next()) {
            // Reconstruction de l'objet Animal depuis le SQL
            Animal a = new Animal(
                    rs.getString("nom"),
                    rs.getString("type_animal"),
                    rs.getString("num_identification"),
                    rs.getString("race"),
                    rs.getInt("annee_naissance"),
                    rs.getDate("date_arrivee"),
                    rs.getString("statut")
            );

            // On remet l'ID technique (SERIAL) qui vient de la base
            a.setId(rs.getInt("id_animal"));

            // Gestion des champs booléens (nullable)
            boolean okH = rs.getBoolean("ok_humains");
            if (!rs.wasNull()) a.setOkHumains(okH);

            boolean okChiens = rs.getBoolean("ok_chiens");
            if (!rs.wasNull()) a.setOkChiens(okChiens);

            boolean okChats = rs.getBoolean("ok_chats");
            if (!rs.wasNull()) a.setOkChats(okChats);

            boolean okBebes = rs.getBoolean("ok_bebes");
            if (!rs.wasNull()) a.setOkBebes(okBebes);

            // Gestion de la FK Famille
            int idFam = rs.getInt("id_famille_origine");
            if (!rs.wasNull()) a.setIdFamilleOrigine(idFam);

            // Ajout au cache mémoire
            cacheAnimaux.add(a);
        }
        rs.close();
        System.out.println("[Info] " + cacheAnimaux.size() + " animaux chargés depuis la base de données.");
    }

    /**
     * Ajoute un animal en base de données et dans le cache local.
     */
    public void ajouterAnimal(Animal animal) throws SpaException, SQLException {
        // Validation métier simple
        if (animal.getNom() == null || animal.getNom().trim().isEmpty()) {
            throw new DonneeInvalideExceptions("Le nom de l'animal ne peut pas être vide.");
        }
        if (animal.getDateArrivee() == null) {
            throw new DonneeInvalideExceptions("La date d'arrivée est obligatoire.");
        }

        // Insertion SQL
        gestion.insert(animal, "animal");

        // Mise à jour cache
        cacheAnimaux.add(animal);
        System.out.println("[Succès] Animal ajouté en BDD avec l'ID " + animal.getId());
    }

    /**
     * Trouve un animal dans la mémoire vive par son ID.
     * Utilisé avant une modification.
     */
    public Animal recupererAnimal(int id) throws SpaException {
        for (Animal a : cacheAnimaux) {
            if (a.getId() == id) {
                return a;
            }
        }
        throw new ElementIntrouvableException("Animal", id);
    }

    /**
     * Applique les modifications d'un animal en base de données.
     */
    public void modifierAnimal(Animal animal) throws SQLException {
        // Envoie l'instruction UPDATE SQL
        gestion.update(animal, "animal");
        System.out.println("[Succès] Animal " + animal.getId() + " mis à jour.");
        // Pas besoin de toucher au cacheAnimaux car l'objet 'animal' modifié est déjà dedans (référence).
    }

    /**
     * Affiche le contenu brut de la table.
     */
    public void afficherTousLesAnimaux() throws SQLException {
        System.out.println("--- Liste des Animaux (BDD) ---");
        gestion.displayTable("animal");
    }

    // --- SÉRIALISATION (Sauvegarde Fichier) ---

    public void sauvegarderCacheSurDisque(String nomFichier) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomFichier))) {
            oos.writeObject(cacheAnimaux);
            System.out.println("Données sauvegardées dans " + nomFichier);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void chargerCacheDepuisDisque(String nomFichier) {
        File f = new File(nomFichier);
        if (!f.exists()) {
            System.out.println("Aucun fichier de sauvegarde trouvé.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            this.cacheAnimaux = (List<Animal>) ois.readObject();
            System.out.println("Données chargées : " + cacheAnimaux.size() + " animaux récupérés.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement : " + e.getMessage());
        }
    }
}