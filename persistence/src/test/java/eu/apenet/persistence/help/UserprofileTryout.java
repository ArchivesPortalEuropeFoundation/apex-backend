/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.persistence.help;

import eu.apenet.persistence.dao.UserDAO;
import eu.apenet.persistence.dao.UserprofileDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.User;
import eu.apenet.persistence.vo.Userprofile;
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
public class UserprofileTryout {

    protected DataSource dataSource;
    protected JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        new UserprofileTryout();
    }

    public UserprofileTryout() {
        try {
            DatabaseConfigurator.getInstance().init();
            dataSource = DatabaseConfigurator.getInstance().getCurrentDataSource();
            jdbcTemplate = DatabaseConfigurator.getInstance().getCurrenJdbcTemplate();
            JpaUtil.init();
            DAOFactory factory = DAOFactory.instance();
            ArchivalInstitution inst = factory.getArchivalInstitutionDAO().findById(368);
            UserprofileDAO profileDAO = factory.getUserprofileDAO();
            Userprofile profile = new Userprofile(368, "testprofile", 1);
            profileDAO.store(profile);

            List<Userprofile> profilesList = profileDAO.getUserprofiles(inst.getAiId());
            for (int i = 0; i < profilesList.size(); i++) {
                Userprofile userprofile = profilesList.get(i);
                System.out.println(userprofile.getNameProfile());
            }
        } catch (Exception ex) {
            Logger.getLogger(UserprofileTryout.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
