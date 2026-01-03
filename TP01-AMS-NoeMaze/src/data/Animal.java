package data;

import java.sql.Date;


/**
 * Représente un animal pris en charge par la SPA.
 * Cette classe correspond à la table animal de la base de données
 * Elle contient les informations d'identité, de statut (adopté/en attente)
 * ainsi que les critères de compatibilité (sociabilité)
 */
public class Animal extends Entity {
    private String nom;
    private String type_animal; // 'Chien' ou 'Chat'
    private String num_identification;
    private String race;
    private int annee_naissance;
    private Date date_arrivee;
    private String statut; // 'En attente', 'Adopte', etc....
    private Boolean ok_humains;
    private Boolean ok_chiens;
    private Boolean ok_chats;
    private Boolean ok_bebes;
    private Integer id_famille_origine;




    /**
     * Constructeur principal pour l'enregistrement d'un nouvel animal
     * Note : Les champs de sociabilité (ok_chiens, ok_chats...) et l'ID de famille
     * sont initialisés à null par défaut. Ils doivent être définis via les setters.
     *
     * @param nom                Le nom de l'animal.
     * @param type_animal        Le type (ex: "Chien", "Chat").
     * @param num_identification Numéro de puce ou tatouage.
     * @param race               La race de l'animal.
     * @param annee_naissance    L'année de naissance estimée ou réelle.
     * @param date_arrivee       La date d'entrée au refuge.
     * @param statut             Le statut initial (ex: "En attente").
     */
    public Animal(String nom, String type_animal, String num_identification, String race, int annee_naissance, Date date_arrivee, String statut) {
        super();
        this.nom = nom;
        this.type_animal = type_animal;
        this.num_identification = num_identification;
        this.race = race;
        this.annee_naissance = annee_naissance;
        this.date_arrivee = date_arrivee;
        this.statut = statut;
        this.ok_humains = null;
        this.ok_chiens = null;
        this.ok_chats = null;
        this.ok_bebes = null;
        this.id_famille_origine = null;
    }



    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getTypeAnimal() { return type_animal; }
    public void setTypeAnimal(String type) { this.type_animal = type; }

    public String getNumIdentification() { return num_identification; }
    public void setNumIdentification(String num) { this.num_identification = num; }

    public String getRace() { return race; }
    public void setRace(String race) { this.race = race; }

    public int getAnneeNaissance() { return annee_naissance; }
    public void setAnneeNaissance(int annee) { this.annee_naissance = annee; }

    public Date getDateArrivee() { return date_arrivee; }
    public void setDateArrivee(Date date) { this.date_arrivee = date; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public Boolean getOkHumains() { return ok_humains; }
    public void setOkHumains(Boolean ok) { this.ok_humains = ok; }

    public Boolean getOkChiens() { return ok_chiens; }
    public void setOkChiens(Boolean ok) { this.ok_chiens = ok; }

    public Boolean getOkChats() { return ok_chats; }
    public void setOkChats(Boolean ok) { this.ok_chats = ok; }

    public Boolean getOkBebes() { return ok_bebes; }
    public void setOkBebes(Boolean ok) { this.ok_bebes = ok; }

    public Integer getIdFamilleOrigine() { return id_famille_origine; }
    public void setIdFamilleOrigine(Integer idFamille) { this.id_famille_origine = idFamille; }




    @Override
    public String toString() {
        return "Animal [ID=" + id + ", Nom=" + nom + " (" + type_animal + "), Race=" + race + ", Statut=" + statut + "]";
    }




    /**
     * Définit la structure de mappage de l'animal pour l'insertion SQL
     * Cette méthode remplit la map avec les types SQL attendus pour chaque colonne
     * et génère la chaîne values formatée pour une requête INSERT
     */
    @Override
    public void getStruct() {
        map.put("nom", fieldType.VARCHAR);
        map.put("type_animal", fieldType.VARCHAR);
        map.put("num_identification", fieldType.VARCHAR);
        map.put("race", fieldType.VARCHAR);
        map.put("annee_naissance", fieldType.INT4);
        map.put("date_arrivee", fieldType.DATE);
        map.put("statut", fieldType.VARCHAR);
        map.put("ok_humains", fieldType.BOOL);
        map.put("ok_chiens", fieldType.BOOL);
        map.put("ok_chats", fieldType.BOOL);
        map.put("ok_bebes", fieldType.BOOL);
        map.put("id_famille_origine", fieldType.INT4);


        this.values = "('" + nom + "', '" + type_animal + "', '" + num_identification + "', '" +
                race + "', " + annee_naissance + ", '" + date_arrivee + "', '" + statut + "', " +
                ok_humains + ", " + ok_chiens + ", " + ok_chats + ", " + ok_bebes + ", " +
                id_famille_origine + ")";
    }


    /**
     * Retourne le nom de la colonne servant de clé primaire dans la table SQL
     * @return "id_animal"
     */
    @Override
    public String getPkName() {
        return "id_animal";
    }



    /**
     * Construit une chaîne de caractères assignant chaque colonne à sa valeur actuelle
     * dans l'objet Java (ex: "nom='oslo', type_animal='Chien'...")
     * @return La clause de mise à jour SQL complète.
     */
    @Override
    public String getUpdateClause() {
        return "nom='" + nom + "', " +
                "type_animal='" + type_animal + "', " +
                "num_identification='" + num_identification + "', " +
                "race='" + race + "', " +
                "annee_naissance=" + annee_naissance + ", " +
                "date_arrivee='" + date_arrivee + "', " +
                "statut='" + statut + "', " +
                "ok_humains=" + ok_humains + ", " +
                "ok_chiens=" + ok_chiens + ", " +
                "ok_chats=" + ok_chats + ", " +
                "ok_bebes=" + ok_bebes + ", " +
                "id_famille_origine=" + id_famille_origine;
    }



}