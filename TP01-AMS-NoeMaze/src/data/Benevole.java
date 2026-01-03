package data;

public class Benevole extends Entity {

    private static final long serialVersionUID = 1L;

    private String nom;
    private String prenom;
    private String telephone;
    private String adresse;
    private String role; // "Benevole", "Veterinaire", "Admin"...

    public Benevole(String nom, String prenom, String telephone, String adresse, String role) {
        super();
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.adresse = adresse;
        // Si le rôle est vide, on peut mettre la valeur par défaut du SQL côté Java aussi
        this.role = (role == null || role.isEmpty()) ? "Benevole" : role;
    }

    // --- Getters & Setters ---
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "Benevole [ID=" + id + ", " + nom + " " + prenom + " (" + role + ")]";
    }

    // --- Mapping SQL ---

    @Override
    public void getStruct() {
        map.put("nom", fieldType.VARCHAR);
        map.put("prenom", fieldType.VARCHAR);
        map.put("telephone", fieldType.VARCHAR);
        map.put("adresse", fieldType.VARCHAR); // TEXT = VARCHAR pour nous
        map.put("role", fieldType.VARCHAR);

        // Gestion des NULLs pour les champs facultatifs
        String telVal = (telephone == null || telephone.isEmpty()) ? "null" : "'" + telephone + "'";
        String adrVal = (adresse == null || adresse.isEmpty()) ? "null" : "'" + adresse + "'";

        this.values = "('" + nom + "', '" + prenom + "', " + telVal + ", " + adrVal + ", '" + role + "')";
    }

    @Override
    public String getPkName() {
        return "id_benevole";
    }

    @Override
    public String getUpdateClause() {
        String telVal = (telephone == null || telephone.isEmpty()) ? "null" : "'" + telephone + "'";
        String adrVal = (adresse == null || adresse.isEmpty()) ? "null" : "'" + adresse + "'";

        return "nom='" + nom + "', " +
                "prenom='" + prenom + "', " +
                "telephone=" + telVal + ", " +
                "adresse=" + adrVal + ", " +
                "role='" + role + "'";
    }
}