package eu.apenet.dashboard.services.ead;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.ctc.wstx.exc.WstxParsingException;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.manual.ExistingFilesChecker;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;
import eu.apenet.persistence.vo.UpFile;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

public class CreateEadTask extends AbstractEadTask {

	@Override
	protected String getActionName() {
		return "create EAD";
	}

	protected Ead execute(XmlType xmlType, UpFile upFile, Integer aiId) throws Exception {
		String fileName = upFile.getFilename();
		try {
			String uploadedFilesPath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath()
					+ APEnetUtilities.FILESEPARATOR + aiId.toString() + APEnetUtilities.FILESEPARATOR;
			
			boolean isConverted;
			try {
				isConverted = Boolean.valueOf(ExistingFilesChecker.extractAttributeFromEad(
						uploadedFilesPath + upFile.getFilename(), "eadheader/revisiondesc/change/item", null, false));
			} catch (Exception e) {
				isConverted = false;
			}

			String title;
			try {
				title = ExistingFilesChecker.extractAttributeFromEad(uploadedFilesPath + upFile.getFilename(),
						"eadheader/filedesc/titlestmt/titleproper", null, true).trim();
			} catch (WstxParsingException e) {
				title = "";
			}

			String eadid = "";
			try {
				eadid = ExistingFilesChecker.extractAttributeFromEad(uploadedFilesPath + upFile.getFilename(),
						"eadheader/eadid", null, true).trim();
			} catch (WstxParsingException e) {
			}
			ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().findById(aiId);
			String countryIso = archivalInstitution.getCountry().getIsoname().trim();

			Ead newEad = null;
			if (XmlType.EAD_FA.equals(xmlType))
				newEad = new FindingAid();
			else if (XmlType.EAD_HG.equals(xmlType))
				newEad = new HoldingsGuide();
			else if (XmlType.EAD_SG.equals(xmlType))
				newEad = new SourceGuide();

			newEad.setArchivalInstitution(archivalInstitution);
			newEad.setEadid(eadid);
			newEad.setTitle(title);
			newEad.setConverted(isConverted);
			newEad.setUploadDate(new Date());
			// / STORING THE NEW FINDING AID IN THE FILE SYSTEM ///
			String startPath = getPath(xmlType, countryIso, aiId) + upFile.getFilename();
			newEad.setPathApenetead(startPath);
			newEad.setUploadMethod(upFile.getUploadMethod());
			newEad = DAOFactory.instance().getEadDAO().store(newEad);

			File destFile = new File(APEnetUtilities.getDashboardConfig().getRepoDirPath() +startPath);

			File srcFile = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath()
					+ APEnetUtilities.FILESEPARATOR + aiId + APEnetUtilities.FILESEPARATOR + fileName);

			FileUtils.copyFile(srcFile, destFile);
			File uploadDir = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath()
					+ APEnetUtilities.FILESEPARATOR + aiId + APEnetUtilities.FILESEPARATOR);

			if (srcFile.exists())
				FileUtils.forceDelete(srcFile);

			if (uploadDir.listFiles().length == 0)
				FileUtils.forceDelete(uploadDir);
			logAction(xmlType, fileName, null);
			return newEad;

		} catch (Exception e) {
			logAction(xmlType, fileName, e);
			
			JpaUtil.rollbackDatabaseTransaction();
			throw new APEnetException("Could not create the file with from: " + upFile.getFilename(), e);
		}
	}

	@Override
	protected void execute(Ead ead, Properties properties) throws APEnetException {
	}

	protected void logAction(XmlType xmlType, String fileName, Exception exception) {
		if (exception == null) {
			logger.info(fileName + "(" + xmlType.getName() + "): " + getActionName() + " - succeed");
		}else {
			logger.error(fileName + "(" + xmlType.getName() + "): " + getActionName() + " - failed",exception);
		}
		
	}

	public String getPath(XmlType xmlType, String countryIso, Integer archivalInstitutionId) {
		String startPath = APEnetUtilities.FILESEPARATOR + countryIso + APEnetUtilities.FILESEPARATOR
				+ archivalInstitutionId + APEnetUtilities.FILESEPARATOR;
		if (xmlType == XmlType.EAD_FA) {
			return startPath + "FA" + APEnetUtilities.FILESEPARATOR;
		} else if (xmlType == XmlType.EAD_HG) {
			return startPath + "HG" + APEnetUtilities.FILESEPARATOR;
		} else if (xmlType == XmlType.EAD_SG) {
			return startPath + "HG" + APEnetUtilities.FILESEPARATOR;
		}
		return null;
	}

}
