package eu.apenet.dashboard.services.ead;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dpt.utils.ead2ese.EseConfig;
import eu.apenet.dpt.utils.ead2ese.EseFileUtils;
import eu.apenet.dpt.utils.ead2ese.XMLUtil;
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

	private static final String FA_XML_TYPE = "fa";

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


	@Override
	protected void execute(Ead ead, Properties properties) throws Exception {
		if (valid(ead)) {
			FindingAid findingAid = (FindingAid) ead;

			try {
				EadDAO eadDAO = DAOFactory.instance().getEadDAO();
				if (!(findingAid.isPublished() && findingAid.getTotalNumberOfDaos() == 0)) {

					EseDAO eseDao = DAOFactory.instance().getEseDAO();


					EseConfig eseConfig = new EseConfig(properties);
					File apenetEad = EseFileUtils.getRepoFile(APEnetUtilities.getConfig().getRepoDirPath(),
							findingAid.getPathApenetead());
					String xmlNameRelative = EseFileUtils.getFileName(APEnetUtilities.FILESEPARATOR, apenetEad);
					int lastIndex = xmlNameRelative.lastIndexOf('.');
					String eseOutputFilename = xmlNameRelative.substring(0, lastIndex) + "-ese"
							+ xmlNameRelative.substring(lastIndex);
					File outputESEDir = EseFileUtils.getOutputESEDir(APEnetUtilities.getConfig().getRepoDirPath(),
							findingAid.getArchivalInstitution().getCountry().getIsoname(), findingAid
									.getArchivalInstitution().getAiId());
					File eseOutputFile = EseFileUtils.getFile(outputESEDir, eseOutputFilename);
					eseOutputFile.getParentFile().mkdirs();
					eseConfig.getTransformerXML2XML().transform(xmlNameRelative, apenetEad, eseOutputFile);
					int numberOfRecords = XMLUtil.analyzeESEXML(eseOutputFile);

					boolean update = false;
					if (numberOfRecords > 1) {
						/*
						 * ESE2EDM stuff
						 */
						File outputEDMDir = EseFileUtils.getOutputEDMDir(APEnetUtilities.getConfig().getRepoDirPath(),
								findingAid.getArchivalInstitution().getCountry().getIsoname(), findingAid
										.getArchivalInstitution().getAiId());
						// OAI Identifier will be built according to the next
						// syntax:
						// NL-HaNA/fa/4.VTHR/edm
						String oaiIdentifier = findingAid.getArchivalInstitution().getRepositorycode()
								+ APEnetUtilities.FILESEPARATOR + FA_XML_TYPE
								+ APEnetUtilities.FILESEPARATOR + findingAid.getEadid();

						EdmConfig config = new EdmConfig(false);
						config.setEdmIdentifier(oaiIdentifier);
						config.setRepositoryCode(findingAid.getArchivalInstitution().getRepositorycode());
						config.setPrefixUrl("http://" + APEnetUtilities.getDashboardConfig().getDomainNameMainServer()
								+ "/web/guest/ead-display/-/ead/fp");
						config.setXmlTypeName(FA_XML_TYPE);
						String minimalConversion = eseConfig.getProperties().getProperty("minimalConversion");
						if (minimalConversion != null && !minimalConversion.isEmpty()) {
							config.setMinimalConversion(Boolean.valueOf(minimalConversion));
						} else {
							config.setMinimalConversion(eseConfig.isMinimalConversion());
						}
						String edmOutputFilename = xmlNameRelative.substring(0, lastIndex) + "-edm"
								+ xmlNameRelative.substring(lastIndex);
						File edmOutputFile = EseFileUtils.getFile(outputEDMDir, edmOutputFilename);
						config.getTransformerXML2XML().transform(eseOutputFile, edmOutputFile);
						eseOutputFile.delete();
						// edmTempOutputFile.delete();
						/*
						 * end of EDM stuf
						 */
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
							eseState = DAOFactory.instance().getEseStateDAO()
									.getEseStateByState(EseState.NOT_PUBLISHED);
						}
						ese.setPath(EseFileUtils.getRelativeEDMFilePath(findingAid.getArchivalInstitution()
								.getCountry().getIsoname(), findingAid.getArchivalInstitution().getAiId(),
								edmOutputFilename));
						ese.setOaiIdentifier(oaiIdentifier);
						ese.setNumberOfRecords(numberOfRecords);
						ese.setFindingAid(findingAid);
						ArchivalInstitution ai = findingAid.getArchivalInstitution();
						ese.setEseState(eseState);
						ese.setEset(ai.getRepositorycode());
						ese.setMetadataFormat(MetadataFormat.EDM);
						if (update) {
							eseDao.update(ese);
						} else {
							eseDao.store(ese);
						}
						findingAid.setTotalNumberOfChos(new Long(numberOfRecords));
						findingAid.setEuropeana(EuropeanaState.CONVERTED);
					} else {
						eseOutputFile.delete();
						findingAid.setEuropeana(EuropeanaState.NO_EUROPEANA_CANDIDATE);
						findingAid.setTotalNumberOfChos(0l);
					}
				} else {
					findingAid.setEuropeana(EuropeanaState.NO_EUROPEANA_CANDIDATE);
				}
				
				eadDAO.store(findingAid);
				logAction(ead);
			} catch (Exception e) {
				logAction(ead, e);
				throw new APEnetException(this.getActionName() + " " + e.getMessage(), e);
			}

		}
	}


}
