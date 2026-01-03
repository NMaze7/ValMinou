package data;

import java.sql.Date;



/**
 * Cette classe correspond à la table historique_emplacement
 * Elle permet de tracer l'historique des déplacements d'un animal en enregistrant
 * où il se trouvait (Box ou Famille) sur une période donnée (Date début / Date fin)
 * C'est une table de liaison essentielle pour la traçabilité sanitaire et administrative
 */
public class HistoriqueEmplacement extends Entity {
    private int id_animal;
    private String type_emplacement; // "Box" ou "Famille"
    private Integer id_box;          // Peut être null
    private Integer id_famille;      // Peut être null
    private Date date_debut;
    private Date date_fin;           // Peut être null (si toujours en cours)
    private String commentaire;





    /**
     * Instancie un nouveau mouvement ou placement
     *
     * @param id_animal        L'identifiant de l'animal concerné
     * @param type_emplacement Le type de lieu ("Box" ou "Famille")
     * @param id_box           L'ID du box (null si l'animal va en famille)
     * @param id_famille       L'ID de la famille (null si l'animal va en box)
     * @param date_debut       La date d'arrivée dans ce lieu
     * @param date_fin         La date de départ (null si l'animal y est encore)
     * @param commentaire      Une observation facultative (peut être null)
     */
    public HistoriqueEmplacement(int id_animal, String type_emplacement, Integer id_box, Integer id_famille, Date date_debut, Date date_fin, String commentaire) {
        super();
        this.id_animal = id_animal;
        this.type_emplacement = type_emplacement;
        this.id_box = id_box;
        this.id_famille = id_famille;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.commentaire = commentaire;
    }




    public int getIdAnimal() { return id_animal; }
    public void setIdAnimal(int id) { this.id_animal = id; }

    public String getTypeEmplacement() { return type_emplacement; }
    public void setTypeEmplacement(String type) { this.type_emplacement = type; }

    public Integer getIdBox() { return id_box; }
    public void setIdBox(Integer id) { this.id_box = id; }

    public Integer getIdFamille() { return id_famille; }
    public void setIdFamille(Integer id) { this.id_famille = id; }

    public Date getDateDebut() { return date_debut; }
    public void setDateDebut(Date date) { this.date_debut = date; }

    public Date getDateFin() { return date_fin; }
    public void setDateFin(Date date) { this.date_fin = date; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String c) { this.commentaire = c; }

    @Override
    public String toString() {
        String lieu = (id_box != null) ? "Box " + id_box : "Famille " + id_famille;
        return "Mouvement [Animal=" + id_animal + ", " + lieu + ", Début=" + date_debut + "]";
    }

    /**
     * Retourne une description textuelle du mouvement
     * @return Une chaîne décrivant l'animal, le lieu et la date de début.
     */
    @Override
    public void getStruct() {
        map.put("id_animal", fieldType.INT4);
        map.put("type_emplacement", fieldType.VARCHAR);
        map.put("id_box", fieldType.INT4);
        map.put("id_famille", fieldType.INT4);
        map.put("date_debut", fieldType.DATE);
        map.put("date_fin", fieldType.DATE);
        map.put("commentaire", fieldType.VARCHAR);

        String boxVal = (id_box == null) ? "null" : String.valueOf(id_box);
        String famVal = (id_famille == null) ? "null" : String.valueOf(id_famille);
        String finVal = (date_fin == null) ? "null" : "'" + date_fin + "'";
        String comVal = (commentaire == null) ? "null" : "'" + commentaire + "'";

        this.values = "(" + id_animal + ", '" + type_emplacement + "', " +
                boxVal + ", " + famVal + ", '" + date_debut + "', " +
                finVal + ", " + comVal + ")";
    }



    /**
     * Retourne le nom de la colonne clé primaire.
     * @return "id_historique"
     */
    @Override
    public String getPkName() {
        return "id_historique";
    }




    /**
     * Permet notamment de mettre à jour la date_fin pour clôturer un emplacement
     * lorsqu'un animal est déplacé ailleurs
     * @return La clause SQL de modification
     */
    @Override
    public String getUpdateClause() {
        String boxVal = (id_box == null) ? "null" : String.valueOf(id_box);
        String famVal = (id_famille == null) ? "null" : String.valueOf(id_famille);
        String finVal = (date_fin == null) ? "null" : "'" + date_fin + "'";
        String comVal = (commentaire == null) ? "null" : "'" + commentaire + "'";

        return "id_animal=" + id_animal + ", " +
                "type_emplacement='" + type_emplacement + "', " +
                "id_box=" + boxVal + ", " +
                "id_famille=" + famVal + ", " +
                "date_debut='" + date_debut + "', " +
                "date_fin=" + finVal + ", " +
                "commentaire=" + comVal;
    }
}