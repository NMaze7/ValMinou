package data;




/**
 * Cette classe correspond à la table planning_activite de la base de données
 * Elle constitue le cœur du module de planning en liant trois entités :
 * Un Creneau (Quand ?)
 * Un TypeActivite (Quoi ?)
 * Un Benevole (Qui ? - Optionnel)
 */
public class PlanningActivite extends Entity {
    private int id_creneau;
    private Integer id_benevole; // Nullable
    private int id_type_activite;




    /**
     * Instancie une nouvelle ligne de planning
     * @param id_creneau       L'identifiant du créneau horaire concerné
     * @param id_benevole      L'identifiant du bénévole responsable (peut être null si non encore assigné)
     * @param id_type_activite L'identifiant du type d'activité prévue (ex: Promenade)
     */
    public PlanningActivite(int id_creneau, Integer id_benevole, int id_type_activite) {
        super();
        this.id_creneau = id_creneau;
        this.id_benevole = id_benevole;
        this.id_type_activite = id_type_activite;
    }

    public int getIdCreneau() { return id_creneau; }
    public Integer getIdBenevole() { return id_benevole; }
    public void setIdBenevole(Integer id) { this.id_benevole = id; }
    public int getIdTypeActivite() { return id_type_activite; }

    @Override
    public String toString() {
        String ben = (id_benevole == null) ? "Aucun bénévole" : "Bénévole " + id_benevole;
        return "Planning [" + id + "] Créneau " + id_creneau + " : Activité " + id_type_activite + " (" + ben + ")";
    }



    /**
     * Prépare les types (tous des INT4 car ce sont des clés étrangères)
     * Gère la conversion du id_benevole en "null" SQL si aucun bénévole
     * n'est affecté à l'instanciation
     */
    @Override
    public void getStruct() {
        map.put("id_creneau", fieldType.INT4);
        map.put("id_benevole", fieldType.INT4);
        map.put("id_type_activite", fieldType.INT4);

        String benVal = (id_benevole == null) ? "null" : String.valueOf(id_benevole);
        this.values = "(" + id_creneau + ", " + benVal + ", " + id_type_activite + ")";
    }


    /**
     * Retourne le nom de la colonne clé primaire
     * @return "id_planning"
     */
    @Override public String getPkName() { return "id_planning"; }

    /**
     * Permet principalement d'assigner ou de changer le bénévole responsable
     * ou de modifier le type d'activité sur un créneau existant
     * @return La clause SQL de modification
     */
    @Override public String getUpdateClause() {
        String benVal = (id_benevole == null) ? "null" : String.valueOf(id_benevole);
        return "id_creneau=" + id_creneau + ", id_benevole=" + benVal + ", id_type_activite=" + id_type_activite;
    }
}