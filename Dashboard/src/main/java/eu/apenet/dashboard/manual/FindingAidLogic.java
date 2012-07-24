package eu.apenet.dashboard.manual;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.utils.ChangeControl;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.dashboard.utils.ZipManager;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.dao.FileStateDAO;
import eu.apenet.persistence.dao.FindingAidDAO;
import eu.apenet.persistence.dao.WarningsDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.EseState;
import eu.apenet.persistence.vo.FileState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.Warnings;

/**
 * This class manages all the logic of a FindingAidVO object extracted of DDBB
 */
public class FindingAidLogic extends EadLogicAbstract {

	private Logger log = Logger.getLogger(getClass());

	private Long size;

	public Long getSize(Integer aiId, Collection<String> fileStates, List<String> statuses, Boolean isLinkedWithHoldingsGuide) {
		if (this.size == null) {
			FindingAidDAO fadao = DAOFactory.instance().getFindingAidDAO();
			this.size = fadao.countFindingAids(aiId, fileStates, statuses, isLinkedWithHoldingsGuide);
		}
		return this.size;
	}

	/**
	 * This method returns only a finding aid with the identificator id
	 * 
	 * @param id
	 * @return FindingAidVO
	 */
	public FindingAid getFindingAid(Integer id) {
		return DAOFactory.instance().getFindingAidDAO().findById(id);
	}

	/**
	 * Delete test
	 * 
	 * @param fa
	 */
	public void delete(FindingAid fa) {
		FindingAidDAO fadao = DAOFactory.instance().getFindingAidDAO();
		fadao.delete(fa);
	}

	public void update(FindingAid fa) {

		FindingAidDAO fadao = DAOFactory.instance().getFindingAidDAO();
		fadao.store(fa);
	}

	public void insert(FindingAid fa) {
		FindingAidDAO fadao = DAOFactory.instance().getFindingAidDAO();
		fadao.store(fa);
	}

	public static void deleteEseFiles(Integer id) throws Exception {
		FindingAidDAO findingAidDao = DAOFactory.instance().getFindingAidDAO();
		FindingAid findingAid = findingAidDao.findById(id);
		SecurityContext.get().checkAuthorized(findingAid);
		// Remove all ese files related to the finding aid from ese table and
		// physically from the repository
		List<Ese> eseList = new ArrayList<Ese>();
		EseDAO eseDao = DAOFactory.instance().getEseDAO();
		eseList = eseDao.getEses(id);

		for (int i = 0; i < eseList.size(); i++) {
			Ese ese = eseList.get(i);

			ContentUtils.deleteFile(APEnetUtilities.getConfig().getRepoDirPath() + ese.getPath(), false);
			if (ese.getPathHtml() != null) {
				FileUtils.deleteDirectory(new File(APEnetUtilities.getConfig().getRepoDirPath() + ese.getPathHtml() + "dir"));
				ContentUtils.deleteFile(APEnetUtilities.getConfig().getRepoDirPath() + ese.getPathHtml(), false);
			}
			if(ese.getEseState().getState().equals(EseState.NOT_PUBLISHED)){
				eseDao.delete(ese);
			}else{
				ese.setPath(null);
				eseDao.update(ese);
			}
			// Register operation
			ChangeControl.logOperation(findingAid, ese, "Remove ese/edm");

		}
		FileStateDAO fileStateDao = DAOFactory.instance().getFileStateDAO();
		FileState fileState = fileStateDao.getFileStateByState(FileState.INDEXED);
		findingAid.setFileState(fileState);
		findingAidDao.store(findingAid);

	}

	public File deliverEseFiles(Integer aiId) throws Exception {
		FindingAidDAO findingAidDao = DAOFactory.instance().getFindingAidDAO();
		ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
		ArchivalInstitution archivalInstitution = aiDao.findById(aiId);
		String thecountry = archivalInstitution.getCountry().getIsoname();
		int instituion = archivalInstitution.getAiId();
		thecountry = thecountry.trim();
		String zipFile = thecountry + "-" + instituion + "-" + "-EUROPEANA-DELIVERY.zip";
		ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(APEnetUtilities.getDashboardConfig().getEuropeanaDirPath() + APEnetUtilities.FILESEPARATOR + zipFile));
		Set<String> fileStates = new HashSet<String>();
		fileStates.add(FileState.INDEXED_CONVERTED_EUROPEANA);
		fileStates.add(FileState.INDEXED_DELIVERED_EUROPEANA);
		fileStates.add(FileState.INDEXED_HARVESTED_EUROPEANA);
		//FindingAidLogic fal = new FindingAidLogic();
		List<FindingAid> findingAidListToUse = findingAidDao.getFindingAids(aiId, fileStates);
		// Remove all ese files related to the finding aid from ese table and
		// physically from the repository
		for (FindingAid findingAid : findingAidListToUse) {
			for (Ese ese : findingAid.getEses()) {
				if (ese.getPath() != null) {
					ZipManager.zip(ese.getPath(), new File(APEnetUtilities.getConfig().getRepoDirPath() + ese.getPath()),
							zipOutputStream);
				}
				// Register operation
				ChangeControl.logOperation(findingAid, ese, "Deliver to Europeana");

			}
		}
		zipOutputStream.close();
		return new File(APEnetUtilities.getDashboardConfig().getEuropeanaDirPath() + APEnetUtilities.FILESEPARATOR + zipFile);

	}

	/**
	 * Method which call to getMatchFindingAids to search in DDBB, it only
	 * return 4 results
	 * 
	 * @param searchTerms
	 * @param page
	 * @param ai
	 * @param option
	 * @return List<FindingAid>
	 */
	public List<FindingAid> search(String searchTerms, Integer page, Integer pageSize, Integer ai, Integer option,
			String orderBy, Boolean orderDecreasing, Collection<String> fileStates, List<String> statuses, Boolean isLinkedWithHoldingsGuide) {
		FindingAidDAO fadao = DAOFactory.instance().getFindingAidDAO();
		return fadao.searchFindingAids(searchTerms, ai, page, pageSize, option, orderBy, orderDecreasing, fileStates, statuses, isLinkedWithHoldingsGuide);
	}

	/**
	 * Method which count max results that query can shows
	 * 
	 * @param searchTerms
	 * @param ai
	 * @param option
	 * @return Long
	 */
	public Long countSearch(String searchTerms, Integer ai, Integer option, Collection<String> fileStates, List<String> statuses, Boolean isLinkedWithHoldingsGuide) {
		FindingAidDAO fadao = DAOFactory.instance().getFindingAidDAO();
		return fadao.countSearchFindingAids(searchTerms, ai, option, fileStates, statuses, isLinkedWithHoldingsGuide);
	}

	public void insert(Warnings warnings) {
		WarningsDAO warningsDao = DAOFactory.instance().getWarningsDAO();
		warningsDao.store(warnings);
	}

	public void update(Warnings warnings) {
		WarningsDAO warningsDao = DAOFactory.instance().getWarningsDAO();
		warningsDao.update(warnings);
	}

	public void delete(Warnings warnings) {
		WarningsDAO warningsDao = DAOFactory.instance().getWarningsDAO();
		warningsDao.delete(warnings);
	}
}