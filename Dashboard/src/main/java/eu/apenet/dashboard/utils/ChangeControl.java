package eu.apenet.dashboard.utils;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.FindingAid;

/**
 * 
 * @author eloy
 */
public class ChangeControl {
	private static Logger LOGGER = Logger.getLogger(ChangeControl.class);

	public static String LOG_IN_OPERATION = "Log in";
	public static String REGISTRATION_OPERATION = "Registration";
	public static String CHANGE_PASSWORD_OPERATION = "Change password";
	public static String MODIFY_REGISTRATION_DATA_OPERATION = "Mod reg data";
	public static String LOG_OUT_OPERATION = "Log out";
	public static String UPLOAD_FA_OPERATION = "Upload FA";
	public static String CHANGE_MANAGER_INSTITUTION = "Change manager instituion";
	public static String MAINTENANCE_MODE_ACTIVATE = "Activate maintenance mode";
	public static String MAINTENANCE_MODE_DEACTIVATE = "Deactivate maintenance mode";

    public final static String UPLOAD_EAD_OPERATION = "Upload EAD";

	public static void logOperation(String operation) {
		LOGGER.info(SecurityContext.get() + ": " + operation);
	}

    public static void logOperation(Ead subject, String action) {
		LOGGER.info(SecurityContext.get() + ": EAD: (" + subject + "," + subject.getArchivalInstitution().getAiname() + ") action: " + action);
	}

	public static void logOperation(FindingAid findingAid, Ese subject, String action) {
		LOGGER.info(SecurityContext.get() + ": FA/ESE: "
					+ findingAid + "/" + subject.getEseId() + " action: " + action);
	}



}
