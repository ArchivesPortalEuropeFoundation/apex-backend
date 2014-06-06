package eu.apenet.dashboard.services.ead;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import eu.archivesportaleurope.persistence.jpa.JpaUtil;

import org.apache.commons.io.FileUtils;

import com.ctc.wstx.exc.WstxParsingException;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.utils.XMLUtils;
import eu.apenet.dashboard.manual.ExistingFilesChecker;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;
import eu.apenet.persistence.vo.UpFile;
import eu.apenet.persistence.vo.UploadMethod;

public class CreateEadTask extends AbstractEadTask {

	@Override
	protected String getActionName() {
		return "create EAD";
	}

	protected Ead execute(XmlType xmlType, UpFile upFile, Integer aiId) throws Exception {
		String fileName = upFile.getFilename();
		try {
			String uploadedFilesPath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath();
			
			boolean isConverted;
			try {
				isConverted = Boolean.valueOf(ExistingFilesChecker.extractAttributeFromEad(
						uploadedFilesPath + upFile.getPath() + upFile.getFilename(), "eadheader/revisiondesc/change/item", null, false));
			} catch (Exception e) {
				isConverted = false;
			}

			String title;
			try {
				title = ExistingFilesChecker.extractAttributeFromEad(uploadedFilesPath + upFile.getPath() + upFile.getFilename(),
						"eadheader/filedesc/titlestmt/titleproper", null, true).trim();
			} catch (WstxParsingException e) {
				title = "";
			}

			String eadid = "";
			try {
				eadid = ExistingFilesChecker.extractAttributeFromEad(uploadedFilesPath + upFile.getPath() + upFile.getFilename(),
						"eadheader/eadid", null, true).trim();
			} catch (WstxParsingException e) {
			}
			ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().findById(aiId);

			Ead newEad = null;
			if (XmlType.EAD_FA.equals(xmlType))
				newEad = new FindingAid();
			else if (XmlType.EAD_HG.equals(xmlType))
				newEad = new HoldingsGuide();
			else if (XmlType.EAD_SG.equals(xmlType))
				newEad = new SourceGuide();

			newEad.setAiId(aiId);
			newEad.setArchivalInstitution(archivalInstitution);
			newEad.setEadid(XMLUtils.removeUnusedCharacters(eadid));
			newEad.setTitle(XMLUtils.removeUnusedCharacters(title));
			newEad.setConverted(isConverted);
			newEad.setUploadDate(new Date());
			// / STORING THE NEW FINDING AID IN THE FILE SYSTEM ///
			String startPath = getPath(xmlType, archivalInstitution) + upFile.getFilename();
            File destFile = new File(APEnetUtilities.getDashboardConfig().getRepoDirPath() + startPath);
            if(destFile.exists()) {
                startPath = getPath(xmlType, archivalInstitution) + System.currentTimeMillis() + "_" + upFile.getFilename();
                destFile = new File(APEnetUtilities.getDashboardConfig().getRepoDirPath() + startPath);
            }
			newEad.setPathApenetead(startPath);
			newEad.setUploadMethod(upFile.getUploadMethod());
			newEad = DAOFactory.instance().getEadDAO().store(newEad);

			File srcFile = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath()
					+ upFile.getPath() + fileName);

			FileUtils.copyFile(srcFile, destFile);
			File uploadDir = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + upFile.getPath());

			if (srcFile.exists())
				FileUtils.forceDelete(srcFile);

			if (uploadDir.exists() && uploadDir.listFiles().length == 0) {
				FileUtils.forceDelete(uploadDir);
			}
			if (UploadMethod.OAI_PMH.equals(upFile.getUploadMethod().getMethod())){
				File parentDir = uploadDir.getParentFile();
				if (parentDir.exists() && parentDir.listFiles().length == 0) {
					FileUtils.forceDelete(parentDir);
				}
			}
			LinkingService.linkWithHgOrSg(newEad);
			logAction(xmlType, fileName, null);
			return newEad;

		} catch (Exception e) {
			logAction(xmlType, fileName, e);
			throw new APEnetException("Could not create the file with from: " + upFile.getPath() + upFile.getFilename(), e);
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

	public static String getPath(XmlType xmlType, ArchivalInstitution archivalInstitution) {
		String countryIso = archivalInstitution.getCountry().getIsoname().trim();
		String startPath = APEnetUtilities.FILESEPARATOR + countryIso + APEnetUtilities.FILESEPARATOR
				+ archivalInstitution.getAiId() + APEnetUtilities.FILESEPARATOR;
		if (xmlType == XmlType.EAD_FA) {
			return startPath + "FA" + APEnetUtilities.FILESEPARATOR;
		} else if (xmlType == XmlType.EAD_HG) {
			return startPath + "HG" + APEnetUtilities.FILESEPARATOR;
		} else if (xmlType == XmlType.EAD_SG) {
			return startPath + "SG" + APEnetUtilities.FILESEPARATOR;
		}
		return null;
	}

}
