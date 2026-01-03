package metier;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import data.Animal;
import data.Box;
import data.Famille;
import data.Gestion;
import exception.DonneeInvalideExceptions;
import exception.DonneeInvalideExceptions;
import exception.ElementIntrouvableException;
import exception.SpaException;

public class SpaService {

    private Gestion gestion;

    // Nos 3 collections en mémoire (Cache)
    private List<Animal> cacheAnimaux;
    private List<Famille> cacheFamilles;
    private List<Box> cacheBoxes;

    public SpaService(Gestion gestion) {
        this.gestion = gestion;
        this.cacheAnimaux = new ArrayList<>();
        this.cacheFamilles = new ArrayList<>();
        this.cacheBoxes = new ArrayList<>();
    }

    /**
     * Synchronise TOUTES les tables (Animal, Famille, Box) avec la BDD.
     */
    public void chargerDonneesDepuisBDD() throws SQLException {
        chargerAnimaux();
        chargerFamilles();
        chargerBoxes();
    }

    // --- CHARGEMENT SQL (Privé pour alléger le code principal) ---

    private void chargerAnimaux() throws SQLException {
        cacheAnimaux.clear();
        ResultSet rs = gestion.select("SELECT * FROM animal");
        while (rs.next()) {
            Animal a = new Animal(
                    rs.getString("nom"),
                    rs.getString("type_animal"),
                    rs.getString("num_identification"),
                    rs.getString("race"),
                    rs.getInt("annee_naissance"),
                    rs.getDate("date_arrivee"),
                    rs.getString("statut")
            );
            a.setId(rs.getInt("id_animal"));

            // Booléens
            if (!rs.wasNull()) { // Gestion simplifiée des nulls
                boolean val = rs.getBoolean("ok_humains"); if(!rs.wasNull()) a.setOkHumains(val);
                val = rs.getBoolean("ok_chiens"); if(!rs.wasNull()) a.setOkChiens(val);
                val = rs.getBoolean("ok_chats"); if(!rs.wasNull()) a.setOkChats(val);
                val = rs.getBoolean("ok_bebes"); if(!rs.wasNull()) a.setOkBebes(val);
            }

            int idFam = rs.getInt("id_famille_origine");
            if (!rs.wasNull()) a.setIdFamilleOrigine(idFam);

            cacheAnimaux.add(a);
        }
        rs.close();
        System.out.println("[Info] " + cacheAnimaux.size() + " animaux chargés.");
    }

    private void chargerFamilles() throws SQLException {
        cacheFamilles.clear();
        ResultSet rs = gestion.select("SELECT * FROM famille");
        while (rs.next()) {
            Famille f = new Famille(
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("adresse"),
                    rs.getString("telephone"),
                    rs.getString("type_famille")
            );
            f.setId(rs.getInt("id_famille"));
            cacheFamilles.add(f);
        }
        rs.close();
        System.out.println("[Info] " + cacheFamilles.size() + " familles chargées.");
    }

    private void chargerBoxes() throws SQLException {
        cacheBoxes.clear();
        ResultSet rs = gestion.select("SELECT * FROM box");
        while (rs.next()) {
            Box b = new Box(
                    rs.getString("nom_reference"),
                    rs.getString("localisation"),
                    rs.getString("type_animal"),
                    rs.getInt("capacite_max")
            );
            b.setId(rs.getInt("id_box"));
            cacheBoxes.add(b);
        }
        rs.close();
        System.out.println("[Info] " + cacheBoxes.size() + " boxes chargés.");
    }

    // ==========================================================
    // MÉTIER : ANIMAL
    // ==========================================================

    public void ajouterAnimal(Animal animal) throws SpaException, SQLException {
        if (animal.getNom() == null || animal.getNom().trim().isEmpty()) {
            throw new DonneeInvalideExceptions("Le nom est obligatoire.");
        }
        gestion.insert(animal, "animal");
        cacheAnimaux.add(animal);
        System.out.println("[Succès] Animal ajouté (ID: " + animal.getId() + ")");
    }

    public Animal recupererAnimal(int id) throws SpaException {
        for (Animal a : cacheAnimaux) {
            if (a.getId() == id) return a;
        }
        throw new ElementIntrouvableException("Animal", id);
    }

    public void modifierAnimal(Animal animal) throws SQLException {
        gestion.update(animal, "animal");
        System.out.println("[Succès] Animal mis à jour.");
    }

    public void afficherTousLesAnimaux() throws SQLException {
        System.out.println("--- Animaux en base ---");
        gestion.displayTable("animal");
    }

    // ==========================================================
    // MÉTIER : FAMILLE
    // ==========================================================

    public void ajouterFamille(Famille famille) throws SpaException, SQLException {
        // Validation basique
        if (famille.getNom() == null || famille.getNom().isEmpty()) {
            throw new DonneeInvalideExceptions("Le nom est obligatoire.");
        }
        gestion.insert(famille, "famille");
        cacheFamilles.add(famille);
        System.out.println("[Succès] Famille ajoutée (ID: " + famille.getId() + ")");
    }

    public Famille recupererFamille(int id) throws SpaException {
        for (Famille f : cacheFamilles) {
            if (f.getId() == id) return f;
        }
        throw new ElementIntrouvableException("Famille", id);
    }

    public void afficherToutesLesFamilles() throws SQLException {
        System.out.println("--- Familles en base ---");
        gestion.displayTable("famille");
    }

    // ==========================================================
    // MÉTIER : BOX
    // ==========================================================

    public void ajouterBox(Box box) throws SpaException, SQLException {
        if (box.getCapaciteMax() < 0) {
            throw new DonneeInvalideExceptions("La capacité ne peut pas être négative.");
        }
        gestion.insert(box, "box");
        cacheBoxes.add(box);
        System.out.println("[Succès] Box ajouté (ID: " + box.getId() + ")");
    }

    public void afficherTousLesBoxes() throws SQLException {
        System.out.println("--- Boxes en base ---");
        gestion.displayTable("box");
    }

    // ==========================================================
    // SAUVEGARDE ET CHARGEMENT (Sérialisation)
    // ==========================================================

    public void sauvegarderCacheSurDisque(String nomFichier) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomFichier))) {
            // On écrit les listes dans l'ordre
            oos.writeObject(cacheAnimaux);
            oos.writeObject(cacheFamilles);
            oos.writeObject(cacheBoxes);
            System.out.println("Sauvegarde effectuée dans " + nomFichier);
        } catch (IOException e) {
            System.err.println("Erreur sauvegarde : " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void chargerCacheDepuisDisque(String nomFichier) {
        File f = new File(nomFichier);
        if (!f.exists()) {
            System.out.println("Fichier introuvable.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            // On lit dans le même ordre
            this.cacheAnimaux = (List<Animal>) ois.readObject();
            this.cacheFamilles = (List<Famille>) ois.readObject();
            this.cacheBoxes = (List<Box>) ois.readObject();
            System.out.println("Données rechargées depuis le fichier !");
            System.out.println("- Animaux : " + cacheAnimaux.size());
            System.out.println("- Familles : " + cacheFamilles.size());
            System.out.println("- Boxes : " + cacheBoxes.size());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur chargement : " + e.getMessage());
        }
    }
}