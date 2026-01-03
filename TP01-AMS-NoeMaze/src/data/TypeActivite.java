package data;

public class TypeActivite extends Entity {
    private String libelle;

    public TypeActivite(String libelle) {
        super();
        this.libelle = libelle;
    }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    @Override
    public String toString() { return id + ". " + libelle; }

    @Override
    public void getStruct() {
        map.put("libelle", fieldType.VARCHAR);
        this.values = "('" + libelle + "')";
    }

    @Override public String getPkName() { return "id_type_activite"; }
    @Override public String getUpdateClause() { return "libelle='" + libelle + "'"; }
}