package eu.apenet.dashboard.harvest;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitutionOaiPmh;


public class DownloadHarvesterErrorsAction extends AbstractAction {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6086665250239818127L;
    private Integer harvestId;

    public Integer getHarvestId() {
        return harvestId;
    }
    private Integer index;

    public void setHarvestId(Integer harvestId) {
        this.harvestId = harvestId;
    }
    
    public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String downloadXml () throws IOException{
    	if (harvestId != null  && index != null){
    		ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO().findById(harvestId.longValue());
    		if (!this.getSecurityContext().isAdmin()){
    			this.getSecurityContext().checkAuthorized(archivalInstitutionOaiPmh.getAiId());
    		}
    		if (archivalInstitutionOaiPmh.getErrorsResponsePath() != null){
    			String[] items = DataHarvester.getErrorResponsePaths(archivalInstitutionOaiPmh);
    			if (items.length > index){
    				ContentUtils.downloadXml(getServletRequest(), getServletResponse(), new File(items[index]));
    			}
    			
    			return null;
    		}
    	}
    	return ERROR;
    }

    public String downloadText () throws IOException{
    	if (harvestId != null){
    		ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO().findById(harvestId.longValue());
    		if (!this.getSecurityContext().isAdmin()){
    			this.getSecurityContext().checkAuthorized(archivalInstitutionOaiPmh.getAiId());
    		}
    		if (archivalInstitutionOaiPmh.getHarvestingDetails() != null){
    			String filename = archivalInstitutionOaiPmh.getUrl() + "_" + archivalInstitutionOaiPmh.getSet() + "_" + archivalInstitutionOaiPmh.getMetadataPrefix() + ".txt";
    			PrintWriter printWriter = ContentUtils.getWriterToDownload(getServletRequest(), getServletResponse(),APEnetUtilities.convertToFilename(filename), "text/plain");
    			printWriter.write(archivalInstitutionOaiPmh.getHarvestingDetails());
    			printWriter.flush();
    			printWriter.close();
    			return null;
    		}
    	}
    	return ERROR;
    }
}