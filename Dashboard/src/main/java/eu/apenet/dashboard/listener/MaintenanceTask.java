package eu.apenet.dashboard.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.QueueAction;
import eu.apenet.persistence.vo.QueuingState;
import eu.apenet.persistence.vo.SourceGuide;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

public class MaintenanceTask extends Thread {
	private static final int MAX_AMOUNT = 1000;
	private static final Logger LOGGER = Logger.getLogger(MaintenanceTask.class);
	private static final String REPUBLISH_ALL = "republish-all";
	private static final String REPUBLISH_HG_SG = "republish-hg-sg";
	private static final String DELETE_ALL = "delete-all";

	public MaintenanceTask() {
		JpaUtil.init();
	}

	@Override
	public void run() {

		LOGGER.info("Start maintenance mode");
		try {
			ContentSearchOptions eadSearchOptions = new ContentSearchOptions();
			eadSearchOptions.setPageSize(MAX_AMOUNT);
			if (REPUBLISH_ALL.equals(APEnetUtilities.getDashboardConfig().getMaintenanceAction())) {
				LOGGER.info("Execute command: " + REPUBLISH_ALL);
				eadSearchOptions.setPublished(true);
				List<QueuingState> queuingStates = new ArrayList<QueuingState>();
				queuingStates.add(QueuingState.NO);
				queuingStates.add(QueuingState.ERROR);
				eadSearchOptions.setQueuing(queuingStates);
				try {
					eadSearchOptions.setContentClass(HoldingsGuide.class);
					EadService.updateEverything(eadSearchOptions, QueueAction.REPUBLISH);
					eadSearchOptions.setContentClass(SourceGuide.class);
					EadService.updateEverything(eadSearchOptions, QueueAction.REPUBLISH);
					eadSearchOptions.setContentClass(FindingAid.class);
					EadService.updateEverything(eadSearchOptions, QueueAction.REPUBLISH);
				} catch (IOException e) {
					LOGGER.error("unexpected error occurs: " + e.getMessage(), e);
				}

			} else if (REPUBLISH_HG_SG.equals(APEnetUtilities.getDashboardConfig().getMaintenanceAction())) {
				LOGGER.info("Execute command: " + REPUBLISH_HG_SG);
				eadSearchOptions.setPublished(true);
				List<QueuingState> queuingStates = new ArrayList<QueuingState>();
				queuingStates.add(QueuingState.NO);
				queuingStates.add(QueuingState.ERROR);
				eadSearchOptions.setQueuing(queuingStates);
				try {
					eadSearchOptions.setContentClass(HoldingsGuide.class);
					EadService.updateEverything(eadSearchOptions, QueueAction.REPUBLISH);
					eadSearchOptions.setContentClass(SourceGuide.class);
					EadService.updateEverything(eadSearchOptions, QueueAction.REPUBLISH);
				} catch (IOException e) {
					LOGGER.error("unexpected error occurs: " + e.getMessage(), e);
				}

			} else if (DELETE_ALL.equals(APEnetUtilities.getDashboardConfig().getMaintenanceAction())) {
				LOGGER.info("Start to add all files to the queue to delete them");
				List<QueuingState> queuingStates = new ArrayList<QueuingState>();
				queuingStates.add(QueuingState.NO);
				queuingStates.add(QueuingState.ERROR);
				eadSearchOptions.setQueuing(queuingStates);
				try {
					eadSearchOptions.setContentClass(HoldingsGuide.class);
					EadService.updateEverything(eadSearchOptions, QueueAction.DELETE);
					eadSearchOptions.setContentClass(SourceGuide.class);
					EadService.updateEverything(eadSearchOptions, QueueAction.DELETE);
					eadSearchOptions.setContentClass(FindingAid.class);
					EadService.updateEverything(eadSearchOptions, QueueAction.DELETE);
				} catch (IOException e) {
					LOGGER.error("unexpected error occurs: " + e.getMessage(), e);
				}
			}
		} finally {
			JpaUtil.closeDatabaseSession();
		}
		LOGGER.info("Ends maintenance mode");
	}
}
