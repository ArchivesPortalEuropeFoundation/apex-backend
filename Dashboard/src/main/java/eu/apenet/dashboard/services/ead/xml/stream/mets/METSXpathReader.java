package eu.apenet.dashboard.services.ead.xml.stream.mets;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import eu.apenet.dashboard.services.ead.xml.stream.mets.xpath.FileXpathHandler;
import eu.apenet.dashboard.services.ead.xml.stream.mets.xpath.MetsFile;
import eu.apenet.dashboard.services.ead.xml.stream.mets.xpath.StructMapDiv;
import eu.apenet.dashboard.services.ead.xml.stream.mets.xpath.StructMapDivXpathHandler;
import eu.archivesportaleurope.xml.ApeXMLConstants;
import eu.archivesportaleurope.xml.xpath.NestedXpathHandler;
import eu.archivesportaleurope.xml.xpath.XmlStreamHandler;

public class METSXpathReader {

	private NestedXpathHandler defaultFileGrpHandler;
	private NestedXpathHandler thumbsFileGrpHandler;
	private FileXpathHandler defaultFileHandler;
	private FileXpathHandler thumbsFileHandler;
	
	private NestedXpathHandler structMapHandler;
	private StructMapDivXpathHandler structMapDivXpathHandler;
	private List<XmlStreamHandler> metsHandlers = new ArrayList<XmlStreamHandler>();
	

	public METSXpathReader() {
		defaultFileGrpHandler = new NestedXpathHandler(ApeXMLConstants.METS_NAMESPACE,  new String[] { "mets", "fileSec","fileGrp"});
		defaultFileGrpHandler.setAttribute("USE", "DEFAULT", false);
		thumbsFileGrpHandler = new NestedXpathHandler(ApeXMLConstants.METS_NAMESPACE,  new String[] { "mets", "fileSec","fileGrp"});
		thumbsFileGrpHandler.setAttribute("USE", "THUMBS", false);
		defaultFileHandler = new FileXpathHandler();
		defaultFileGrpHandler.getHandlers().add(defaultFileHandler);
		
		thumbsFileHandler = new FileXpathHandler();
		thumbsFileGrpHandler.getHandlers().add(thumbsFileHandler);
		
		metsHandlers.add(defaultFileGrpHandler);
		metsHandlers.add(thumbsFileGrpHandler);
		
		
		structMapHandler = new NestedXpathHandler(ApeXMLConstants.METS_NAMESPACE,  new String[] { "mets", "structMap"});
		structMapHandler.setAttribute("TYPE", "PHYSICAL", false);
		metsHandlers.add(structMapHandler);
		
		structMapDivXpathHandler  = new StructMapDivXpathHandler();
		structMapHandler.getHandlers().add(structMapDivXpathHandler);
	}

	public void processCharacters(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
			for (XmlStreamHandler handler : metsHandlers) {
				handler.processCharacters(xpathPosition, xmlReader);
			}


	}

	public void processStartElement(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
			for (XmlStreamHandler handler : metsHandlers) {
				handler.processStartElement(xpathPosition, xmlReader);
			}

	}

	public void processEndElement(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
			for (XmlStreamHandler handler : metsHandlers) {
				handler.processEndElement(xpathPosition, xmlReader);
			}

	}


	public List<DaoInfo> getData() {
		List<DaoInfo> results = new ArrayList<DaoInfo>();
		Map<String, MetsFile> defaultMetsFiles = defaultFileHandler.getResults();
		Map<String, MetsFile> thumbsMetsFiles = thumbsFileHandler.getResults();
		List<StructMapDiv> divs = structMapDivXpathHandler.getResults();
		for (StructMapDiv div: divs){
			MetsFile defaultMetsFile = getMetsFile(defaultMetsFiles, div.getFileIds());
			if (defaultMetsFile != null){
				DaoInfo daoInfo = new DaoInfo();
				daoInfo.setLabel(div.getLabel());
				daoInfo.setReference(defaultMetsFile);
				MetsFile thumbsMetsFile = getMetsFile(thumbsMetsFiles, div.getFileIds());
				daoInfo.setThumbnail(thumbsMetsFile);
				results.add(daoInfo);
			
			}
		}
		return results;
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
