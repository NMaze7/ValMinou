package data;

import java.sql.*;
import java.util.HashMap;

public class Gestion {

    private Connection conn;

    public Gestion(Connection conn) {
        this.conn = conn;
    }

    /**
     * Récupère la structure d'une table SQL (Nom colonne -> Type)
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
     * Méthode générique pour insérer n'importe quelle entité Animal, Box, etc...
     */
    public void insert(Entity data, String table) throws SQLException {
        data.getStruct(); // Remplit la map Java
        HashMap<String, fieldType> tableStruct = structTable(table, false); // Récupère la map SQL

        // --- DÉBUT DU BLOC DE DÉBOGAGE ---
        if (!data.check(tableStruct)) {
            // On cherche manuellement l'erreur pour l'afficher à l'utilisateur
            System.err.println(">>> DÉTAIL DE L'ERREUR DE STRUCTURE pour la table '" + table + "' <<<");

            for (String key : data.getMap().keySet()) {
                // 1. Vérifier si la colonne existe
                if (!tableStruct.containsKey(key)) {
                    System.err.println("   [ERREUR] La colonne Java '" + key + "' n'existe pas dans la table SQL.");
                    continue;
                }
                // 2. Vérifier si le type correspond
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
        // --- FIN DU BLOC DE DÉBOGAGE ---

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
     * Met à jour une entité en base de données.
     */
    public void update(Entity data, String table) throws SQLException {
        String pkName = data.getPkName();
        String setClause = data.getUpdateClause();
        int id = data.getId();
        String query = "UPDATE " + table + " SET " + setClause + " WHERE " + pkName + "=" + id;
        execute(query);
    }


    /**
     * Exécute une requête SELECT et retourne le résultat brut (ResultSet).
     * Utile pour recharger les objets depuis la base.
     */
    public ResultSet select(String query) throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(query);
    }

}