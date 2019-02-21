/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.ead2edm;

import java.io.File;

import javax.xml.transform.TransformerException;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dpt.utils.ead2edm.EdmConfig;
import eu.apenet.dpt.utils.ead2edm.EdmFileUtils;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ese;

/**
 * 
 * @author bverhoef
 */
public class EAD2EDMConverter {
        //define COLON variable
	private static final String COLON = ":";


	public static void generateHtml(Ese ese) throws TransformerException {
		File eseFile = EdmFileUtils.getRepoFile(APEnetUtilities.getConfig().getRepoDirPath(), ese.getPath());
		EseDAO eseDao = DAOFactory.instance().getEseDAO();
		String xmlNameRelative = EdmFileUtils.getFileName(APEnetUtilities.FILESEPARATOR, eseFile);
		if (ese.getNumberOfRecords() > 0) {
			
			int numberOfFiles = 0;
			EdmConfig config = new EdmConfig();
				int numberOfRecords = ese.getNumberOfRecords();
				File htmlOutputFile = EdmFileUtils.getFile(EdmFileUtils.getOutputHTMLDir(APEnetUtilities.getConfig().getRepoDirPath(), ese.getFindingAid().getArchivalInstitution().getCountry().getIsoname(), ese.getFindingAid().getArchivalInstitution().getAiId()), xmlNameRelative + ".html");
				File htmlDirectory = htmlOutputFile.getParentFile();
				htmlDirectory.mkdirs();
				String htmlDirname = eseFile.getName() + ".htmldir";
				File htmlSubDirectory = EdmFileUtils.getFile(htmlDirectory, htmlDirname);
				htmlSubDirectory.mkdirs();
				EdmFileUtils.createDirs(htmlSubDirectory.getAbsolutePath(), numberOfRecords);
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
				// EAD2EDMConverter.class.getClassLoader().getResource("htmlsources");
				// FileUtils.copyDir(new File(url.getFile()), htmlSubDirectory,
				// config.getStatus());
				// add index html when generate html
				numberOfFiles++;
				numberOfFiles += numberOfRecords;
				ese.setPathHtml(EdmFileUtils.getRelativeEDMHTMLFilePath(ese.getFindingAid().getArchivalInstitution().getCountry().getIsoname(), ese.getFindingAid().getArchivalInstitution().getAiId(), xmlNameRelative + ".html"));
				eseDao.store(ese);


			
		} 

	}




}
