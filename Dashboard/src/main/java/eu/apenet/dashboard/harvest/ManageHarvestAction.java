package eu.apenet.dashboard.harvest;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.listener.HarvesterDaemon;
import eu.apenet.persistence.dao.ArchivalInstitutionOaiPmhDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitutionOaiPmh;

/**
 * User: Yoann Moranville
 * Date: 12/11/2013
 *
 * @author Yoann Moranville
 */
public class ManageHarvestAction extends AbstractAction {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6086665250239818127L;
	private static final Logger LOGGER = Logger.getLogger(ManageHarvestAction.class);
    private static final SimpleDateFormat DATE_TIME = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    private Integer harvestId;
    private boolean processOnceADay = true;
    private String selectedAction;

    public Integer getHarvestId() {
        return harvestId;
    }

    public void setHarvestId(Integer harvestId) {
        this.harvestId = harvestId;
    }

    @Override
    protected void buildBreadcrumbs() {
        super.buildBreadcrumbs();
        addBreadcrumb(getText("admin.harvestmanagement.title"));
    }

    public String execute() throws Exception {
        ArchivalInstitutionOaiPmhDAO archivalInstitutionOaiPmhDAO = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO();
        processOnceADay = HarvesterDaemon.isProcessOnceADay();
        getServletRequest().setAttribute("numberOfActiveItems", archivalInstitutionOaiPmhDAO.countEnabledItems());
        getServletRequest().setAttribute("allOaiProfiles", DisplayHarvestProfileItem.getItems(archivalInstitutionOaiPmhDAO.getArchivalInstitutionOaiPmhs(), new Date()));
		getServletRequest().setAttribute("firstItems", DisplayHarvestProfileItem.getItems(archivalInstitutionOaiPmhDAO.getFirstItems(), new Date()));
        getServletRequest().setAttribute("harvestActive", HarvesterDaemon.isActive());
        getServletRequest().setAttribute("harvestProcessing", HarvesterDaemon.isHarvesterProcessing());
        getServletRequest().setAttribute("defaultHarvestingProcessing", APEnetUtilities.getDashboardConfig().isDefaultHarvestingProcessing());
        getServletRequest().setAttribute("currentTime", DATE_TIME.format(new Date()));
        getServletRequest().setAttribute("dailyHarvesting",processOnceADay );
        
        return SUCCESS;
    }

    public String manageHarvestItem(){
        ArchivalInstitutionOaiPmhDAO archivalInstitutionOaiPmhDAO = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO();
        ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh = archivalInstitutionOaiPmhDAO.findById(harvestId.longValue());
    	if ("NOW".equals(selectedAction)){
    		archivalInstitutionOaiPmh.setNewHarvesting(new Date());
    		archivalInstitutionOaiPmhDAO.update(archivalInstitutionOaiPmh);
    	}else if ("DISABLE".equals(selectedAction)){
    		archivalInstitutionOaiPmh.setEnabled(false);
    		archivalInstitutionOaiPmhDAO.update(archivalInstitutionOaiPmh);
    	}else if ("ENABLE".equals(selectedAction)){
    		archivalInstitutionOaiPmh.setEnabled(true);
    		archivalInstitutionOaiPmhDAO.update(archivalInstitutionOaiPmh);
    	}else if ("DELETE".equals(selectedAction)){
    		archivalInstitutionOaiPmh.setEnabled(true);
    		archivalInstitutionOaiPmhDAO.delete(archivalInstitutionOaiPmh);
    	}else if ("FULL".equals(selectedAction)){
    		archivalInstitutionOaiPmh.setFrom(null);
    		archivalInstitutionOaiPmhDAO.update(archivalInstitutionOaiPmh);
    	}
    	return SUCCESS;
    }


    public String startStopHarvester() {
        if(HarvesterDaemon.isActive()) {
            HarvesterDaemon.stop();
        } else {
            HarvesterDaemon.start(processOnceADay);
        }
        return SUCCESS;
    }

	public boolean isProcessOnceADay() {
		return processOnceADay;
	}

	public void setProcessOnceADay(boolean processOnceADay) {
		this.processOnceADay = processOnceADay;
	}

	public String getSelectedAction() {
		return selectedAction;
	}

	public void setSelectedAction(String selectedAction) {
		this.selectedAction = selectedAction;
	}


    
}