/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.persistence.help;

import eu.apenet.persistence.dao.UserDAO;
import eu.apenet.persistence.dao.IngestionprofileDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.User;
import eu.apenet.persistence.vo.Ingestionprofile;
import eu.archivesportaleurope.database.mock.DatabaseConfigurator;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author papp
 */
public class IngestionprofileTryout {

    protected DataSource dataSource;
    protected JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        new IngestionprofileTryout();
    }

    public IngestionprofileTryout() {
        try {
            DatabaseConfigurator.getInstance().init();
            dataSource = DatabaseConfigurator.getInstance().getCurrentDataSource();
            jdbcTemplate = DatabaseConfigurator.getInstance().getCurrenJdbcTemplate();
            JpaUtil.init();
            DAOFactory factory = DAOFactory.instance();
            ArchivalInstitution inst = factory.getArchivalInstitutionDAO().findById(368);
            IngestionprofileDAO profileDAO = factory.getIngestionprofileDAO();
            Ingestionprofile profile = new Ingestionprofile(368, "testprofile", 1);
            profileDAO.store(profile);

            List<Ingestionprofile> profilesList = profileDAO.getIngestionprofiles(inst.getAiId());
            for (int i = 0; i < profilesList.size(); i++) {
                Ingestionprofile ingestionprofile = profilesList.get(i);
                System.out.println(ingestionprofile.getNameProfile());
            }
        } catch (Exception ex) {
            Logger.getLogger(IngestionprofileTryout.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
