package eu.apenet.dashboard.interceptors;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.apenet.persistence.vo.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * User: Yoann Moranville
 * Date: 15/05/2012
 *
 * @author Yoann Moranville
 */
public class FixDatabaseAndFilesInterceptor extends AbstractInterceptor {
    private static final Logger LOG = Logger.getLogger(FixDatabaseAndFilesInterceptor.class);

    @Override
    public void init() {
        prepareDbForNewDashboard();
    }

    @Override
    public String intercept(ActionInvocation actionInvocation) throws Exception {
        return actionInvocation.invoke();
    }

    private void prepareDbForNewDashboard() {
        LOG.info("We check if some DB entries in FA, HG or SG contains still occurrences related to old TMP directory instead of REPO directory"); //For this, it is simply if path starts with a AI id then it is an old TMP path, if it starts with a country abbr it is a correct REPO path

        List<ArchivalInstitution> archivalInstitutions = DAOFactory.instance().getArchivalInstitutionDAO().findAll();
        try {
            for(ArchivalInstitution archivalInstitution : archivalInstitutions) {
                Set<SourceGuide> sourceGuides = archivalInstitution.getSourceGuides();
                for(SourceGuide sourceGuide : sourceGuides) {
                    if(isPathOld(sourceGuide.getPathApenetead())) {
                    	changeOldPath(sourceGuide, archivalInstitution);
                    }
                }
                Set<HoldingsGuide> holdingsGuides = archivalInstitution.getHoldingsGuides();
                for(HoldingsGuide holdingsGuide : holdingsGuides) {
                    if(isPathOld(holdingsGuide.getPathApenetead())) {
                    	changeOldPath(holdingsGuide, archivalInstitution);
                    }
                }
                Set<FindingAid> findingAids = archivalInstitution.getFindingAids();
                for(FindingAid findingAid : findingAids) {
                    if(isPathOld(findingAid.getPathApenetead())) {
                    	changeOldPath(findingAid, archivalInstitution);
                    }else {
                    	changeOldRepoPaths(findingAid);
                    }
                }
            }
        } catch (APEnetException e) {
            LOG.info("We stop the loop because of an error, please see below", e);
        }
    }

//    private void changePath(Ead ead, ArchivalInstitution archivalInstitution) throws APEnetException {
//        String srcFilePath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + "tmp" + ead.getPathApenetead();
//        String newDbPath = APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + ead.getPathApenetead();
//        String destFilePath = APEnetUtilities.getConfig().getRepoDirPath() + newDbPath;
//
//        try {
//            LOG.info(XmlType.getEadType(ead).getName() + " id: " + ead.getId() + " path is old, we change it and move the file to REPO dir");
//
//            File destDir = new File(destFilePath.substring(0, destFilePath.lastIndexOf("/") + 1));
//            if(!destDir.exists())
//                destDir.mkdirs();
//            LOG.info("Move from: " + srcFilePath + " to: " + destFilePath);
//            FileUtils.moveFile(new File(srcFilePath), new File(destFilePath));
//            HibernateUtil.beginDatabaseTransaction();
//            ead.setPathApenetead(newDbPath);
//            DAOFactory.instance().getEadDAO().update(ead);
//            HibernateUtil.commitDatabaseTransaction();
//        } catch (Exception e) {
//            LOG.error("Error occurred when correcting paths in the Dashboard...");
//            HibernateUtil.rollbackDatabaseTransaction();
//            try {
//                if(!new File(srcFilePath).exists())
//                    FileUtils.copyFile(new File(destFilePath), new File(srcFilePath));
//            } catch(Exception e1) {
//                LOG.error("Could not copy the file back to its place, but the DB has been rolled back", e1);
//            }
//            throw new APEnetException("Could not change path in DB or move file of " + ead.toString(), e);
//        }
//    }
    private void changeOldPath(Ead ead, ArchivalInstitution archivalInstitution) throws APEnetException {
        String srcFilePath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath()  + ead.getPathApenetead();
        String fileName = ead.getPathApenetead().substring(ead.getPathApenetead().lastIndexOf("/") +1);
        String newDbPath = instantiateCorrectDirPath(ead) + fileName;
        String destFilePath = APEnetUtilities.getConfig().getRepoDirPath() + newDbPath;

        try {
            LOG.info(XmlType.getEadType(ead).getName() + " id: " + ead.getId() + " path is old, we change it and move the file to REPO dir - Move from: " + srcFilePath + " to: " + destFilePath + " dbpath: " + newDbPath);
            File destinationFile = new File(destFilePath);
            if(!destinationFile.getParentFile().exists())
            	destinationFile.getParentFile().mkdirs();
            FileUtils.moveFile(new File(srcFilePath),destinationFile );
            HibernateUtil.beginDatabaseTransaction();
            ead.setPathApenetead(newDbPath);
            DAOFactory.instance().getEadDAO().update(ead);
            HibernateUtil.commitDatabaseTransaction();
        } catch (Exception e) {
            LOG.error("Error occurred when correcting paths in the Dashboard..." + e.getMessage());
            HibernateUtil.rollbackDatabaseTransaction();
            try {
                if(!new File(srcFilePath).exists())
                    FileUtils.copyFile(new File(destFilePath), new File(srcFilePath));
            } catch(Exception e1) {
                LOG.error("Could not copy the file back to its place, but the DB has been rolled back");
            }
            //throw new APEnetException("Could not change path in DB or move file of " + ead.toString(), e);
        }
    }
    /**
     * Changes the path inside REPO for all the finding aids that are under their own directories:
     * - /repo/FR/1/FA/{NAME_FA}/{NAME_FA}.xml will become
     * - /repo/FR/1/FA/{NAME_FA}.xml
     * @param findingAid The FA that needs to be checked and possibly have its DB path modified
     * @param faDirPath The main directory for the FAs of this institution
     * @throws APEnetException If there is a problem so that the loop stops
     */
    private void changeOldRepoPaths(FindingAid findingAid) throws APEnetException {
    	ArchivalInstitution ai = findingAid.getArchivalInstitution();
        String faDirPath = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + ai.getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR + ai.getAiId() + APEnetUtilities.FILESEPARATOR + "FA" + APEnetUtilities.FILESEPARATOR;
        String faPath = APEnetUtilities.getConfig().getRepoDirPath() + findingAid.getPathApenetead();
        File faOldFile = new File(faPath);
        String faNewPath = faDirPath + faOldFile.getName();
        if(!faPath.equals(faNewPath)) {
        	LOG.info(findingAid + "- oldpath: " + faPath + " new path: " + faNewPath);
            try {
                File faOldDir = faOldFile.getParentFile();
                String faNewDbPath = faNewPath.replace(APEnetUtilities.getConfig().getRepoDirPath(), "");

                FileUtils.moveFile(new File(faPath), new File(faNewPath));
                FileUtils.deleteDirectory(faOldDir);
                HibernateUtil.beginDatabaseTransaction();
                findingAid.setPathApenetead(faNewDbPath);
                DAOFactory.instance().getEadDAO().update(findingAid);
                HibernateUtil.commitDatabaseTransaction();
            } catch (Exception e) {
                LOG.error("Error occurred when correcting paths in the Dashboard for FA only..." + findingAid + e.getMessage());
                HibernateUtil.rollbackDatabaseTransaction();
                try {
                    if(!new File(faPath).exists())
                        FileUtils.copyFile(new File(faNewPath), new File(faPath));
                } catch(Exception e1) {
                    LOG.error("Could not copy the file back to its place, but the DB has been rolled back");
                }
                //throw new APEnetException("Could not change path in DB or move file of " + findingAid.toString(), e);
            }
        }
    }

    private boolean isPathOld(String path) {
        String firstPartOfPath = path.split("/")[1];
        return StringUtils.isNumeric(firstPartOfPath) || firstPartOfPath.equalsIgnoreCase("tmp");
    }
    public String instantiateCorrectDirPath(Ead ead) {
    	ArchivalInstitution ai = ead.getArchivalInstitution();
        String startPath = APEnetUtilities.FILESEPARATOR + ai.getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR + ai.getAiId() + APEnetUtilities.FILESEPARATOR;
        XmlType xmlType = XmlType.getEadType(ead);
        if(xmlType == XmlType.EAD_FA){
            return startPath + "FA" + APEnetUtilities.FILESEPARATOR;
        } else if(xmlType == XmlType.EAD_HG){
            return startPath + "HG" + APEnetUtilities.FILESEPARATOR;
        } else if(xmlType == XmlType.EAD_SG){
            return startPath + "HG" + APEnetUtilities.FILESEPARATOR;
        }
        return null;

    }
}
