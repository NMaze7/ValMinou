package data;
import java.sql.Time;



/**
 * Représente un modèle d'horaire type pour une semaine standard
 * Cette classe correspond à la table modele_semaine_type de la base de données
 * Elle sert de template pour définir les activités récurrentes (ex: "Tous les Lundis
 * de 8h à 10h, c'est le nettoyage")
 * Cela permet de générer automatiquement des  Creneau réels sans avoir à les saisir
 * manuellement chaque semaine
 */
public class ModeleSemaineType extends Entity {
    private String jour_semaine;
    private Time heure_debut;
    private Time heure_fin;
    private int nb_benevoles_min_defaut;
    private Integer id_type_activite_defaut;




    /**
     * Instancie un nouveau modèle de créneau type
     * @param jour   Le jour de la semaine concerné (ex: "Lundi", "Mardi")
     * @param debut  L'heure de début par défaut
     * @param fin    L'heure de fin par défaut
     * @param nb     Le nombre de bénévoles requis par défaut
     * @param idType L'identifiant du type d'activité par défaut (peut être null)
     */
    public ModeleSemaineType(String jour, Time debut, Time fin, int nb, Integer idType) {
        this.jour_semaine = jour;
        this.heure_debut = debut;
        this.heure_fin = fin;
        this.nb_benevoles_min_defaut = nb;
        this.id_type_activite_defaut = idType;
    }




    /**
     * Prépare les types SQL (VARCHAR, TIME, INT4) et formate la chaîne values
     * Gère spécifiquement le cas où id_type_activite_defaut est null en le
     * convertissant en la chaîne "null" pour la requête SQL
     */
    @Override
    public void getStruct() {
        map.put("jour_semaine", fieldType.VARCHAR);
        map.put("heure_debut", fieldType.TIME);
        map.put("heure_fin", fieldType.TIME);
        map.put("nb_benevoles_min_defaut", fieldType.INT4);
        map.put("id_type_activite_defaut", fieldType.INT4);

        String typeVal = (id_type_activite_defaut == null) ? "null" : String.valueOf(id_type_activite_defaut);
        this.values = "('" + jour_semaine + "', '" + heure_debut + "', '" + heure_fin + "', " + nb_benevoles_min_defaut + ", " + typeVal + ")";
    }

    @Override public String getPkName() { return "id_modele"; }
    @Override public String getUpdateClause() { return "jour_semaine='" + jour_semaine + "'"; } // Simplifié
}