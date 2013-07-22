package eu.apenet.dashboard.manual;

import org.apache.log4j.Logger;
import org.oclc.oai.harvester.app.RawWriteAPEnet;

import java.io.IOException;
import java.io.OutputStream;

public class ManualOAIPMHEADUploader extends ManualUploader {
    private static final Logger LOG = Logger.getLogger(ManualOAIPMHEADUploader.class);

    private String oaiServer;
    private String oaiFormat;
    private String oaiSet;
    private String oaiTimeFrom;
    private String oaiTimeTo;

    public ManualOAIPMHEADUploader(String oaiServer, String oaiFormat, String oaiSet, String oaiTimeFrom, String oaiTimeTo){
        this.oaiServer = oaiServer;
        this.oaiFormat = oaiFormat;
        this.oaiSet = oaiSet;
        this.oaiTimeFrom = oaiTimeFrom;
        this.oaiTimeTo = oaiTimeTo;
    }

    /**
     * Begins the harvesting process given an OutputStream to store the harvested data.
     * @param out The outputStream where the harvesting result will be written too. It should already be instanciated. Preferences would be that it is a FileOutputStream.
     * @return The token for the next harvest step
     * @throws Exception is thrown if the server response is wrong
     */
    public String harvestBegin(OutputStream out) throws Exception {
        try {
            String token = RawWriteAPEnet.run_getToken(this.oaiServer, this.oaiTimeFrom, this.oaiTimeTo, this.oaiFormat, this.oaiSet, out);
            LOG.debug("Token: " + token);
            return token;
        } catch (Exception e){
            LOG.error("Error while harvesting " + this.oaiServer + "...", e);
            throw new Exception(e);
        }
    }

    private void harvestFinished(OutputStream out, boolean success) throws Exception {
        try {
            out.write("</harvest>\n".getBytes("UTF-8"));
            if(success)
                LOG.debug("Harvest finished");
        } catch(Exception e){
            throw new Exception(e);
        }
    }

    public String harvesting(OutputStream out, String token) throws Exception {
        if (token != null && !"".equals(token)) {
            if(!token.startsWith("Error record")){
                token = RawWriteAPEnet.run_getToken(this.oaiServer, token, out);
                LOG.debug("Token: " + token);
                return token;
            } else {
                throw new Exception("Error: " + token);
            }
        }
        return null;
    }

/**
    @Override
	public Boolean upload() {
		return null;
	}
**/
	
	private Boolean checkFormat(){
		return null;
	}


    public String getOaiServer() {
        return oaiServer;
    }

    public void setOaiServer(String oaiServer) {
        this.oaiServer = oaiServer;
    }

    public String getOaiFormat() {
        return oaiFormat;
    }

    public void setOaiFormat(String oaiFormat) {
        this.oaiFormat = oaiFormat;
    }

    public String getOaiSet() {
        return oaiSet;
    }

    public void setOaiSet(String oaiSet) {
        this.oaiSet = oaiSet;
    }
}
