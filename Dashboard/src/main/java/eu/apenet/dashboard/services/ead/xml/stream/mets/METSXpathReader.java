package eu.apenet.dashboard.services.ead.xml.stream.mets;

import java.util.List;
import java.util.Map;

import eu.apenet.dashboard.services.ead.xml.stream.mets.xpath.FileXpathHandler;
import eu.apenet.dashboard.services.ead.xml.stream.mets.xpath.MetsFile;
import eu.apenet.dashboard.services.ead.xml.stream.mets.xpath.StructMapDiv;
import eu.apenet.dashboard.services.ead.xml.stream.mets.xpath.StructMapDivXpathHandler;
import eu.archivesportaleurope.xml.ApeXMLConstants;
import eu.archivesportaleurope.xml.xpath.AbstractXpathReader;
import eu.archivesportaleurope.xml.xpath.handler.AttributeXpathHandler;
import eu.archivesportaleurope.xml.xpath.handler.NestedXpathHandler;
import eu.archivesportaleurope.xml.xpath.handler.TextXpathHandler;

public class METSXpathReader extends AbstractXpathReader<MetsInfo> {

	private NestedXpathHandler defaultFileGrpHandler;
	private NestedXpathHandler thumbsFileGrpHandler;
	private FileXpathHandler defaultFileHandler;
	private FileXpathHandler thumbsFileHandler;
	
	private NestedXpathHandler structMapHandler;
	private StructMapDivXpathHandler structMapDivXpathHandler;
	private NestedXpathHandler xmlDataHandler;
	private AttributeXpathHandler rightsCategoryHandler;
	private AttributeXpathHandler rightsOtherCategoryHandler;
	
	private TextXpathHandler rightsConstraintsHandler;
	private TextXpathHandler rightsDeclarationHandler;
	private TextXpathHandler rightsHolderHandler;
	private TextXpathHandler rightsCommentsHandler;
	

	protected void internalInit() {
		defaultFileGrpHandler = new NestedXpathHandler(ApeXMLConstants.METS_NAMESPACE,  new String[] { "mets", "fileSec","fileGrp"});
		defaultFileGrpHandler.setAttribute("USE", "DEFAULT", false);
		thumbsFileGrpHandler = new NestedXpathHandler(ApeXMLConstants.METS_NAMESPACE,  new String[] { "mets", "fileSec","fileGrp"});
		thumbsFileGrpHandler.setAttribute("USE", "THUMBS", false);
		defaultFileHandler = new FileXpathHandler();
		defaultFileGrpHandler.getHandlers().add(defaultFileHandler);
		
		thumbsFileHandler = new FileXpathHandler();
		thumbsFileGrpHandler.getHandlers().add(thumbsFileHandler);
		
		getXpathHandlers().add(defaultFileGrpHandler);
		getXpathHandlers().add(thumbsFileGrpHandler);
		
		
		structMapHandler = new NestedXpathHandler(ApeXMLConstants.METS_NAMESPACE,  new String[] { "mets", "structMap"});
		structMapHandler.setAttribute("TYPE", "PHYSICAL", false);
		xmlDataHandler = new NestedXpathHandler(ApeXMLConstants.METS_NAMESPACE,  new String[] { "mets", "amdSec", "rightsMD", "mdWrap", "xmlData"});
		rightsCategoryHandler = new AttributeXpathHandler(ApeXMLConstants.METS_RIGHTS_NAMESPACE, new String[] { "RightsDeclarationMD"}, "RIGHTSCATEGORY");
		rightsOtherCategoryHandler = new AttributeXpathHandler(ApeXMLConstants.METS_RIGHTS_NAMESPACE, new String[] { "RightsDeclarationMD"}, "OTHERCATEGORYTYPE");
		
		rightsDeclarationHandler =new TextXpathHandler(ApeXMLConstants.METS_RIGHTS_NAMESPACE, new String[] { "RightsDeclarationMD", "RightsHolder", "RightsDeclaration"}, true);
		rightsConstraintsHandler =new TextXpathHandler(ApeXMLConstants.METS_RIGHTS_NAMESPACE, new String[] { "RightsDeclarationMD", "Context", "Constraints","ConstraintDescription"}, true);
		rightsHolderHandler =new TextXpathHandler(ApeXMLConstants.METS_RIGHTS_NAMESPACE, new String[] { "RightsDeclarationMD", "RightsHolder", "RightsHolderName"}, true);
		rightsCommentsHandler =new TextXpathHandler(ApeXMLConstants.METS_RIGHTS_NAMESPACE, new String[] { "RightsDeclarationMD", "RightsHolder", "RightsHolderComments"}, true);
		getXpathHandlers().add(structMapHandler);
		getXpathHandlers().add(xmlDataHandler);
		xmlDataHandler.getHandlers().add(rightsCategoryHandler);
		xmlDataHandler.getHandlers().add(rightsOtherCategoryHandler);
		xmlDataHandler.getHandlers().add(rightsHolderHandler);
		xmlDataHandler.getHandlers().add(rightsCommentsHandler);
		xmlDataHandler.getHandlers().add(rightsDeclarationHandler);
		xmlDataHandler.getHandlers().add(rightsConstraintsHandler);		
		structMapDivXpathHandler  = new StructMapDivXpathHandler();
		structMapHandler.getHandlers().add(structMapDivXpathHandler);
	}



	public MetsInfo getData() throws METSParserException {
		MetsInfo metsInfo = new MetsInfo();
		metsInfo.setRightsDeclaration(rightsDeclarationHandler.getFirstResult());
		metsInfo.setRightsConstraint(rightsConstraintsHandler.getFirstResult());
		metsInfo.setRightsCategory(rightsCategoryHandler.getResultAsString());
		metsInfo.setRightsOtherCategory(rightsOtherCategoryHandler.getResultAsString());
		metsInfo.setRightsHolder(rightsHolderHandler.getResultAsString());
		metsInfo.setRightsComments(rightsCommentsHandler.getResultAsString());
		Map<String, MetsFile> defaultMetsFiles = defaultFileHandler.getResults();
		Map<String, MetsFile> thumbsMetsFiles = thumbsFileHandler.getResults();
		List<StructMapDiv> divs = structMapDivXpathHandler.getResults();
		int numberOfThumbnails = 0;
		int numberOfLinks = 0;
		for (StructMapDiv div: divs){
			MetsFile defaultMetsFile = getMetsFile(defaultMetsFiles, div.getFileIds());
			if (defaultMetsFile != null){
				numberOfLinks++;
				DaoInfo daoInfo = new DaoInfo();
				daoInfo.setLabel(div.getLabel());
				daoInfo.setReference(defaultMetsFile);
				MetsFile thumbsMetsFile = getMetsFile(thumbsMetsFiles, div.getFileIds());
				daoInfo.setThumbnail(thumbsMetsFile);
				metsInfo.getDaoInfos().add(daoInfo);
				if (thumbsMetsFile != null){
					numberOfThumbnails++;
				}
				if (numberOfThumbnails > 0 && numberOfThumbnails != numberOfLinks){
					throw new METSParserException("There are thumbnails, but the number of thumbnails less that the number of references. The problem start with DIV: " + div.getLabel() + ":<br/>file ID: "+defaultMetsFile.getId());
				}
			}
		}
		return metsInfo;
	}
	private MetsFile getMetsFile(Map<String, MetsFile> defaultMetsFiles, List<String> fileIds){
		boolean found = false;
		for (int i=0; !found && i < fileIds.size(); i++){
			MetsFile metsFile = defaultMetsFiles.get(fileIds.get(i));
			if (metsFile != null){
				found = true;
				fileIds.remove(i);
				return metsFile;
			}
		}
		return null;
	}
}
