package eu.apenet.dashboard.services.ead;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.xml.sax.SAXException;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dpt.utils.ead2ese.EseConfig;
import eu.apenet.dpt.utils.ead2ese.FileUtils;
import eu.apenet.dpt.utils.ead2ese.XMLUtil;
import eu.apenet.dpt.utils.ead2ese.stax.ESEParser;
import eu.apenet.dpt.utils.ead2ese.stax.RecordParser;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.EseState;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.MetadataFormat;

public class ConvertToEseTask extends AbstractEadTask {
	private static final String COLON = ":";
	@Override
	protected void execute(Ead ead, Properties properties) throws Exception {
		if (ead instanceof FindingAid) {
			EseDAO eseDao = DAOFactory.instance().getEseDAO();
			EadDAO eadDAO = DAOFactory.instance().getEadDAO();
			FindingAid findingAid = (FindingAid) ead;
			EseConfig eseConfig = new EseConfig(properties);
			File apenetEad = FileUtils.getRepoFile(APEnetUtilities.getConfig().getRepoDirPath(),
					findingAid.getPathApenetead());
			String xmlNameRelative = FileUtils.getFileName(APEnetUtilities.FILESEPARATOR, apenetEad);
			int lastIndex = xmlNameRelative.lastIndexOf('.');
			String xmlOutputFilename = xmlNameRelative.substring(0, lastIndex) + "-ese"
					+ xmlNameRelative.substring(lastIndex);
			// String xmlOutputFilenameTemp = FileUtils.getTempFile(findingAid,
			// xmlOutputFilename);
			File outputXMLDir = FileUtils.getOutputXMLDir(APEnetUtilities.getConfig().getRepoDirPath(), findingAid
					.getArchivalInstitution().getCountry().getIsoname(), findingAid.getArchivalInstitution().getAiId());
			File xmlOutputFile = FileUtils.getFile(outputXMLDir, xmlOutputFilename);
			// File xmlOutputFileTemp = FileUtils.getTempFile(findingAid,
			// xmlOutputFilename);
			xmlOutputFile.getParentFile().mkdirs();
			eseConfig.getTransformerXML2XML().transform(xmlNameRelative, apenetEad, xmlOutputFile);
			int numberOfRecords = analyzeESEXML(xmlNameRelative, xmlOutputFile);
			boolean update = false;
			if (numberOfRecords > 0) {
				Ese ese = null;
				if (findingAid.getEses().isEmpty()) {
					ese = new Ese();
					ese.setCreationDate(new Date());
				} else {
					ese = findingAid.getEses().iterator().next();
					update = true;
					if (ese.getPathHtml() != null) {
						FileUtils.deleteDir(FileUtils.getRepoFile(APEnetUtilities.getConfig().getRepoDirPath(),
								ese.getPathHtml()));
						ese.setPathHtml(null);
					}
				}
				// OAI Identifier will be built according to the next syntax:
				// isoname/ai_id/fa_eadid
				String oaiIdentifier = findingAid.getArchivalInstitution().getCountry().getIsoname()
						+ APEnetUtilities.FILESEPARATOR + findingAid.getArchivalInstitution().getAiId()
						+ APEnetUtilities.FILESEPARATOR + findingAid.getEadid();

				// Ese example = new Ese();
				// example.setOaiIdentifier(oaiIdentifier);
				List<Ese> esesToBeDeleted = eseDao.getEsesFromDeletedFindingaids(oaiIdentifier);
				EseState eseState;
				if (esesToBeDeleted.size() > 0) {
					if (!update) {
						for (Ese eseToBeDeleted : esesToBeDeleted) {
							// FileUtils.deleteDir(FileUtils.getRepoFile(ese.getPathHtml()));
							eseDao.delete(eseToBeDeleted);
						}
						eseState = DAOFactory.instance().getEseStateDAO().getEseStateByState(EseState.REMOVED);
					} else {
						eseState = ese.getEseState();
					}
					ese.setModificationDate(new Date());
				} else {
					ese.setModificationDate(ese.getCreationDate());
					eseState = DAOFactory.instance().getEseStateDAO().getEseStateByState(EseState.NOT_PUBLISHED);
				}
				ese.setPath(FileUtils.getRelativeESEFilePath(findingAid.getArchivalInstitution().getCountry()
						.getIsoname(), findingAid.getArchivalInstitution().getAiId(), xmlOutputFilename));
				ese.setOaiIdentifier(oaiIdentifier);
				ese.setNumberOfRecords(numberOfRecords);
				ese.setFindingAid(findingAid);
				ArchivalInstitution ai = findingAid.getArchivalInstitution();
				String eset = "";
				while (ai != null) {
					eset = COLON + ai.getAiId() + eset;
					ai = ai.getParent();
				}
				eset = findingAid.getArchivalInstitution().getCountry().getIsoname() + eset;
				ese.setEseState(eseState);
				ese.setEset(eset);
				ese.setMetadataFormat(DAOFactory.instance().getMetadataFormatDAO()
						.getMetadataFormatByName(MetadataFormat.ESE));
				if (update) {
					eseDao.update(ese);
				} else {
					eseDao.store(ese);
				}
			}
			findingAid.setTotalNumberOfDaos(new Long(numberOfRecords));
			findingAid.setEuropeana(EuropeanaState.CONVERTED);
			eadDAO.store(findingAid);
		}
	}

	private static int analyzeESEXML(String xmlNameRelative, File outputFile) throws XMLStreamException, SAXException,
			IOException {

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
		xmlReader.close();
		return numberOfRecords;
	}
}
