package eu.apenet.dashboard.harvest;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.listener.HarvesterDaemon;
import eu.apenet.dashboard.listener.HarvesterTask;
import eu.apenet.persistence.dao.ArchivalInstitutionOaiPmhDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitutionOaiPmh;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        getServletRequest().setAttribute("archivalInstitutionOaiPmhs", archivalInstitutionOaiPmhDAO.findAll());
        getServletRequest().setAttribute("harvestActive", HarvesterDaemon.isActive());
        getServletRequest().setAttribute("harvestProcessing", HarvesterDaemon.isHarvesterProcessing());
        getServletRequest().setAttribute("currentTime", DATE_TIME.format(new Date()));
        getServletRequest().setAttribute("dailyHarvesting",processOnceADay );
        
        return SUCCESS;
    }

    public String idleHarvest() throws Exception {
        ArchivalInstitutionOaiPmhDAO archivalInstitutionOaiPmhDAO = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO();
        ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh = archivalInstitutionOaiPmhDAO.findById(harvestId.longValue());
        archivalInstitutionOaiPmh.setEnabled(false);
        archivalInstitutionOaiPmhDAO.update(archivalInstitutionOaiPmh);
        return SUCCESS;
    }

    public String activateHarvest() throws Exception {
        ArchivalInstitutionOaiPmhDAO archivalInstitutionOaiPmhDAO = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO();
        ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh = archivalInstitutionOaiPmhDAO.findById(harvestId.longValue());
        if(!APEnetUtilities.getDashboardConfig().isDefaultHarvestingProcessing()) { //it means on test servers, not prod
            archivalInstitutionOaiPmh.setLastHarvesting(null);
        }
        archivalInstitutionOaiPmh.setEnabled(true);
        archivalInstitutionOaiPmhDAO.update(archivalInstitutionOaiPmh);
        return SUCCESS;
    }

    public String startHarvest() throws Exception {
//        ArchivalInstitutionOaiPmhDAO archivalInstitutionOaiPmhDAO = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO();
//        ArchivalInstitutionOaiPmh archivalInstitutionOaiPmh = archivalInstitutionOaiPmhDAO.findById(harvestId.longValue());
        HarvesterDaemon.setHarvesterProcessing(true);
        new Thread(
            new DataHarvester(harvestId.longValue(), false)
        ).start();

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
    
}