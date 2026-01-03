package data;

public class Famille extends Entity {
    private String nom_famille;
    private String ville;
    private int taille_maison; // m2
    private Boolean jardin;




    public Famille(String nom_famille, String ville, int taille_maison, Boolean jardin) {
        super();
        this.nom_famille = nom_famille;
        this.ville = ville;
        this.taille_maison = taille_maison;
        this.jardin = jardin;
    }




    public String getNomFamille() { return nom_famille; }
    public void setNomFamille(String nom) { this.nom_famille = nom; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public int getTailleMaison() { return taille_maison; }
    public void setTailleMaison(int taille) { this.taille_maison = taille; }

    public Boolean getJardin() { return jardin; }
    public void setJardin(Boolean jardin) { this.jardin = jardin; }

    @Override
    public String toString() {
        return "Famille [ID=" + id + ", Nom=" + nom_famille + ", Ville=" + ville + "]";
    }


    @Override
    public void getStruct() {
        map.put("nom_famille", fieldType.VARCHAR);
        map.put("ville", fieldType.VARCHAR);
        map.put("taille_maison", fieldType.INT4);
        map.put("jardin", fieldType.BOOL);

        this.values = "('" + nom_famille + "', '" + ville + "', " + taille_maison + ", " + jardin + ")";
    }

    @Override
    public String getPkName() {
        return "id_famille";
    }

    @Override
    public String getUpdateClause() {
        return "nom_famille='" + nom_famille + "', " +
                "ville='" + ville + "', " +
                "taille_maison=" + taille_maison + ", " +
                "jardin=" + jardin;
    }
}