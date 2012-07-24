package eu.apenet.persistence.help;

import java.sql.*;

/**
 * User: Yoann Moranville
 * Date: Sep 7, 2010
 *
 * @author Yoann Moranville
 */
public class PopulatingDatabase {
    /**
     * Yoann:
     * url = jdbc:postgresql:apenet
     * username = apenet_dashboard
     * password = AP3n3tSQLD4sh
     * driver = org.postgresql.Driver
     *
     * Bastiaan:
     * url =
     * username =
     * password =
     * driver = 
     */
    private static final String url = "jdbc:postgresql:apenet";
    private static final String username = "apenet_dashboard";
    private static final String password = "AP3n3tSQLD4sh";
    private static final String driver = "org.postgresql.Driver";

    private Connection conn;

    public static void main(String[] args){
        new PopulatingDatabase();
    }

    public PopulatingDatabase(){
        try {
            if("".equals(url) || "".equals(username) || "".equals(password) || "".equals(driver)){
                System.out.println("Program terminating! You need to enter correct parameters in the class file (url, username, password and driver)");
                System.exit(0);
            }
            System.out.println("Population of database begins");
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, username, password);
            /****DO NOT MODIFY THIS ORDER***/
            createCountryEntries();
            createUserStateEntries();
            createPartnerEntries();
            createArchivalInstitutionEntries();
            createFileStateEntries();
            createThumbnailStateEntries();
            createUploadMethodEntries();
            createHoldingsGuideEntries();
            createFindingAidEntries();
            createFileTypeEntries();
            createUpFileStateEntries();
            createUpFileEntries();
            createOperationTypeEntries();
            /*******/
            conn.close();
            System.out.println("Population of database finished");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void createArchivalInstitutionEntries() throws SQLException {
        Statement st = conn.createStatement();
        st.execute("INSERT INTO archival_institution (ai_id, ainame, registration_date, eag_path, p_id) VALUES (1, 'NameArchival1', '1592-12-31', '/tmp/eag.xml', 1)");
        st.execute("INSERT INTO archival_institution (ai_id, ainame, registration_date, eag_path, p_id) VALUES (2, 'NameArchival2', '1980-10-06', '/tmp/2/eag.xml', 1)");
    }

    private void createCountryEntries() throws SQLException {
        Statement st = conn.createStatement();
        st.execute("INSERT INTO country (cou_id, cname, isoname, al_order) VALUES (1, 'SPAIN', 'ES', 1)");
        st.execute("INSERT INTO country (cou_id, cname, isoname, al_order) VALUES (2, 'FRANCE', 'FR', 2)");
        st.execute("INSERT INTO country (cou_id, cname, isoname, al_order) VALUES (3, 'GERMANY', 'DE', 3)");
        st.execute("INSERT INTO country (cou_id, cname, isoname, al_order) VALUES (4, 'GREECE', 'GR', 4)");
        st.execute("INSERT INTO country (cou_id, cname, isoname, al_order) VALUES (5, 'POLAND', 'PL', 5)");
    }

    private void createFileStateEntries() throws SQLException {
        Statement st = conn.createStatement();
        st.execute("INSERT INTO file_state (fs_id, state) VALUES (1, 'New')");
        st.execute("INSERT INTO file_state (fs_id, state) VALUES (2, 'Not_Validated_Not_Converted')");
        st.execute("INSERT INTO file_state (fs_id, state) VALUES (3, 'Not_Validated_Converted')");
        st.execute("INSERT INTO file_state (fs_id, state) VALUES (4, 'Validated_Converted')");
        st.execute("INSERT INTO file_state (fs_id, state) VALUES (5, 'Validated_Not_Converted')");
        st.execute("INSERT INTO file_state (fs_id, state) VALUES (6, 'Validated_Final_Error')");
        st.execute("INSERT INTO file_state (fs_id, state) VALUES (7, 'Indexing')");
        st.execute("INSERT INTO file_state (fs_id, state) VALUES (8, 'Indexed_Not converted to ESE/EDM')");
        st.execute("INSERT INTO file_state (fs_id, state) VALUES (9, 'Indexed_Converted to ESE/EDM')");
        st.execute("INSERT INTO file_state (fs_id, state) VALUES (10, 'Indexed_Delivered to Europeana')");
        st.execute("INSERT INTO file_state (fs_id, state) VALUES (11, 'Indexed_Harvested to Europeana')");
        st.execute("INSERT INTO file_state (fs_id, state) VALUES (12, 'Indexed_No html')");
        st.execute("INSERT INTO file_state (fs_id, state) VALUES (13, 'Indexed_Not Linked')");
        st.execute("INSERT INTO file_state (fs_id, state) VALUES (14, 'Indexed_Linked')");
    }

    private void createFindingAidEntries() throws SQLException {
        Statement st = conn.createStatement();
        st.execute("INSERT INTO finding_aid (fa_id, fa_title, upload_date, path_apenetead, thu_s_id, number_cached_thumbnails, number_clicks, hg_id, fs_id, um_id, fa_eadid, ai_id, iscaching) VALUES (1, 'FAnb1', '2001-01-01', '/tmp/fa1.xml', 1, 0, 0, 1, 1, 1, '01', 1, FALSE)");
        st.execute("INSERT INTO finding_aid (fa_id, fa_title, upload_date, path_apenetead, thu_s_id, number_cached_thumbnails, number_clicks, hg_id, fs_id, um_id, fa_eadid, ai_id, iscaching) VALUES (2, 'FAnb2', '2001-01-01', '/tmp/fa2.xml', 1, 0, 0, 1, 1, 1, '01', 1, FALSE)");
    }

    private void createHoldingsGuideEntries() throws SQLException {
        Statement st = conn.createStatement();
        st.execute("INSERT INTO holdings_guide (hg_id, hg_tittle, upload_date, path_apenetead, ai_id, fs_id, um_id, hg_eadid) VALUES (1, 'HGTitle', '2001-01-01', '/tmp/hg1.xml', 1, 1, 1, '01')");
    }

    private void createPartnerEntries() throws SQLException {
        Statement st = conn.createStatement();
        st.execute("INSERT INTO partner (p_id, us_id, cou_id, nick, email_address, pwd) VALUES (1, 1, 1, 'nrep1', 'nrep1@apenet.eu', 'nrep1')");
        //Password is "toto"
        st.execute("INSERT INTO partner (p_id, us_id, cou_id, nick, email_address, pwd) VALUES (2, 1, 1, 'cprov1', 'cprov1@apenet.eu', 'C5wmJdwh7wX2rU3fR8XyA4N6oyw=')");
    }

    private void createThumbnailStateEntries() throws SQLException {
        Statement st = conn.createStatement();
        st.execute("INSERT INTO thumbnails_state (thu_s_id, state) VALUES (1, 'No')");
        st.execute("INSERT INTO thumbnails_state (thu_s_id, state) VALUES (2, 'Caching')");
        st.execute("INSERT INTO thumbnails_state (thu_s_id, state) VALUES (3, 'Cached')");
    }

    private void createUploadMethodEntries() throws SQLException {
        Statement st = conn.createStatement();
        st.execute("INSERT INTO upload_method (um_id, method) VALUES (1, 'OAI-PMH')");
        st.execute("INSERT INTO upload_method (um_id, method) VALUES (2, 'FTP')");
        st.execute("INSERT INTO upload_method (um_id, method) VALUES (3, 'HTTP')");
    }

    private void createUserStateEntries() throws SQLException {
        Statement st = conn.createStatement();
        st.execute("INSERT INTO user_state (us_id, state) VALUES (0, 'Blocked')");
        st.execute("INSERT INTO user_state (us_id, state) VALUES (1, 'Actived')");
    }

    private void createFileTypeEntries() throws SQLException {
        Statement st = conn.createStatement();
        st.execute("INSERT INTO file_type (ft_id, ftype) VALUES (0, 'zip')");
        st.execute("INSERT INTO file_type (ft_id, ftype) VALUES (1, 'xml')");
        st.execute("INSERT INTO file_type (ft_id, ftype) VALUES (2, 'xsl')");
    }

    private void createUpFileStateEntries() throws SQLException {
        Statement st = conn.createStatement();
        st.execute("INSERT INTO up_file_state (ufs_id, state) VALUES (0, 'New')");
    }

    private void createUpFileEntries() throws SQLException {
        Statement st = conn.createStatement();
        st.execute("INSERT INTO up_file (uf_id, path, um_id, ufs_id, ai_id, ft_id, fname) VALUES (0, '/tmp/up/hg1.xml', 3, 0, 1, 1, 'hg1.xml')");
        st.execute("INSERT INTO up_file (uf_id, path, um_id, ufs_id, ai_id, ft_id, fname) VALUES (1, '/tmp/up/fa1.xml', 3, 0, 2, 1, 'fa1.xml')");
    }

    private void createOperationTypeEntries() throws SQLException {
        Statement st = conn.createStatement();
        st.execute("INSERT INTO operation_type (ot_id, optype) VALUES (1, 'Upload FA')");
        st.execute("INSERT INTO operation_type (ot_id, optype) VALUES (2, 'Upload eag')");
        st.execute("INSERT INTO operation_type (ot_id, optype) VALUES (3, 'Upload HG')");
        st.execute("INSERT INTO operation_type (ot_id, optype) VALUES (4, 'Remove FA')");
        st.execute("INSERT INTO operation_type (ot_id, optype) VALUES (5, 'Remove HG')");
        st.execute("INSERT INTO operation_type (ot_id, optype) VALUES (6, 'Change password')");
        st.execute("INSERT INTO operation_type (ot_id, optype) VALUES (7, 'Log in')");
        st.execute("INSERT INTO operation_type (ot_id, optype) VALUES (8, 'Log out')");
        st.execute("INSERT INTO operation_type (ot_id, optype) VALUES (9, 'Upload al')");
        st.execute("INSERT INTO operation_type (ot_id, optype) VALUES (10, 'Overwrite al')");
        st.execute("INSERT INTO operation_type (ot_id, optype) VALUES (11, 'Download al')");
        st.execute("INSERT INTO operation_type (ot_id, optype) VALUES (12, 'Remove AI')");
        st.execute("INSERT INTO operation_type (ot_id, optype) VALUES (13, 'Edited al')");
    }

}
