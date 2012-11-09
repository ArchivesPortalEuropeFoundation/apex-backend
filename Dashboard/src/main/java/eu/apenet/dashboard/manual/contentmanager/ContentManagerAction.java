package eu.apenet.dashboard.manual.contentmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.IndexUtils;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.dao.FindingAidDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.FileState;
import eu.apenet.persistence.vo.FindingAid;

/**
 * This class has all the actions contemplated into Content Manager section of
 * the Dashboard.
 */
public class ContentManagerAction extends AbstractContentManagerAction {

	private static final long serialVersionUID = -154747215300156989L;
	private Logger log = Logger.getLogger(getClass());

	private static final String UTF8 = "UTF-8";
	private static final String END_ITEM = "}";
	private static final String START_ITEM = "{";

	public ContentManagerAction() {
		if (getXmlTypeId() == null) {
			setXmlType(XmlType.EAD_FA);
			setXmlTypeId(getXmlType().getIdentifier());
		} else {
			setXmlType(XmlType.getType(getXmlTypeId()));
		}
		if (getPageNumber() == null) {
			setPageNumber(0);
		}
		if (getOrderBy() == null) {
			setOrderBy(ORDER_BY_DATE);
		}
		if (getOrderDecreasing() == null) {
			setOrderDecreasing(false);
		}
	}

	/**
	 * Detects if a search is running or not.
	 * 
	 * @return boolean
	 */
	private boolean isLaunchingASearch() {
		if ((getSearchTerms() != null && !getSearchTerms().isEmpty())
				|| (getConvertStatus() != null && !getConvertStatus().isEmpty())
				|| (getValidateStatus() != null && !getValidateStatus().isEmpty())
				|| (getHoldingsGuideStatus() != null && !getHoldingsGuideStatus().isEmpty())
				|| (getIndexStatus() != null && !getIndexStatus().isEmpty())
				|| (getConvertEseStatus() != null && !getConvertEseStatus().isEmpty())
				|| (getDeliverStatus() != null && !getDeliverStatus().isEmpty())) {
			return true;
		}
		return false;
	}

	/**
	 * It should be the last process for content-manager, it's able render the
	 * content-manager values.
	 * 
	 * @return SUCCESS or ERROR
	 */
	private String finishAction() {
		return SUCCESS;
	}

	// /**
	// * This method returns if an aiId is able to do something and returns the
	// state.
	// * If not, write an error and returns false.
	// *
	// * @param aiId
	// * @return boolean
	// */
	// private boolean checkAction(Integer aiId){
	// if(AuthorizationUtils.checkAuthorized(aiId)){
	// return true;
	// }
	// addActionError(getText("aiId.violation.authorization"));
	// log.error("aiId violation authorization error");
	// return false;
	// }
	/**
	 * Validation method of input data.
	 */
	public void validate() {
		log.debug("entering validate function");
		if (getPageNumber() < 0) {
			setPageNumber(0);
			addFieldError("page", getText("page.valid"));
		}
		if (getXmlTypeId() == null) {
			setXmlType(XmlType.EAD_FA);
			setXmlTypeId(getXmlType().getIdentifier());
		} else {
			setXmlType(XmlType.getType(getXmlTypeId()));
		}
		if (getId() != null) {
			if (getId() < 0) {
				addFieldError("id", getText("id.valid"));
			}
		}

		if (getSearchTerms() == null || getSearchTerms().trim().length() == 0) {
			setSearchTerms("");
		}
		if (getSearch() == null || getSearch() < 0 || getSearch() > 2) {
			setSearch(0);
		}
		if (getLimit() == null) {
			setLimit(20); // Default number of results per page
		}
		if (getOrderBy() == null) {
			setOrderBy(ORDER_BY_DATE);
		}
		if (getOrderDecreasing() == null) {
			setOrderDecreasing(false);
		}
		if (getPageNumber() == null) {
			setPageNumber(0); // First page
		}
	}

	/**
	 * This method is the first action (content), which is launched in content
	 * manager. It shows the EADCMUnits of a partner.
	 * 
	 * @return SUCCESS or ERROR
	 */
	public String execute() {
		log.trace("execute function in " + getClass().toString());
		return finishAction();
	}

	/**
	 * This action call to APEnetEADValidate from ContentManager (validate
	 * action).
	 * 
	 * @return SUCCESS or ERROR
	 * @throws APEnetException
	 */
	public String APEnetEADValidate() throws APEnetException {
		ContentManager.APEnetEADValidate(getXmlType(), getId());
		return finishAction();
	}







	/**
	 * (delete action). This method call to ContentManager.delete method to
	 * delete an ead.
	 * 
	 * @return SUCCESS or ERROR
	 * @throws Exception
	 */
	public String delete() throws Exception {
		boolean isBeingHarvested = ContentManager.isBeingHarvested();
		if (!isBeingHarvested
				|| (isBeingHarvested && !(getXmlType().equals(XmlType.EAD_FA) && ContentManager
						.eadHasEsePublished(getId())))) {
			ContentManager.delete(getId(), getXmlType());
			setHarvesting(0);
		} else {
			setHarvesting(1);
		}
		return finishAction();
	}

	/**
	 * (download action) This method provides the functionality of download when
	 * an ead is selected.
	 * 
	 * @return ERROR or DOWNLOAD
	 */
	public String download() {
		Integer aiId = getAiId();
		try {
			if (!getXmlType().equals(XmlType.EAC_CPF)) {
				File tempFile = ContentManager.download(getId(), getXmlType());
				setInputStream(new FileInputStream(tempFile));
				setFileName(tempFile.getName());
				setFileSize(tempFile.length());
			} else {
				setInputStream(ContentManager.getInputStream(getId()));
				setFileName(XmlType.EAC_CPF.getName() + "_" + getId());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		if (getInputStream() == null) {
			log.warn("Download KO, file inputStream not available, aiId :" + aiId + "; file id :" + getId() + ":");
			return ERROR;
		} else {
			try {
				if (getInputStream().available() > 0) {
					log.info("Download OK and available, start download of file for aiId :" + aiId + "; file id :"
							+ getId() + ":");
					return DOWNLOAD;
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		log.warn("Download KO, open but not available (empty file?), aiId :" + aiId + "; file id :" + getId() + ":");
		return ERROR;
	}

	/**
	 * (changeLimitPerPage action). This function reset pageNumber when limit
	 * per page is changed
	 * 
	 * @return SUCCESS or ERROR
	 */
	public String changeLimitPerPage() {
		setPageNumber(0);
		return finishAction();
	}

	/**
	 * (downloadAllESE action).
	 * 
	 * @return ERROR or DOWNLOAD
	 */
	public String downloadAllESE() {
		Integer aiId = getAiId();
		try {
			File tempFile = ContentManager.deliverEseFiles(aiId);
			setInputStream(new FileInputStream(tempFile));
			setFileName(tempFile.getName());
			setFileSize(tempFile.length());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		if (getInputStream() == null) {
			log.info("Download KO, file inputStream not available, aiId :" + aiId + ":");
			return ERROR;
		} else {
			try {
				if (getInputStream().available() > 0) {
					log.info("Download OK and available, start download of file for aiId :" + aiId + ":");
					return DOWNLOAD;
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		log.info("Download KO, open but not available(empty file?)");
		return ERROR;

	}

	/**
	 * (getFindingAidsNotLinked action). Render all the information related to
	 * HG statistics by HG indexed.
	 * 
	 * @return SUCCESS or ERROR
	 */
	public String getFindingAidsNotLinked() {
		Integer aiId = getAiId();
		try {
			setListOfFindingAidsNotIndex(new ArrayList<FindingAid>());
			setListOfFindingAidsIndexed(new ArrayList<FindingAid>());
			setListOfFindingAidsOut(new ArrayList<CLevel>());
			FindingAidDAO faDao = DAOFactory.instance().getFindingAidDAO();
			// First list: listOfFindingAidsNotIndex, finding_aids uploaded
			// but not indexed
			setListOfFindingAidsNotIndex(faDao.getFindingAidsByHoldingsGuideId(getId(), aiId,
					false));
			// Second list: ListOfFindingAidsIndexed, indexed and linked
			// finding_aids
			setListOfFindingAidsIndexed(faDao.getFindingAidsByHoldingsGuideId(getId(), aiId,
					true));
			// Third list: ListOfFindingAidsOut, finding_aids out of the
			// system
			CLevelDAO cLevelDao = DAOFactory.instance().getCLevelDAO();
			setListOfFindingAidsOut(cLevelDao.getCLevelsOutOfSystemByHoldingsGuideId(getId(), aiId));
			setListNotLinkedId(getId());
			setXmlType(XmlType.EAD_HG);
			return finishAction();
		} catch (Exception e) {
			log.error("Error trying to obtain the list fo finding aids not linked by a holdings guide", e);
			return ERROR;
		}

	}

	/**
	 * (deliverToEuropeana action).
	 * 
	 * @return SUCCESS, NONE or ERROR
	 */
	public String deliverToEuropeana() {
		Integer aiId = getAiId();
		String result = ContentManager.deliverToEuropeana(getId());
		if (result.equals(NONE)) {
			setHarvesting(1);
			log.info("setting harvesting to 1, file id:" + getId() + ": aiId :" + aiId + ":");
		} else if (result.equals(SUCCESS)) {
			setHarvesting(0);
			log.info("setting harvesting to 0, file id:" + getId() + ": aiId :" + aiId + ":");
		}/*
		 * else if (result.equals(ERROR)){ setHarvesting(2); }
		 */

		return result;
	}

	/**
	 * (deleteFromEuropeana action).
	 * 
	 * @return SUCCESS or ERROR
	 */
	public String deleteFromEuropeana() {
		boolean isBeingHarvested = ContentManager.isBeingHarvested();
		try {// Change ese_state from ese to "Removed"
			if (!isBeingHarvested
					|| (isBeingHarvested && !(getXmlType().equals(XmlType.EAD_FA) && ContentManager
							.eadHasEsePublished(getId())))) {
				ContentManager.deleteFromEuropeana(this.getId());
				setHarvesting(0);
			} else {
				setHarvesting(1);
			}
			return finishAction();
		} catch (Exception ex) {
			log.error("Delete from Europeana: " + ex.getMessage());
		}

		return ERROR;
	}




	/**
	 * (deleteFromIndex action). This method calls to deleteOnlyFromIndex in
	 * Content Manager and shows the content manager files.
	 * 
	 * @return SUCCESS or ERROR
	 */
	public String deleteOnlyFromIndex() {
		return SUCCESS;
	}

	/**
	 * (removeEseForFA action). New action for delete each Ese, it calls to
	 * ContentManager functions.
	 * 
	 * @return SUCCESS or ERROR
	 */
	public String deleteOnlyEses() {
		boolean isBeingHarvested = ContentManager.isBeingHarvested();
		try {
			if (!isBeingHarvested
					|| (isBeingHarvested && !(getXmlType().equals(XmlType.EAD_FA) && ContentManager
							.eadHasEsePublished(getId())))) {
				ContentManager.deleteFromEuropeana(getId());
				ContentManager.deleteEseFiles(getId());
				setHarvesting(0);
			} else {
				setHarvesting(1);
			}
			return SUCCESS;
		} catch (Exception e) {
			log.error("Error trying to remove ESE files: ", e);

		}
		return ERROR;
	}

	public String preIndex() {

		StringBuffer buffer = new StringBuffer();

		try {
			ServletActionContext.getRequest().setCharacterEncoding(UTF8);
			ServletActionContext.getResponse().setCharacterEncoding(UTF8);
			ServletActionContext.getResponse().setContentType("application/json");
			Writer writer = new OutputStreamWriter(ServletActionContext.getResponse().getOutputStream(), UTF8);

			buffer.append(START_ITEM);
			buffer.append("\"indexing\"");
			buffer.append(":");
			buffer.append("\"" + IndexUtils.getIndexing().toString() + "\"");
			buffer.append(END_ITEM);

			writer.write(buffer.toString());
			writer.close();

			buffer = null;
			writer = null;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return NONE;

	}

}