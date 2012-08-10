package eu.archivesportaleurope.database.migrate;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class UpdateDatabase {

	/**
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException {
		// String url = "jdbc:postgresql:apexv1";
		Properties props = new Properties();
		// props.setProperty("user", "postgres");
		if (args.length >= 2) {
			String url = args[0];
			String username = args[1];
			props.setProperty("user", username);
			if (args.length >= 3) {
				props.setProperty("password", args[2]);
			}
			executeUpdate(url, props);
		} else {
			System.err.println("syntax is: databaseurl username [password]");
		}

	}

	private static void executeUpdate(String url, Properties props) throws SQLException {
		Connection conn = DriverManager.getConnection(url, props);
		DatabaseMetaData md = conn.getMetaData();
		ResultSet rs = md.getTables("public", null, "%", new String[] { "TABLE" });

		while (rs.next()) {
			// get table name
			String tableName = rs.getString(3);

			String query = "SELECT pg_attribute.attname, format_type(pg_attribute.atttypid, pg_attribute.atttypmod) "
					+ " FROM pg_index, pg_class, pg_attribute "
					+ " WHERE pg_class.oid = '"
					+ tableName
					+ "'::regclass AND indrelid = pg_class.oid AND pg_attribute.attrelid = pg_class.oid AND pg_attribute.attnum = any(pg_index.indkey) AND indisprimary";
			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery(query);
			result.next();
			String idNameOld = result.getString(1);
			boolean renamed = false;
			boolean addSequence = false;
			if (!"id".equals(idNameOld)) {
				statement.executeUpdate("ALTER TABLE " + tableName + "  RENAME COLUMN " + idNameOld + "  TO id;");
				renamed = true;
			}
			String idName = "id";
			// System.out.println(tableName + " " + idName);
			result = md.getColumns("public", null, tableName, idName);
			// result =
			// statement.executeQuery("SELECT * FROM information_schema.columns WHERE table_name ='"
			// + tableName + "'");
			result.next();
			if (result.getString(13) == null) {
				addSequence = true;
				result = statement.executeQuery("SELECT max(" + idName + ") FROM " + tableName + "");
				result.next();
				long maxIdValue = result.getLong(1) + 1;
				String sequenceName = tableName.toUpperCase() + "_ID_SEQ";
				try {
					statement.executeUpdate("CREATE SEQUENCE " + sequenceName + " START WITH " + maxIdValue
							+ " INCREMENT BY 1 NO MAXVALUE NO MINVALUE CACHE 1");
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				statement.executeUpdate("ALTER SEQUENCE  " + sequenceName + " OWNED BY " + tableName + "." + idName);
				statement.executeUpdate("ALTER TABLE " + tableName + " ALTER COLUMN " + idName
						+ " SET DEFAULT NEXTVAL(' " + sequenceName + "')");
				statement.executeUpdate("REVOKE ALL ON SEQUENCE " + sequenceName + " FROM PUBLIC");
				statement.executeUpdate("REVOKE ALL ON SEQUENCE " + sequenceName + " FROM admin");
				statement.executeUpdate("GRANT ALL ON SEQUENCE " + sequenceName + " TO admin");
				statement.executeUpdate("GRANT ALL ON SEQUENCE " + sequenceName + " TO apenet_dashboard");
				// statement.executeUpdate("ALTER TABLE "+tableName +
				// " ALTER  COLUMN "+idName+ " TYPE SERIAL");
			}
			String logLine = "Update: " + tableName;
			if (renamed){
				logLine += "\trenamed id";
			}
			if (addSequence){
				logLine += "\tadded sequence";
			}
			System.out.println(logLine);

		}
	}

}
