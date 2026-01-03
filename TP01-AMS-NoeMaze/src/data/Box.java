package data;

public class Box extends Entity {
    private String nom_reference;
    private String localisation;
    private String type_animal;
    private int capacite_max;

    public Box(String nom_reference, String localisation, String type_animal, int capacite_max) {
        super();
        this.nom_reference = nom_reference;
        this.localisation = localisation;
        this.type_animal = type_animal;
        this.capacite_max = capacite_max;
    }

    // --- Getters & Setters ---
    public String getNomReference() { return nom_reference; }
    public void setNomReference(String nom) { this.nom_reference = nom; }

    public String getLocalisation() { return localisation; }
    public void setLocalisation(String loc) { this.localisation = loc; }

    public String getTypeAnimal() { return type_animal; }
    public void setTypeAnimal(String type) { this.type_animal = type; }

    public int getCapaciteMax() { return capacite_max; }
    public void setCapaciteMax(int cap) { this.capacite_max = cap; }

    @Override
    public String toString() {
        return "Box [ID=" + id + ", Ref=" + nom_reference + ", Cap=" + capacite_max + "]";
    }

    // --- Mapping SQL ---
    @Override
    public void getStruct() {
        map.put("nom_reference", fieldType.VARCHAR);
        map.put("localisation", fieldType.VARCHAR);
        map.put("type_animal", fieldType.VARCHAR);
        map.put("capacite_max", fieldType.INT4);

        String locVal = (localisation == null) ? "null" : "'" + localisation + "'";
        String nomVal = (nom_reference == null) ? "null" : "'" + nom_reference + "'";

        this.values = "(" + nomVal + ", " + locVal + ", '" + type_animal + "', " + capacite_max + ")";
    }

    @Override
    public String getPkName() {
        return "id_box";
    }

    @Override
    public String getUpdateClause() {
        String locVal = (localisation == null) ? "null" : "'" + localisation + "'";
        String nomVal = (nom_reference == null) ? "null" : "'" + nom_reference + "'";

        return "nom_reference=" + nomVal + ", " +
                "localisation=" + locVal + ", " +
                "type_animal='" + type_animal + "', " +
                "capacite_max=" + capacite_max;
    }
}