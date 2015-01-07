package eu.apenet.dashboard.archivallandscape;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Action;

import eu.apenet.dashboard.AbstractAction;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 * Class to fix the wrong values in production database.
 *
 */
public class ArchivalLandscapeFixWrongDBAction extends AbstractAction {
	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = 6768583151830191135L;

	// Log.
	private final Logger log = Logger.getLogger(ArchivalLandscapeFixWrongDBAction.class);

	public ArchivalLandscapeFixWrongDBAction() {
		super();
	}

	/**
	 * Method to execute the action.
	 */
	public String execute() throws Exception {
		log.debug("Starting the process to assign correctly the correct AL order.");

		String result = Action.SUCCESS;

		// Recover the country list.
		List<Country> countryList = DAOFactory.instance().getCountryDAO().findAll();

		// Checks if exists some country.
		if (countryList != null && !countryList.isEmpty()) {
			Iterator<Country> countryIt = countryList.iterator();
			while (countryIt.hasNext()) {
				// For each country try to recover the Archival institutions for
				// each country.
				Country country = countryIt.next();
				log.debug("Trying to order AL for country: " + country.getCname());

				result = this.recoverAndUpdateAIOrderInRoot(country);

				if (result.equals(Action.SUCCESS)) {
					log.debug("AL ordered for country: " + country.getCname());
				} else {
					log.error("AL no ordered for country: " + country.getCname());
				}
			}
		} else {
			log.debug("No countries in database.");
		}

		log.debug("Ending the process to assign correctly the correct AL order.");
		return result;
	}

	/**
	 * Method for recover and update the order of the institutions at root level
	 * for the specified country.
	 *
	 * @param country The current country.
	 * @return Result.
	 */
	private String recoverAndUpdateAIOrderInRoot(Country country) {
		String result = Action.SUCCESS;
		// Counter.
		int count = 0;

		try {
			JpaUtil.beginDatabaseTransaction();
			// For production and acceptance database the AL order is sequential
			// so recover the institutions in this order.
			// Initially only recover the parent institutions.
			ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
			List<ArchivalInstitution> aiParentList = archivalInstitutionDAO.getArchivalInstitutionsByCountryIdForAL(country.getId(), true);
	
			// Checks if exists some institutions.
			if (aiParentList != null && !aiParentList.isEmpty()) {
				Iterator<ArchivalInstitution> aiIt = aiParentList.iterator();
				while (aiIt.hasNext()) {
					ArchivalInstitution archivalInstitution = aiIt.next();
	
					log.debug("Changing AL order for institution: " + archivalInstitution.getAiname() + " (" + archivalInstitution.getAlorder() + " -> " + count + ")");
	
					archivalInstitution.setAlorder(count);
					count++;
					if (archivalInstitution.isGroup()) {
						recoverAndUpdateAIOrderInChilds(archivalInstitution);
					}
					archivalInstitutionDAO.updateSimple(archivalInstitution);
				}
			} else {
				log.debug("No institution for current country (" + country.getCname() + ").");
			}
		} catch (Exception e) {
			JpaUtil.rollbackDatabaseTransaction();
			result = Action.ERROR;
			log.error("Exception processing country: " + country.getCname() + ". Cause: " + e.getMessage());
		} finally {
			JpaUtil.commitDatabaseTransaction();
		}

		return result;
	}

	/**
	 * Method for recover and update the order of the child institutions of the 
	 * specified one.
	 *
	 * @param parentAI Parent archival institution.
	 * @return Result.
	 */
	private String recoverAndUpdateAIOrderInChilds(ArchivalInstitution parentAI) {
		String result = Action.SUCCESS;
		// Counter.
		int count = 0;

		try {
			// List of child institutions of the specified one.
			ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
			List<ArchivalInstitution> aiChildsList = archivalInstitutionDAO.getArchivalInstitutionsByParentAiId(parentAI.getAiId(), true);
	
			// Checks if exists some institutions.
			if (aiChildsList != null && !aiChildsList.isEmpty()) {
				Iterator<ArchivalInstitution> aiIt = aiChildsList.iterator();
				while (aiIt.hasNext()) {
					ArchivalInstitution archivalInstitution = aiIt.next();
	
					log.debug("Changing AL order for institution: " + archivalInstitution.getAiname() + " (" + archivalInstitution.getAlorder() + " -> " + count + ")");
	
					archivalInstitution.setAlorder(count);
					count++;
					if (archivalInstitution.isGroup()) {
						recoverAndUpdateAIOrderInChilds(archivalInstitution);
					}
					archivalInstitutionDAO.updateSimple(archivalInstitution);
				}
			} else {
				log.debug("No child institution for current institution: " + parentAI.getAiname() + " (id: " + parentAI.getAiId() + ").");
			}
		} catch (Exception e) {
			result = Action.ERROR;
			log.error("Exception processing childs of institution: " + parentAI.getAiname() + ". Cause: " + e.getMessage());
		}
		
		return result;
	}
}
