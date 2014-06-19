package eu.apenet.dashboard.services.eag;

import org.apache.log4j.Logger;

import eu.apenet.persistence.vo.ArchivalInstitution;

public class EagService {
	private static final Logger LOGGER = Logger.getLogger(EagService.class);
	
	public static void unpublish(ArchivalInstitution archivalInstitution){
		LOGGER.info("unpublish: " + archivalInstitution.getAiname() + " " + archivalInstitution.isGroup() + " " + archivalInstitution.getEagPath());
	}
	public static void publish(ArchivalInstitution archivalInstitution){
		LOGGER.info("publish: " + archivalInstitution.getAiname() + " " + archivalInstitution.isGroup() + " "+ archivalInstitution.getEagPath());
	}
}
