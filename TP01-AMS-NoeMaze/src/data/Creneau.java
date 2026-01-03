package data;

import java.sql.Date;
import java.sql.Time;

public class Creneau extends Entity {
    private Date date_creneau;
    private Time heure_debut;
    private Time heure_fin;
    private int nb_benevoles_min;
    private boolean est_semaine_type;

    public Creneau(Date date_creneau, Time heure_debut, Time heure_fin, int nb_benevoles_min, boolean est_semaine_type) {
        super();
        this.date_creneau = date_creneau;
        this.heure_debut = heure_debut;
        this.heure_fin = heure_fin;
        this.nb_benevoles_min = nb_benevoles_min;
        this.est_semaine_type = est_semaine_type;
    }


    public Date getDateCreneau() { return date_creneau; }
    public Time getHeureDebut() { return heure_debut; }
    public Time getHeureFin() { return heure_fin; }

    @Override
    public String toString() {
        return "Creneau [" + id + "] Le " + date_creneau + " de " + heure_debut + " à " + heure_fin;
    }

    @Override
    public void getStruct() {
        map.put("date_creneau", fieldType.DATE);
        map.put("heure_debut", fieldType.TIME);
        map.put("heure_fin", fieldType.TIME);
        map.put("nb_benevoles_min", fieldType.INT4);
        map.put("est_semaine_type", fieldType.BOOL);
        this.values = "('" + date_creneau + "', '" + heure_debut + "', '" + heure_fin + "', " +
                nb_benevoles_min + ", " + est_semaine_type + ")";
    }


    @Override public String getPkName() { return "id_creneau"; }
    @Override public String getUpdateClause() {
        return "date_creneau='" + date_creneau + "', heure_debut='" + heure_debut + "', " +
                "heure_fin='" + heure_fin + "', nb_benevoles_min=" + nb_benevoles_min;
    }
}