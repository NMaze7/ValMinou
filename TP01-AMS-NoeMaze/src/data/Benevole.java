package data;



/**
 * Représente un membre intervenant au sein de la SPA
 * Cette classe correspond à la table benevole de la base de données
 * Elle sert à gérer l'ensemble du personnel : bénévoles classiques, vétérinaires,
 * administrateurs,
 * Elle contient les informations de contact ainsi que le rôle spécifique de la personne
 * au sein de l'organisation
 */
public class Benevole extends Entity {
    private String nom;
    private String prenom;
    private String telephone;
    private String adresse;
    private String role; // "Benevole", "Veterinaire", "Admin"...




    /**
     * Instancie un nouveau bénévole ou membre du personnel
     * Le constructeur gère une valeur par défaut pour le champ role
     * si celui-ci est null ou vide, la valeur "Benevole" sera attribuée automatiquement
     * @param nom       Le nom de famille
     * @param prenom    Le prénom
     * @param telephone Le numéro de téléphone (peut être null)
     * @param adresse   L'adresse postale complète (peut être null)
     * @param role      Le rôle dans l'association (ex: "Veterinaire"). Si vide, devient "Benevole"
     */
    public Benevole(String nom, String prenom, String telephone, String adresse, String role) {
        super();
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.adresse = adresse;
        this.role = (role == null || role.isEmpty()) ? "Benevole" : role;
    }



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



    /**
     * Cette méthode prépare l'instruction INSERT en gérant spécifiquement
     * les champs facultatifs (téléphone, adresse). Si ces champs sont null
     * ou vides en Java, ils sont convertis en NULL SQL
     */
    @Override
    public void getStruct() {
        map.put("nom", fieldType.VARCHAR);
        map.put("prenom", fieldType.VARCHAR);
        map.put("telephone", fieldType.VARCHAR);
        map.put("adresse", fieldType.VARCHAR);
        map.put("role", fieldType.VARCHAR);

        String telVal = (telephone == null || telephone.isEmpty()) ? "null" : "'" + telephone + "'";
        String adrVal = (adresse == null || adresse.isEmpty()) ? "null" : "'" + adresse + "'";
        this.values = "('" + nom + "', '" + prenom + "', " + telVal + ", " + adrVal + ", '" + role + "')";
    }


    /**
     * Retourne le nom de la colonne clé primaire
     * @return "id_benevole"
     */
    @Override
    public String getPkName() {
        return "id_benevole";
    }



    /**
     * Met à jour l'ensemble des coordonnées et le rôle
     * Gère également la conversion vers NULL SQL si l'adresse ou le téléphone
     * ont été effacés (rendus vides) dans l'objet Java
     * @return La chaîne de mise à jour formatée pour SQL
     */
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