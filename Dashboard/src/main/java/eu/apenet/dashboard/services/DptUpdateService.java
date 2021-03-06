package eu.apenet.dashboard.services;

import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.utils.ChangeControl;
import eu.apenet.persistence.dao.DptUpdateDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.DptUpdate;

/**
 * User: Yoann Moranville
 * Date: 24/05/2012
 *
 * @author Yoann Moranville
 */
public final class DptUpdateService {
    public static void addDptVersion(String version, String newVersion){
        if (SecurityContext.get().isAdmin()) {
            for(DptUpdate dptUpdate : DAOFactory.instance().getDptUpdateDAO().findAll()) {
                dptUpdate.setNewVersion(newVersion);
                DAOFactory.instance().getDptUpdateDAO().update(dptUpdate);
            }
            DptUpdate dptUpdate = new DptUpdate(version, newVersion);
            DAOFactory.instance().getDptUpdateDAO().store(dptUpdate);
            ChangeControl.logOperation("Creation of DPT version: " + version + ", new version is " + newVersion);
        }
    }
    public static void deleteDptVersion(Long id){
        if (SecurityContext.get().isAdmin()) {
            DptUpdateDAO dptUpdateDAO = DAOFactory.instance().getDptUpdateDAO();
            DptUpdate dptUpdate = dptUpdateDAO.findById(id);
            ChangeControl.logOperation("Deletion of DPT version: " + dptUpdate.getVersion() + " (id: " + dptUpdate.getId() + ")");
            dptUpdateDAO.delete(dptUpdate);
        }
    }
}
