/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.dashboard.test.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 *
 * @author Mahbub
 */
public class DBManager {
    
    private DBManager() {
    }
    
    public static DBManager getInstance() {
        return DBManagerHolder.INSTANCE;
    }
    
    private static class DBManagerHolder {

        private static final DBManager INSTANCE = new DBManager();
    }
    
    private Connection dbCon = null;

    private void getConnection() throws SQLException, IOException {
        Properties connectionProps = new Properties();
        InputStream is = ClassLoader.getSystemResourceAsStream("dbConfig.properties");
        connectionProps.load(is);
        dbCon = DriverManager.getConnection(connectionProps.getProperty("dbUrl"), connectionProps);
        System.out.println("Connected to database");
    }

    private void init() throws SQLException, IOException {
        if (dbCon == null || dbCon.isClosed()) {
            this.getConnection();
        }
    }

    public void executeSqlScript(String[] statements) throws SQLException, IOException {
        init();
        System.out.println("Runn..");
        Statement myStatement = dbCon.createStatement(); // create a Statement object connected to the database connection
        System.out.println(statements.length);
        int number = statements.length; // determine the number of statements to be executed first

        for (int i = 0; i < number; i++) { // loop through the array to execute each individual statement
            String stringSql = statements[i] + ";";
            System.out.println(stringSql);
            try {
                int result = myStatement.executeUpdate(stringSql);
                System.out.println(result + " records affected");
            } catch (SQLException e) {
                System.out.println(e.toString());
            }

        } // end for loop

    } //
}
