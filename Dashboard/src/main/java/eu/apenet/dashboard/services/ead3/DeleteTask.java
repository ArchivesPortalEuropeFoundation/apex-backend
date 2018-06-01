/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.dao.Ead3DAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead3;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author kaisar
 */
public class DeleteTask extends AbstractEad3Task {

    @Override
    protected void execute(Ead3 ead3, Properties properties) throws Exception {
        if (valid(ead3)) {
            try {
                Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
                CLevelDAO cLevelDAO = DAOFactory.instance().getCLevelDAO();
                
                JpaUtil.beginDatabaseTransaction();
                ead3 = ead3DAO.findById(ead3.getId());
                Set<CLevel> clevels = ead3.getcLevels();
                
                for (CLevel c : clevels) {
                    c.setParentId(null);
                }
                cLevelDAO.store(clevels);
                JpaUtil.commitDatabaseTransaction();
                ContentUtils.deleteFile(APEnetUtilities.getConfig().getRepoDirPath() + ead3.getPath());
                ead3DAO.delete(ead3);
                logAction(ead3);
            } catch (Exception e) {
                logAction(ead3, e);
                throw new APEnetException(this.getActionName() + " " + e.getMessage(), e);
            }
        }
    }

    @Override
    protected String getActionName() {
        return "delete";
    }

    private static boolean valid(Ead3 ead3) {
        boolean valid = false;
        if (!ead3.isPublished()) {
            valid = true;
        }
        return valid;
    }

}
