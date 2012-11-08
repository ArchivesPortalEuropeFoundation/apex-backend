/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.ead2ese;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dpt.utils.ead2ese.EseConfig;
import eu.apenet.dpt.utils.ead2ese.EseFileUtils;
import eu.apenet.dpt.utils.ead2ese.XMLUtil;
import eu.apenet.dpt.utils.ead2ese.stax.ESEParser;
import eu.apenet.dpt.utils.ead2ese.stax.RecordParser;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.dao.FileStateDAO;
import eu.apenet.persistence.dao.FindingAidDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.EseState;
import eu.apenet.persistence.vo.FileState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.MetadataFormat;

/**
 * 
 * @author bverhoef
 */
public class EAD2ESEConverter {
	private static final String COLON = ":";

	//private static final Logger LOGGER = Logger.getLogger(EAD2ESEConverter.class);
	public static void convertEAD2ESE(Integer findingAidId, EseConfig config) throws TransformerException, XMLStreamException, SAXException, IOException {
		EseDAO eseDao = DAOFactory.instance().getEseDAO();
		FindingAidDAO findingAidDAO = DAOFactory.instance().getFindingAidDAO();
		FileStateDAO fileStateDAO = DAOFactory.instance().getFileStateDAO();
		FindingAid findingAid = findingAidDAO.findById(findingAidId);
		File apenetEad = EseFileUtils.getRepoFile(APEnetUtilities.getConfig().getRepoDirPath(), findingAid.getPathApenetead());
		String xmlNameRelative = EseFileUtils.getFileName(APEnetUtilities.FILESEPARATOR, apenetEad);
		int lastIndex = xmlNameRelative.lastIndexOf('.');
		String xmlOutputFilename = xmlNameRelative.substring(0, lastIndex) + "-ese"
				+ xmlNameRelative.substring(lastIndex);
		// String xmlOutputFilenameTemp = FileUtils.getTempFile(findingAid,
		// xmlOutputFilename);
		File outputXMLDir = EseFileUtils.getOutputXMLDir(APEnetUtilities.getConfig().getRepoDirPath(), findingAid.getArchivalInstitution().getCountry().getIsoname(), findingAid.getArchivalInstitution().getAiId());
		File xmlOutputFile = EseFileUtils.getFile(outputXMLDir, xmlOutputFilename);
		//File xmlOutputFileTemp = FileUtils.getTempFile(findingAid, xmlOutputFilename);
			xmlOutputFile.getParentFile().mkdirs();
			config.getTransformerXML2XML().transform(xmlNameRelative, apenetEad, xmlOutputFile);
			int numberOfRecords = analyzeESEXML(xmlNameRelative, xmlOutputFile);
			boolean update = false;
			if (numberOfRecords > 0) {
				Ese ese = null;
				if(findingAid.getEses().isEmpty()){
					ese = new Ese();
					ese.setCreationDate(new Date());
				}else{
					ese = findingAid.getEses().iterator().next();
					update = true;
					if (ese.getPathHtml() != null) {
						EseFileUtils.deleteDir(EseFileUtils.getRepoFile(APEnetUtilities.getConfig().getRepoDirPath(), ese.getPathHtml()));
						ese.setPathHtml(null);
					}
				}
				// OAI Identifier will be built according to the next syntax: isoname/ai_id/fa_eadid
				String oaiIdentifier = findingAid.getArchivalInstitution().getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR + findingAid.getArchivalInstitution().getAiId() + APEnetUtilities.FILESEPARATOR + findingAid.getEadid();
				
//				Ese example = new Ese();
//				example.setOaiIdentifier(oaiIdentifier);
				List<Ese> esesToBeDeleted = eseDao.getEsesFromDeletedFindingaids(oaiIdentifier);
				EseState eseState;
				if (esesToBeDeleted.size() > 0){
					if(!update){
						for (Ese eseToBeDeleted: esesToBeDeleted){
							//FileUtils.deleteDir(FileUtils.getRepoFile(ese.getPathHtml()));
							eseDao.delete(eseToBeDeleted);
						}
						eseState = DAOFactory.instance().getEseStateDAO().getEseStateByState(EseState.REMOVED);
					}else{
						eseState = ese.getEseState();
					}
					ese.setModificationDate(new Date());
				}else {
					ese.setModificationDate(ese.getCreationDate());
					eseState = DAOFactory.instance().getEseStateDAO().getEseStateByState(EseState.NOT_PUBLISHED);
				}
				ese.setPath(EseFileUtils.getRelativeESEFilePath(findingAid.getArchivalInstitution().getCountry().getIsoname(), findingAid.getArchivalInstitution().getAiId(), xmlOutputFilename));
				ese.setOaiIdentifier(oaiIdentifier);
				ese.setNumberOfRecords(numberOfRecords);
				ese.setFindingAid(findingAid);
				ArchivalInstitution ai = findingAid.getArchivalInstitution();
				String eset =  "";
				while (ai != null){
					eset = COLON + ai.getAiId() + eset; 
					ai = ai.getParent();
				}
				eset = findingAid.getArchivalInstitution().getCountry().getIsoname() + eset;
				ese.setEseState(eseState);
				ese.setEset(eset);
                ese.setMetadataFormat(DAOFactory.instance().getMetadataFormatDAO().getMetadataFormatByName(MetadataFormat.ESE));
                if(update){
                	eseDao.update(ese);
                }else{
                	eseDao.store(ese);
                }
			}
			findingAid.setTotalNumberOfDaos(new Long(numberOfRecords));
			findingAid.setFileState(fileStateDAO.getFileStateByState(FileState.INDEXED_CONVERTED_EUROPEANA));
			findingAidDAO.store(findingAid);

	}

	public static void generateHtml(Ese ese) throws TransformerException {
		File eseFile = EseFileUtils.getRepoFile(APEnetUtilities.getConfig().getRepoDirPath(), ese.getPath());
		EseDAO eseDao = DAOFactory.instance().getEseDAO();
		String xmlNameRelative = EseFileUtils.getFileName(APEnetUtilities.FILESEPARATOR, eseFile);
		if (ese.getNumberOfRecords() > 0) {
			
			int numberOfFiles = 0;
			EseConfig config = new EseConfig();
				int numberOfRecords = ese.getNumberOfRecords();
				File htmlOutputFile = EseFileUtils.getFile(EseFileUtils.getOutputHTMLDir(APEnetUtilities.getConfig().getRepoDirPath(), ese.getFindingAid().getArchivalInstitution().getCountry().getIsoname(), ese.getFindingAid().getArchivalInstitution().getAiId()), xmlNameRelative + ".html");
				File htmlDirectory = htmlOutputFile.getParentFile();
				htmlDirectory.mkdirs();
				String htmlDirname = eseFile.getName() + ".htmldir";
				File htmlSubDirectory = EseFileUtils.getFile(htmlDirectory, htmlDirname);
				htmlSubDirectory.mkdirs();
				EseFileUtils.createDirs(htmlSubDirectory.getAbsolutePath(), numberOfRecords);
				config.getTransformerXML2HTML().setParameter("outputdir", htmlDirname);
				double temp = ese.getNumberOfRecords();
				int recordsPerDirectory = 100;
				int start = 1;
				while (temp > recordsPerDirectory) {
					temp = temp / recordsPerDirectory;
					start = start * recordsPerDirectory;
				}
				if (start == 1) {
					start = 100;
				}
				
				config.getTransformerXML2HTML().setParameter("startNumber", start);
				config.getTransformerXML2HTML().setParameter("eseId", ese.getEseId());
				config.getTransformerXML2HTML().setParameter("maxItemsPerDirectory", recordsPerDirectory);
				config.getTransformerXML2HTML().transform(xmlNameRelative, eseFile, htmlOutputFile);
				// URL url =
				// EAD2ESEConverter.class.getClassLoader().getResource("htmlsources");
				// FileUtils.copyDir(new File(url.getFile()), htmlSubDirectory,
				// config.getStatus());
				// add index html when generate html
				numberOfFiles++;
				numberOfFiles += numberOfRecords;
				ese.setPathHtml(EseFileUtils.getRelativeESEHTMLFilePath(ese.getFindingAid().getArchivalInstitution().getCountry().getIsoname(), ese.getFindingAid().getArchivalInstitution().getAiId(), xmlNameRelative + ".html"));
				eseDao.store(ese);


			
		} 

	}

	private static int  analyzeESEXML(String xmlNameRelative, File outputFile) throws XMLStreamException, SAXException, IOException {

			XMLStreamReader xmlReader = XMLUtil.getXMLStreamReader(outputFile);
			ESEParser parser = new ESEParser();
			RecordParser recordParser = new RecordParser();
			parser.registerParser(recordParser);
			// count number of records
			parser.parse(xmlReader, null);
			int numberOfRecords = recordParser.getNumberOfRecords();
			if (numberOfRecords == 0) {
				outputFile.delete();
			} else {
				XMLUtil.validateESE(outputFile);
			}

		return numberOfRecords;
	}



}
