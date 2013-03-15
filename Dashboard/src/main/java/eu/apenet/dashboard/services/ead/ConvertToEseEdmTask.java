package eu.apenet.dashboard.services.ead;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.xml.sax.SAXException;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dpt.utils.ead2ese.EseConfig;
import eu.apenet.dpt.utils.ead2ese.EseFileUtils;
import eu.apenet.dpt.utils.ead2ese.XMLUtil;
import eu.apenet.dpt.utils.ead2ese.stax.ESEParser;
import eu.apenet.dpt.utils.ead2ese.stax.RecordParser;
import eu.apenet.dpt.utils.ese2edm.EdmConfig;
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
import eu.apenet.persistence.vo.ValidatedState;

public class ConvertToEseEdmTask extends AbstractEadTask {

	@Override
	protected String getActionName() {
		return "convert to ESE/EDM";
	}

	public static boolean valid(Ead ead) {
		if (ead instanceof FindingAid) {
			FindingAid findingAid = (FindingAid) ead;
			return ValidatedState.VALIDATED.equals(ead.getValidated())
					&& EuropeanaState.NOT_CONVERTED.equals(findingAid.getEuropeana());
		}
		return false;
	}

	private static final String COLON = ":";

	@Override
	protected void execute(Ead ead, Properties properties) throws Exception {
		if (valid(ead)) {
			FindingAid findingAid = (FindingAid) ead;

			try {
				EseDAO eseDao = DAOFactory.instance().getEseDAO();
				EadDAO eadDAO = DAOFactory.instance().getEadDAO();

				EseConfig eseConfig = new EseConfig(properties);
				File apenetEad = EseFileUtils.getRepoFile(APEnetUtilities.getConfig().getRepoDirPath(),
						findingAid.getPathApenetead());
				String xmlNameRelative = EseFileUtils.getFileName(APEnetUtilities.FILESEPARATOR, apenetEad);
				int lastIndex = xmlNameRelative.lastIndexOf('.');
				String eseOutputFilename = xmlNameRelative.substring(0, lastIndex) + "-ese"
						+ xmlNameRelative.substring(lastIndex);
				// String xmlOutputFilenameTemp =
				// FileUtils.getTempFile(findingAid,
				// xmlOutputFilename);
				File outputXMLDir = EseFileUtils.getOutputXMLDir(APEnetUtilities.getConfig().getRepoDirPath(),
						findingAid.getArchivalInstitution().getCountry().getIsoname(), findingAid
								.getArchivalInstitution().getAiId());
				File eseOutputFile = EseFileUtils.getFile(outputXMLDir, eseOutputFilename);
				// File xmlOutputFileTemp =
				// FileUtils.getTempFile(findingAid,
				// xmlOutputFilename);
				eseOutputFile.getParentFile().mkdirs();
				eseConfig.getTransformerXML2XML().transform(xmlNameRelative, apenetEad, eseOutputFile);
				int numberOfRecords = analyzeESEXML(xmlNameRelative, eseOutputFile);
				/*
				 * ESE2EDM stuff
				 */
				EdmConfig config = new EdmConfig(false);
				String edmTempOutputFilename = xmlNameRelative.substring(0, lastIndex) + "-edm-temp"
						+ xmlNameRelative.substring(lastIndex);
				File edmTempOutputFile = EseFileUtils.getFile(outputXMLDir, edmTempOutputFilename);
				config.getTransformerXML2XML().transform(eseOutputFile, edmTempOutputFile);
				config.setTransferToFileOutput(true);
				String edmOutputFilename = xmlNameRelative.substring(0, lastIndex) + "-edm"
						+ xmlNameRelative.substring(lastIndex);
				File edmOutputFile = EseFileUtils.getFile(outputXMLDir, edmOutputFilename);
				config.getTransformerXML2XML().transform(edmTempOutputFile, edmOutputFile);
				outputXMLDir.delete();
				edmTempOutputFile.delete();
				/*
				 * end of EDM stuf
				 */
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
							EseFileUtils.deleteDir(EseFileUtils.getRepoFile(APEnetUtilities.getConfig()
									.getRepoDirPath(), ese.getPathHtml()));
							ese.setPathHtml(null);
						}
					}
					// OAI Identifier will be built according to the next
					// syntax:
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
					ese.setPath(EseFileUtils.getRelativeESEFilePath(findingAid.getArchivalInstitution().getCountry()
							.getIsoname(), findingAid.getArchivalInstitution().getAiId(), edmOutputFilename));
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
					ese.setMetadataFormat(MetadataFormat.EDM);
					if (update) {
						eseDao.update(ese);
					} else {
						eseDao.store(ese);
					}
				}
				findingAid.setTotalNumberOfDaos(new Long(numberOfRecords));
				findingAid.setEuropeana(EuropeanaState.CONVERTED);
				eadDAO.store(findingAid);
				logAction(ead, true);
			} catch (Exception e) {
				logAction(ead, false);
				throw new APEnetException("Could not convert to ese/edm the file with ID: " + ead.getId(), e);
			}

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
