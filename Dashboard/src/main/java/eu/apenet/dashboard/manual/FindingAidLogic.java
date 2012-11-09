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

}