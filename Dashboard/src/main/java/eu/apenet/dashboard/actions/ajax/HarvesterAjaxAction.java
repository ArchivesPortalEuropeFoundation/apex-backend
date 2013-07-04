package eu.apenet.dashboard.actions.ajax;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.manual.ManualOAIPMHEADUploader;
import eu.apenet.dashboard.utils.HarvestingStatus;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.LinkedList;

/**
 * User: Yoann Moranville
 * Date: 3/22/11
 *
 * @author Yoann Moranville
 */
public class HarvesterAjaxAction extends AjaxControllerAbstractAction {
    private static final long serialVersionUID = -5935378939892679814L;

    private String oaiUrl;
    private String oaiMetadataFormat;
    private String oaiSet;
    private String oaiToken;
    private String oaiType;
    private String oaiFromDate;
    private String oaiToDate;

    @Override
    public String execute() {
        resetHarvestedFiles();
        Integer ai_id = getAiId();
        LOG.info("Providing the Harvest control page for AI_ID=" + ai_id + ", URL=" + oaiUrl + ", METADATAFORMAT=" + oaiMetadataFormat + ", SET=" + oaiSet + ", FROM=" + oaiFromDate + ", TO=" + oaiToDate);
        if(HarvestingStatus.isHarvesting(ai_id)) {
            LOG.error("This institution is already harvesting, please try again later or contact an administrator");
            addActionError(getText("harvesterAjaxAction.institutionAlreadyHarvesting"));
            return ERROR;
        }
//        HarvestingStatus.addHarvestingInstitution(ai_id);
        return SUCCESS;
    }

    public String harvest() {
        LinkedList<File> harvestedFiles = getHarvestedFiles();
        Writer writer = null;
        try {
            writer = openOutputWriter();

            Integer ai_id = getAiId();
            if(PT_TYPE.equals(oaiType)) {
                oaiFromDate = null;
                oaiToDate = null;
            }

            ManualOAIPMHEADUploader manualUploader = new ManualOAIPMHEADUploader(oaiUrl, oaiMetadataFormat, oaiSet, oaiFromDate, oaiToDate);
            String pathForFile = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + ai_id + APEnetUtilities.FILESEPARATOR;
            File saveDirectory = new File(pathForFile);
            if(!saveDirectory.exists())
                saveDirectory.mkdir();
            String oaiTokenEncoded = APEnetUtilities.encodeString(oaiToken);
            final File fileOut = new File((StringUtils.isEmpty(oaiTokenEncoded)? pathForFile + "no_token.xml" : pathForFile + oaiTokenEncoded + ".xml"));
            OutputStream out = new FileOutputStream(fileOut);
            String token = "";

            try {
                if(oaiToken == null || "".equals(oaiToken)) {
                    token = manualUploader.harvestBegin(out);
                } else {
                    token = manualUploader.harvesting(out, oaiToken);
                }

                if(token != null && !"".equals(token))
                    writer.append(new JSONObject().put("currentToken", token).toString());
                else
                    writer.append(new JSONObject().put("finished", true).toString());
            } catch (Exception e){
                LOG.error("Could not finish harvesting, we start again with previous token", e);
                writer.append(new JSONObject().put("currentToken", oaiToken).toString());
            }

            writer.close();
            out.close();
            LOG.info("Retrieved token: " + token);

            final HttpSession session = getServletRequest().getSession();


            if(FI_TYPE.equals(oaiType)) { //Then we know each request will contain one or more complete FA
                harvestedFiles.add(fileOut);
                session.setAttribute(LIST_HARVEST, harvestedFiles);
            } else {
                /**
                 * For Portugal:
                 * - Harvest the WHOLE set
                 * - Recreate X FAs from the set
                 * - Do this only after whole harvest
                 */
                if(PT_TYPE.equals(oaiType)){
                    harvestedFiles.add(fileOut);
                    session.setAttribute(LIST_HARVEST, harvestedFiles);
                }
            }
        } catch (Exception e){
            try {
                if(writer != null)
                    writer.close();
            } catch (IOException ioe){}
            LOG.error("Error", e);
        }
        return null;
    }

    private void resetHarvestedFiles(){
        HttpSession session = getServletRequest().getSession();
        if(session.getAttribute(LIST_HARVEST) != null)
            session.removeAttribute(LIST_HARVEST);
    }

    private LinkedList<File> getHarvestedFiles(){
        HttpSession session = getServletRequest().getSession();
        if(session.getAttribute(LIST_HARVEST) != null)
            return (LinkedList<File>) session.getAttribute(LIST_HARVEST);
        return new LinkedList<File>();
    }

    public String stopHarvest() {
        Writer writer = null;
        try {
            writer = openOutputWriter();
            HarvestingStatus.removeHarvestingInstitution(getAiId());
            writer.append(new JSONObject().put("finished", true).toString());
            writer.close();
        } catch (Exception e){
            try {
                if(writer != null)
                    writer.close();
            } catch (IOException ioe){}
            LOG.error("Error", e);
        }
        return null;
    }

    /* Getters and Setters */
    public String getOaiUrl() {
        return oaiUrl;
    }
    public void setOaiUrl(String oaiUrl) {
        this.oaiUrl = oaiUrl;
    }
    public String getOaiMetadataFormat() {
        return oaiMetadataFormat;
    }
    public void setOaiMetadataFormat(String oaiMetadataFormat) {
        this.oaiMetadataFormat = oaiMetadataFormat;
    }
    public String getOaiSet() {
        return oaiSet;
    }
    public void setOaiSet(String oaiSet) {
        this.oaiSet = oaiSet;
    }
    public String getOaiToken() {
        return oaiToken;
    }
    public void setOaiToken(String oaiToken) {
        this.oaiToken = oaiToken;
    }
    public String getOaiType() {
        return oaiType;
    }
    public void setOaiType(String oaiType) {
        this.oaiType = oaiType;
    }
}
