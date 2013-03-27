package eu.apenet.dashboard.services.eag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import net.sf.saxon.s9api.SaxonApiException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.xslt.EagXslt;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

public class ParseEAGAction extends ActionSupport {
	
	private final Logger logger = Logger.getLogger(ParseEAGAction.class);; //TODO instance

	@Override
	public String execute() throws Exception {
		//parseAllExistingEAGToEAG2012();
		parseEAGs();
		return SUCCESS;
	}
	
	private void parseEAGs() throws SAXException, IOException{
		ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
		List<ArchivalInstitution> institutions = aiDao.findAll();
		if(institutions!=null && institutions.size()>0){
			Iterator<ArchivalInstitution> institutionsIterator = institutions.iterator();
			while(institutionsIterator.hasNext()){
				ArchivalInstitution institution = institutionsIterator.next();
				String tempDirOutputPath = institution.getEagPath();
				if(tempDirOutputPath!=null && !tempDirOutputPath.isEmpty()){
					tempDirOutputPath = APEnetUtilities.getDashboardConfig().getRepoDirPath() + tempDirOutputPath;
					if(!new File(tempDirOutputPath).exists()){
						tempDirOutputPath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + institution.getEagPath();
					}
					FileInputStream in = null;
					String tempOutputFilePath = tempDirOutputPath + ".new";
					File tempOutputFile = new File(tempOutputFilePath);
					String xslFilePath = APEnetUtilities.getDashboardConfig().getSystemXslDirPath()+ APEnetUtilities.FILESEPARATOR + "eag2eag2012.xsl";
					logger.info("'" + institution.getAiname() + "' is parsing file: '" + tempDirOutputPath);
					in  = new FileInputStream(new File(tempDirOutputPath));
					TransformationTool.createTransformation(in, tempOutputFile, new File(xslFilePath), null, true, true, null, true, null);

					File file = new File(tempDirOutputPath);
					try {
						FileUtils.moveFile(file , new File(tempDirOutputPath+".old")); //backup old EAGs, if it's not needed comment this line
						FileUtils.moveFile(new File(tempDirOutputPath+".new"), file);
					} catch (IOException e) {
						logger.error("problem moving file "+file.getAbsolutePath());
					}
				}
			}
		}
	}

	private boolean parseAllExistingEAGToEAG2012() throws FileNotFoundException {
		boolean failure = false;
		//1. get all institutions and his EAG
		ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
		List<ArchivalInstitution> institutions = aiDao.findAll();
		Iterator<ArchivalInstitution> institutionsIterator = null;
		String eagPath = null;
		if(institutions!=null && institutions.size()>0){
			institutionsIterator = institutions.iterator();
			while(institutionsIterator.hasNext() && !failure){
				ArchivalInstitution institution = institutionsIterator.next();
				eagPath = institution.getEagPath(); //get path to be checked
				//2. check if eag is valid
				if(eagPath!=null && !eagPath.isEmpty()){
					eagPath = APEnetUtilities.getDashboardConfig().getRepoDirPath() + eagPath;
					if(!new File(eagPath).exists()){
						eagPath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + institution.getEagPath();
					}
//							APEnetEAGDashboard eag = new APEnetEAGDashboard(institution.getAiId(), eagPath);
//							if(eag.APEnetEAGValidate(eagPath)){
					//3. parse EAG to EAG2012_temp
					try{
						File eagPathFile = new File(eagPath);
						File destinationFile = new File(eagPathFile.getParentFile(), eagPathFile.getName()+ "_convertedToEAG2012.xml");
						PrintWriter writer = new PrintWriter(destinationFile);
						EagXslt.convertAi(writer, eagPathFile, null);
					}catch(SaxonApiException e){
						logger.error("problems parsing EAG to EAG2012",e);
						failure = true; //stop
					}
//							}
				}
			}
			if(!failure){
				//4. if all is ok remove EAG and move _convertedToEAG2012.xml
				institutionsIterator = institutions.iterator();
				while(institutionsIterator.hasNext()){
					eagPath = institutionsIterator.next().getEagPath();
					if(eagPath!=null && !eagPath.isEmpty() && eagPath.contains("_convertedToEAG2012.xml")){
						eagPath = APEnetUtilities.getDashboardConfig().getRepoDirPath() + eagPath;
						File file = new File(eagPath);
						File outputFile = new File(eagPath.substring(0,eagPath.indexOf("_convertedToEAG2012.xml"))+".xml");
						try {
							FileUtils.moveFile(outputFile, new File(outputFile.getAbsolutePath()+".old")); //backup old EAGs, if it's not needed comment this line
							FileUtils.moveFile(outputFile, file);
						} catch (IOException e) {
							logger.error("problem moving file "+file.getAbsolutePath());
						}
					}
				}
			}
		}
		return failure;
	}

}
