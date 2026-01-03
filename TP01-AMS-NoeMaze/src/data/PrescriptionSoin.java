package data;

import java.sql.Date;

public class PrescriptionSoin extends Entity {
    private int id_animal;
    private String description_soin;
    private String frequence;       // ex: "Matin et Soir", "1 fois par jour"
    private Date date_fin_traitement; // Peut être null (traitement à vie ou ponctuel)

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

    // --- Mapping SQL ---

    @Override
    public void getStruct() {
        map.put("id_animal", fieldType.INT4);
        map.put("description_soin", fieldType.VARCHAR); // TEXT = VARCHAR
        map.put("frequence", fieldType.VARCHAR);
        map.put("date_fin_traitement", fieldType.DATE);

        // Gestion des NULLs pour la requête SQL
        String freqVal = (frequence == null || frequence.isEmpty()) ? "null" : "'" + frequence + "'";
        String dateVal = (date_fin_traitement == null) ? "null" : "'" + date_fin_traitement + "'";

        this.values = "(" + id_animal + ", '" + description_soin + "', " + freqVal + ", " + dateVal + ")";
    }

    @Override
    public String getPkName() {
        return "id_soin";
    }

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