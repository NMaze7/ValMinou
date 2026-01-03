package data;

import java.sql.*;
import java.util.HashMap;



/**
 * Cette classe encapsule la connexion JDBC et fournit des méthodes utilitaires
 * pour effectuer les opérations CRUD (Create, Read, Update, Delete) de manière générique
 * Elle permet notamment de vérifier la correspondance structurelle entre les objets Java
 * (héritant de Entity et les tables SQL avant toute insertion
 */
public class Gestion {

    private Connection conn;


    /**
     * Initialise le gestionnaire avec une connexion active
     * @param conn La connexion JDBC établie à la base de données
     */
    public Gestion(Connection conn) {
        this.conn = conn;
    }

    /**
     * Cette méthode interroge la base pour connaître les colonnes et leurs types,
     * puis les convertit vers l'énumération fieldType pour permettre
     * la comparaison avec les objets Java
     * @param table   Le nom de la table SQL à analyser
     * @param display Si true, affiche le résultat de l'analyse dans la console
     * @return Une Map associant le nom de la colonne à son type standardisé
     * @throws SQLException Si une erreur survient
     */
    public HashMap<String, fieldType> structTable(String table, boolean display) throws SQLException {
        HashMap<String, fieldType> structure = new HashMap<>();
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet rs = meta.getColumns(null, null, table.toLowerCase(), null);

        while (rs.next()) {
            String column = rs.getString("COLUMN_NAME");
            String type = rs.getString("TYPE_NAME").toUpperCase();
            fieldType ft;


            switch (type) {
                case "INT4":
                case "INTEGER":
                case "SERIAL":
                    ft = fieldType.INT4;
                    break;
                case "FLOAT8":
                case "DOUBLE PRECISION":
                case "NUMERIC":
                case "DECIMAL":
                    ft = fieldType.FLOAT8;
                    break;
                case "VARCHAR":
                case "TEXT":
                case "CHAR":
                    ft = fieldType.VARCHAR;
                    break;
                case "DATE":
                    ft = fieldType.DATE;
                    break;
                case "BOOL":
                case "BOOLEAN":
                case "BIT":
                    ft = fieldType.BOOL;
                    break;
                case "TIME":
                    ft = fieldType.TIME;
                    break;
                default:
                    ft = fieldType.VARCHAR;
            }

            structure.put(column, ft);

            if (display) {
                System.out.println(column + " : " + type + " -> " + ft);
            }
        }
        rs.close();
        return structure;
    }

    /**
     * La méthode effectue les étapes suivantes :
     * Récupère la structure attendue par l'objet Java (via getStruct)
     * Récupère la structure réelle de la table SQL (via structTable)
     * Compare les deux (via check). Si discordance, affiche une erreur détaillée et stop
     * Construit la requête INSERT SQL
     * Exécute l'insertion et récupère la clé primaire générée (SERIAL) pour mettre à jour l'objet
     * @param data  L'entité à insérer (doit hériter de Entity)
     * @param table Le nom de la table cible
     * @throws SQLException Si la structure ne correspond pas ou si l'insertion échoue
     */
    public void insert(Entity data, String table) throws SQLException {
        data.getStruct();
        HashMap<String, fieldType> tableStruct = structTable(table, false);
        if (!data.check(tableStruct)) {
            System.err.println(">>> DÉTAIL DE L'ERREUR DE STRUCTURE pour la table '" + table + "' <<<");
            for (String key : data.getMap().keySet()) {
                if (!tableStruct.containsKey(key)) {
                    System.err.println("   [ERREUR] La colonne Java '" + key + "' n'existe pas dans la table SQL.");
                    continue;
                }
                fieldType typeJava = data.getMap().get(key);
                fieldType typeSQL = tableStruct.get(key);
                if (typeJava != typeSQL) {
                    System.err.println("   [ERREUR] Colonne '" + key + "' : Java attend " + typeJava + " mais SQL est " + typeSQL);
                } else {
                    System.out.println("   [OK] " + key + " (" + typeJava + ")");
                }
            }
            throw new SQLException("Erreur : La structure de l'objet Java ne correspond pas à la table SQL '" + table + "'. (Voir détails ci-dessus)");
        }
        String valuesPart = data.getValues();
        if (valuesPart.startsWith("(")) {
            valuesPart = valuesPart.substring(1);
        }
        String query = "INSERT INTO " + table + " VALUES (DEFAULT, " + valuesPart;
        try (PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        data.setId(keys.getInt(1));
                    }
                }
            }
        }
    }


    /**
     * Méthode utilitaire pour exécuter une requête SQL simple : UPDATE, DELETE...
     */
    public void execute(String query) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
        }
    }

    /**
     * Affiche le contenu brut d'une table
     */
    public void displayTable(String table) throws SQLException {
        String sql = "SELECT * FROM " + table;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columns = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    System.out.print(rsmd.getColumnName(i) + "=" + rs.getObject(i));
                    if (i < columns) System.out.print(", ");
                }
                System.out.println();
            }
        }
    }


    /**
     * Met à jour une entité en base de données
     */
    public void update(Entity data, String table) throws SQLException {
        String pkName = data.getPkName();
        String setClause = data.getUpdateClause();
        int id = data.getId();
        String query = "UPDATE " + table + " SET " + setClause + " WHERE " + pkName + "=" + id;
        execute(query);
    }


    /**
     * Exécute une requête SELECT et retourne le résultat
     * Utile pour recharger les objets depuis la base.
     */
    public ResultSet select(String query) throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(query);
    }

}