package data;



/**
 * Cette classe correspond à la table famille de la base de données
 * Elle permet de stocker les coordonnées des personnes qui interviennent
 * pour aider les animaux, soit en tant que famille d'accueil temporaire,
 * soit en tant qu'adoptants définitifs.
 */
public class Famille extends Entity {
    private String nom;
    private String prenom;
    private String adresse;
    private String telephone;
    private String type_famille; // (ex: Accueil, Adoptante...)



    /**
     * Instancie une nouvelle famille
     * @param nom          Le nom de famille
     * @param prenom       Le prénom
     * @param adresse      L'adresse postale (peut être null)
     * @param telephone    Le numéro de téléphone (peut être null)
     * @param type_famille Le type de relation avec le refuge (ex: "Accueil", "Adoptante")
     */
    public Famille(String nom, String prenom, String adresse, String telephone, String type_famille) {
        super();
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.type_famille = type_famille;
    }



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

    /**
     * Prépare les types de données et formate la chaîne values
     */
    @Override
    public void getStruct() {
        map.put("nom", fieldType.VARCHAR);
        map.put("prenom", fieldType.VARCHAR);
        map.put("adresse", fieldType.VARCHAR);
        map.put("telephone", fieldType.VARCHAR);
        map.put("type_famille", fieldType.VARCHAR);


        String adrVal = (adresse == null) ? "null" : "'" + adresse + "'";
        String telVal = (telephone == null) ? "null" : "'" + telephone + "'";

        this.values = "('" + nom + "', '" + prenom + "', " + adrVal + ", " + telVal + ", '" + type_famille + "')";
    }



    /**
     * Retourne le nom de la colonne clé primaire
     * @return "id_famille"
     */
    @Override
    public String getPkName() {
        return "id_famille";
    }




    /**
     * Met à jour l'identité, les coordonnées et le type de famille
     * @return La clause SQL de modification
     */
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