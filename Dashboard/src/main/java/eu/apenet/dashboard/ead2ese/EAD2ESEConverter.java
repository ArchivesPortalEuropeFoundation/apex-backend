/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.ead2ese;

import java.io.File;
import java.io.IOException;

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
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ese;

/**
 * 
 * @author bverhoef
 */
public class EAD2ESEConverter {
	private static final String COLON = ":";


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
