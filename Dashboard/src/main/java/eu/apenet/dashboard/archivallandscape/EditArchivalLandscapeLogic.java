package eu.apenet.dashboard.archivallandscape;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.vo.Country;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

public class EditArchivalLandscapeLogic {

	private static Logger log = Logger.getLogger(EditArchivalLandscapeLogic.class);
	
	public static Institution localizateInstitution(List<Institution> list,String cadena) {
		Institution insti = null;
		Iterator<Institution> it = list.iterator();
		boolean exit = false;
		while(!exit && it.hasNext()){
			insti = it.next();
			String id = insti.getId();
			if(!id.equals(cadena)){
				if(insti.getInstitutions()==null){
					insti = null;
				}else{
					insti = localizateInstitution(insti.getInstitutions(),cadena);
					if(insti!=null){
						exit = true;
					}
				}
			}else{
				exit = true;
			}
		}
		return insti;
	}
	
	/**
	 * This method reads 
	 * @param first
	 * @return ArrayList<Institution>
	 */
	public static List<Institution> navigate(Boolean first){
		List<Institution> list = null;
		try {
			ArchivalLandscape a = new ArchivalLandscape();
			String path = a.getmyPath(a.getmyCountry()) + "tmp" + APEnetUtilities.FILESEPARATOR + a.getmyCountry() + "AL.xml";
			if(!(new File(a.getmyPath(a.getmyCountry()) + a.getmyCountry() + "AL.xml").exists())){
				String path2 = APEnetUtilities.getDashboardConfig().getArchivalLandscapeDirPath() + APEnetUtilities.FILESEPARATOR + "AL.xml";
				a.read(new File(path2),"edit");
				new EditAction().makeTemporal();
			}else if(first){
				new EditAction().makeTemporal();
			}
			list = xmlReadFunction(path);
		} catch (Exception e){
			log.error(e.getMessage(),e);
		}
		return list;
	}

//    private static List<Institution> getArchivalInstitutionListFromDB(ArchivalLandscape a) {
//        List<Institution> institutions = new ArrayList<Institution>();
//
//        CountryDAO countryDAO = DAOFactory.instance().getCountryDAO();
//        Country country = countryDAO.getCountryByCname(a.getmyCountryName());
//        ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
//
//
//        List<ArchivalInstitution> archivalInstitutions = archivalInstitutionDAO.getArchivalInstitutionsByCountryId(country.getId());
//        for(ArchivalInstitution archivalInstitution : archivalInstitutions) {
//            if(archivalInstitution.getParentAiId() == null) {
//                Institution institution = new Institution();
//                institution.setId(archivalInstitution.getAiId()+"");
//                institution.setLevel("series");
//                institution.setName(archivalInstitution.getAiname());
//
//                institution.setInstitutions(archivalInstitutionToInstitution(new ArrayList<ArchivalInstitution>(archivalInstitution.getChildArchivalInstitutions())));
//
//                institutions.add(institution);
//            }
//        }
//
//        return institutions;
//    }
//
//    public static List<Institution> archivalInstitutionToInstitution(List<ArchivalInstitution> archivalInstitutions) {
//        List<Institution> institutions = new ArrayList<Institution>(archivalInstitutions.size());
//        for(ArchivalInstitution archivalInstitution : archivalInstitutions) {
//            Institution institution = new Institution();
//            institution.setId(archivalInstitution.getAiId()+"");
//            institution.setName(archivalInstitution.getAiname());
//            institution.setLevel("series");
//            institution.setInstitutions(archivalInstitutionToInstitution(new ArrayList<ArchivalInstitution>(archivalInstitution.getChildArchivalInstitutions())));
//            institutions.add(institution);
//        }
//        return institutions;
//    }

	/**
	 * This function generates a new instance of DocumentBuilder 
	 * and return it
	 * @return DocumentBuilder
	 * @throws ParserConfigurationException
	 */
	public static DocumentBuilder buildDocument() throws ParserConfigurationException{
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		return docBuilder;
	}
	
	/**
	 * Move a node up or down (depends on a boolean attribute) in a document (coAL.xml), which is indicated 
	 * by a String route and a DocumentBuilder docBuilder. 
	 * 
	 * @param route
	 * @param docBuilder
	 * @param insti
	 * @param up
	 * @return Document
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws XPathExpressionException 
	 */
	public static Document moveNode(String route,DocumentBuilder docBuilder, Institution insti,boolean up) throws SAXException, IOException, XPathExpressionException{
		Document doc = docBuilder.parse(route);
		try {//Remove the whiteSpaces in AL.xml
			removeWhiteSpaceInXML(doc);
		} catch (Exception e) {
			log.error("Could not be removed the whiteSpaceNodes in temporal AL.xml:"+e.getCause());
		}
		doc.normalize();
		doc.normalizeDocument();
		NodeList nodeList = doc.getElementsByTagName("c"); 
		boolean done = false;
		for(int i=0;!done && i<nodeList.getLength();i++){ //First search the reference node depends on boolean parameter.
			Node node = nodeList.item(i);
			Node nSeeker = null;
			NamedNodeMap attributes = node.getAttributes();
			boolean found = false;
			if(attributes.getLength()>0){
				for(int j=0;!found && j<attributes.getLength();j++){//looking forward c
					if(attributes.item(j).getNodeName().equals("id") && attributes.item(j).getNodeValue().equals(insti.getId())){
						if(up){
							if(node.getPreviousSibling()!=null && node.getPreviousSibling().getNodeName().trim().equals("c")){
								nSeeker = node.getPreviousSibling();
							}else{
								nSeeker = node;
							}
						}else{ //down case
							if(node.getNextSibling()!=null && node.getNextSibling().getNodeName().trim().equals("c")){
								nSeeker = node.getNextSibling();
							}else{
								nSeeker = node;
							}
						}
						found = true;
					}
				}
			} //When the reference node is found, it's used to move the node.
			if(found){
				Node parent = node.getParentNode();
				for(int k=0;k<parent.getChildNodes().getLength() && !done;k++){
					if(parent.getChildNodes().item(k).getNodeName().equals("c")){ 
						if(up){
							parent.insertBefore(node, nSeeker);
						}else{//down
							if(nSeeker.getNextSibling()!=null){
								parent.insertBefore(node, nSeeker.getNextSibling());
							}else{//last child
								parent.appendChild(node);
							}
						}
						done = true;
					}
				}
			}
		}
		return doc;
	}
	
	public static Document removeWhiteSpaceInXML(Document doc) throws XPathExpressionException {
		XPathFactory xpathFactory = XPathFactory.newInstance(); 
		// XPath to find empty text nodes. 
		XPathExpression xpathExp = xpathFactory.newXPath().compile("//text()[normalize-space(.) = '']");   
		NodeList emptyTextNodes = (NodeList) xpathExp.evaluate(doc, XPathConstants.NODESET); 
		// Remove each empty text node from document. 
		for (int i = 0; i < emptyTextNodes.getLength(); i++) { 
		    Node emptyTextNode = emptyTextNodes.item(i); 
		    emptyTextNode.getParentNode().removeChild(emptyTextNode); 
		}
		return doc;
	}

	/**
	 * Returns an institution matched by a String id. First clean the name
	 * and last compare the institution by levels and if it's found returns it.
	 * @param insti
	 * @param id
	 * @return Institution
	 */
	public static Institution getInstiById(Institution insti, String id){
		Institution temp = null;
		if(insti.getId().equals(id)){ //Found
			temp=insti;
		}else if(insti.getInstitutions()!=null){
			for(int i=0;temp==null && i<insti.getInstitutions().size();i++){
				temp = getInstiById(insti.getInstitutions().get(i),id);
			}
		}
		return temp;
	}

	/**
	 * This method provide a XML navigation and communicates when it's needed call 
	 * to navigateReadXML method. It's used in xmlReadFunction().
	 * @param r
	 * @param level
	 * @param id 
	 * @return Institution
	 * @throws XMLStreamException
	 */
	private static Institution navigateReadXML(XMLStreamReader r, String level, String id) throws XMLStreamException{
		Institution institution = new Institution();
		institution.setLevel(level);
		institution.setId(id);
		String refinedText = null;
		while(r.hasNext()){
			Integer event = r.next(); 
			switch(event){
				case XMLStreamConstants.START_ELEMENT:
					String localName = r.getLocalName().trim();
					if(localName.equals("c")){
						level = null;
						id = null;
						for (int i = 0; i < r.getAttributeCount(); i++) {
							if(r.getAttributeLocalName(i)!=null && r.getAttributeLocalName(i).trim().equals("id")){
								id = r.getAttributeValue(i).trim();
							}else if (r.getAttributeLocalName(i).trim().equals("level") && !r.getAttributeValue(i).trim().equals("fonds")) {
								level = r.getAttributeValue(i).trim();
							}
							if(id!=null && level!=null){
								List<Institution> institutions = institution.getInstitutions();
								if(institutions==null){
									institutions = new ArrayList<Institution>();
								}
								Institution tempInsti = navigateReadXML(r,level,id);
								event = r.getEventType();
								if(tempInsti!=null){
									institutions.add(tempInsti);
									institution.setInstitutions(institutions);
								}
								break;
							}
						}
					}else if(localName.equals("unittitle") && institution.getName()==null){
						refinedText = "";
					}
					break;
				case XMLStreamConstants.END_ELEMENT:
					if(r.getLocalName().trim().equals("c")){
						return institution;
					}else if(r.getLocalName().trim().equals("unittitle") && institution.getName()!=null && institution.getName().length()>0){
						refinedText = null;
					}
					break;
				case XMLStreamConstants.CHARACTERS:
					if(!r.isWhiteSpace() && refinedText!=null){
						if(institution.getName()!=null){
							refinedText = institution.getName();
						}
						refinedText = r.getText().trim();
						institution.setName(refinedText);
					}
			}
		}
		return institution;
	}
	
	/**
	 * This function read the XML file, parse it to a list of Institutions and
	 * returns it. Needs the XML route.
	 * @param route
	 * @return ArrayList<Institution>
	 * @throws FileNotFoundException
	 * @throws XMLStreamException
	 */
	private static List<Institution> xmlReadFunction(String route) throws FileNotFoundException, XMLStreamException{
		XMLInputFactory factory = XMLInputFactory.newFactory();
		XMLStreamReader r = factory.createXMLStreamReader(new FileReader(route));
		List<Institution> list = new ArrayList<Institution>();
		Institution tempInstitution = null;
		boolean unittitle = false;
		String name = null;
		while(r.hasNext()) {
			Integer event = r.next();
			switch (event) {
				case XMLStreamConstants.START_ELEMENT:
					String localName = r.getLocalName();
					if(localName.equals("c")){
						
						String level = null;
						String id = null;
						boolean fonds = false;
						for (int i = 0; i < r.getAttributeCount() && !fonds; i++) {
							if (r.getAttributeLocalName(i).trim().equals("level") && !r.getAttributeValue(i).trim().equals("fonds")) {
								if(tempInstitution==null){
									tempInstitution = new Institution();
								}
								level = r.getAttributeValue(i);
							}else if(r.getAttributeLocalName(i).trim().equals("id")){
								if(tempInstitution==null){
									tempInstitution = new Institution();
								}
								id = r.getAttributeValue(i);
							}else if(r.getAttributeLocalName(i).trim().equals("level") && r.getAttributeValue(i).trim().equals("fonds")){
								fonds = true;
								tempInstitution = null;
							}
						}
						if(!fonds){
							Institution returnedInstitution = navigateReadXML(r, level, id);
							if(returnedInstitution!=null){
								list.add(returnedInstitution);
							}
							event = r.getEventType();
						}
						break;
							
					}else if(localName.equals("unittitle") && tempInstitution!=null){
						unittitle = true;
						name = "";
					}
					break;
				case XMLStreamConstants.CHARACTERS:
					if(unittitle){
						name += r.getText();
					}
					break;
				case XMLStreamConstants.END_ELEMENT:
					if (r.getLocalName().trim().equals("ead")){
						r.close();
						return list;
					}else if(unittitle && r.getLocalName().trim().equals("unittitle") && tempInstitution!=null){
						tempInstitution.setName(name);
						name = "";
					}else if(!unittitle && r.getLocalName().trim().equals("c") && tempInstitution!=null && tempInstitution.getLevel()!=null && tempInstitution.getId()!=null){
						list.add(tempInstitution);
						tempInstitution = null;
					}
			}
		}
		r.close();
		return list;
	}
	
//	public static Long countIndexedContentByInstitutionGroupId(String identifier,boolean isGroup){
//		Long counter = new Long(0);
//		try{
//			if(!isGroup){
//				counter = DAOFactory.instance().getFindingAidDAO().countFindingAidsIndexedByInternalArchivalInstitutionId(identifier);
//			}else{
//				counter = recursiveCountIndexedContentByInstitutionGroup(identifier);
//			}
//		}catch(Exception e){
//			log.error("Error trying to obtain if an institution/group has indexed content",e);
//		}
//		return counter;
//	}
//	
//	private static Long recursiveCountIndexedContentByInstitutionGroup(String identifier){
//		Long counter = new Long(0);
//		ArchivalInstitution aiTemp = new ArchivalInstitution();
//		aiTemp = (ArchivalInstitution) DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitutionByInternalAlId(identifier);
//		Set<ArchivalInstitution> archivalInstitutions = aiTemp.getChildArchivalInstitutions();
//		Iterator<ArchivalInstitution> iteratorArchivalInstitutions = archivalInstitutions.iterator();
//		while(iteratorArchivalInstitutions.hasNext()){
//			aiTemp = iteratorArchivalInstitutions.next();
//			if(aiTemp.isGroup()){
//				counter += recursiveCountIndexedContentByInstitutionGroup(aiTemp.getInternalAlId());
//			}else{
//				counter += DAOFactory.instance().getFindingAidDAO().countFindingAidsIndexedByInternalArchivalInstitutionId(aiTemp.getInternalAlId());
//			}
//		}
//		return counter;
//	}
	
}