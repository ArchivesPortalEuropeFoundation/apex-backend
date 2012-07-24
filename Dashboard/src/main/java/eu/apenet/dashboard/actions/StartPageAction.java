package eu.apenet.dashboard.actions;

import com.opensymphony.xwork2.ActionSupport;
import eu.apenet.dashboard.interceptors.StartingProjectInterceptor;
import eu.apenet.dashboard.security.cipher.BasicDigestPwd;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.apenet.persistence.vo.User;
import eu.apenet.persistence.vo.UserRole;
import eu.apenet.persistence.vo.UserState;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * User: Yoann Moranville
 * Date: Feb 1, 2011
 *
 * @author Yoann Moranville
 */

public class StartPageAction extends ActionSupport {

    private static final long serialVersionUID = -1268753803009511463L;
	private static final Logger LOG = Logger.getLogger(StartPageAction.class);
    private static final String STARTPAGE = "start_page";

    private String password;
    private String username;
    private String emailAddress;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String execute() throws Exception {
        if(StartingProjectInterceptor.isDisabled() || DAOFactory.instance().getUserDAO().doesAdminExist())
            return SUCCESS;

        if(password != null && username != null && emailAddress != null){
            try {
                createAdminAccount();
                return SUCCESS;
            } catch (Exception e){
                LOG.error("Error creating the admin account", e);
            }
        }
        return STARTPAGE;
    }

    private void createAdminAccount() throws Exception {
        LOG.info("Create admin account...");
        String digestedPwd = BasicDigestPwd.generateDigest(password);
        HibernateUtil.beginDatabaseTransaction();
        try {
            UserRole roleType = DAOFactory.instance().getUserRoleDAO().getUserRole(UserRole.ROLE_ADMIN);
            UserState userState = DAOFactory.instance().getUserStateDAO().getUserStateByState(UserState.ACTIVATED);
            User partner = new User(userState, roleType, "admin", "admin", emailAddress, digestedPwd);
            DAOFactory.instance().getUserDAO().insertSimple(partner);
            HibernateUtil.commitDatabaseTransaction();
        } catch (Exception e){
            LOG.error("Could not create Partner admin information", e);
            HibernateUtil.rollbackDatabaseTransaction();
        }
        HibernateUtil.closeDatabaseSession();
    }
}
