package data;


/**
 * Cette classe correspond à la table type_activite de la base de données
 * C'est une table qui permet de standardiser les tâches
 * (ex: "Promenade", "Nettoyage Box", "Soins Vétérinaires", "Accueil Public")
 * afin de les utiliser dans le planning
 */
public class TypeActivite extends Entity {
    private String libelle;


    /**
     * Crée un nouveau type d'activité
     * @param libelle Le nom de l'activité (ex: "Promenade Chien")
     */
    public TypeActivite(String libelle) {
        super();
        this.libelle = libelle;
    }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    @Override
    public String toString() { return id + ". " + libelle; }


    /**
     * Prépare l'insertion du libellé en tant que VARCHAR
     */
    @Override
    public void getStruct() {
        map.put("libelle", fieldType.VARCHAR);
        this.values = "('" + libelle + "')";
    }


    /**
     * Retourne le nom de la colonne clé primaire
     * @return "id_type_activite"
     */
    @Override public String getPkName() { return "id_type_activite"; }


    /**
     * Permet de corriger ou de renommer une activité existante
     * @return La clause SQL de modification
     */
    @Override public String getUpdateClause() { return "libelle='" + libelle + "'"; }
}