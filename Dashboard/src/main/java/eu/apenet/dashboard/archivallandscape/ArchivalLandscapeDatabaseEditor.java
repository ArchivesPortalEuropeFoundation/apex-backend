package eu.apenet.dashboard.archivallandscape;

import java.util.List;

import com.opensymphony.xwork2.Action;

import eu.apenet.dashboard.AbstractAction;
import eu.apenet.persistence.vo.ArchivalInstitution;

/**
 * Class which performs the required actions to change the internal identifiers
 * in database which the ones in the file.
 */
public class ArchivalLandscapeDatabaseEditor extends AbstractAction {
	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = 5929230297999552373L;

	//changed identifiers
	private List<String> oldSameNameInstitution;
	private List<String> newSameNameInstitution;

	/**
	 * Empty constructor.
	 */
	public ArchivalLandscapeDatabaseEditor() {
		super();
	}

	/**
	 * Constructor with params.
	 *
	 * @param oldSameNameInstitution List of internal identifiers in database.
	 * @param newSameNameInstitution List of internal identifiers in file.
	 */
	public ArchivalLandscapeDatabaseEditor(List<String> oldSameNameInstitution, List<String> newSameNameInstitution) {
		super();
		this.setOldSameNameInstitution(oldSameNameInstitution);
		this.setNewSameNameInstitution(newSameNameInstitution);
	}

	/**
	 * Action for change the internal identifiers in database.
	 *
	 * @param institutionsInDB List of institions in database.
	 *
	 * @return String.ACTION_RESULT
	 */
	public String changeDatabaseIdentifier(List<ArchivalInstitution> institutionsInDB) {
		String state = Action.ERROR;
		boolean isValid = (this.getOldSameNameInstitution() != null && this.getNewSameNameInstitution() != null);

		if (isValid
				&& this.getOldSameNameInstitution().size() == this.getNewSameNameInstitution().size()) {
			for (int i = 0; institutionsInDB != null && i < this.getOldSameNameInstitution().size(); i++) {
				institutionsInDB = changeDatabaseInstitutionByIdentifiers(this.getOldSameNameInstitution().get(i), this.getNewSameNameInstitution().get(i), institutionsInDB);
			}
		}

		if(isValid && institutionsInDB !=null) {
			state = Action.SUCCESS;
		}

		return state;
	}

	/**
	 * Change the internal identifier of an institution in database for the one
	 * of that institution in the file.
	 *
	 * @param oldSameNameInstitution Internal identifier in database.
	 * @param newSameNameInstitution Internal identifier in file.
	 * @param archivalInstitutionList List of institutions in database.
	 *
	 * @return Actualized list of institutions in database.
	 */
	private List<ArchivalInstitution> changeDatabaseInstitutionByIdentifiers(String oldSameNameInstitution, String newSameNameInstitution, List<ArchivalInstitution> archivalInstitutionList) {
		boolean found = false;
		for (int i = 0; !found && i < archivalInstitutionList.size(); i++) {
			ArchivalInstitution archivalInstitution = archivalInstitutionList.get(i);
			if (archivalInstitution.getInternalAlId().equalsIgnoreCase(oldSameNameInstitution)) {
				found = true;
				archivalInstitution.setInternalAlId(newSameNameInstitution);
			}
		}

		if (!found) {
			return null;
		}

		return archivalInstitutionList;
	}

	/**
	 * @return the oldSameNameInstitution
	 */
	public List<String> getOldSameNameInstitution() {
		return this.oldSameNameInstitution;
	}

	/**
	 * @param oldSameNameInstitution the oldSameNameInstitution to set
	 */
	public void setOldSameNameInstitution(List<String> oldSameNameInstitution) {
		this.oldSameNameInstitution = oldSameNameInstitution;
	}

	/**
	 * @return the newSameNameInstitution
	 */
	public List<String> getNewSameNameInstitution() {
		return this.newSameNameInstitution;
	}

	/**
	 * @param newSameNameInstitution the newSameNameInstitution to set
	 */
	public void setNewSameNameInstitution(List<String> newSameNameInstitution) {
		this.newSameNameInstitution = newSameNameInstitution;
	}
}
