package eu.apenet.dashboard.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.util.TextProviderHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

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
		deleteFile(srcFile, failOnError);
	}

	public static String getDaysFromMilliseconds(Long milliseconds) {
		double days = milliseconds.doubleValue() / 86400000.0d;
		return ((int) Math.ceil(days)) + "";
	}

	public static void deleteFile(File file, boolean failOnError) throws IOException {
		try {
			if (file.exists()) {
				FileUtils.forceDelete(file);
			} else {
				LOGGER.warn(file + " does not exist");
			}
		} catch (IOException e) {
			if (failOnError) {
				throw e;
			} else {
				LOGGER.error(e.getMessage());
			}
		}
	}

	public static boolean containsPublishedFiles(String identifier, Integer countryIdentifier) {
		ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO()
				.getArchivalInstitutionByInternalAlId(identifier, countryIdentifier);
		return containsPublishedFiles(archivalInstitution);
	}

	public static boolean containsPublishedFiles(ArchivalInstitution archivalInstitution) {
		if (archivalInstitution.isGroup()) {
			boolean hasEads = false;
			Iterator<ArchivalInstitution> childIterator = archivalInstitution.getChildArchivalInstitutions().iterator();
			while (!hasEads && childIterator.hasNext()) {
				hasEads = hasEads || containsPublishedFiles(childIterator.next());
			}
			return hasEads;
		} else {
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

	public static boolean containsEads(String identifier, Integer countryIdentifier) {
		ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO()
				.getArchivalInstitutionByInternalAlId(identifier, countryIdentifier);
		if (archivalInstitution == null)
			return false;
		return containsEads(archivalInstitution);
	}

	public static boolean containsEads(ArchivalInstitution archivalInstitution) {
		if (archivalInstitution.isGroup()) {
			boolean hasEads = false;
			Iterator<ArchivalInstitution> childIterator = archivalInstitution.getChildArchivalInstitutions().iterator();
			while (!hasEads && childIterator.hasNext()) {
				hasEads = hasEads || containsEads(childIterator.next());
			}
			return hasEads;
		} else {
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
					LOGGER.debug("Deleting the registries in archival_institution_oai_pmh entity related to the institution: "
							+ ai.getAiId());
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
				LOGGER.info("Remove AI named: " + ai.getAiname() + " with id: " + ai.getAiId()); // TODO: roll back of this
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
			LOGGER.error("The file " + filePath + " could not be removed: " + APEnetUtilities.generateThrowableLog(e));
			return Action.ERROR;
		} catch (Exception ex) {
			LOGGER.error("The institution " + ai.getAiId() + " could not be removed from database " + APEnetUtilities.generateThrowableLog(ex));
			return Action.ERROR;
		}
	}



	public static void changeSearchable(Ead ead, boolean searchable) {
		changeContainsSearchableItems(ead.getArchivalInstitution(), searchable);
		ead.setPublished(searchable);
		ead.setPublishDate(new Date());

	}

	public static void updateContainsSearchableItemsInAiGroups(ArchivalInstitution archivalInstitution) {
		if (archivalInstitution.isGroup()) {
			updateContainsSearchableItemsInOldAiGroups(archivalInstitution);
		} else {
			updateContainsSearchableItemsInNewAiGroups(archivalInstitution);
		}
	}

	private static void updateContainsSearchableItemsInNewAiGroups(ArchivalInstitution child) {
		ArchivalInstitution parent = child.getParent();
		if (parent != null) {
			if (child.isContainSearchableItems() && !parent.isContainSearchableItems()) {
				parent.setContainSearchableItems(true);
				DAOFactory.instance().getArchivalInstitutionDAO().insertSimple(parent);
				LOGGER.info("AI: '" + parent.getAiname() + "' has now searchable items");
				updateContainsSearchableItemsInNewAiGroups(parent);
			} else if (!child.isContainSearchableItems() && parent.isContainSearchableItems()) {
				boolean containSearchableItems = false;
				for (ArchivalInstitution tempChild : parent.getChildArchivalInstitutions()) {
					if (tempChild != null && tempChild.getAiId() != child.getAiId()) {
						containSearchableItems = containSearchableItems || tempChild.isContainSearchableItems();
					}
				}
				if (!containSearchableItems) {
					parent.setContainSearchableItems(false);
					DAOFactory.instance().getArchivalInstitutionDAO().insertSimple(parent);
					LOGGER.info("AI: '" + parent.getAiname() + "' has now no searchable items left");
					updateContainsSearchableItemsInNewAiGroups(parent);
				}
			}
		}
	}

	private static void updateContainsSearchableItemsInOldAiGroups(ArchivalInstitution oldParent) {
		boolean containSearchableItems = false;
		for (ArchivalInstitution tempChild : oldParent.getChildArchivalInstitutions()) {
			containSearchableItems = containSearchableItems || tempChild.isContainSearchableItems();

		}
		if (!containSearchableItems) {
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
				ai.setContentLastModifiedDate(new Date());
				DAOFactory.instance().getArchivalInstitutionDAO().insertSimple(ai);
				updateContainsSearchableItemsInNewAiGroups(ai);
			} else {
				EadDAO eadDAO = DAOFactory.instance().getEadDAO();
				EadSearchOptions eadSearchOptions = new EadSearchOptions();
				eadSearchOptions.setArchivalInstitionId(ai.getAiId());
				eadSearchOptions.setPublished(true);
				eadSearchOptions.setEadClass(FindingAid.class);
				long numberOfPublishedEads = eadDAO.countEads(eadSearchOptions);
				if (numberOfPublishedEads <= 1) {
					eadSearchOptions.setEadClass(HoldingsGuide.class);
					numberOfPublishedEads += eadDAO.countEads(eadSearchOptions);
					if (numberOfPublishedEads <= 1) {
						eadSearchOptions.setEadClass(SourceGuide.class);
						numberOfPublishedEads += eadDAO.countEads(eadSearchOptions);
						if (numberOfPublishedEads == 1) {
							LOGGER.info("AI: '" + ai.getAiname() + "' has now no searchable items left");
							ai.setContainSearchableItems(searchable);
							ai.setContentLastModifiedDate(new Date());
							ArchivalInstitution parent = ai.getParent();
							if (parent == null) {
								DAOFactory.instance().getArchivalInstitutionDAO().insertSimple(ai);
							} else {
								updateContainsSearchableItemsInNewAiGroups(ai);
							}
						}
					}
				}
				if (numberOfPublishedEads != 1) {
					ai.setContentLastModifiedDate(new Date());
					DAOFactory.instance().getArchivalInstitutionDAO().insertSimple(ai);
				}

			}
		} else {
			ai.setContentLastModifiedDate(new Date());
			DAOFactory.instance().getArchivalInstitutionDAO().insertSimple(ai);
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

	public static void downloadXml(HttpServletRequest request, HttpServletResponse response, File file)
			throws IOException {
		download(request, response, file, MIME_TYPE_APPLICATION_XML);
	}

	public static void download(HttpServletRequest request, HttpServletResponse response, File file, String contentType)
			throws IOException {
		if (file.exists()) {
			response.setContentLength((int) file.length());
			FileInputStream inputStream = new FileInputStream(file); // read the
																		// file
			download(request, response, inputStream, file.getName(), contentType);
		} else {
			LOGGER.error("File does not exist: " + file);
		}
	}

	public static void download(HttpServletRequest request, HttpServletResponse response, InputStream inputStream,
			String name, String contentType) throws IOException {
		String browserType = (String) request.getHeader("User-Agent");
		if (MIME_TYPE_APPLICATION_XML.equals(contentType) && browserType.indexOf("MSIE") > 0) {
			response.setContentType("application/file-download");
		} else {
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

	public static PrintWriter getWriterToDownload(HttpServletRequest request, HttpServletResponse response,
			String name, String contentType) throws IOException {
		String browserType = (String) request.getHeader("User-Agent");
		if (MIME_TYPE_APPLICATION_XML.equals(contentType) && browserType.indexOf("MSIE") > 0) {
			response.setContentType("application/file-download");
		} else {
			response.setContentType(contentType);
		}

		response.setBufferSize(4096);
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-disposition", "attachment;filename=" + name);
		return new PrintWriter(response.getOutputStream());
	}

	public static OutputStream getOutputStreamToDownload(HttpServletRequest request, HttpServletResponse response,
			String name, String contentType) throws IOException {
		String browserType = (String) request.getHeader("User-Agent");
		if (MIME_TYPE_APPLICATION_XML.equals(contentType) && browserType.indexOf("MSIE") > 0) {
			response.setContentType("application/file-download");
		} else {
			response.setContentType(contentType);
		}

		response.setBufferSize(4096);
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-disposition", "attachment;filename=" + name);
		return response.getOutputStream();
	}

	public static Map<String, String> getEmailConfiguration(boolean localized) {
		Map<String, String> results = new HashMap<String, String>();
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			String file_path = "emails/email-configuration.xml";
			InputStream path = Thread.currentThread().getContextClassLoader().getResourceAsStream(file_path);
			Document documento = builder.parse(path);
			if (localized){
				return readConfigFileLocalized(results, documento);
			}else {
				return readConfigFile(results, documento);
			}
			
		} catch (Exception e) {
			return results;
		}
	}
	public static Map<String, String> readConfigFile(Map<String, String> dropDownTable, Node section) {
		if (section != null) {
			NodeList emails = section.getChildNodes();
			String subject = new String();
			String to = new String();
			for (int i = 0; i < emails.getLength(); i++) {
				Node email = emails.item(i);
				if (email.hasChildNodes() && !email.getNodeName().equals("subject")
						&& !email.getNodeName().equals("to")) {
					dropDownTable = readConfigFile(dropDownTable, email);
				} else {
					if (email.getNodeName().equals("subject")) {
						subject = email.getTextContent();			
					}
					if (email.getNodeName().equals("to")) {
						to = email.getTextContent();
					}
				}
			}
			if (section.getNodeName().equals("email")) {
				dropDownTable.put(subject, to);
			}
		}
		return dropDownTable;
	}
	public static Map<String, String> readConfigFileLocalized(Map<String, String> dropDownTable, Node section) {
		ValueStack valueStack = ActionContext.getContext().getValueStack();
		if (section != null) {
			NodeList emails = section.getChildNodes();
			String subject = new String();
			String to = new String();
			for (int i = 0; i < emails.getLength(); i++) {
				Node email = emails.item(i);
				if (email.hasChildNodes() && !email.getNodeName().equals("subject")
						&& !email.getNodeName().equals("to")) {
					dropDownTable = readConfigFileLocalized(dropDownTable, email);
				} else {
					if (email.getNodeName().equals("subject")) {
						subject = TextProviderHelper.getText(email.getTextContent(), null, valueStack);					
					}
					if (email.getNodeName().equals("to")) {
						to = email.getTextContent();
					}
				}
			}
			if (section.getNodeName().equals("email")) {
				dropDownTable.put(subject, to);
			}
		}
		return dropDownTable;
	}
}
