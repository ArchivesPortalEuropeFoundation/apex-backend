package eu.apenet.dashboard.actions.ajax;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.manual.HarvesterParser;
import eu.apenet.dashboard.utils.HarvestingStatus;
import org.json.JSONObject;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.Writer;
import java.util.LinkedList;

/**
 * User: Yoann Moranville
 * Date: 3/28/11
 *
 * @author Yoann Moranville
 */
public class AutomaticCheckAction extends AjaxControllerAbstractAction {
    private String oaiType;

    public String getOaiType() {
        return oaiType;
    }

    public void setOaiType(String oaiType) {
        this.oaiType = oaiType;
    }

    @Override
    public String execute() {
        HttpSession session = getServletRequest().getSession();
        Integer ai_id = getAiId();
        if(session.getAttribute("listUploadedFiles") == null){
            //do conversion of the OAI DC harvested files into FAs
            Writer writer = null;
            try {
                writer = openOutputWriter();
                LinkedList<File> harvestedFiles = (LinkedList<File>) session.getAttribute(LIST_HARVEST);

                if(PT_TYPE.equals(oaiType)){
                    HarvesterParser parser = new HarvesterParser(harvestedFiles, ai_id);
                    parser.dublinCoreToEad();
                    writer.append(new JSONObject().put("finished", true).toString());
                } else if(FI_TYPE.equals(oaiType)){
                    int size = 0;
                    for(File file : harvestedFiles){
                        LOG.info("Checking file: " + file.getName());
                        HarvesterParser parser = new HarvesterParser(file, ai_id);
                        size += parser.analyzeStream().size();
                    }
                    writer.append(new JSONObject().put("fullFinished", true).put("numberEadContent", size).toString());
                    HarvestingStatus.removeHarvestingInstitution(ai_id);
                }
                writer.close();
            } catch (Exception e){
                LOG.error("Error", e);
                try {
                    if(writer != null){
                        writer.append(new JSONObject().put("finished", false).toString());
                        HarvestingStatus.removeHarvestingInstitution(ai_id);
                        writer.close();
                    }
                } catch (Exception ex){
                    LOG.error("Error", ex);
                }
            }
        }
        return null;
    }

    public String dbToEadAjax() {
        HttpSession session = getServletRequest().getSession();
        if(session.getAttribute("listUploadedFiles") == null){
            Writer writer = null;
            Integer ai_id = getAiId();
            try {
                writer = openOutputWriter();

                LinkedList<File> harvestedFiles = (LinkedList<File>) session.getAttribute(LIST_HARVEST);
                HarvesterParser parser = new HarvesterParser(harvestedFiles, ai_id);

                int size = parser.dbToEad(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath());
                session.removeAttribute(LIST_HARVEST);
                writer.append(new JSONObject().put("finished", true).put("numberEadContent", size).toString());
                writer.close();
            } catch (Exception e){
                LOG.error("Error", e);
                try {
                    if(writer != null){
                        writer.append(new JSONObject().put("finished", false).toString());
                        writer.close();
                    }
                } catch (Exception ex){
                    LOG.error("Error", ex);
                }
            } finally {
                HarvestingStatus.removeHarvestingInstitution(ai_id);
            }
        }
        return null;
    }
}
