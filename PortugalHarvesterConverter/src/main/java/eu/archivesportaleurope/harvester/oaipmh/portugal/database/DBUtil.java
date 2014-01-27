package eu.archivesportaleurope.harvester.oaipmh.portugal.database;

import eu.archivesportaleurope.harvester.oaipmh.portugal.objects.CLevel;
import eu.archivesportaleurope.harvester.oaipmh.portugal.objects.EadContent;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: yoannmoranville
 * Date: 22/01/14
 *
 * @author yoannmoranville
 */
public class DBUtil {
    private Connection dbConnection;
    private final static Logger LOG = Logger.getLogger(DBUtil.class);

    public DBUtil(){
        String dbPath = "jdbc:derby:potugal";
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();

            System.setProperty("derby.system.home", "database");
            dbConnection = DriverManager.getConnection(dbPath);
            dropTables();
            createTables();
        } catch (Exception e){
            try{
                dbPath += ";create=true";
                dbConnection = DriverManager.getConnection(dbPath);
                createTables();
            } catch (SQLException ex) {
                throw new RuntimeException(e);
            }
        }
    }

    private void createTables(){
        LOG.info("Creating the database tables");
        List<String> queries = Arrays.asList(
                "CREATE TABLE ead_content(" +
                        "  id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1)," +
                        "  eadid VARCHAR(255)," +
                        "  xmldata VARCHAR(32672)" +
                        ")",
                "CREATE TABLE c_level(" +
                        "  id INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1)," +
                        "  ec_id INTEGER," +
                        "  parent_cl_id INTEGER," +
                        "  unitid VARCHAR(512)," +
                        "  level VARCHAR(50)," +
                        "  orderId INTEGER," +
                        "  xmldata VARCHAR(32672)" +
                        ")"
        );
        Statement statement;
        try{
            for(String createTableQuery : queries) {
                statement = dbConnection.createStatement();
                statement.execute(createTableQuery);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void dropTables(){
        List<String> queries = Arrays.asList(
                "DROP TABLE ead_content",
                "DROP TABLE c_level"
        );
        for(String query : queries) {
            try {
                Statement statement = dbConnection.createStatement();
                statement.execute(query);
            } catch (Exception e){
//                e.printStackTrace();
            }
        }
    }

    private ResultSet selectEadContentByEadid(String eadid) throws SQLException {
        String query = "SELECT * FROM ead_content WHERE eadid = '" + eadid + "'";
        return launchSelectQuery(query);
    }
    public long retrieveEadContentIdByEadid(String eadid) throws SQLException {
        EadContent eadContent = retrieveEadContentByEadid(eadid);
        if(eadContent != null) {
            return eadContent.getEcId();
        }
        return -1;
    }
    public EadContent retrieveEadContentByEadid(String eadid) throws SQLException {
        ResultSet resultSet = selectEadContentByEadid(eadid);
        try {
            EadContent eadContent = new EadContent();
            if(resultSet.next()) {
                eadContent.setEcId(resultSet.getLong("id"));
                eadContent.setXml(resultSet.getString("xmldata"));
                eadContent.setEadid(resultSet.getString("eadid"));
            }
            return eadContent;
        } catch (SQLException e) {
            LOG.error("Error, returns null", e);
            return null;
        }
    }
    public void saveEadContentEadidAndXml(EadContent eadContent) throws SQLException {
        String query = "INSERT INTO ead_content (eadid, xmldata) VALUES ('" + eadContent.getEadid() + "', '" + eadContent.getXml().replaceAll("'", "&quot;") + "')";
        launchQuery(query);
    }
    public void deleteEadContentById(long id) throws SQLException {
        String query = "DELETE FROM ead_content WHERE id = " + id;
        launchQuery(query);
    }
    public void updateCLevelOther(String column, Object value, Long clId) throws SQLException {
        String query = "UPDATE c_level SET " + column + " = " + value + " WHERE id = " + clId;
        launchQuery(query);
    }
    private ResultSet selectCLevelByIdAndEcIdSelect(String id, long ecId) throws SQLException {
        String query = "SELECT * FROM c_level WHERE unitid = '" + id + "' AND ec_id = " + ecId;
        return launchSelectQuery(query);
    }
    public CLevel selectCLevelByIdAndEcId(String id, long ecId) throws SQLException {
        ResultSet resultSet = selectCLevelByIdAndEcIdSelect(id, ecId);
        try {
            CLevel cLevel = null;
            if(resultSet.next()) {
                cLevel = retrieveCLevelFromResultSet(resultSet);
            }
            return cLevel;
        } catch (SQLException e) {
            LOG.error("Error, returns null", e);
            return null;
        }
    }

    public void insertCLevel(CLevel cLevel) throws SQLException {
        String query = "INSERT INTO c_level (level, ec_id, unitid, orderId, xmldata) VALUES ('" + cLevel.getLevel() + "', " + cLevel.getEcId() + ", '" + cLevel.getUnitid() + "', " + cLevel.getOrderId() + ", '" + cLevel.getXml().replaceAll("'", "&quot;") + "')";
        launchQuery(query);
    }
    public void deleteCLevelById(long id) throws SQLException {
        String query = "DELETE FROM c_level WHERE id = " + id;
        launchQuery(query);
    }
    private ResultSet retrieveNextClevelsSelect(long ecId, int from, int maxElements) throws SQLException {
        String query = "SELECT * FROM c_level WHERE ec_id = " + ecId + " ORDER BY orderId OFFSET " + from + " ROWS FETCH NEXT " + maxElements + " ROWS ONLY";
        return launchSelectQuery(query);
    }

    public CLevel retrieveCLevelByUnitid(String unitid, Long ecId) throws SQLException {
        String query = "SELECT * FROM c_level WHERE unitid = '" + unitid + "' AND ec_id = " + ecId;
        ResultSet resultSet = launchSelectQuery(query);
        try {
            CLevel cLevel = null;
            if(resultSet.next()) {
                cLevel = retrieveCLevelFromResultSet(resultSet);
            }
            return cLevel;
        } catch (SQLException e) {
            LOG.error("Error, returns null", e);
            return null;
        }
    }

    public List<CLevel> retrieveNextClevels(long ecId, int from, int maxElements) throws SQLException {
        ResultSet resultSet = retrieveNextClevelsSelect(ecId, from, maxElements);
        try {
            List<CLevel> cLevels = new ArrayList<CLevel>();
            while(resultSet.next()) {
                CLevel cLevel = retrieveCLevelFromResultSet(resultSet);
                cLevels.add(cLevel);
            }
            return cLevels;
        } catch (SQLException e) {
            LOG.error("Error, returns null", e);
            return null;
        }
    }
    public List<CLevel> retrieveAllClevelsOrdered(long ecId) throws SQLException {
        String query = "SELECT * FROM c_level WHERE ec_id = " + ecId + " ORDER BY unitid";
        ResultSet resultSet = launchSelectQuery(query);
        try {
            List<CLevel> cLevels = new ArrayList<CLevel>();
            while(resultSet.next()) {
                CLevel cLevel = retrieveCLevelFromResultSet(resultSet);
                cLevels.add(cLevel);
            }
            return cLevels;
        } catch (SQLException e) {
            LOG.error("Error, returns null", e);
            return null;
        }
    }
    private CLevel retrieveCLevelFromResultSet(ResultSet resultSet) throws SQLException {
        CLevel cLevel = new CLevel();
        cLevel.setClId(resultSet.getLong("id"));
        cLevel.setLevel(resultSet.getString("level"));
        cLevel.setEcId(resultSet.getLong("ec_id"));
        cLevel.setUnitid(resultSet.getString("unitid"));
        cLevel.setOrderId(resultSet.getInt("orderId"));
        cLevel.setXml(resultSet.getString("xmldata"));

        return cLevel;
    }

    public List<CLevel> retrieveAllChildren(long parentId) throws SQLException {
        String query = "SELECT * FROM c_level WHERE parent_cl_id = " + parentId;
        ResultSet resultSet = launchSelectQuery(query);
        try {
            List<CLevel> cLevels = new ArrayList<CLevel>();
            while(resultSet.next()) {
                CLevel cLevel = retrieveCLevelFromResultSet(resultSet);
                cLevels.add(cLevel);
            }
            return cLevels;
        } catch (SQLException e) {
            LOG.error("Error, returns null", e);
            return null;
        }
    }

    public void launchQuery(String query) throws SQLException {
        Statement statement = dbConnection.createStatement();
        statement.execute(query);
    }
    private ResultSet launchSelectQuery(String query) throws SQLException {
        Statement statement = dbConnection.createStatement();
        return statement.executeQuery(query);
    }
}
