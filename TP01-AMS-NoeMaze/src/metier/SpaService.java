package metier;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import data.*;
import exception.DonneeInvalideExceptions;
import exception.DonneeInvalideExceptions;
import exception.ElementIntrouvableException;
import exception.SpaException;

public class SpaService {

    private Gestion gestion;

    // Listes en mémoire (Cache)
    private List<Animal> cacheAnimaux;
    private List<Famille> cacheFamilles;
    private List<Box> cacheBoxes;
    private List<Benevole> cacheBenevoles;            // Nouveau
    private List<HistoriqueEmplacement> cacheHistorique; // Nouveau

    public SpaService(Gestion gestion) {
        this.gestion = gestion;
        this.cacheAnimaux = new ArrayList<>();
        this.cacheFamilles = new ArrayList<>();
        this.cacheBoxes = new ArrayList<>();
        this.cacheBenevoles = new ArrayList<>();
        this.cacheHistorique = new ArrayList<>();
    }

    /**
     * Synchronise TOUTES les tables avec la BDD au démarrage.
     */
    public void chargerDonneesDepuisBDD() throws SQLException {
        chargerAnimaux();
        chargerFamilles();
        chargerBoxes();
        chargerBenevoles();
        chargerHistorique();
    }

    // ==========================================================
    // MÉTHODES PRIVÉES DE CHARGEMENT
    // ==========================================================

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

            if (!rs.wasNull()) {
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

    private void chargerBenevoles() throws SQLException {
        cacheBenevoles.clear();
        ResultSet rs = gestion.select("SELECT * FROM benevole");
        while (rs.next()) {
            Benevole b = new Benevole(
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("telephone"),
                    rs.getString("adresse"),
                    rs.getString("role")
            );
            b.setId(rs.getInt("id_benevole"));
            cacheBenevoles.add(b);
        }
        rs.close();
        System.out.println("[Info] " + cacheBenevoles.size() + " bénévoles chargés.");
    }

    private void chargerHistorique() throws SQLException {
        cacheHistorique.clear();
        ResultSet rs = gestion.select("SELECT * FROM historique_emplacement");
        while (rs.next()) {
            // Lecture sécurisée des clés étrangères (Int vs Null)
            int boxIdTemp = rs.getInt("id_box");
            Integer boxId = rs.wasNull() ? null : boxIdTemp;

            int famIdTemp = rs.getInt("id_famille");
            Integer famId = rs.wasNull() ? null : famIdTemp;

            HistoriqueEmplacement h = new HistoriqueEmplacement(
                    rs.getInt("id_animal"),
                    rs.getString("type_emplacement"),
                    boxId,
                    famId,
                    rs.getDate("date_debut"),
                    rs.getDate("date_fin"),
                    rs.getString("commentaire")
            );
            h.setId(rs.getInt("id_historique"));
            cacheHistorique.add(h);
        }
        rs.close();
        System.out.println("[Info] " + cacheHistorique.size() + " historiques chargés.");
    }

    // ==========================================================
    // MÉTIER : ANIMAL
    // ==========================================================

    public void ajouterAnimal(Animal animal) throws SpaException, SQLException {
        if (animal.getNom() == null || animal.getNom().isEmpty()) throw new DonneeInvalideExceptions("Nom obligatoire");
        gestion.insert(animal, "animal");
        cacheAnimaux.add(animal);
        System.out.println("[Succès] Animal ajouté (ID: " + animal.getId() + ")");
    }

    public Animal recupererAnimal(int id) throws SpaException {
        for (Animal a : cacheAnimaux) if (a.getId() == id) return a;
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
        if (famille.getNom() == null || famille.getNom().isEmpty()) throw new DonneeInvalideExceptions("Nom obligatoire");
        gestion.insert(famille, "famille");
        cacheFamilles.add(famille);
        System.out.println("[Succès] Famille ajoutée (ID: " + famille.getId() + ")");
    }

    public void afficherToutesLesFamilles() throws SQLException {
        System.out.println("--- Familles en base ---");
        gestion.displayTable("famille");
    }

    // ==========================================================
    // MÉTIER : BOX
    // ==========================================================

    public void ajouterBox(Box box) throws SpaException, SQLException {
        gestion.insert(box, "box");
        cacheBoxes.add(box);
        System.out.println("[Succès] Box ajouté (ID: " + box.getId() + ")");
    }

    public void afficherTousLesBoxes() throws SQLException {
        System.out.println("--- Boxes en base ---");
        gestion.displayTable("box");
    }

    // ==========================================================
    // MÉTIER : BENEVOLE
    // ==========================================================

    public void ajouterBenevole(Benevole benevole) throws SpaException, SQLException {
        if (benevole.getNom() == null || benevole.getNom().isEmpty()) throw new DonneeInvalideExceptions("Nom obligatoire");
        gestion.insert(benevole, "benevole");
        cacheBenevoles.add(benevole);
        System.out.println("[Succès] Bénévole ajouté (ID: " + benevole.getId() + ")");
    }

    public void afficherTousLesBenevoles() throws SQLException {
        System.out.println("--- Bénévoles en base ---");
        gestion.displayTable("benevole");
    }

    // ==========================================================
    // MÉTIER : HISTORIQUE (Avec Logique Temporelle)
    // ==========================================================

    public void ajouterHistorique(HistoriqueEmplacement h) throws SpaException, SQLException {
        // 1. Vérifications
        recupererAnimal(h.getIdAnimal());
        if (h.getDateDebut() == null) throw new DonneeInvalideExceptions("Date de début obligatoire.");
        if (h.getIdBox() == null && h.getIdFamille() == null) throw new DonneeInvalideExceptions("Lieu obligatoire.");

        // 2. LOGIQUE PROF : Clôturer l'ancien emplacement avant d'ouvrir le nouveau
        cloturerDeplacementPrecedent(h.getIdAnimal(), h.getDateDebut());

        // 3. Insertion et Mise à jour Cache
        gestion.insert(h, "historique_emplacement");
        cacheHistorique.add(h);

        System.out.println("[Succès] Mouvement enregistré (Précédent clôturé).");
    }

    /**
     * Cherche le mouvement "en cours" (date_fin = null) pour cet animal
     * et le termine à la date du nouveau mouvement.
     */
    private void cloturerDeplacementPrecedent(int idAnimal, Date dateCloture) throws SQLException {
        for (HistoriqueEmplacement h : cacheHistorique) {
            // Si c'est le même animal ET que l'emplacement n'est pas fini
            if (h.getIdAnimal() == idAnimal && h.getDateFin() == null) {

                // Mise à jour Objet Java
                h.setDateFin(dateCloture);

                // Mise à jour SQL (Update manuel ciblé)
                String sql = "UPDATE historique_emplacement SET date_fin = '" + dateCloture + "' " +
                        "WHERE id_historique = " + h.getId();
                gestion.execute(sql);

                System.out.println(">> Ancien emplacement clôturé automatiquement.");
                return; // On a trouvé, on s'arrête
            }
        }
    }

    public void afficherHistoriqueAnimal(int idAnimal) {
        System.out.println("--- Historique pour l'Animal ID " + idAnimal + " ---");
        boolean trouve = false;
        for (HistoriqueEmplacement h : cacheHistorique) {
            if (h.getIdAnimal() == idAnimal) {
                String fin = (h.getDateFin() == null) ? "En cours" : h.getDateFin().toString();
                String lieu = (h.getIdBox() != null) ? "Box " + h.getIdBox() : "Famille " + h.getIdFamille();
                System.out.println(h.getDateDebut() + " -> " + fin + " : " + lieu + " (" + h.getTypeEmplacement() + ")");
                trouve = true;
            }
        }
        if (!trouve) System.out.println("Aucun mouvement trouvé.");
    }

    // ==========================================================
    // SAUVEGARDE ET CHARGEMENT (Sérialisation)
    // ==========================================================

    public void sauvegarderCacheSurDisque(String nomFichier) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomFichier))) {
            oos.writeObject(cacheAnimaux);
            oos.writeObject(cacheFamilles);
            oos.writeObject(cacheBoxes);
            oos.writeObject(cacheBenevoles);
            oos.writeObject(cacheHistorique);
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
            this.cacheAnimaux = (List<Animal>) ois.readObject();
            this.cacheFamilles = (List<Famille>) ois.readObject();
            this.cacheBoxes = (List<Box>) ois.readObject();
            this.cacheBenevoles = (List<Benevole>) ois.readObject();
            this.cacheHistorique = (List<HistoriqueEmplacement>) ois.readObject();

            System.out.println("Données rechargées !");
            System.out.println("- Animaux : " + cacheAnimaux.size());
            System.out.println("- Familles : " + cacheFamilles.size());
            System.out.println("- Boxes : " + cacheBoxes.size());
            System.out.println("- Bénévoles : " + cacheBenevoles.size());
            System.out.println("- Historiques : " + cacheHistorique.size());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur chargement : " + e.getMessage());
        }
    }
}