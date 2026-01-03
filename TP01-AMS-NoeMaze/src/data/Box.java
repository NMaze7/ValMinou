package data;

public class Box extends Entity {
    private String nom_box;
    private double surface;
    private Boolean interieur; // Est-ce un box intérieur ?
    private String type_animal_autorise; // 'Chien', 'Chat'



    public Box(String nom_box, double surface, Boolean interieur, String type_animal_autorise) {
        super();
        this.nom_box = nom_box;
        this.surface = surface;
        this.interieur = interieur;
        this.type_animal_autorise = type_animal_autorise;
    }


    public String getNomBox() { return nom_box; }
    public void setNomBox(String nom) { this.nom_box = nom; }

    public double getSurface() { return surface; }
    public void setSurface(double surface) { this.surface = surface; }

    public Boolean getInterieur() { return interieur; }
    public void setInterieur(Boolean interieur) { this.interieur = interieur; }

    public String getTypeAnimalAutorise() { return type_animal_autorise; }
    public void setTypeAnimalAutorise(String type) { this.type_animal_autorise = type; }

    @Override
    public String toString() {
        return "Box [ID=" + id + ", Nom=" + nom_box + " (" + type_animal_autorise + ")]";
    }


    @Override
    public void getStruct() {
        map.put("nom_box", fieldType.VARCHAR);
        map.put("surface", fieldType.FLOAT8);
        map.put("interieur", fieldType.BOOL);
        map.put("type_animal_autorise", fieldType.VARCHAR);

        this.values = "('" + nom_box + "', " + surface + ", " + interieur + ", '" + type_animal_autorise + "')";
    }

    @Override
    public String getPkName() {
        return "id_box";
    }

    @Override
    public String getUpdateClause() {
        return "nom_box='" + nom_box + "', " +
                "surface=" + surface + ", " +
                "interieur=" + interieur + ", " +
                "type_animal_autorise='" + type_animal_autorise + "'";
    }
}