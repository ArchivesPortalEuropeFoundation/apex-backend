package eu.apenet.dashboard.services.ead;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.utils.PropertiesKeys;
import eu.apenet.dashboard.utils.PropertiesUtil;
import eu.apenet.dpt.utils.ead2edm.DigitalObjectCounter;
import eu.apenet.dpt.utils.ead2edm.EdmFileUtils;
import eu.apenet.dpt.utils.ead2edm.XMLUtil;
import eu.apenet.dpt.utils.ead2edm.EdmConfig;
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
import eu.apenet.persistence.vo.Warnings;

import java.io.IOException;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

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
                    && (EuropeanaState.NOT_CONVERTED.equals(findingAid.getEuropeana()) || EuropeanaState.FATAL_ERROR.equals(findingAid.getEuropeana()));
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

                    // OAI Identifier will be built according to the next
                    // syntax:
                    // NL-HaNA/fa/4.VTHR/edm
                    String oaiIdentifier = findingAid.getArchivalInstitution().getRepositorycode()
                            + APEnetUtilities.FILESEPARATOR + FA_XML_TYPE
                            + APEnetUtilities.FILESEPARATOR + findingAid.getEadid();

                    EdmConfig edmConfig = new EdmConfig(properties);
                    edmConfig.setEdmIdentifier(oaiIdentifier);
                    edmConfig.setRepositoryCode(findingAid.getArchivalInstitution().getRepositorycode());
                    edmConfig.setHost(PropertiesUtil.get(PropertiesKeys.APE_PORTAL_DOMAIN));
                    edmConfig.setXmlTypeName(FA_XML_TYPE);
                    edmConfig.setOutputBaseDirectory(EdmFileUtils.getOutputEDMDirPath(APEnetUtilities.getConfig().getRepoDirPath(),
                    findingAid.getArchivalInstitution().getCountry().getIsoname(), findingAid
                    .getArchivalInstitution().getAiId()));

                    File apenetEad = EdmFileUtils.getRepoFile(APEnetUtilities.getConfig().getRepoDirPath(),
                            findingAid.getPathApenetead());
//                    String xmlNameRelative = EdmFileUtils.getFileName(APEnetUtilities.FILESEPARATOR, apenetEad);
//                    int lastIndex = xmlNameRelative.lastIndexOf('.');
//                    String edmOutputFilename = xmlNameRelative.substring(0, lastIndex) + "-edm"
//                            + xmlNameRelative.substring(lastIndex);
                    File edmOutputDir = EdmFileUtils.getOutputEDMDir(edmConfig.getOutputBaseDirectory(), findingAid.getEadid());
//                    File edmOutputFile = EdmFileUtils.getFile(edmOutputDir, edmOutputFilename);
                    edmOutputDir.mkdirs();
                    boolean errors = false;
                    StringBuilder warn = new StringBuilder();
                    try {
                        edmConfig.getTransformerXML2XML().transform(apenetEad);
                    } catch (TransformerException e) {
                        errors = true;
                        warn.append("<span class=\"validation-error\">");
                        warn.append("EDM conversion error: ").append(e.getMessage()).append("</span>").append("<br />");
                        if (ead instanceof FindingAid) {
                            ((FindingAid) ead).setEuropeana(EuropeanaState.FATAL_ERROR);
                        }
                    }
                    if (errors) {
                        boolean warningExists = false;
                        Set<Warnings> warningsFromEad = ead.getWarningses();
                        if (!warningsFromEad.isEmpty()) {
                            for (Warnings warning : warningsFromEad) {
                                if (warning.getIswarning()) {
                                    warningExists = true;
                                    warning.setAbstract_(warn.toString());
                                } else {
                                    warningsFromEad.remove(warning);
                                }
                            }
                        }
                        if (!warningExists) {
                            Warnings warnings = new Warnings();
                            warnings.setAbstract_(warn.toString());
                            warnings.setIswarning(true);
                            warnings.setEad(ead);
                            ead.getWarningses().add(warnings);
                        }
                    } else {
                        DigitalObjectCounter digitalObjectCounter = XMLUtil.analyzeEdmXml(edmOutputDir);
                        int numberOfRecords = digitalObjectCounter.getNumberOfProvidedCHO();

                        boolean update = false;
                        if (numberOfRecords > 1) {
                            Ese ese = null;
                            if (findingAid.getEses().isEmpty()) {
                                ese = new Ese();
                                ese.setCreationDate(new Date());
                            } else {
                                ese = findingAid.getEses().iterator().next();
                                update = true;
                                if (ese.getPathHtml() != null) {
                                    EdmFileUtils.deleteDir(EdmFileUtils.getRepoFile(APEnetUtilities.getConfig()
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
                                    esesToBeDeleted.forEach((eseToBeDeleted) -> {
                                        // FileUtils.deleteDir(FileUtils.getRepoFile(ese.getPathHtml()));
                                        eseDao.delete(eseToBeDeleted);
                                    });
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
                            ese.setPath(EdmFileUtils.getRelativeEDMFilePath(findingAid.getArchivalInstitution()
                                    .getCountry().getIsoname(), findingAid.getArchivalInstitution().getAiId(),
                                    edmOutputDir.getName()));
                            ese.setOaiIdentifier(oaiIdentifier);
                            ese.setNumberOfRecords(numberOfRecords);
                            ese.setNumberOfWebResource(digitalObjectCounter.getNumberOfWebResource());
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
                            findingAid.setTotalNumberOfWebResourceEdm(new Long(digitalObjectCounter.getNumberOfWebResource()));
                            findingAid.setEuropeana(EuropeanaState.CONVERTED);
                        } else {
                            edmOutputDir.delete();
                            findingAid.setEuropeana(EuropeanaState.NO_EUROPEANA_CANDIDATE);
                            findingAid.setTotalNumberOfChos(0l);
                        }
                    }
                } else {
                    findingAid.setEuropeana(EuropeanaState.NO_EUROPEANA_CANDIDATE);
                }

                eadDAO.store(findingAid);
                logAction(ead);
            } catch (IOException | XMLStreamException | SAXException e) {
                logAction(ead, e);
                throw new APEnetException(this.getActionName() + " " + e.getMessage(), e);
            }

        }
    }

}
