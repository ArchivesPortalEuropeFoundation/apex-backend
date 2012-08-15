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
import eu.apenet.persistence.vo.IndexQueue;
import eu.apenet.persistence.vo.ResumptionToken;
import eu.apenet.persistence.vo.SourceGuide;
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
            IndexQueue indexQueue = ead.getIndexQueue();
            if(indexQueue != null ){
                eadcmUnit.setQueuePosition(indexQueue.getPosition());
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
		FindingAidLogic.deleteEseFiles(id);
	}
	
	/*
	public static String deleteEseFiles(Integer id) throws Exception {
		
		if (!isBeingHarvested()) {
			FindingAidLogic.deleteEseFiles(id);
			return Action.SUCCESS;
		}
		else {
			return Action.NONE;
		}
	}
	 */

	public static File deliverEseFiles(Integer id) throws Exception {
		FindingAidLogic fal = new FindingAidLogic();
		return fal.deliverEseFiles(id);
	}

    public static String deliverToEuropeana(int faId) {
		FindingAidDAO findingaidDao = DAOFactory.instance().getFindingAidDAO();
		FindingAid findingaid = findingaidDao.findById(faId);
		SecurityContext.get().checkAuthorized(findingaid);
		removeOldResumptionTokens();
		Ese ese = null;
		if(!isBeingHarvested()){
			EseDAO esesDao = DAOFactory.instance().getEseDAO();
			List<Ese> eses = esesDao.getEses(faId);
			if(eses!=null && !eses.isEmpty()){
				Iterator<Ese> iterator = eses.iterator();
				while(iterator.hasNext()){
					ese = iterator.next();
					if(ese.getEseState().getState().equals(EseState.NOT_PUBLISHED) || ese.getEseState().getState().equals(EseState.REMOVED)){
						ese.setEseState(DAOFactory.instance().getEseStateDAO().getEseStateByState(EseState.PUBLISHED));
						esesDao.update(ese);
					}
				}
				//Changing FA's state to "Indexed, delivered to Europeana"
				FileStateDAO fileStateDao = DAOFactory.instance().getFileStateDAO();

				FileState fileState = fileStateDao.getFileStateByState(FileState.INDEXED_DELIVERED_EUROPEANA);
				findingaid.setFileState(fileState);
				findingaidDao.store(findingaid);

			}else{
				return Action.ERROR;
			}
		}else{
			return Action.NONE;
		}
		return Action.SUCCESS;

	}

      
    public static void deleteFromEuropeana(int faId) throws Exception {
		FindingAidDAO findingaidDao = DAOFactory.instance().getFindingAidDAO();
		FindingAid findingaid = findingaidDao.findById(faId);
		SecurityContext.get().checkAuthorized(findingaid);
		//Change ese_state from ese to "Removed"
        Ese ese = null;
        EseDAO esesDao = DAOFactory.instance().getEseDAO();
        List<Ese> eses = esesDao.getEses(faId);
        if(eses!=null && !eses.isEmpty()){
            Iterator<Ese> iterator = eses.iterator();
            while(iterator.hasNext()){
                ese = iterator.next();
                if(ese.getEseState().getState().equals(EseState.PUBLISHED)){
                    ese.setEseState(DAOFactory.instance().getEseStateDAO().getEseStateByState(EseState.REMOVED));
                    esesDao.update(ese);
                }
            }
        }
        //Changing FA's state to "Indexed, converted to ESE/EDM"
        FileStateDAO fileStateDao = DAOFactory.instance().getFileStateDAO();
        FileState fileState=null;
        fileState = fileStateDao.getFileStateByState("Indexed_Converted to ESE/EDM");
        findingaid.setFileState(fileState);
        findingaidDao.store(findingaid);
	}


    /**
	 * Deletes all values in DDBB that has never been used again.
	 */
	public static void removeOldResumptionTokens() {
		ResumptionTokenDAO resumptionTokenDao = DAOFactory.instance().getResumptionTokenDAO();
		List<ResumptionToken> resumptionTokenList = resumptionTokenDao.getOldResumptionTokensThan(new Date());
		Iterator<ResumptionToken> iterator = resumptionTokenList.iterator();
		while(iterator.hasNext()){
			resumptionTokenDao.delete(iterator.next());
		}
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
		Xsd_enum schema = Xsd_enum.XSD_APE_SCHEMA;
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        Ead ead = eadDAO.findById(fileIdentifier, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		ArchivalInstitution archivalInstitution = ead.getArchivalInstitution();
        String filepath = APEnetUtilities.getConfig().getRepoDirPath() + ead.getPathApenetead();
        log.info("'" + archivalInstitution.getAiname() + "' is validating file: '" + ead.getEadid() + "' with id: '" + fileIdentifier + "'");
        File file = new File(filepath);

        try {
            /* Special for Spanish non UTF8 data */
            List<SAXParseException> exceptions = null;
            XMLStreamReader reader = null;
            try {
                reader = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream(file), "UTF-8");
                reader.next();
            } catch (Exception e){
                exceptions = new ArrayList<SAXParseException>();
                exceptions.add(new SAXParseException("The file is not UTF-8 - We will try to convert it right now to UTF-8 or make sure your file is UTF-8 before re-uploading. If not, the file will not pass the index step and will fail.", new LocatorImpl()));
                log.warn("ERROR - not UTF-8 ? - Trying to convert it to UTF-8");
                try {
                    simpleUtf8Conversion(APEnetUtilities.getDashboardConfig().getRepoDirPath() + ead.getPathApenetead(), archivalInstitution.getAiId());
                    exceptions = null;
                    log.trace("File converted to UTF-8, we try to validate like it would normally.");
                } catch (Exception ex){
                    exceptions.add(new SAXParseException("File could not be converted to UTF-8 using before.xsl", new LocatorImpl()));
                }
            } finally {
                if(reader != null)
                    reader.close();
            }
            /* End: Special for Spanish non UTF8 data */

            if(exceptions == null){
                exceptions = DocumentValidation.xmlValidation(new FileInputStream(file), schema);
            }
            FileStateDAO fileStateDAO = DAOFactory.instance().getFileStateDAO();
            if (exceptions != null) {
                StringBuilder warn = new StringBuilder();
                int count = 0;
                for (SAXParseException exception : exceptions) {
                    if((count++) % 2 == 0){
                        warn.append("<span class=\"colorwarning1\">");
                    }else{
                        warn.append("<span class=\"colorwarning2\">");
                    }
                    warn.append("l.").append(exception.getLineNumber()).append(" c.").append(exception.getColumnNumber()).append(": ").append(exception.getMessage()).append("</span>").append("<br />");
                }
                if (ead.getFileState().getState().equals(FileState.NEW))
                    ead.setFileState(fileStateDAO.getFileStateByState(FileState.NOT_VALIDATED_NOT_CONVERTED));
                else if (ead.getFileState().getState().equals(FileState.NOT_VALIDATED_CONVERTED))
                    ead.setFileState(fileStateDAO.getFileStateByState(FileState.VALIDATING_FINAL_ERROR));

                boolean warningExists = false;
                Set<Warnings> warningsFromEad = ead.getWarningses();
                WarningsDAO warningsDAO = null;
                if(!warningsFromEad.isEmpty()){
                	warningsDAO = DAOFactory.instance().getWarningsDAO();
                    for (Warnings warning : warningsFromEad) {
                        if (!warning.getIswarning()) {
                            warningExists = true;
                            warning.setAbstract_(warn.toString());
                            warningsDAO.update(warning);
                        }
                    }
                }
                if (!warningExists) {
                    Warnings warnings = new Warnings();
                    warnings.setAbstract_(warn.toString());
                    warnings.setIswarning(false);
                    if(warningsDAO==null){
                    	warningsDAO = DAOFactory.instance().getWarningsDAO();
                    }
                    warningsDAO.store(setCorrectWarning(ead, warnings));
                }

            } else {
                if (ead.getFileState().getState().equals(FileState.NEW)){
                    ead.setFileState(fileStateDAO.getFileStateByState(FileState.VALIDATED_NOT_CONVERTED));
                }else if (ead.getFileState().getState().equals(FileState.NOT_VALIDATED_CONVERTED)){
                    ead.setFileState(fileStateDAO.getFileStateByState(FileState.VALIDATED_CONVERTED));
                }
                Set<Warnings> warningsFromEad = ead.getWarningses();
                if(!warningsFromEad.isEmpty()){
                	WarningsDAO warningsDAO = DAOFactory.instance().getWarningsDAO();
                	warningsDAO = DAOFactory.instance().getWarningsDAO();
                	for (Warnings warning : warningsFromEad) {
                        if (!warning.getIswarning()){
                        	warningsDAO.delete(warning);
                        }
                    }
                }
            }
            eadDAO.update(ead);
        } catch (Exception e) {
            throw new APEnetException("Could not validate the file with ID: " + ead.getId(), e);
        }
	}

    private static Warnings setCorrectWarning(Ead ead, Warnings warnings){
        if(ead instanceof FindingAid)
            warnings.setFindingAid((FindingAid)ead);
        else if(ead instanceof HoldingsGuide)
            warnings.setHoldingsGuide((HoldingsGuide) ead);
        else if(ead instanceof SourceGuide)
            warnings.setSourceGuide((SourceGuide)ead);
        return warnings;
    }

    public static void simpleUtf8Conversion(String filePath, Integer aiId) throws APEnetException {
        try {
            File file = new File(filePath);
            String xslFilePath = APEnetUtilities.getDashboardConfig().getSystemXslDirPath() + APEnetUtilities.FILESEPARATOR + "before.xsl";

            InputStream in = new FileInputStream(file);
            File outputfile = new File(APEnetUtilities.getDashboardConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR +  aiId.toString() + APEnetUtilities.FILESEPARATOR + "converted_" + file.getName());
            TransformationTool.createTransformation(in, outputfile, FileUtils.openInputStream(new File(xslFilePath)), null, true, true, null, true, null);
            in.close();

            FileUtils.copyFile(outputfile, file);
	        outputfile.delete();
        } catch (Exception e){
            throw new APEnetException("Could not convert to UTF8");
        }
    }

    @Deprecated
    public static void convertToAPEnetEAD(int fileIdentifier) throws APEnetException {
        convertToAPEnetEAD(XmlType.EAD_FA, fileIdentifier, "default.xsl", null, null);
    }


	public static void convertToAPEnetEAD(XmlType xmlType, int fileIdentifier, String xslFileName, CounterCLevelCall counterCLevelCall, Map<String, String> conversionParameters) throws APEnetException {
		FileStateDAO fileStateDAO = DAOFactory.instance().getFileStateDAO();
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        Ead ead = eadDAO.findById(fileIdentifier, xmlType.getClazz());
		ArchivalInstitution archivalInstitution = ead.getArchivalInstitution();

		String mainagencycode = archivalInstitution.getRepositorycode();
		HashMap<String, String> parameters = new HashMap<String, String>();
        if(conversionParameters != null) {
            for(String key : conversionParameters.keySet())
                parameters.put(key, conversionParameters.get(key));
        }
		if (mainagencycode != null)
			parameters.put("mainagencycode", mainagencycode);
        String countryCode = archivalInstitution.getCountry().getIsoname();
        if(StringUtils.isNotEmpty(countryCode))
            parameters.put("countrycode", countryCode);
		String languageXmlPath = APEnetUtilities.getDashboardConfig().getSystemXslDirPath() + APEnetUtilities.FILESEPARATOR + "languages.xml";
		languageXmlPath = languageXmlPath.replaceAll("\\\\", "/");
		if (new File(languageXmlPath).exists())
			parameters.put("loclanguage", languageXmlPath);

        String filepath = APEnetUtilities.getConfig().getRepoDirPath() + ead.getPathApenetead();
        File file = new File(filepath);

        try {
            InputStream in;
            StringWriter xslMessages;
            File outputfile;
        	String tempDirOutputPath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR +  archivalInstitution.getAiId() + APEnetUtilities.FILESEPARATOR;
            if (xslFileName == null || xslFileName.equals("default.xsl")) {
                String tempOutputFilePath = tempDirOutputPath + "convert_" + fileIdentifier + "_.xml";
                File tempDirOutput = new File(tempDirOutputPath);
                if (!tempDirOutput.exists()) {
                	tempDirOutput.mkdirs();
                }
                File tempOutputFile = new File(tempOutputFilePath);
                String xslFilePath = APEnetUtilities.getDashboardConfig().getSystemXslDirPath() + APEnetUtilities.FILESEPARATOR + "before.xsl";
                log.info("'" + archivalInstitution.getAiname() + "' is converting file: '" + ead.getEadid() + "' with id: '" + fileIdentifier + "'");
                in = new FileInputStream(file);
                TransformationTool.createTransformation(in, tempOutputFile, new File(xslFilePath), null, true, true, null, true, null);

                xslFilePath = APEnetUtilities.getDashboardConfig().getSystemXslDirPath() + APEnetUtilities.FILESEPARATOR + "default.xsl";
                File xslFile = TransformationTool.modifyDefaultXslFile(APEnetUtilities.getDashboardConfig().getSystemXslDirPath() + APEnetUtilities.FILESEPARATOR, xslFilePath);

                in = new FileInputStream(tempOutputFile);
                outputfile = new File(tempDirOutputPath + "converted_" + file.getName());
                xslMessages = TransformationTool.createTransformation(in, outputfile, xslFile, parameters, true, true, null, true, counterCLevelCall);
                tempOutputFile.delete();
            } else {
                String xslFilePath = APEnetUtilities.getDashboardConfig().getXslDirPath() + APEnetUtilities.FILESEPARATOR + xslFileName;
                in = new FileInputStream(file);
                outputfile = new File(tempDirOutputPath + "converted_" + file.getName());
                xslMessages = TransformationTool.createTransformation(in, outputfile, FileUtils.openInputStream(new File(xslFilePath)), null, true, true, null, true, null);
            }

            StringBuilder xslWarnings = new StringBuilder();
            String[] xslWarningLines = xslMessages.toString().split("\n");

            int count = 0;
            for(String xslWarningLine : xslWarningLines){
                if((count++) % 2 == 0)
                    xslWarnings.append("<span class=\"colorwarning1\">");
                else
                    xslWarnings.append("<span class=\"colorwarning2\">");
                xslWarnings.append(xslWarningLine).append("</span>").append("<br/>");
            }

            boolean warningExists = false;
            Set<Warnings> warningsFromEad = ead.getWarningses();
            WarningsDAO warningsDAO = null;
            if(!warningsFromEad.isEmpty()){
            	warningsDAO = DAOFactory.instance().getWarningsDAO();
            	for (Warnings warning : warningsFromEad) {
                    if (warning.getIswarning()) {
                        warningExists = true;
                        warning.setAbstract_(xslWarnings.toString());
                        warningsDAO.update(warning);
                    } else {
                    	warningsDAO.delete(warning);
                    }
                }
            }
            if (!warningExists){
                Warnings warnings = new Warnings();
                warnings.setAbstract_(xslWarnings.toString());
                warnings.setIswarning(true);
                if(warningsDAO==null){
                	warningsDAO = DAOFactory.instance().getWarningsDAO();
                }
                warningsDAO.store(setCorrectWarning(ead, warnings));
            }

            ead.setFileState(fileStateDAO.getFileStateByState(FileState.NOT_VALIDATED_CONVERTED));
            eadDAO.update(ead);

            log.debug("Converted file takes name of original file.");
            FileUtils.copyFile(outputfile, file);
            outputfile.delete();
        } catch (Exception e) {
            throw new APEnetException("Could not convert the file with ID: " + ead.getId(), e);
        }
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