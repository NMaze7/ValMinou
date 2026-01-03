package data;

public class Famille extends Entity {
    private String nom;
    private String prenom;
    private String adresse;
    private String telephone;
    private String type_famille; // (ex: Accueil, Adoptante...)

    public Famille(String nom, String prenom, String adresse, String telephone, String type_famille) {
        super();
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.type_famille = type_famille;
    }

    // --- Getters & Setters ---
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getTypeFamille() { return type_famille; }
    public void setTypeFamille(String type) { this.type_famille = type; }

    @Override
    public String toString() {
        return "Famille [ID=" + id + ", " + nom + " " + prenom + " (" + type_famille + ")]";
    }

    // --- Mapping SQL ---
    @Override
    public void getStruct() {
        map.put("nom", fieldType.VARCHAR);
        map.put("prenom", fieldType.VARCHAR);
        map.put("adresse", fieldType.VARCHAR); // TEXT en SQL se gère comme VARCHAR en Java
        map.put("telephone", fieldType.VARCHAR);
        map.put("type_famille", fieldType.VARCHAR);

        // Construction du VALUES : attention aux nulls pour adresse/tel
        String adrVal = (adresse == null) ? "null" : "'" + adresse + "'";
        String telVal = (telephone == null) ? "null" : "'" + telephone + "'";

        this.values = "('" + nom + "', '" + prenom + "', " + adrVal + ", " + telVal + ", '" + type_famille + "')";
    }

    @Override
    public String getPkName() {
        return "id_famille";
    }

    @Override
    public String getUpdateClause() {
        String adrVal = (adresse == null) ? "null" : "'" + adresse + "'";
        String telVal = (telephone == null) ? "null" : "'" + telephone + "'";

        return "nom='" + nom + "', " +
                "prenom='" + prenom + "', " +
                "adresse=" + adrVal + ", " +
                "telephone=" + telVal + ", " +
                "type_famille='" + type_famille + "'";
    }
}