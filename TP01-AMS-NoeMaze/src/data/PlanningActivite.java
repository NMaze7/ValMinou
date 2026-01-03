package data;

public class PlanningActivite extends Entity {
    private int id_creneau;
    private Integer id_benevole; // Nullable
    private int id_type_activite;

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

    @Override
    public void getStruct() {
        map.put("id_creneau", fieldType.INT4);
        map.put("id_benevole", fieldType.INT4);
        map.put("id_type_activite", fieldType.INT4);

        String benVal = (id_benevole == null) ? "null" : String.valueOf(id_benevole);
        this.values = "(" + id_creneau + ", " + benVal + ", " + id_type_activite + ")";
    }

    @Override public String getPkName() { return "id_planning"; }
    @Override public String getUpdateClause() {
        String benVal = (id_benevole == null) ? "null" : String.valueOf(id_benevole);
        return "id_creneau=" + id_creneau + ", id_benevole=" + benVal + ", id_type_activite=" + id_type_activite;
    }
}