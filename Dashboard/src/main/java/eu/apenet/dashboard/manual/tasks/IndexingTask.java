package eu.apenet.dashboard.manual.tasks;

import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.manual.contentmanager.ContentManager;

/**
 * User: Yoann Moranville
 * Date: 3/28/11
 *
 * @author Yoann Moranville
 */
public class IndexingTask extends AbstractTask {

    public IndexingTask(int aiId, int id){
        super(aiId, id);
    }

    @Override
    public void run() {
        try {
            ContentManager.index(XmlType.EAD_FA, id);
            insertPipelineStatus(Status.INDEXED);
        } catch (Exception e){
            LOG.error("ERROR", e);
        }
    }
}