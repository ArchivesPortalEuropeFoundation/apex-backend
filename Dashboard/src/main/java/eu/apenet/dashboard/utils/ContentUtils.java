package eu.apenet.dashboard.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.opensymphony.xwork2.Action;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.persistence.dao.AiAlternativeNameDAO;
import eu.apenet.persistence.dao.ArchivalInstitutionOaiPmhDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.EadSearchOptions;
import eu.apenet.persistence.dao.SentMailRegisterDAO;
import eu.apenet.persistence.dao.UpFileDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.AiAlternativeName;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.ArchivalInstitutionOaiPmh;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SentMailRegister;
import eu.apenet.persistence.vo.SourceGuide;
import eu.apenet.persistence.vo.UpFile;

/**
 * 
 * @author jara
 */
public class ContentUtils {

	public static final String MIME_TYPE_APPLICATION_XML = "application/xml";
	private static final Logger LOGGER = Logger.getLogger(ContentUtils.class);
	// private Partner partner;
	private final static String PREFIX = "_remove";
	private List<FindingAid> fasDeleted = new ArrayList<FindingAid>();
	private List<HoldingsGuide> hgsDeleted = new ArrayList<HoldingsGuide>();
	private List<SentMailRegister> sentMailRegisterList = new ArrayList<SentMailRegister>();

	public List<FindingAid> getFasDeleted() {
		return fasDeleted;
	}

	public void setFasDeleted(List<FindingAid> fasDeleted) {
		this.fasDeleted = fasDeleted;
	}

	public List<HoldingsGuide> getHgsDeleted() {
		return hgsDeleted;
	}

	public void setHgsDeleted(List<HoldingsGuide> hgsDeleted) {
		this.hgsDeleted = hgsDeleted;
	}

	// public Partner getPartner() {
	// return partner;
	// }
	//
	// public ContentUtils(Partner partner) {
	// super();
	// this.partner = partner;
	// }
	//
	// public void setPartner(Partner partner) {
	// this.partner = partner;
	// }

	public static void deleteFile(String path) throws IOException {
		File newfile = new File(path);
		if (newfile.exists())
			deleteFile(path, true);
	}

	public List<SentMailRegister> getSentMailRegisterList() {
		return sentMailRegisterList;
	}

	public void setSentMailRegisterList(List<SentMailRegister> sentMailRegisterList) {
		this.sentMailRegisterList = sentMailRegisterList;
	}

	public static void deleteFile(String path, boolean failOnError) throws IOException {
		File srcFile = new File(path);
		deleteFile(srcFile,failOnError);
	}
	public static void deleteFile(File file, boolean failOnError) throws IOException {
		try {
			if (file.exists()){
				FileUtils.forceDelete(file);
			}else {
				LOGGER.warn(file + " does not exist");
			}
		} catch (IOException e) {
			if (failOnError){
				throw e;
			}else {
				LOGGER.error(e.getMessage());
			}
		}
	}
	public static boolean containsPublishedFiles(String identifier, Integer countryIdentifier){
		ArchivalInstitution archivalInstitution =  DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitutionByInternalAlId(identifier, countryIdentifier);
		return containsPublishedFiles(archivalInstitution);
	}
	public static boolean containsPublishedFiles(ArchivalInstitution archivalInstitution){
		if (archivalInstitution.isGroup()){
			boolean hasEads = false;
			Iterator<ArchivalInstitution> childIterator = archivalInstitution.getChildArchivalInstitutions().iterator();
			while (!hasEads && childIterator.hasNext()){
				hasEads = hasEads || containsPublishedFiles(childIterator.next());
			}
			return hasEads;
		}else {
			EadDAO eadDAO = DAOFactory.instance().getEadDAO();
			EadSearchOptions eadSearchOptions = new EadSearchOptions();
			eadSearchOptions.setArchivalInstitionId(archivalInstitution.getAiId());
			eadSearchOptions.setPublishedToAll(true);
			eadSearchOptions.setEadClass(FindingAid.class);
			boolean hasEads = eadDAO.existEads(eadSearchOptions);
			if (!hasEads) {
				eadSearchOptions.setEadClass(HoldingsGuide.class);
				hasEads = hasEads || eadDAO.existEads(eadSearchOptions);
			}
			if (!hasEads) {
				eadSearchOptions.setEadClass(SourceGuide.class);
				hasEads = hasEads || eadDAO.existEads(eadSearchOptions);
			}
			return hasEads;
		}
	}
	public static boolean containsEads(String identifier, Integer countryIdentifier){
		ArchivalInstitution archivalInstitution =  DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitutionByInternalAlId(identifier, countryIdentifier);
		if (archivalInstitution == null)
			return false;
		return containsEads(archivalInstitution);
	}
	public static boolean containsEads(ArchivalInstitution archivalInstitution){
		if (archivalInstitution.isGroup()){
			boolean hasEads = false;
			Iterator<ArchivalInstitution> childIterator = archivalInstitution.getChildArchivalInstitutions().iterator();
			while (!hasEads && childIterator.hasNext()){
				hasEads = hasEads || containsPublishedFiles(childIterator.next());
			}
			return hasEads;
		}else {
			EadDAO eadDAO = DAOFactory.instance().getEadDAO();
			EadSearchOptions eadSearchOptions = new EadSearchOptions();
			eadSearchOptions.setArchivalInstitionId(archivalInstitution.getAiId());
			eadSearchOptions.setEadClass(FindingAid.class);
			boolean hasEads = eadDAO.existEads(eadSearchOptions);
			if (!hasEads) {
				eadSearchOptions.setEadClass(HoldingsGuide.class);
				hasEads = hasEads || eadDAO.existEads(eadSearchOptions);
			}
			if (!hasEads) {
				eadSearchOptions.setEadClass(SourceGuide.class);
				hasEads = hasEads || eadDAO.existEads(eadSearchOptions);
			}
			return hasEads;
		}
	}
//	public static Long countIndexedContentByInstitutionGroupId(String identifier,boolean isGroup){

//	return counter;
//}
//
//private static Long recursiveCountIndexedContentByInstitutionGroup(String identifier){
//	Long counter = new Long(0);
//	ArchivalInstitution aiTemp = new ArchivalInstitution();
//	aiTemp = (ArchivalInstitution) DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitutionByInternalAlId(identifier);
//	Set<ArchivalInstitution> archivalInstitutions = aiTemp.getChildArchivalInstitutions();
//	Iterator<ArchivalInstitution> iteratorArchivalInstitutions = archivalInstitutions.iterator();
//	while(iteratorArchivalInstitutions.hasNext()){
//		aiTemp = iteratorArchivalInstitutions.next();
//		if(aiTemp.isGroup()){
//			counter += recursiveCountIndexedContentByInstitutionGroup(aiTemp.getInternalAlId());
//		}else{
//			counter += DAOFactory.instance().getFindingAidDAO().countFindingAidsIndexedByInternalArchivalInstitutionId(aiTemp.getInternalAlId());
//		}
//	}
//	return counter;
//}
	// Delete an archival institution and all his files (EAG, FA and HG)
	// associated in the system (DDBB, repository and index)
	public String deleteArchivalInstitution(ArchivalInstitution ai, boolean execute) throws Exception {
		String result = "ok";
		String filePath = null;

		try {
			EadDAO eadDAO = DAOFactory.instance().getEadDAO();
			EadSearchOptions eadSearchOptions = new EadSearchOptions();
			eadSearchOptions.setArchivalInstitionId(ai.getAiId());
			eadSearchOptions.setEadClass(FindingAid.class);
			boolean hasEads = eadDAO.existEads(eadSearchOptions);
			if (!hasEads) {
				eadSearchOptions.setEadClass(HoldingsGuide.class);
				hasEads = hasEads || eadDAO.existEads(eadSearchOptions);
			}
			if (!hasEads) {
				eadSearchOptions.setEadClass(SourceGuide.class);
				hasEads = hasEads || eadDAO.existEads(eadSearchOptions);
			}
			if (hasEads)
				throw new APEnetException("This archival institutions has ead files");
			if (execute) {
				// / FIRST: DELETE FROM DATABASE ///

				// 1. Delete all the Archival Institution Oai-Pmh related to
				// this Institution
				ArchivalInstitutionOaiPmhDAO aiOaiPmhDao = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO();
				List<ArchivalInstitutionOaiPmh> aiOaiPmhs = aiOaiPmhDao.getArchivalInstitutionOaiPmhs(ai.getAiId());
				for (ArchivalInstitutionOaiPmh anAiOaiPmh : aiOaiPmhs) {
					LOGGER.debug("Deleting the registries in archival_institution_oai_pmh entity related to the institution: " + ai.getAiId());
					aiOaiPmhDao.deleteSimple(anAiOaiPmh);
				}

				// 2. Delete the Up Files related to this Institution

				UpFileDAO upFileDao = DAOFactory.instance().getUpFileDAO();
				List<UpFile> upFiles = upFileDao.getUpFiles(ai.getAiId());
				for (UpFile upFile : upFiles) {
					LOGGER.debug("Deleting the registries in upFile entity related to the institution: " + ai.getAiId());
					upFileDao.deleteSimple(upFile);
				}

				// 5. Delete, if exists, all email links related to this
				// institution not completed yet.
				SentMailRegisterDAO sentMailRegisterDao = DAOFactory.instance().getSentMailRegisterDAO();
				setSentMailRegisterList(sentMailRegisterDao.getSentMailRegisterByAi(ai.getAiId()));
				for (SentMailRegister smr : sentMailRegisterList) {
					LOGGER.debug("Deleting the registries in sent_mail_register entity related to the institution: "
							+ ai.getAiId());
					sentMailRegisterDao.deleteSimple(smr);
				}

				// 6. Delete, if exists, all names in other language referenced
				// to ai
				AiAlternativeNameDAO aiAlternativeNameDAO = DAOFactory.instance().getAiAlternativeNameDAO();
				List<AiAlternativeName> listToBeRemoved = aiAlternativeNameDAO.findByAIId(ai);
				LOGGER.debug("Deleting the registries in ai_alternative_name entity related to the institution: "
						+ ai.getAiId());
				for (AiAlternativeName aiAlternativeName : listToBeRemoved) {
					aiAlternativeNameDAO.deleteSimple(aiAlternativeName);
				}

				// 8. Finally delete the archival institution
				LOGGER.debug("Deleting the registries in archival_institution entity related to the institution: "
						+ ai.getAiId());
				DAOFactory.instance().getArchivalInstitutionDAO().deleteSimple(ai);
				LOGGER.debug("Storing the operation 'remove AI' in p_operation entity ");
				storeOperation("Remove AI"); // TODO: roll back of this
												// operation
			}

			// Rename the files in tmp folder
			String tmpDirPath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath()
					+ APEnetUtilities.FILESEPARATOR + ai.getAiId();
			File tmpDirFile = new File(tmpDirPath);
			if (tmpDirFile.exists()) {
				LOGGER.debug("Renaming the tmp folder from the repository related the institution: " + ai.getAiId());
				if (new File(tmpDirPath + "_old").exists()) {
					FileUtils.forceDelete(new File(tmpDirPath + "_old"));
					tmpDirFile.renameTo(new File(tmpDirPath + "_old"));
				} else
					tmpDirFile.renameTo(new File(tmpDirPath + "_old"));
			}

			// Rename the files in Country Repo folder
			String country = ai.getCountry().getIsoname();
			String repoDirPath = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + country
					+ APEnetUtilities.FILESEPARATOR + ai.getAiId();
			File repoDirFile = new File(repoDirPath);
			if (repoDirFile.exists()) {
				LOGGER.debug("Renaming the repo folder from the repository related to the institution: " + ai.getAiId());
				if (new File(repoDirPath + "_old").exists()) {
					FileUtils.forceDelete(new File(repoDirPath + "_old"));
					repoDirFile.renameTo(new File(repoDirPath + "_old"));
				} else
					repoDirFile.renameTo(new File(repoDirPath + "_old"));
			}
			return result;
		} catch (IOException e) {
			LOGGER.error("The file " + filePath + " could not be removed: " + e.getMessage(), e);
			return Action.ERROR;
		} catch (Exception ex) {
			LOGGER.error("The institution " + ai.getAiId() + " could not be removed from database ", ex);
			return Action.ERROR;
		}
	}

	// This method deletes an ead from the Index using its identifier eadid
	// without commit
	/*
	 * public static void deleteFromIndexWithoutCommit(String eadid, int aiId)
	 * throws SolrServerException, IOException{ UpdateSolrServerHolder server =
	 * UpdateSolrServerHolder.getInstance(); server.deleteByQuery("(" +
	 * SolrFields.AI_ID + ":" + aiId + " AND " + SolrFields.EADID + ":\"" +
	 * eadid + "\")"); }
	 */

	public void storeOperation(String op) {
		ChangeControl.logOperation(op);
	}

	public static void changeSearchable(Ead ead, boolean searchable) {
		changeContainsSearchableItems(ead.getArchivalInstitution(), searchable);
		ead.setPublished(searchable);
		ead.setPublishDate(new Date());

	}
	public static void updateContainsSearchableItemsInAiGroups(ArchivalInstitution archivalInstitution){
		if (archivalInstitution.isGroup()){
			updateContainsSearchableItemsInOldAiGroups(archivalInstitution);
		}else {
			updateContainsSearchableItemsInNewAiGroups(archivalInstitution);
		}
	}
	private static void updateContainsSearchableItemsInNewAiGroups(ArchivalInstitution child){
		ArchivalInstitution parent = child.getParent();
		if (parent != null){
			if (child.isContainSearchableItems() && !parent.isContainSearchableItems()){		
				parent.setContainSearchableItems(true);
				DAOFactory.instance().getArchivalInstitutionDAO().insertSimple(parent);
				LOGGER.info("AI: '" + parent.getAiname() + "' has now searchable items");
				updateContainsSearchableItemsInNewAiGroups(parent);
			}else if (!child.isContainSearchableItems() && parent.isContainSearchableItems()){
				boolean containSearchableItems = false;
				for (ArchivalInstitution tempChild: parent.getChildArchivalInstitutions()){
					if (tempChild.getAiId() != child.getAiId()){
						containSearchableItems = containSearchableItems || tempChild.isContainSearchableItems();
					}
				}
				if (!containSearchableItems){
					parent.setContainSearchableItems(false);
					DAOFactory.instance().getArchivalInstitutionDAO().insertSimple(parent);
					LOGGER.info("AI: '" + parent.getAiname() + "' has now no searchable items left");
					updateContainsSearchableItemsInNewAiGroups(parent);
				}
			}
		}
	}
	private static void updateContainsSearchableItemsInOldAiGroups(ArchivalInstitution oldParent){
		boolean containSearchableItems = false;
		for (ArchivalInstitution tempChild: oldParent.getChildArchivalInstitutions()){
				containSearchableItems = containSearchableItems || tempChild.isContainSearchableItems();

		}
		if (!containSearchableItems){
			oldParent.setContainSearchableItems(false);
			DAOFactory.instance().getArchivalInstitutionDAO().insertSimple(oldParent);
			LOGGER.info("AI: '" + oldParent.getAiname() + "' has now no searchable items left");
			updateContainsSearchableItemsInNewAiGroups(oldParent);
		}		
	}
	private static void changeContainsSearchableItems(ArchivalInstitution ai, boolean searchable) {
		if (searchable != ai.isContainSearchableItems()) {
			if (searchable == true) {
				LOGGER.info("AI: '" + ai.getAiname() + "' has now searchable items");
				ai.setContainSearchableItems(searchable);
				DAOFactory.instance().getArchivalInstitutionDAO().insertSimple(ai);
				updateContainsSearchableItemsInAiGroups(ai);
			} else {
				EadDAO eadDAO = DAOFactory.instance().getEadDAO();
				EadSearchOptions eadSearchOptions = new EadSearchOptions();
				eadSearchOptions.setArchivalInstitionId(ai.getAiId());
				eadSearchOptions.setPublished(true);
				eadSearchOptions.setEadClass(FindingAid.class);

				boolean eadExist = eadDAO.existEads(eadSearchOptions);
				if (!eadExist) {
					eadSearchOptions.setEadClass(HoldingsGuide.class);
					eadExist =  eadDAO.existEads(eadSearchOptions);
					if (!eadExist) {
						eadSearchOptions.setEadClass(SourceGuide.class);
						eadExist =  eadDAO.existEads(eadSearchOptions);
						if (!eadExist) {
							LOGGER.info("AI: '" + ai.getAiname() + "' has now no searchable items left");
							ai.setContainSearchableItems(searchable);
							ArchivalInstitution parent = ai.getParent();
							if (parent == null) {
								DAOFactory.instance().getArchivalInstitutionDAO().insertSimple(ai);
							} else {
								changeContainsSearchableItems(parent, searchable);
							}
						}
					}
				}

				// long numberOfEads = DAOFactory.instance().getEadDAO().e
			}
		}

	}

	// This method renames a file and adds the "_remove" prefix to it in order
	// to mark it as deleted
	public static String renameFileToRemove(String path) throws IOException {
		if (path.endsWith(APEnetUtilities.FILESEPARATOR)) {
			// The File is a directory
			// It is necessary to obtain the path without the last file
			// separator
			path = path.substring(0, path.length() - 1);
		}

		File srcFile = new File(path);
		File destFile = new File(path.substring(0, path.lastIndexOf(APEnetUtilities.FILESEPARATOR) + 1) + PREFIX
				+ srcFile.getName());
		if (srcFile.exists() && srcFile.canWrite() && !destFile.exists()) {
			srcFile.renameTo(destFile);
		}
		return destFile.getPath();
	}

	// This method renames a file and removes the "_remove" prefix to it in
	// order to mark it as not deleted
	public static String rollbackRenameFileFromRemove(String path) {
		if (path.endsWith(APEnetUtilities.FILESEPARATOR)) {
			// The File is a directory
			// It is necessary to obtain the path without the last file
			// separator
			path = path.substring(0, path.length() - 1);
		}

		File srcFile = new File(path);
		File destFile = new File(path.substring(0, path.lastIndexOf(APEnetUtilities.FILESEPARATOR) + 1)
				+ srcFile.getName().substring(PREFIX.length()));
		if (srcFile.exists() && srcFile.canWrite() && srcFile.getName().contains(PREFIX) && !destFile.exists()) {
			srcFile.renameTo(destFile);
		}
		return destFile.getPath();
	}

	/**
	 * This method open the Archival Landscape and remove the link relative to
	 * the holdings guide which is the parameter
	 * 
	 * @param holdingsGuide
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerException
	 */
	public static boolean removeHoldingsGuideFromArchivalLandscape(HoldingsGuide holdingsGuide)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		String path = null;
		boolean found = false;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			path = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR
					+ holdingsGuide.getArchivalInstitution().getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR
					+ "AL" + APEnetUtilities.FILESEPARATOR
					+ holdingsGuide.getArchivalInstitution().getCountry().getIsoname() + "AL.xml";
			InputStream sfile = new FileInputStream(path);
			Document doc = dBuilder.parse(sfile);
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName("unittitle");
			Node parentNode = null;
			// 1. Get the C level file that we search
			for (int i = 0; i < nodeList.getLength() && !found; i++) {
				Iterator<AiAlternativeName> names = holdingsGuide.getArchivalInstitution().getAiAlternativeNames()
						.iterator();
				while (names.hasNext()) {
					AiAlternativeName name = names.next();
					if (nodeList.item(i).getTextContent().equals(name.getAiAName())) {
						parentNode = nodeList.item(i).getParentNode().getParentNode(); // should
																						// be
																						// C
																						// level
																						// file
						found = true;
					}
				}
			}
			// 2. Search the link to holdings guide target
			if (found) {
				found = false;// recycle found
				Node target = null;
				NodeList list = parentNode.getChildNodes();
				for (int i = 0; i < list.getLength() && !found; i++) {
					target = list.item(i);
					if (target.getNodeName().toLowerCase().trim().equals("otherfindaid")) {
						NodeList targetChilds = target.getChildNodes();
						for (int j = 0; j < targetChilds.getLength() && !found; j++) {
							if (targetChilds.item(j).getNodeName().toLowerCase().trim().equals("p")) {
								for (int k = 0; k < targetChilds.item(j).getChildNodes().getLength() && !found; k++) { // It
																														// should
																														// be
																														// the
																														// first
																														// result
									Node tempNode = targetChilds.item(j).getChildNodes().item(k);
									if (tempNode.getNodeName().toLowerCase().trim().equals("extref")) {
										NamedNodeMap attributes = tempNode.getAttributes();
										for (int x = 0; x < attributes.getLength() && !found; x++) { // It
																										// should
																										// have
																										// only
																										// xlink:href
																										// attribute
											Node attribute = attributes.item(x);
											if (attribute.getNodeName().toLowerCase().equals("xlink:href")) {
												if (attribute.getTextContent().contains(holdingsGuide.getEadid())) {
													found = true;
													target = targetChilds.item(j);
												}
											}
										}
									}
								}
							}
						}
					}
				}
				// 3. Remove the target node
				if (found) {
					Node otherfindaid = target.getParentNode();
					otherfindaid.removeChild(target);
					boolean hasChildrens = false;
					NodeList childNodes = otherfindaid.getChildNodes();
					for (int i = 0; !hasChildrens && i < childNodes.getLength(); i++) {
						if (childNodes.item(i).getNodeName().toLowerCase().equals("p")) {
							hasChildrens = true;
						}
					}
					// 4. Remove otherfindaid if it has not child
					if (!hasChildrens) {
						otherfindaid.getParentNode().removeChild(otherfindaid);
					}
					// 5. Save results
					TransformerFactory tf = TransformerFactory.newInstance(); // Save
																				// the
																				// document
																				// with
																				// changes
					Transformer transformer = tf.newTransformer();
					transformer.transform(new DOMSource(doc), new StreamResult(new File(path)));
					LOGGER.info("The file was stored with the target node remove (link): HG-> "
							+ holdingsGuide.getEadid());
//					// 5. Finally, it is necessary to update the global Archival
//					// Landscape
//					LOGGER.info("Rebuilding the whole AL");
//					ArchivalLandscape archivalLandscape = new ArchivalLandscape(holdingsGuide.getArchivalInstitution()
//							.getCountry());
//					archivalLandscape.changeAL();

					return found;
				}
			}
		} catch (SAXException e) {
			LOGGER.error("There was an error trying to manage XML with path -> " + path);
			throw e;
		} catch (IOException e) {
			LOGGER.error("There was an error trying to manage file with path -> " + path);
			throw e;
		} catch (TransformerException e) {
			LOGGER.error("There was an error trying to store XML with path -> " + path
					+ "with the target node remove (link): HG-> " + holdingsGuide.getEadid());
			throw e;
		} catch (ParserConfigurationException e) {
			LOGGER.error("Error trying to load a new DocumentBuilder from factory");
			throw e;
		}
		return found;
	}

	// This method inserts the APEnet EAG reference into the local archival
	// landscape for
	// a specific country (country related to the archival institution) and
	// rebuilds the
	// global archival landscape
	public static void addLinkHGtoAL(HoldingsGuide hg) {
		ArchivalInstitution archivalInstitution = hg.getArchivalInstitution();
		LOGGER.info("Modifying Archival Landscape because of HG file modification for Archival Institution id: "
				+ hg.getArchivalInstitution().getAiId());

		// It is necessary to insert eagPath in the local Archival Landscape for
		// the current country
		// and rebuild the global Archival Landscape
		try {

			Boolean found = false;
			Boolean otherfindaidTagFound = false;
			int i;
			int j;
			Node parentNode = null;
			Node grandparentNode = null;
			NodeList nodeList = null;
			NodeList parentNodeList = null;
			i = 0;
			j = 0;

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputStream sfile = new FileInputStream(APEnetUtilities.getConfig().getRepoDirPath()
					+ APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname()
					+ APEnetUtilities.FILESEPARATOR + "AL" + APEnetUtilities.FILESEPARATOR
					+ archivalInstitution.getCountry().getIsoname() + "AL.xml");
			Document doc = dBuilder.parse(sfile);
			doc.getDocumentElement().normalize();

			nodeList = doc.getElementsByTagName("unittitle");
			while ((!found) && (i < nodeList.getLength())) {
				if (nodeList.item(i).getTextContent().equals(archivalInstitution.getAiname())) {
					parentNode = nodeList.item(i).getParentNode();
					grandparentNode = parentNode.getParentNode();
					parentNodeList = grandparentNode.getChildNodes();
					j = 0;
					while ((!otherfindaidTagFound) && (j < parentNodeList.getLength())) {
						if ((parentNodeList.item(j).getNodeName().trim().equals("otherfindaid"))
								&& (parentNodeList.item(j).getNodeType() == Node.ELEMENT_NODE)) {
							otherfindaidTagFound = true;
						} else {
							j = j + 1;
						}
					}
					found = true;
				} else {
					i = i + 1;
				}

			}

			if (found) {
				LOGGER.info("<repository> tag found within the AL file");
				// Everything is OK and the archival institution has been found
				// within the archival landscape
				if (otherfindaidTagFound) {
					// The local archival landscape already has a repository tag
					// so it is necessary to change its value
					Element extrefNode = null;
					NodeList otherNode = doc.getElementsByTagName("otherfindaid");
					Boolean extrefFound = false;
					if (otherNode.item(0).hasChildNodes()) {
						NodeList otherNodeList = otherNode.item(0).getChildNodes();
						String str = "";
						for (int ee = 0; ee < otherNodeList.getLength(); ee++) {
							Node pNode = otherNodeList.item(ee);// .getFirstChild();
							if (pNode.hasChildNodes()) {
								LOGGER.info("<extref> tag found within the AL file");
								Node extrefNode2 = pNode.getFirstChild();
								NamedNodeMap attributes = extrefNode2.getAttributes();
								LOGGER.info("xlink:href attributes exist in <otherfindaid> tag");
								Node attributeHref2 = attributes.getNamedItem("xlink:href");
								str = attributeHref2.getTextContent().trim();
								if (str.equals(hg.getEadid())) {
									extrefFound = true;
								} else {
									// check if the str doesn't exist in ddbb I
									// have to delete
									ArchivalInstitution ai = hg.getArchivalInstitution();
									boolean hgFound = DAOFactory.instance().getEadDAO()
											.isEadidUsed(str, ai.getAiId(), HoldingsGuide.class) != null;
									if (!hgFound) {
										pNode.getParentNode().removeChild(pNode);
									}

								}
							}
						}
						if (!extrefFound)
						// We have to add this one.
						{
							LOGGER.info("Adding p Node into otherfindaid Node.");
							Node otherfindaidNode = otherNode.item(0);
							Node nodep = doc.createElement("p");
							extrefNode = doc.createElement("extref");
							LOGGER.info("Changing xlink:href content to " + hg.getEadid());
							extrefNode.setAttribute("xlink:href", hg.getEadid());
							nodep.appendChild(extrefNode);
							otherfindaidNode.appendChild(nodep);
						}
					}
				} else {
					LOGGER.info("<otherfindaid> tag not found within the AL file. A new <otherfindaid> tag will be inserted");
					// It is necessary to add a new repository tag within did
					// tag
					Element otherfindaidNode = null;
					Element pNode = null;
					Element extrefNode = null;
					Node did = nodeList.item(i).getParentNode();
					Node c = did.getParentNode();

					otherfindaidNode = doc.createElement("otherfindaid");
					otherfindaidNode.setAttribute("encodinganalog", "3.4.5");
					c.appendChild(otherfindaidNode);
					pNode = doc.createElement("p");
					extrefNode = doc.createElement("extref");
					extrefNode.setAttribute("xlink:href", hg.getEadid());
					pNode.appendChild(extrefNode);
					otherfindaidNode.appendChild(pNode);
					// otherfindaidNode.appendChild(extrefNode);

				}

				Result result = new StreamResult(new java.io.File(APEnetUtilities.getConfig().getRepoDirPath()
						+ APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname()
						+ APEnetUtilities.FILESEPARATOR + "AL" + APEnetUtilities.FILESEPARATOR
						+ archivalInstitution.getCountry().getIsoname() + "AL.xml"));
				;
				Source source = new DOMSource(doc);
				Transformer transformer;
				transformer = TransformerFactory.newInstance().newTransformer();
				transformer.transform(source, result);

//				// Finally, it is necessary to rebuild the global Archival
//				// Landscape
//				LOGGER.info("Update the whole AL");
//				ArchivalLandscape archivalLandscape = new ArchivalLandscape(hg.getArchivalInstitution().getCountry());
//				archivalLandscape.changeAL();

				result = null;
				source = null;
				transformer = null;

			} else {
				// The archival institution doesn't exist in the archival
				// landscape
				LOGGER.error("The archival institution doesn't exist within the archical landscape");
			}

			nodeList = null;
			parentNode = null;
			grandparentNode = null;
			parentNodeList = null;
			dbFactory = null;
			dBuilder = null;
			sfile = null;
			doc = null;

		} catch (TransformerConfigurationException e) {
			LOGGER.error("Error configuring Transformer during the creation of "
					+ hg.getArchivalInstitution().getEagPath() + " file. " + e.getMessage());
		} catch (TransformerFactoryConfigurationError e) {
			LOGGER.error("Error configuring Transformer Factory during the creation of "
					+ hg.getArchivalInstitution().getEagPath() + " file. " + e.getMessage());
		} catch (TransformerException e) {
			LOGGER.error("Transformer error during the creation of " + hg.getArchivalInstitution().getEagPath()
					+ " file. " + e.getMessage());
		} catch (IOException e) {
			LOGGER.error("The file " + hg.getArchivalInstitution().getEagPath() + " could not be removed. "
					+ e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

	}
	public static void downloadXml(HttpServletRequest request, HttpServletResponse response, File file) throws IOException{
		download(request, response, file, MIME_TYPE_APPLICATION_XML);
	}
	public static void download (HttpServletRequest request, HttpServletResponse response, File file, String contentType) throws IOException{
		if (file.exists()){
			response.setContentLength((int) file.length());
			FileInputStream inputStream = new FileInputStream(file);  //read the file
        	download(request, response, inputStream, file.getName(), contentType);
		}else {
			LOGGER.error("File does not exist: "  + file);
		}
	}
	public static void download (HttpServletRequest request, HttpServletResponse response, InputStream inputStream, String name, String contentType) throws IOException{
		String browserType=(String)request.getHeader("User-Agent");
		if(MIME_TYPE_APPLICATION_XML.equals(contentType) && browserType.indexOf("MSIE") > 0){
			response.setContentType("application/file-download");
		}else {
			response.setContentType(contentType);
		}

		response.setBufferSize(4096);
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-disposition", "attachment;filename=" + name);
		OutputStream outputStream = response.getOutputStream();
        try {
            int c;
            while ((c = inputStream.read()) != -1) {
            	outputStream.write(c);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
			outputStream.flush();
			outputStream.close();
        }
	}
	public static PrintWriter getWriterToDownload(HttpServletRequest request, HttpServletResponse response, String name, String contentType) throws IOException{
		String browserType=(String)request.getHeader("User-Agent");
		if(MIME_TYPE_APPLICATION_XML.equals(contentType) && browserType.indexOf("MSIE") > 0){
			response.setContentType("application/file-download");
		}else {
			response.setContentType(contentType);
		}

		response.setBufferSize(4096);
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-disposition", "attachment;filename=" + name);
		return new PrintWriter(response.getOutputStream());
	}
	public static OutputStream getOutputStreamToDownload(HttpServletRequest request, HttpServletResponse response, String name, String contentType) throws IOException{
		String browserType=(String)request.getHeader("User-Agent");
		if(MIME_TYPE_APPLICATION_XML.equals(contentType) && browserType.indexOf("MSIE") > 0){
			response.setContentType("application/file-download");
		}else {
			response.setContentType(contentType);
		}

		response.setBufferSize(4096);
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-disposition", "attachment;filename=" + name);
		return response.getOutputStream();
	}
}
