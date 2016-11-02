/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.Ead3DAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead3;
import java.util.Properties;

/**
 *
 * @author kaisar
 */
public class DeleteTask extends AbstractEad3Task {

    @Override
    protected void execute(Ead3 ead3, Properties properties) throws Exception {
        if (vaild(ead3)) {
            Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
            ead3DAO.delete(ead3);
            ContentUtils.deleteFile(APEnetUtilities.getConfig().getRepoDirPath() + ead3.getPath());
        }
    }

    @Override
    protected String getActionName() {
        return "delete";
    }

    private static boolean vaild(Ead3 ead3) {
        boolean valid = false;
        if (!ead3.isPublished()) {
            valid = true;
        }
        return valid;
    }

}
