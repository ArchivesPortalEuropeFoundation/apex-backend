package eu.apenet.dashboard.services.eaccpf;

import java.util.Properties;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.EacCpfDAO;

import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EacCpf;
import java.io.File;

public class DeleteTask extends AbstractEacCpfTask {
    
    @Override
    protected String getActionName() {
        return "delete";
    }
    
    public static boolean valid(EacCpf eacCpf) {
        boolean valid = false;
        if (!eacCpf.isPublished()) {
            valid = true;
        }
        return valid;
    }
    
    @Override
    protected void execute(EacCpf eacCpf, Properties properties) throws Exception {
        
        if (valid(eacCpf)) {
            try {
                File fileToDelete = new File(APEnetUtilities.getConfig().getRepoDirPath() + eacCpf.getPath());
                if (fileToDelete.exists() && !fileToDelete.isDirectory()) {
                    ContentUtils.deleteFile(fileToDelete, true);
                }
                EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
//                eacCpfDAO.delete(eacCpf);
                eacCpfDAO.deleteById(eacCpf.getId());
                logAction(eacCpf);
            } catch (Exception e) {
                logAction(eacCpf, e);
                throw new APEnetException(this.getActionName() + " " + e.getMessage(), e);
            }
        }
        
    }
}
