package eu.apenet.dashboard.manual.tasks;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.manual.contentmanager.ContentManager;
import eu.apenet.dashboard.security.SecurityContext;


/**
 * User: Yoann Moranville
 * Date: 3/28/11
 *
 * @author Yoann Moranville
 */
public class ValidationTask extends AbstractTask {

    public ValidationTask(int aiId, int id){
        super(aiId, id);
    }

    @Override
    public void run() {
        LOG.info("Running ValidationTask");
        try {
            ContentManager.APEnetEADValidate(XmlType.EAD_FA, id);
            insertPipelineStatus(Status.VALIDATED);
            LOG.info("ValidationTask is finished");
        } catch (APEnetException e) {
            LOG.error("ValidationTask failed");
        }
    }
}
