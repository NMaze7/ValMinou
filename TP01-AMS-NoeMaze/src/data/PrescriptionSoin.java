package data;

import java.sql.Date;


/**
 * Cette classe correspond à la table prescription_soin de la base de données
 * Elle permet au personnel vétérinaire ou aux bénévoles qualifiés de suivre
 * les traitements en cours (médicaments, pansements, régimes spéciaux)
 */
public class PrescriptionSoin extends Entity {
    private int id_animal;
    private String description_soin;
    private String frequence;       // ex: "Matin et Soir", "1 fois par jour"
    private Date date_fin_traitement; // Peut être null (traitement à vie ou ponctuel)



    /**
     * Enregistre un nouveau soin ou traitement pour un animal
     * @param id_animal           L'identifiant de l'animal recevant le soin
     * @param description_soin    La description du produit ou de l'acte (ex: "Antibiotique X")
     * @param frequence           La posologie ou fréquence (ex: "2x / jour"). Peut être null
     * @param date_fin_traitement La date d'arrêt du traitement. Si null, le traitement est considéré comme permanent ou à durée indéterminée
     */
    public PrescriptionSoin(int id_animal, String description_soin, String frequence, Date date_fin_traitement) {
        super();
        this.id_animal = id_animal;
        this.description_soin = description_soin;
        this.frequence = frequence;
        this.date_fin_traitement = date_fin_traitement;
    }


    public int getIdAnimal() { return id_animal; }
    public void setIdAnimal(int id) { this.id_animal = id; }

    public String getDescriptionSoin() { return description_soin; }
    public void setDescriptionSoin(String d) { this.description_soin = d; }

    public String getFrequence() { return frequence; }
    public void setFrequence(String f) { this.frequence = f; }

    public Date getDateFinTraitement() { return date_fin_traitement; }
    public void setDateFinTraitement(Date d) { this.date_fin_traitement = d; }

    @Override
    public String toString() {
        return "Soin [Animal=" + id_animal + ", " + description_soin + "]";
    }

    /**
     * Gère les champs facultatifs frequence et date_fin_traitement
     * S'ils sont null en Java, ils sont convertis en "null" pour la base de données
     */
    @Override
    public void getStruct() {
        map.put("id_animal", fieldType.INT4);
        map.put("description_soin", fieldType.VARCHAR);
        map.put("frequence", fieldType.VARCHAR);
        map.put("date_fin_traitement", fieldType.DATE);
        String freqVal = (frequence == null || frequence.isEmpty()) ? "null" : "'" + frequence + "'";
        String dateVal = (date_fin_traitement == null) ? "null" : "'" + date_fin_traitement + "'";

        this.values = "(" + id_animal + ", '" + description_soin + "', " + freqVal + ", " + dateVal + ")";
    }


    /**
     * Retourne le nom de la colonne clé primaire
     * @return "id_soin"
     */
    @Override
    public String getPkName() {
        return "id_soin";
    }


    /**
     * Permet de modifier la description, la fréquence ou de prolonger/arrêter
     * un traitement (date de fin)
     * @return La clause SQL de modification
     */
    @Override
    public String getUpdateClause() {
        String freqVal = (frequence == null || frequence.isEmpty()) ? "null" : "'" + frequence + "'";
        String dateVal = (date_fin_traitement == null) ? "null" : "'" + date_fin_traitement + "'";

        return "id_animal=" + id_animal + ", " +
                "description_soin='" + description_soin + "', " +
                "frequence=" + freqVal + ", " +
                "date_fin_traitement=" + dateVal;
    }
}