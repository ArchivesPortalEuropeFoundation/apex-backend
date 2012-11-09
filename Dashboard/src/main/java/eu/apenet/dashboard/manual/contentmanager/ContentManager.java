package eu.apenet.dashboard.manual.contentmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.LocatorImpl;

import com.opensymphony.xwork2.Action;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.manual.EADCMUnit;
import eu.apenet.dashboard.manual.EadLogicAbstract;
import eu.apenet.dashboard.manual.FindingAidLogic;
import eu.apenet.dashboard.manual.HoldingsGuideLogic;
import eu.apenet.dashboard.security.SecurityContext;

import eu.apenet.dpt.utils.service.DocumentValidation;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.util.Xsd_enum;
import eu.apenet.dpt.utils.util.extendxsl.CounterCLevelCall;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.CpfContentDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.dao.EseStateDAO;
import eu.apenet.persistence.dao.FileStateDAO;
import eu.apenet.persistence.dao.FindingAidDAO;
import eu.apenet.persistence.dao.ResumptionTokenDAO;
import eu.apenet.persistence.dao.WarningsDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CpfContent;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.EseState;
import eu.apenet.persistence.vo.FileState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.QueueItem;
import eu.apenet.persistence.vo.ResumptionToken;
import eu.apenet.persistence.vo.SourceGuide;
import eu.apenet.persistence.vo.ValidatedState;
import eu.apenet.persistence.vo.Warnings;


/**
 * This class is in charge of obtaining all the EAD files which belongs to a
 * partner and creating those Finding Aids or Holdings Guide objects needed to
 * display them on the screen when the partner clicks on Dashboard Content
 * Manager option. It extends from ContentManagerIndexer.
 */
public class ContentManager extends ContentManagerIndexer{

	private static final int MAX_TITLE = 120;

	private static final Logger log = Logger.getLogger(ContentManager.class);


//	
	public static Long getFindingAidsSize(Integer aiId, List<String> statuses, Boolean isLinkedWithHoldingsGuide) {
		return new FindingAidLogic().getSize(aiId, null, statuses, isLinkedWithHoldingsGuide);
	}
//
	public static Long getHoldingsGuideSize(Integer aiId) {
		return new HoldingsGuideLogic().getSize(aiId);
	}

    public static Long getEacCpfSize(int aiId){
        return DAOFactory.instance().getCpfContentDAO().countCpfContentByArchivalInstitution(aiId);
    }


	/**     
	 * This method is called in ContentManagerAction to do the "get 
	 * Finding Aid/Holdings Guide List" depending on the parameters.
	 * It returns a list of FindingAids casted to EADCMUnits
	 * 
	 * @param page
	 * @param aiId
	 * @return List<EADCMUnit>
	 * @throws SolrServerException
	 * @throws MalformedURLException
	 */
	public List<EADCMUnit> getEADFiles(Integer page, Integer aiId, XmlType xmlType, Integer limit, String orderBy, Boolean orderDecreasing, List<String> statuses, Boolean isLinkedToHoldingsGuide){
        log.debug("entering getEADFiles function");
		List<EADCMUnit> tempList = new ArrayList<EADCMUnit>();
		if(xmlType.equals(XmlType.EAD_FA)) {
            List<FindingAid> listFinding = DAOFactory.instance().getFindingAidDAO().searchFindingAids(null, aiId, page, limit, 0, orderBy, orderDecreasing, null, statuses, isLinkedToHoldingsGuide);
            for (FindingAid findingTemp : listFinding)
                tempList.add(convertEadToEADCMUnit(findingTemp, aiId));
		} else if(xmlType.equals(XmlType.EAD_HG) || xmlType.equals(XmlType.EAD_SG)) {
            List<Ead> listEad = DAOFactory.instance().getEadDAO().getEadPage(null, aiId, orderBy, orderDecreasing, page, limit, xmlType.getClazz());
            for(Ead ead : listEad)
                tempList.add(convertEadToEADCMUnit(ead, aiId));
        }
		return tempList;
	}

    public List<EADCMUnit> getCPFFiles(int pageNumber, int aiId, int limit){
        CpfContentDAO cpfContentDAO = DAOFactory.instance().getCpfContentDAO();
        List<CpfContent> cpfContents = cpfContentDAO.retrieveCpfContentByArchivalInstitution(pageNumber, limit, aiId);
        List<EADCMUnit> eadcmUnits = new ArrayList<EADCMUnit>();
        for(CpfContent cpfContent : cpfContents){
            eadcmUnits.add(convertCpfVOToEADCMUnit(cpfContent));
        }
        return eadcmUnits;
    }

    public static EADCMUnit convertEadToEADCMUnit(Ead ead, Integer aiId){
        log.debug("entering convertEadToEADCMUnit function");
        EADCMUnit eadcmUnit = new EADCMUnit();
        eadcmUnit.setEadCMUnitId(ead.getId());
		eadcmUnit.setEadCMUnitEadId(ead.getEadid());
        if(ead.getTitle().length() > MAX_TITLE)
			eadcmUnit.setEadCMUnitTitle(ead.getTitle().substring(0, MAX_TITLE) + "...");
		else
			eadcmUnit.setEadCMUnitTitle(ead.getTitle());

		eadcmUnit.setEadCMUnitListWarnings(ead.getWarningses());
		eadcmUnit.setEadCMUnitState(ead.getFileState().getId());
		eadcmUnit.setEadCMUnitUpDate(ead.getUploadDate());
        eadcmUnit.setEadCMUnitUpMeth(ead.getUploadMethod().getMethod());
        eadcmUnit.setNumberOfDAOs(ead.getTotalNumberOfDaos());
        eadcmUnit.setCountEadCMUnitDocs(ead.getTotalNumberOfUnits());

        if(ead.getFileState().getState().equals(FileState.READY_TO_INDEX)){
            QueueItem indexQueue = ead.getQueueItem();
            if(indexQueue != null ){
                eadcmUnit.setQueuePosition(-1);
	            String errors = indexQueue.getErrors();
	            if (errors!=null)
	            	eadcmUnit.setIndexError(errors);
            }
            else
                eadcmUnit.setQueuePosition(-1);
        }

        if(ead instanceof FindingAid) {
            log.debug("convertEadToEADCMUnit function is FA");
        	if(Arrays.asList(FileState.INDEXED_FILE_STATES).contains(ead.getFileState().getState())){
                long start = System.currentTimeMillis();
        		String title = DAOFactory.instance().getHoldingsGuideDAO().getLinkedHoldingsGuideTitleByFindingAidEadid(ead.getEadid(), aiId);
                log.debug("Find title of HG linked to FA in: " + (System.currentTimeMillis() - start) + " ms");
        		if(title!=null){ 
        			eadcmUnit.setEadCMUnitHolding(title);         		
        		}else{
        			eadcmUnit.setEadCMUnitHolding("No Holdings Guide");
        		}
        	}else{
                eadcmUnit.setEadCMUnitHolding("No Holdings Guide");
            }
        } else if(ead instanceof HoldingsGuide){
            if(ead.getFileState().getState().equals(FileState.INDEXED_LINKED)){
                eadcmUnit.setPossibleFindingAidsLinked(DAOFactory.instance().getCLevelDAO().countTotalCLevelsByHoldingsGuideId(ead.getId())); //max. number
                eadcmUnit.setFindingAidsLinked(DAOFactory.instance().getFindingAidDAO().countFindingAidsIndexedByHoldingsGuideId(ead.getId(), aiId, Arrays.asList(FileState.INDEXED_FILE_STATES))); //currently number
            }
        }
        log.debug("convertEadToEADCMUnit function return");
        return eadcmUnit;
    }

    public static EADCMUnit convertCpfVOToEADCMUnit(CpfContent cpfContent) {
		EADCMUnit eadcmUnit = new EADCMUnit();
		eadcmUnit.setEadCMUnitId(cpfContent.getId().intValue());
		eadcmUnit.setEadCMUnitEadId(cpfContent.getCpfId());
		eadcmUnit.setEadCMUnitTitle(cpfContent.getCpfId());
		eadcmUnit.setEadCMUnitListWarnings(null);
		eadcmUnit.setEadCMUnitState(null);
		eadcmUnit.setEadCMUnitUpDate(new Date());
		eadcmUnit.setEadCMUnitHolding(null);
		eadcmUnit.setEadCMUnitNumberThumbnails(null);
		eadcmUnit.setEadCMUnitUpMeth(null);
		eadcmUnit.setEadCMUnitIsCaching(null);
		eadcmUnit.setCountEadCMUnitDocs(null);
		eadcmUnit.setNumberOfDAOs(null);
		return eadcmUnit;
	}

	/**
	 * This method extract and returns a file by id from DDBB and FileSystem
	 * to be downloaded by user.
	 * @param id
	 * @param xmlType
     * @return InputStream
	 * @throws Exception
	 */
	public static File download(Integer id, XmlType xmlType) throws Exception {
		return EadLogicAbstract.download(id, xmlType);
	}

    public static InputStream getInputStream(Integer id) throws Exception {
        String cpfContentXml = DAOFactory.instance().getCpfContentDAO().findById(id.longValue()).getXml();
        return IOUtils.toInputStream(cpfContentXml);
	}

	public static Integer preview(Integer id, XmlType xmlType) {
        EadLogicAbstract.createPreviewHTML(id, xmlType);
        return id;
	}

	public static void deleteEseFiles(Integer id) throws Exception {
	}
	


	public static File deliverEseFiles(Integer id) throws Exception {
		return null;
	}

    public static String deliverToEuropeana(int faId) {

    	return null;
	}

      
    public static void deleteFromEuropeana(int faId) throws Exception {

	}




	/**
	 * This method calls to delete method of finding aid or holdings guide
	 * logic depends on holding parameter.
	 * 
	 * @param id
	 * @param xmlType
	 * @throws Exception
	 */
	public static void delete(Integer id, XmlType xmlType) throws Exception {
        EadLogicAbstract.delete(id, xmlType);
	}

	/**
	 * Method which talk with DDBB and format the results in a List<EADCMUnit>
	 * 
	 * @param searchTerms
	 * @param page
	 * @param xmlType
	 * @param orderDecreasing
	 * @return List<EADCMUnit>
	 * @throws SolrServerException
	 * @throws MalformedURLException
	 */
	public static EadResult search(String searchTerms, Integer page, Integer pageSize, Integer ai, XmlType xmlType,Integer option, String orderBy, Boolean orderDecreasing, List<String> statuses, Boolean isLinkedWithHoldingsGuide){
		EadResult result = new EadResult();
		List<EADCMUnit> temp = new ArrayList<EADCMUnit>();
		FindingAidDAO findingAidDAO = DAOFactory.instance().getFindingAidDAO();
		if (xmlType.equals(XmlType.EAD_FA)) {
			List<FindingAid> listFinding = findingAidDAO.searchFindingAids(searchTerms, ai, page, pageSize, option, orderBy, orderDecreasing, null, statuses, isLinkedWithHoldingsGuide);
			result.setTotalSize(findingAidDAO.countSearchFindingAids(searchTerms, ai, option, null, statuses, isLinkedWithHoldingsGuide));
            if (listFinding != null) {
				for(FindingAid findingAid : listFinding)
					temp.add(convertEadToEADCMUnit(findingAid, ai));
			}
		} else if(xmlType.equals(XmlType.EAD_SG) || xmlType.equals(XmlType.EAD_HG)){
			EadDAO eadDAO = DAOFactory.instance().getEadDAO();
            List<Ead> listEad = eadDAO.getMatchEads(searchTerms, ai, page, pageSize, orderBy, orderDecreasing, xmlType.getClazz());
            result.setTotalSize(eadDAO.getMatchCountEads(searchTerms, ai, xmlType.getClazz()));
            if (listEad != null) {
				for(Ead ead : listEad)
					temp.add(convertEadToEADCMUnit(ead, ai));
			}
        }
		result.setResults(temp);
		return result;
	}


	public List<String> getXslFiles(int aiId) {
		ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
		ArchivalInstitution archivalInstitution = archivalInstitutionDAO.findById(aiId);
		String countryIso = archivalInstitution.getCountry().getIsoname();
		String pathToXsl = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + countryIso
				+ APEnetUtilities.FILESEPARATOR + aiId + APEnetUtilities.FILESEPARATOR + "XSL"
				+ APEnetUtilities.FILESEPARATOR;
		File xslDir = new File(pathToXsl);
		List<String> xslFiles = new ArrayList<String>();
		xslFiles.add("default.xsl");
		if (xslDir.exists() && xslDir.isDirectory()) {
			String suffixes[] = { ".xsl", ".xslt" };
			xslFiles.addAll(Arrays.asList(xslDir.list(new SuffixFileFilter(suffixes))));
		}
		return xslFiles;
	}

	public static void APEnetEADValidate(XmlType xmlType, int fileIdentifier) throws APEnetException {

	}





    @Deprecated
    public static void convertToAPEnetEAD(int fileIdentifier) throws APEnetException {
    }


	public static void convertToAPEnetEAD(XmlType xmlType, int fileIdentifier, String xslFileName, CounterCLevelCall counterCLevelCall, Map<String, String> conversionParameters) throws APEnetException {
	}

	
	/**
	 * This method checks if Europeana (or any client) is harvesting APEnet OAI-PMH repository
	 * @return TRUE if Europeana (or any client) is harvesting APEnet OAI-PMH repository, FALSE otherwise
	 */
	public static boolean isBeingHarvested () {
		boolean result = true;
		ResumptionTokenDAO resumptionTokenDAO = DAOFactory.instance().getResumptionTokenDAO();
		List<ResumptionToken> listValidResumptionTokens = resumptionTokenDAO.getGreaterResumptionTokensThan(new Date());
		if(listValidResumptionTokens==null || listValidResumptionTokens.isEmpty()){
			result = false;
		}
		
		resumptionTokenDAO = null;
		listValidResumptionTokens = null;
		return result;
	}

	/**
	 * This method checks if a EAD has ESE files converted and published in Europeana
	 * 
	 * @param eadId The EAD's identifier
	 * @return TRUE if the EAD has ESE files converted and published in Europeana. FALSE otherwise
	 */
	public static boolean eadHasEsePublished(Integer eadId) {
		
		boolean result;
		
		// At the moment, only FA can have ESE files converted. Please, change this behavior if another kind of EAD can have
		// ESE files converted
		EseDAO eseDao = DAOFactory.instance().getEseDAO();
		EseStateDAO eseStateDao = DAOFactory.instance().getEseStateDAO();
		EseState eseState = eseStateDao.getEseStateByState(EseState.PUBLISHED);
		List<Ese> eseList = eseDao.getEsesByFindingAidAndState(eadId, eseState);
		if (eseList == null || eseList.isEmpty()) {
			result = false;
		}
		else {
			result = true;
		}
		
		eseDao = null;
		eseStateDao = null;
		eseState = null;
		return result;
	}
	public static class EadResult {
		private List<EADCMUnit> results;
		private long totalSize = 0;
		public List<EADCMUnit> getResults() {
			return results;
		}
		public void setResults(List<EADCMUnit> results) {
			this.results = results;
		}
		public long getTotalSize() {
			return totalSize;
		}
		public void setTotalSize(long totalSize) {
			this.totalSize = totalSize;
		}
		
	}
}