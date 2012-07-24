package eu.apenet.dashboard.manual.tasks;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.dashboard.manual.contentmanager.ContentManager;

/**
 * User: Yoann Moranville
 * Date: 3/28/11
 *
 * @author Yoann Moranville
 */
public class ConversionTask extends AbstractTask {

    public ConversionTask(int aiId, int id){
        super(aiId, id);
    }

    @Override
    public void run() {
        LOG.info("Running ConversionTask");
        try {
            ContentManager.convertToAPEnetEAD(id);
            insertPipelineStatus(Status.CONVERTED);
            LOG.info("ConversionTask is finished");
        } catch (APEnetException e) {
            LOG.error("ConversionTask failed");
        }
    }
}
