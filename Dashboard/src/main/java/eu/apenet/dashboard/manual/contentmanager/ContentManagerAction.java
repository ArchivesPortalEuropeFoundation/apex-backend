package eu.apenet.dashboard.manual.contentmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.IndexUtils;
import eu.apenet.dashboard.actions.ajax.AjaxControllerAbstractAction;
import eu.apenet.dashboard.manual.FindingAidLogic;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.dao.FileStateDAO;
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
		log.debug("entering finishAction function");
		if (isLaunchingASearch()) {
			return searchEADCMUnit(); // Same page
		}

		return getEADCMUnits(); // Show results
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
		if (getSize() == null) {
			setSize(getSize(getXmlType()));
			if (getSize() == null) {
				setSize(new Long(-1));
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
	 * (createHG action).
	 * 
	 * @return SUCCESS or ERROR
	 */
	public String createHG() {
		Integer aiId = getAiId();
		FindingAidDAO findingAidDao = DAOFactory.instance().getFindingAidDAO();
		Set<String> fileStates = new HashSet<String>();
		fileStates.add(FileState.VALIDATED_CONVERTED);
		fileStates.add(FileState.VALIDATED_NOT_CONVERTED);
		fileStates.add(FileState.INDEXED);
		fileStates.add(FileState.INDEXED_CONVERTED_EUROPEANA);
		fileStates.add(FileState.INDEXED_DELIVERED_EUROPEANA);
		fileStates.add(FileState.INDEXED_HARVESTED_EUROPEANA);
		fileStates.add(FileState.INDEXED_NO_HTML);
		List<FindingAid> findingAidSet = findingAidDao.getFindingAids(aiId, fileStates);
		log.info(getAiname() + " is creating an HG");
		for (FindingAid findingAid : findingAidSet) {
			log.info("Ready to insert " + findingAid.getEadid());
		}
		return finishAction();
	}

	/**
	 * (indextotemp action).
	 * 
	 * @return SUCCESS or ERROR
	 * @throws Exception
	 */
	public String indexToTemp() throws Exception {
		boolean processed = ContentManager.indexProcess(getXmlType(), getId());
		if (processed) {
			return finishAction();
		} else {
			return ERROR;
		}

	}

	/**
	 * (deleteEse action).
	 * 
	 * @return SUCCESS or ERROR
	 * @throws Exception
	 */
	public String deleteEse() throws Exception {
		Integer aiId = getAiId();
		boolean isBeingHarvested = ContentManager.isBeingHarvested();
		try {
			log.info("Institution with id :" + aiId + ": is trying to delete ESE with id :" + getId() + ":");
			if (!isBeingHarvested
					|| (isBeingHarvested && !(getXmlType().equals(XmlType.EAD_FA) && ContentManager
							.eadHasEsePublished(getId())))) {
				ContentManager.deleteEseFiles(getId());
				setHarvesting(0);
			} else {
				setHarvesting(1);
			}
			return finishAction();
		} catch (Exception e) {
			log.error("Unable to delete ese files for FA which fa_id is " + getId() + ". Error:" + e.getMessage());
			throw e;
		}

	}

	/**
	 * (deletefromindexqueue action).
	 * 
	 * @return SUCCESS or ERROR
	 */
	public String deletefromIndexQueue() {
		ContentManager.deletefromIndexQueue(getXmlType(), getId());
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
					Arrays.asList(FileState.NOT_INDEXED_FILE_STATES)));
			// Second list: ListOfFindingAidsIndexed, indexed and linked
			// finding_aids
			setListOfFindingAidsIndexed(faDao.getFindingAidsByHoldingsGuideId(getId(), aiId,
					Arrays.asList(FileState.INDEXED_FILE_STATES)));
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
	 * (batchAction action).
	 * 
	 * @return SUCCESS or INPUT
	 */
	public String batchAction() { // TODO: talk with jara about batch action and
									// aiId, which aiId can be checked? after
									// aiId check
		Integer aiId = getAiId();
		if (getType() == null) {
			log.warn("A whole batch is being launched, but type of batch is null, nothing is done.");
			return SUCCESS;
		}
		if (aiId != null) {
			FindingAidDAO findingAidDao = DAOFactory.instance().getFindingAidDAO();
			FileStateDAO fileStateDAO = DAOFactory.instance().getFileStateDAO();
			List<FindingAid> findingAidListToUse = new ArrayList<FindingAid>();
			List<FindingAid> findingAidSelectedListToUse = new ArrayList<FindingAid>();
			List<FindingAid> findingAidSearchListToUse = new ArrayList<FindingAid>();

			HttpSession session = ServletActionContext.getRequest().getSession();
			List<Integer> tempIds = (List<Integer>) session.getAttribute(AjaxControllerAbstractAction.LIST_IDS);

			if (getType().equals("conversion")) {
				Set<String> notConvertedFileStates = new HashSet<String>();
				notConvertedFileStates.add(FileState.NEW);
				notConvertedFileStates.add(FileState.NOT_VALIDATED_NOT_CONVERTED);
				notConvertedFileStates.add(FileState.VALIDATED_NOT_CONVERTED);
				findingAidListToUse = findingAidDao.getFindingAids(aiId, notConvertedFileStates);
				findingAidSelectedListToUse = findingAidDao.getFindingAids(aiId, tempIds, notConvertedFileStates);
				log.info("Number of Finding Aids to convert: " + findingAidListToUse.size());
				log.info("Number of Selected Finding Aids to convert: " + findingAidSelectedListToUse.size());
			} else if (getType().equals("validation")) {
				Set<String> notValidatedFileStates = new HashSet<String>();
				notValidatedFileStates.add(FileState.NEW);
				notValidatedFileStates.add(FileState.NOT_VALIDATED_CONVERTED);
				findingAidListToUse = findingAidDao.getFindingAids(aiId, notValidatedFileStates);
				findingAidSelectedListToUse = findingAidDao.getFindingAids(aiId, tempIds, notValidatedFileStates);
				log.info("Number of Finding Aids to validate: " + findingAidListToUse.size());
				log.info("Number of Selected Finding Aids to validate: " + findingAidSelectedListToUse.size());
			} else if (getType().equals("indexing")) {
				Set<String> readyForIndexFileStates = new HashSet<String>();
				readyForIndexFileStates.add(FileState.VALIDATED_CONVERTED);
				readyForIndexFileStates.add(FileState.VALIDATED_NOT_CONVERTED);
				findingAidListToUse = findingAidDao.getFindingAids(aiId, readyForIndexFileStates);
				findingAidSelectedListToUse = findingAidDao.getFindingAids(aiId, tempIds, readyForIndexFileStates);
				log.info("Number of Finding Aids to index: " + findingAidListToUse.size());
				log.info("Number of Selected Finding Aids to index: " + findingAidSelectedListToUse.size());
				if (IndexUtils.getIndexing())
					setIndexing(1);
				else {
					setIndexing(0);
				}
			} else if (getType().equals("deleting")) {
				List<FileState> fileStates = fileStateDAO.findAll();
				Set<String> fileStatesStrings = new HashSet<String>();
				for (FileState fileState : fileStates) {
					if (!fileState.getState().equals(FileState.INDEXING))
						fileStatesStrings.add(fileState.getState());
				}
				findingAidListToUse = findingAidDao.getFindingAids(aiId, fileStatesStrings);
				findingAidSelectedListToUse = findingAidDao.getFindingAids(aiId, tempIds, fileStatesStrings);
				removeFAWhichHaveEsePublished(findingAidListToUse, findingAidSelectedListToUse);
				log.info("Number of Finding Aids to delete: " + findingAidListToUse.size());
				log.info("Number of Selected Finding Aids to delete: " + findingAidSelectedListToUse.size());
			} else if (getType().equals("deletingFromIndex")) {
				Set<String> fileStates = new HashSet<String>();
				fileStates.add(FileState.INDEXED);
				fileStates.add(FileState.INDEXED_CONVERTED_EUROPEANA);
				fileStates.add(FileState.INDEXED_DELIVERED_EUROPEANA);
				fileStates.add(FileState.INDEXED_HARVESTED_EUROPEANA);
				fileStates.add(FileState.INDEXED_NO_HTML);
				fileStates.add(FileState.INDEXED_NOT_LINKED);
				fileStates.add(FileState.INDEXED_LINKED);
				findingAidListToUse = findingAidDao.getFindingAids(aiId, fileStates);
				findingAidSelectedListToUse = findingAidDao.getFindingAids(aiId, tempIds, fileStates);
				removeFAWhichHaveEsePublished(findingAidListToUse, findingAidSelectedListToUse);
				log.info("Number of Finding Aids to delete from index: " + findingAidListToUse.size());
				log.info("Number of Selected Finding Aids to delete from index: " + findingAidSelectedListToUse.size());
			} else if (getType().equals("convertingToEse")) {
				Set<String> fileStates = new HashSet<String>();
				fileStates.add(FileState.INDEXED);
				findingAidListToUse = findingAidDao.getFindingAids(aiId, fileStates);
				if (StringUtils.isNotBlank(getSearchTerms()))
					findingAidSearchListToUse = new FindingAidLogic().search(getSearchTerms(), null, null, aiId,
							getSearch(), getOrderBy(), getOrderDecreasing(), fileStates, getSearchStatuses(),
							isLinkedToHoldingsGuide());
				findingAidSelectedListToUse = findingAidDao.getFindingAids(aiId, tempIds, fileStates);
				log.info("Number of Finding Aids to convert to ESE: " + findingAidListToUse.size());
				log.info("Number of Search Finding Aids to convert to ESE: " + findingAidSearchListToUse.size());
				log.info("Number of Selected Finding Aids to convert to ESE: " + findingAidSelectedListToUse.size());
			} else if (getType().equals("deletingEse")) {
				Set<String> fileStates = new HashSet<String>();
				fileStates.add(FileState.INDEXED_CONVERTED_EUROPEANA);
				fileStates.add(FileState.INDEXED_DELIVERED_EUROPEANA);
				fileStates.add(FileState.INDEXED_HARVESTED_EUROPEANA);
				findingAidListToUse = findingAidDao.getFindingAids(aiId, fileStates);
				if (StringUtils.isNotBlank(getSearchTerms()))
					findingAidSearchListToUse = new FindingAidLogic().search(getSearchTerms(), null, null, aiId,
							getSearch(), getOrderBy(), getOrderDecreasing(), fileStates, getSearchStatuses(),
							isLinkedToHoldingsGuide());
				findingAidSelectedListToUse = findingAidDao.getFindingAids(aiId, tempIds, fileStates);
				removeFAWhichHaveEsePublished(findingAidListToUse, findingAidSelectedListToUse);
				log.info("Number of Finding Aids to delete the ESE files: " + findingAidListToUse.size());
				log.info("Number of Search Finding Aids to delete the ESE files: " + findingAidSearchListToUse.size());
				log.info("Number of Selected Finding Aids to delete the ESE files: "
						+ findingAidSelectedListToUse.size());
			} else if (getType().equals("deliveringToEuropeana")) {
				if (ContentManager.isBeingHarvested()) {
					setHarvesting(1);
				} else {
					Set<String> fileStates = new HashSet<String>();
					fileStates.add(FileState.INDEXED_CONVERTED_EUROPEANA);
					findingAidListToUse = findingAidDao.getFindingAids(aiId, fileStates);
					if (StringUtils.isNotBlank(getSearchTerms()))
						findingAidSearchListToUse = new FindingAidLogic().search(getSearchTerms(), null, null, aiId,
								getSearch(), getOrderBy(), getOrderDecreasing(), fileStates, getSearchStatuses(),
								isLinkedToHoldingsGuide());
					findingAidSelectedListToUse = findingAidDao.getFindingAids(aiId, tempIds, fileStates);
					log.info("Number of Finding Aids to deliver to Europeana: " + findingAidListToUse.size());
					log.info("Number of Search Finding Aids to deliver to Europeana: "
							+ findingAidSearchListToUse.size());
					log.info("Number of Selected Finding Aids to deliver to Europeana: "
							+ findingAidSelectedListToUse.size());
					setHarvesting(0);
				}
			} else if (getType().equals("batchEuropeanaDelete")) {
				if (ContentManager.isBeingHarvested()) {
					setHarvesting(1);
				} else {
					Set<String> fileStates = new HashSet<String>();
					fileStates.add(FileState.INDEXED_DELIVERED_EUROPEANA);
					findingAidListToUse = findingAidDao.getFindingAids(aiId, fileStates);
					if (StringUtils.isNotBlank(getSearchTerms()))
						findingAidSearchListToUse = new FindingAidLogic().search(getSearchTerms(), null, null, aiId,
								getSearch(), getOrderBy(), getOrderDecreasing(), fileStates, getSearchStatuses(),
								isLinkedToHoldingsGuide());
					findingAidSelectedListToUse = findingAidDao.getFindingAids(aiId, tempIds, fileStates);
					log.info("Number of Finding Aids to delete from Europeana: " + findingAidListToUse.size());
					log.info("Number of Search Finding Aids to delete from Europeana: "
							+ findingAidSearchListToUse.size());
					log.info("Number of Selected Finding Aids to delete from Europeana: "
							+ findingAidSelectedListToUse.size());
					setHarvesting(0);
				}
			} else if (getType().equals("batchDoItAll")) {
				Set<String> fileStates = new HashSet<String>();
				fileStates.add(FileState.NEW);
				fileStates.add(FileState.NOT_VALIDATED_NOT_CONVERTED);
				fileStates.add(FileState.NOT_VALIDATED_CONVERTED);
				fileStates.add(FileState.VALIDATED_CONVERTED);
				fileStates.add(FileState.VALIDATED_NOT_CONVERTED);
				findingAidListToUse = findingAidDao.getFindingAids(aiId, fileStates);
				findingAidSelectedListToUse = findingAidDao.getFindingAids(aiId, tempIds, fileStates);
				log.info("Number of Finding Aids to convert, validate and index: " + findingAidListToUse.size());
				log.info("Number of Selected Finding Aids to convert, validate and index: "
						+ findingAidSelectedListToUse.size());
			} else {
				return SUCCESS;
			}

			ServletActionContext.getRequest().setAttribute("nbFindingAidForBatch", findingAidListToUse.size());
			ServletActionContext.getRequest().setAttribute("nbSelectedFindingAidForBatch",
					findingAidSelectedListToUse.size());
			ServletActionContext.getRequest().setAttribute("nbSearchFindingAidForBatch",
					findingAidSearchListToUse.size());
			ServletActionContext.getRequest().setAttribute("type", getType());
			ServletActionContext.getRequest().setAttribute("aiId", aiId);

			setNbFindingAidForBatch(findingAidListToUse.size());
			setNbSelectedFindingAidForBatch(findingAidSelectedListToUse.size());
			setNbSearchFindingAidForBatch(findingAidSearchListToUse.size());

			StringBuilder faIdArray = new StringBuilder("[");
			int count = 0;
			int size = findingAidListToUse.size();
			for (FindingAid fa : findingAidListToUse) {
				faIdArray.append(fa.getId());
				if (count < size - 1)
					faIdArray.append(",");
				count++;
			}
			faIdArray.append("]");

			StringBuilder faIdArray_small = new StringBuilder("[");
			count = 0;
			size = findingAidSelectedListToUse.size();
			for (FindingAid fa : findingAidSelectedListToUse) {
				faIdArray_small.append(fa.getId());
				if (count < size - 1)
					faIdArray_small.append(",");
				count++;
			}
			faIdArray_small.append("]");

			StringBuilder faIdArray_search = new StringBuilder("[");
			count = 0;
			size = findingAidSearchListToUse.size();
			for (FindingAid fa : findingAidSearchListToUse) {
				faIdArray_search.append(fa.getId());
				if (count < size - 1)
					faIdArray_search.append(",");
				count++;
			}
			faIdArray_search.append("]");

			session.setAttribute("faID", faIdArray.toString());
			session.setAttribute("faID_small", faIdArray_small.toString());
			session.setAttribute("faID_search", faIdArray_search.toString());

			log.debug("returning input");
			return INPUT;
		} else {
			log.warn("aiId from session is null?");
			return SUCCESS;
		}
	}

	/**
	 * This method removes from the lists those FAs which have ESE files
	 * published in Europeana
	 * 
	 * @param findingAidListToUse
	 * @param findingAidSelectedListToUse
	 */
	@SuppressWarnings("rawtypes")
	private void removeFAWhichHaveEsePublished(List<FindingAid> findingAidListToUse,
			List<FindingAid> findingAidSelectedListToUse) {
		if (ContentManager.isBeingHarvested()) {
			// Remove all the finding aids which have ESE files converted and
			// published
			Iterator iterator = findingAidListToUse.iterator();
			int numberOfFindingAidsSelected = findingAidSelectedListToUse.size();
			while (iterator.hasNext()) {
				FindingAid findingAid = (FindingAid) iterator.next();
				if (ContentManager.eadHasEsePublished(findingAid.getId())) {
					iterator.remove();
				}
			}
			iterator = findingAidSelectedListToUse.iterator();
			while (iterator.hasNext()) {
				FindingAid findingAid = (FindingAid) iterator.next();
				if (ContentManager.eadHasEsePublished(findingAid.getId())) {
					iterator.remove();
				}
			}
			int numberOfSuccesfullyFindingAidsSelected = findingAidSelectedListToUse.size();
			if (numberOfFindingAidsSelected > 0) {
				if (numberOfSuccesfullyFindingAidsSelected == 0) {
					setHarvesting(1);
				} else {
					setHarvesting(0);
				}
			} else {
				if (findingAidListToUse.size() > 0) {
					setHarvesting(0);
				} else {
					setHarvesting(1);
				}
			}
		} else {
			setHarvesting(0);
		}
	}

	/**
	 * (deleteFromIndex action). This method calls to deleteOnlyFromIndex in
	 * Content Manager and shows the content manager files.
	 * 
	 * @return SUCCESS or ERROR
	 */
	public String deleteOnlyFromIndex() {
		Integer aiId = getAiId();
		boolean isBeingHarvested = ContentManager.isBeingHarvested();
		try {
			if (!isBeingHarvested
					|| (isBeingHarvested && !(getXmlType().equals(XmlType.EAD_FA) && ContentManager
							.eadHasEsePublished(getId())))) {
				ContentManager.deleteOnlyFromIndex(getId(), getXmlType(), aiId, true);
				setHarvesting(0);
			} else {
				setHarvesting(1);
			}
			return finishAction();

		} catch (Exception e) {
			log.error("Error trying deleteOnlyFromIndex: " + e.getCause());
		}

		return ERROR;
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