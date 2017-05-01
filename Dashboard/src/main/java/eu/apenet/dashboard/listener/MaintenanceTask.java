package eu.apenet.dashboard.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.eaccpf.EacCpfService;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.QueueAction;
import eu.apenet.persistence.vo.QueuingState;
import eu.apenet.persistence.vo.SourceGuide;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

public class MaintenanceTask extends Thread {
	private static final int MAX_AMOUNT = 1000;
	private static final Logger LOGGER = Logger.getLogger(MaintenanceTask.class);
	private static final String REPUBLISH = "republish-";
	private static final String DELETE = "delete-";

	public MaintenanceTask() {
		JpaUtil.init();
	}

	@Override
	public void run() {

		LOGGER.info("Start maintenance mode");
		try {
			ContentSearchOptions contentSearchOptions = new ContentSearchOptions();
                        contentSearchOptions.setContentClass(FindingAid.class);
			contentSearchOptions.setPageSize(MAX_AMOUNT);
			List<QueuingState> queuingStates = new ArrayList<QueuingState>();
			queuingStates.add(QueuingState.NO);
			queuingStates.add(QueuingState.ERROR);
			contentSearchOptions.setQueuing(queuingStates);
			String maintenanceAction = APEnetUtilities.getDashboardConfig().getMaintenanceAction();
			if (StringUtils.isNotBlank(maintenanceAction)){
				if (maintenanceAction.startsWith(REPUBLISH)) {
					LOGGER.info("Execute command: " + maintenanceAction);
					contentSearchOptions.setPublished(true);
					updateEverything(maintenanceAction,contentSearchOptions, QueueAction.REPUBLISH);
	
				} else if (maintenanceAction.startsWith(DELETE)) {
					LOGGER.info("Start to add all files to the queue to delete them");
					updateEverything(maintenanceAction,contentSearchOptions, QueueAction.DELETE);
				}
			}
		} finally {
			JpaUtil.closeDatabaseSession();
		}
		
		LOGGER.info("Ends maintenance mode");
	}
	private void updateEverything(String maintenanceAction,ContentSearchOptions contentSearchOptions, QueueAction queueAction){
		boolean all = maintenanceAction.endsWith("all");
		try {
			if (all || maintenanceAction.contains("hg")){
				contentSearchOptions.setContentClass(HoldingsGuide.class);
				EadService.updateEverything(contentSearchOptions, queueAction);
			}
			if (all || maintenanceAction.contains("sg")){
				contentSearchOptions.setContentClass(SourceGuide.class);
				EadService.updateEverything(contentSearchOptions, queueAction);
			}
			if (all || maintenanceAction.contains("fa")){
				contentSearchOptions.setContentClass(FindingAid.class);
				EadService.updateEverything(contentSearchOptions, queueAction);
			}
			if (all || maintenanceAction.contains("eaccpf")){
				contentSearchOptions.setContentClass(EacCpf.class);
				EacCpfService.updateEverything(contentSearchOptions, queueAction);
			}
		} catch (IOException e) {
			LOGGER.error("unexpected error occurs: " + e.getMessage(), e);
		}
	}
}
