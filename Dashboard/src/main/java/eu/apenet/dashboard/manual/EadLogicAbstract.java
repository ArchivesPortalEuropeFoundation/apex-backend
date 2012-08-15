package eu.apenet.dashboard.manual;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.exceptions.APEnetRuntimeException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.indexing.EADParser;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.utils.ChangeControl;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.*;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.apenet.persistence.vo.*;

import java.io.File;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public abstract class EadLogicAbstract {
//	abstract Boolean delete(Integer id) throws Exception ;
	private static Logger log = Logger.getLogger(EadLogicAbstract.class);

    public static File download(Integer id, XmlType xmlType) {
		Ead ead = DAOFactory.instance().getEadDAO().findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		String path = APEnetUtilities.getConfig().getRepoDirPath() + ead.getPathApenetead();
		try {
            File file = new File(path);
	        if(file.exists())
	            return file;
		} catch(Exception e) {
			log.error("Download function error, trying to open the file '" + path + "'", e);
		}
		return null;
    }

    public static void createPreviewHTML(Integer id, XmlType xmlType){
        Ead ead = DAOFactory.instance().getEadDAO().findById(id, xmlType.getClazz());
        if(ead.getEadContent() == null){
            try {
                EADParser.parseEad(ead);
            } catch (Exception e) {
                throw new APEnetRuntimeException(e);
            }
        }
    }

    /**
     * The isOverwrite boolean is set to false, so the next parameters can be set to anything, they will never be used
     * @param id identifier of the file within the DB
     * @param xmlType the type of XML of this file
     * @return boolean if it works or not
     */
    public static boolean delete(Integer id, XmlType xmlType) {
        return deleteOrOverwrite(id, xmlType, false, null, false, null, -1);
    }

    public static boolean deleteOrOverwrite(Integer id, XmlType xmlType, boolean isOverwrite, FileUnit fileUnit, boolean isConverted, String title, int archivalInstitutionId) {
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        Ead ead = eadDAO.findById(id, xmlType.getClazz());

        String filePath = ead.getPathApenetead();
        String dirPath = filePath.substring(0,(filePath.lastIndexOf(APEnetUtilities.FILESEPARATOR) + 1));
        String filePathBeginning = filePath.substring(0, filePath.lastIndexOf(APEnetUtilities.FILESEPARATOR) + 1);
        String filePathEnd = filePath.substring(filePath.lastIndexOf(APEnetUtilities.FILESEPARATOR) + 1);

        List<String> eseRenamed = new ArrayList<String>();
        List<String> eseHtmlRenamed = new ArrayList<String>();

        Set<Ese> eseList = null;
        
        try {
            try {
                /// DELETE FROM DATABASE ///
                HibernateUtil.beginDatabaseTransaction();

                WarningsDAO warningsDao = DAOFactory.instance().getWarningsDAO();
                Set<Warnings> warningsList = ead.getWarningses();
                for (Warnings warnings : warningsList) {
                    log.debug("Removing warnings for EAD which EADID is " + ead.getEadid());
                    warningsDao.deleteSimple(warnings);
                }

                if(ead instanceof FindingAid) {
                    EseDAO eseDao = DAOFactory.instance().getEseDAO();
                    EseStateDAO eseStateDao = DAOFactory.instance().getEseStateDAO();
                    eseList = ((FindingAid)ead).getEses();
                    for(Ese ese : eseList) {
                        log.debug("Removing ESE for EAD which EADID is " + ead.getEadid());
                        if(ese.getEseState().getState().equals(EseState.PUBLISHED) || ese.getEseState().getState().equals(EseState.REMOVED)) {
                            ese.setFindingAid(null);
                            ese.setPath(null);
                            ese.setEseState(eseStateDao.getEseStateByState(EseState.REMOVED));
                            eseDao.updateSimple(ese);
                        } else if(ese.getEseState().getState().equals(EseState.NOT_PUBLISHED)) {
                            eseDao.deleteSimple(ese);
                        }
                    }
                }

                log.debug("Removing EAD which EADID is " + ead.getEadid());
                eadDAO.deleteSimple(ead);

                FileState fileState = ead.getFileState();
                if ((fileState.getId() > 7) && (fileState.getId() < 15)) {
                    log.debug("Removing the EAD which eadid is " + ead.getEadid() + " from the index");
                    ContentUtils.deleteFromIndex(ead.getEadid(), ead.getArchivalInstitution().getAiId());
                    if(ead instanceof HoldingsGuide)
                        ContentUtils.removeHoldingsGuideFromArchivalLandscape((HoldingsGuide)ead);
                }

                if(isOverwrite) {
                    UpFileDAO upFileDAO = DAOFactory.instance().getUpFileDAO();
                    //Remove new finding aid recently uploaded from up_file table
                    log.debug("Removing recently uploaded EAD which EADID is " + fileUnit.getEadid() + " from up_table");
                    UpFile upFile = upFileDAO.findById(fileUnit.getFileId());
                    upFileDAO.deleteSimple(upFile);

                    /// STORING THE NEW FINDING AID IN DATABASE ///

                    Ead newEad = null;
                    if(ead instanceof FindingAid)
                        newEad = new FindingAid();
                    else if(ead instanceof HoldingsGuide)
                        newEad = new HoldingsGuide();
                    else if(ead instanceof SourceGuide)
                        newEad = new SourceGuide();

                    ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().findById(archivalInstitutionId);
                    newEad.setArchivalInstitution(archivalInstitution);
                    String countryIso = archivalInstitution.getCountry().getIsoname().trim();

                    newEad.setEadid(fileUnit.getEadid());
                    newEad.setTitle(title);
                    newEad.setUploadDate(new Date());
                    newEad.setPathApenetead(APEnetUtilities.FILESEPARATOR + countryIso + APEnetUtilities.FILESEPARATOR + archivalInstitutionId + APEnetUtilities.FILESEPARATOR + fileUnit.getFileName());

                    FileStateDAO fileStateDAO = DAOFactory.instance().getFileStateDAO();
                    if (isConverted) {
                        log.debug("File already converted in local tool");
                        newEad.setFileState(fileStateDAO.getFileStateByState(FileState.NOT_VALIDATED_CONVERTED));
                    } else {
                        log.debug("File not converted in local tool");
                        newEad.setFileState(fileStateDAO.getFileStateByState(FileState.NEW));
                    }
                    newEad.setUploadMethod(upFile.getUploadMethod());
                    eadDAO.insertSimple(newEad);
                }
                
                
            } catch (Exception e){
                HibernateUtil.rollbackDatabaseTransaction();
                log.error("There were errors during Database Transaction while deleting the file '" + ead.getEadid() + "' in the repository [Database Rollback]. Error: " + e.getMessage());
                throw new APEnetException("In order to not go through the rest of the function", e);
            }

            try {
                log.debug("Renaming the EAD which eadid is " + ead.getEadid() + " from " + APEnetUtilities.getConfig().getRepoDirPath() + dirPath);
                ContentUtils.renameFileToRemove(APEnetUtilities.getConfig().getRepoDirPath() + ead.getPathApenetead());  //dirPath normally

                if(ead instanceof FindingAid) {
                    // Rename all ESE physically from the repository to _remove...
                    for (Ese ese : eseList) {
                        log.debug("Renaming the ESE file " + ese.getPath());
                        eseRenamed.add(ContentUtils.renameFileToRemove(APEnetUtilities.getConfig().getRepoDirPath() + ese.getPath()));
                        log.debug("Renaming the HTML ESE file " + ese.getPathHtml());
                        eseHtmlRenamed.add(ContentUtils.renameFileToRemove(APEnetUtilities.getConfig().getRepoDirPath() + ese.getPathHtml()));
                        log.debug("Renaming the ESE-HTML folder " + ese.getPathHtml() + "dir");
                        File htmlDir = new File (APEnetUtilities.getConfig().getRepoDirPath() + ese.getPathHtml() + "dir");
                        if (htmlDir.exists()) {
                            ContentUtils.renameFileToRemove(APEnetUtilities.getConfig().getRepoDirPath() + ese.getPathHtml() + "dir");
                            ese.setPathHtml(null);
                        }
                        ChangeControl.logOperation((FindingAid)ead, ese, "Remove ese/edm");
                    }
                }

                if(isOverwrite) {
                    /// STORING THE NEW FINDING AID IN THE FILE SYSTEM ///
                    File destFile = new File(APEnetUtilities.getDashboardConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitutionId + APEnetUtilities.FILESEPARATOR + fileUnit.getFileName());
                    File srcFile = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitutionId + APEnetUtilities.FILESEPARATOR + fileUnit.getFileName());

                    // Copy the file from up repository to temp repository and rename the file in up repository to _remove...
                    FileUtils.copyFile(srcFile, destFile);
                    ContentUtils.renameFileToRemove(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitutionId + APEnetUtilities.FILESEPARATOR + fileUnit.getFileName());
                }
            } catch (Exception e) {
                HibernateUtil.rollbackDatabaseTransaction();

                FileState fileState = ead.getFileState();
                if((fileState.getId() > 7) && (fileState.getId() < 15)) {
                    try {
                        ContentUtils.indexRollback(XmlType.getEadType(ead), ead.getId());
                    } catch (Exception ex) {
                        log.error("FATAL ERROR. Error during Index Rollback [Re-indexing process because of the rollback]. The file affected is '" + ead.getEadid() + "'. Error:" + ex.getMessage());
                    }

                    if(ead instanceof HoldingsGuide) {
                        try {
                            ContentUtils.addLinkHGtoAL((HoldingsGuide)ead);
                        }catch(Exception excep){
                            log.error("FATAL ERROR. Error during re-link with Archival Landscape: "+excep.getMessage());
                        }
                    }

                    //Due to the re-indexing of this FA, the current state is "Indexed_Not Converted to ESE/EDM". It is necessary to restore the original state if the original state is different from "Indexed_Not Converted to ESE/EDM"
                    if(fileState.getId() != 8) {
                        try {
                            ContentUtils.restoreOriginalStateOfEAD(ead);
                        } catch (Exception ex) {
                            log.error("Error restoring the original state of the EAD. Check Database");
                        }
                    }
                }

                //Rename the FA to the original name in temporal repository or final repository
                try {
                    if(ead instanceof FindingAid) {
                        dirPath = filePath.substring(0, (filePath.lastIndexOf(APEnetUtilities.FILESEPARATOR) + 1));
                        dirPath = dirPath.substring(0, (dirPath.length() - 1));
                        dirPath = dirPath.substring(0, dirPath.lastIndexOf(APEnetUtilities.FILESEPARATOR) + 1);
                        ContentUtils.rollbackRenameFileFromRemove(APEnetUtilities.getConfig().getRepoDirPath() + dirPath + "_remove" + filePathEnd.substring(0, filePathEnd.lastIndexOf(".")));
                    } else { //HG and SG
                        ContentUtils.rollbackRenameFileFromRemove(APEnetUtilities.getConfig().getRepoDirPath() + filePathBeginning + "_remove" + filePathEnd);
                    }
                } catch (Exception ex) {
                    log.error("FATAL ERROR. Error during File system Rollback renaming the file " + APEnetUtilities.getConfig().getRepoDirPath() + dirPath + "_remove" + filePathEnd);
                }

                if(isOverwrite) {
                    try {
                        ContentUtils.rollbackRenameFileFromRemove(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitutionId + APEnetUtilities.FILESEPARATOR + "_remove" + filePathEnd);
                    } catch (Exception ex) {
                        log.error("FATAL ERROR. Error during File system Rollback renaming the file " + APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitutionId + APEnetUtilities.FILESEPARATOR + "_remove" + filePathEnd);
                    }
                }

                if(ead instanceof FindingAid) {
                    for (String anEseRenamed : eseRenamed) {
                        File eseFile = new File(anEseRenamed);
                        if (eseFile.exists()) {
                            try {
                                ContentUtils.rollbackRenameFileFromRemove(anEseRenamed);
                            } catch (Exception ex) {
                                log.error("FATAL ERROR. Error during File system Rollback renaming the file " + anEseRenamed);
                            }
                        }
                    }
                    for (String anEseHtmlRenamed : eseHtmlRenamed) {
                        File eseFile = new File(anEseHtmlRenamed);
                        if (eseFile.exists()) {
                            try {
                                ContentUtils.rollbackRenameFileFromRemove(anEseHtmlRenamed);
                            } catch (Exception ex) {
                                log.error("FATAL ERROR. Error during File system Rollback renaming the file " + anEseHtmlRenamed);
                            }
                        }
                        File eseHtmlDir = new File(anEseHtmlRenamed + "dir");
                        if (eseHtmlDir.exists()) {
                            try {
                                ContentUtils.rollbackRenameFileFromRemove(anEseHtmlRenamed + "dir");
                            } catch (Exception ex) {
                                log.error("FATAL ERROR. Error during File system Rollback renaming the file " + anEseHtmlRenamed + "dir");
                            }
                        }
                    }
                }
                log.error("There were errors during File system Transaction while removing the file "+ ead.getEadid() + " in the repository [Database Rollback, Index Rollback and File system Rollback]. Error: "+ e.getMessage());
                throw new APEnetException("In order to not go through the rest of the function", e);
            }

            try {
                HibernateUtil.commitDatabaseTransaction();

                log.info("The EAD which eadid is " + ead.getEadid() + " has been remove from the repository");
                ChangeControl.logOperation(ead, "Remove EAD");

                ContentUtils.deleteFile(APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + filePathBeginning + "_remove" + filePathEnd);

                if(ead instanceof FindingAid) {
                    // Remove all _remove ESE physically from the repository if exits and ese has never published
                    for (String anEseRenamed : eseRenamed) {
                        // Remove ESE file
                        File eseFile = new File(anEseRenamed);
                        if (eseFile.exists())
                            FileUtils.forceDelete(eseFile);
                    }
                    for (String anEseHtmlRenamed : eseHtmlRenamed) {
                        // Remove _remove HTML files related to ESE files removed and subfolders ESE-HTML
                        File eseFile = new File(anEseHtmlRenamed);
                        if (eseFile.exists())
                            FileUtils.forceDelete(eseFile);
                        File eseHtmlDir = new File(anEseHtmlRenamed + "dir");
                        if (eseHtmlDir.exists())
                            FileUtils.forceDelete(eseHtmlDir);
                    }
                }

                if(isOverwrite) { //todo: Check if it works fine overwrite.
                    File srcFile = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitutionId + APEnetUtilities.FILESEPARATOR + "_remove" + fileUnit.getFileName());
                    File uploadDir = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitutionId + APEnetUtilities.FILESEPARATOR);
    
                    if (srcFile.exists())
                        FileUtils.forceDelete(srcFile);
    
                    if (uploadDir.listFiles().length == 0)
                        FileUtils.forceDelete(uploadDir);
                }
            } catch (Exception e) {
                log.error("FATAL ERROR. Error during Database, Index or File System commits. Please, check inconsistencies in Database, Index and File system for file which eadid is: " + ead.getEadid() + " and whose archival institution is " + ead.getArchivalInstitution().getAiname());
                throw new APEnetException("In order to not go through the rest of the function", e);
            }
            
        } catch (APEnetException e) {
            log.error("Problem while deleting file", e);
            return false;
        }
        return true;
    }
}
