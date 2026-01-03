package data;
import java.sql.Time;

public class ModeleSemaineType extends Entity {
    private String jour_semaine;
    private Time heure_debut;
    private Time heure_fin;
    private int nb_benevoles_min_defaut;
    private Integer id_type_activite_defaut;

    public ModeleSemaineType(String jour, Time debut, Time fin, int nb, Integer idType) {
        this.jour_semaine = jour;
        this.heure_debut = debut;
        this.heure_fin = fin;
        this.nb_benevoles_min_defaut = nb;
        this.id_type_activite_defaut = idType;
    }

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