package data;

import java.sql.*;

import java.util.HashMap;

public class Gestion {

    private Connection conn;

    public Gestion(Connection conn) {
        this.conn = conn;
    }

    public void createTableProduct() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS produit (" +
                       "id SERIAL PRIMARY KEY, " +
                       "lot INT, " +
                       "nom VARCHAR(100), " +
                       "description TEXT, " +
                       "categorie VARCHAR(50), " +
                       "prix FLOAT8" +
                       ")";
        execute(query);
    }

    public void execute(String query) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(query);
        stmt.close();
    }

    public HashMap<String, fieldType> structTable(String table, boolean display) throws SQLException {
        HashMap<String, fieldType> structure = new HashMap<>();
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet rs = meta.getColumns(null, null, table, null);

        while (rs.next()) {
            String column = rs.getString("COLUMN_NAME");
            String type = rs.getString("TYPE_NAME");
            fieldType ft;

            switch (type.toUpperCase()) {
                case "INT4":
                case "INTEGER":
                    ft = fieldType.INT4;
                    break;
                case "FLOAT8":
                case "DOUBLE PRECISION":
                    ft = fieldType.FLOAT8;
                    break;
                case "VARCHAR":
                case "TEXT":
                    ft = fieldType.VARCHAR;
                    break;
                default:
                    ft = fieldType.VARCHAR;
            }

            structure.put(column, ft);

            if (display) {
                System.out.println(column + " : " + type);
            }
        }
        rs.close();
        return structure;
    }

    public void displayTable(String table) throws SQLException {
        String sql = "SELECT * FROM " + table;
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();

        while (rs.next()) {
            for (int i = 1; i <= columns; i++) {
                System.out.print(rsmd.getColumnName(i) + "=" + rs.getObject(i));
                if (i < columns) System.out.print(", ");
            }
            System.out.println();
        }

        rs.close();
        pstmt.close();
    }

    public void insert(IData data, String table) throws SQLException {
        data.getStruct();
        HashMap<String, fieldType> tableStruct = structTable(table, false);

        if (!data.check(tableStruct)) {
            throw new SQLException("La structure de l'instance ne correspond pas à la table " + table);
        }

        if (data instanceof Produit p) {
            // Vérification doublon sur nom+lot
            String checkSql = "SELECT id, prix, description FROM " + table +
                              " WHERE nom='" + p.getNom() + "' AND lot=" + p.getLot();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(checkSql);

            if (rs.next()) {
                int oldId = rs.getInt("id");
                double newPrix = rs.getDouble("prix") + p.getPrix();
                String newDesc = rs.getString("description") + " " + p.getDescription();
                String updateSql = "UPDATE " + table +
                                   " SET prix=" + newPrix + ", description='" + newDesc + "' WHERE id=" + oldId;
                stmt.executeUpdate(updateSql);
            } else {
                String insertSql = "INSERT INTO " + table + " (lot, nom, description, categorie, prix) VALUES " +
                                   "(" + p.getLot() + ", '" + p.getNom() + "', '" +
                                   p.getDescription() + "', '" + p.getCategorie() + "', " +
                                   p.getPrix() + ")";
                stmt.executeUpdate(insertSql, Statement.RETURN_GENERATED_KEYS);
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) p.setId(keys.getInt(1));
                keys.close();
            }
            rs.close();
            stmt.close();
        }
    }
}

