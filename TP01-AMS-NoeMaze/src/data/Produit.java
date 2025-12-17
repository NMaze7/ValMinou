package data;

import java.util.HashMap;

public class Produit implements IData {

    private int id;
    private int lot;
    private String nom;
    private String description;
    private String categorie;
    private double prix;
    private String values;
    private HashMap<String, fieldType> map;

    public Produit(int lot, String nom, String description, String categorie, double prix) {
        this.lot = lot;
        this.nom = nom;
        this.description = description;
        this.categorie = categorie;
        this.prix = prix;
        map = new HashMap<>();
        values = "";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getLot() { return lot; }
    public void setLot(int lot) { this.lot = lot; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    @Override
    public String toString() {
        return "Produit [id=" + id + ", lot=" + lot + ", nom=" + nom + 
               ", description=" + description + ", categorie=" + categorie + 
               ", prix=" + prix + "]";
    }

    @Override
    public void getStruct() {
        map.put("lot", fieldType.INT4);
        map.put("nom", fieldType.VARCHAR);
        map.put("description", fieldType.VARCHAR);
        map.put("categorie", fieldType.VARCHAR);
        map.put("prix", fieldType.FLOAT8);

        values = "(" + lot + ", '" + nom + "', '" + description + "', '" + categorie + "', " + prix + ")";
    }

    @Override
    public String getValues() {
        return values;
    }

    @Override
    public HashMap<String, fieldType> getMap() {
        return map;
    }

    @Override
    public boolean check(HashMap<String, fieldType> tableStruct) {
        for (String key : map.keySet()) {
            if (!tableStruct.containsKey(key)) return false;
            if (map.get(key) != tableStruct.get(key)) return false;
        }
        return true;
    }
}
