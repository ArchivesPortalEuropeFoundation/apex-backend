package eu.apenet.dashboard.manual;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eu.apenet.commons.exceptions.APEnetRuntimeException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.archivallandscape.ArchivalLandscape;
import eu.apenet.dashboard.utils.ChangeControl;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.FindingAidDAO;
import eu.apenet.persistence.dao.HoldingsGuideDAO;
import eu.apenet.persistence.dao.WarningsDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.apenet.persistence.vo.FileState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.Warnings;

public class HoldingsGuideLogic extends EadLogicAbstract {
	private Logger log = Logger.getLogger(getClass());



	/**
	 * It's called after index process is launched, adds otherfindaid tag with
	 * relative information to partner's AL and main AL.xml
	 * 
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerException
	 */
	public void editALWithOtherFindingAid(Integer id) throws IOException, SAXException, ParserConfigurationException,
			TransformerException {
		// Search in AL, add child <otherfindaid> to c element (previews node
		// brother is <did>)
		HoldingsGuideDAO hgdao = DAOFactory.instance().getHoldingsGuideDAO();
		HoldingsGuide hg = hgdao.findById(id);
		ArchivalLandscape a = new ArchivalLandscape();
		String path = a.getmyPath(a.getmyCountry()) + a.getmyCountry() + "AL.xml";
		InputStream sfile = new FileInputStream(new File(path));

		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		Document doc = docBuilder.parse(sfile);
		sfile.close();

		NodeList listNodes = doc.getElementsByTagName("unittitle");
		boolean changes = false;
		for (int i = 0; i < listNodes.getLength(); i++) {
			if (listNodes.item(i).getFirstChild().getNodeValue().equals(hg.getArchivalInstitution().getAiname())) {
				// It's create the new node child of c (brother of did)
				Element otherfindaid = doc.createElement("otherfindaid");
				otherfindaid.setAttribute("encodinganalog", "3.4.5");
				Element p = doc.createElement("p");
				Element extref = doc.createElement("extref");
				extref.setAttribute("xlink:href", hg.getPathApenetead());
				extref.setAttribute("xlink:title", hg.getTitle());
				p.appendChild(extref);
				otherfindaid.appendChild(p);
				listNodes.item(i).getParentNode().appendChild(otherfindaid); // Saved
				changes = true;
			}
		}
		if (changes) {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(new DOMSource(doc), new StreamResult(new File(path))); // Stored
			a.changeAL(); // Change main AL
		}
	}
}
