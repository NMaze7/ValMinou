package data;

import java.sql.Date;
import java.sql.Time;



/**
 * Représente une plage horaire (créneau) définie dans le planning de la SPA
 * Cette classe correspond à la table creneau de la base de données
 * Elle permet de définir une date précise et des heures de début et de fin,
 * servant de support temporel pour planifier des activités (promenades, soins, nettoyage)
 * et y affecter des bénévoles
 */
public class Creneau extends Entity {
    private Date date_creneau;
    private Time heure_debut;
    private Time heure_fin;
    private int nb_benevoles_min;
    private boolean est_semaine_type;



    /**
     * Instancie un nouveau créneau horaire
     * @param date_creneau     La date spécifique du créneau (java.sql.Date)
     * @param heure_debut      L'heure de début de l'activité
     * @param heure_fin        L'heure de fin de l'activite
     * @param nb_benevoles_min Le nombre minimum de bénévoles requis pour ce créneau (par défaut 1)
     * @param est_semaine_type Indique si ce créneau fait partie d'un modèle récurrent (true) ou s'il est ponctuel (false)
     */
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



    /**
     * Configure les types (DATE, TIME, BOOL) et formate la chaîne values
     */
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

    /**
     * Retourne le nom de la colonne clé primaire
     * @return "id_creneau"
     */
    @Override public String getPkName() { return "id_creneau"; }


    /**
     * Permet de modifier la date, les horaires et le nombre de bénévoles requis
     * @return La clause SQL de modification
     */
    @Override public String getUpdateClause() {
        return "date_creneau='" + date_creneau + "', heure_debut='" + heure_debut + "', " +
                "heure_fin='" + heure_fin + "', nb_benevoles_min=" + nb_benevoles_min;
    }
}