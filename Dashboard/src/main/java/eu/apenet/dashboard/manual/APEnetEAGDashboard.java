package eu.apenet.dashboard.manual;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
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
import org.xml.sax.SAXParseException;

import eu.apenet.commons.ResourceBundleSource;
import eu.apenet.commons.StrutsResourceBundleSource;
import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.infraestructure.APEnetEAG;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.utils.XMLUtils;
import eu.apenet.dashboard.archivallandscape.ArchivalLandscape;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.dpt.utils.service.DocumentValidation;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.util.Xsd_enum;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.EadSearchOptions;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.HoldingsGuide;

/**
 * User: Eloy García
 * Date: Sep 23d, 2010
 */
/**
 * This class is in charge of managing each EAG file uploaded or edited by a
 * partner. This class gathers all the operations a partner can do within the
 * Dashboard Content Provider Information option
 */
public class APEnetEAGDashboard{

	// Attributes
	private final Logger log = Logger.getLogger(getClass());

	private String eagPath;
	protected Integer aiId;
	private String id;

	protected List<String> warnings_ead;
	private String name;
	
	public String getEagPath() {
		return eagPath;
	}
	

	public void setEagPath(String eagPath) {
		this.eagPath = eagPath;
	}
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getFilename() {
		return APEnetUtilities.convertToFilename(this.getId()) + ".xml";
	}

	public Integer getAiId() {
		return aiId;
	}


	public void setAiId(Integer aiId) {
		this.aiId = aiId;
	}


	public List<String> getWarnings_ead() {
		return warnings_ead;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	// Constructor
	public APEnetEAGDashboard(Integer aiId, String tempEagPath) {
		
		ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
		ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(aiId);

        this.eagPath = archivalInstitution.getEagPath();        	
        
		this.aiId = aiId;

		if (this.eagPath == null){			
			if (tempEagPath != null) {
	        	this.eagPath = tempEagPath;
	        }
			this.id = "";
			this.name = "";
			if (archivalInstitution != null) {
				this.name = archivalInstitution.getAiname().toString();				
			}
		}
		else {


			if (tempEagPath != null) {
	        	this.eagPath = tempEagPath;
	        }
	        else {
				this.eagPath = APEnetUtilities.getConfig().getRepoDirPath() + this.eagPath;			
	        }
		}
	
			
	}


	// Methods
	public boolean validate() throws APEnetException, SAXException {
		warnings_ead = new ArrayList<String>();
		// EAG file is stored temporally in the location defined in eagPath
		// attribute
		log.debug("Path of EAG: " + this.getEagPath());
		File file = new File(this.getEagPath());
		// Xsd_enum schema = Xsd_enum.XSD_APE_EAG_SCHEMA; //todo: Now we use EAG
		// 2012 for this, but it needs to be completed
		Xsd_enum schema = Xsd_enum.XSD_EAG_2012_SCHEMA;
		try {
			InputStream in = new FileInputStream(file);
			List<SAXParseException> exceptions = DocumentValidation.xmlValidation(in, schema);
			if (exceptions != null) {
				StringBuilder warn;
				for (SAXParseException exception : exceptions) {
					warn = new StringBuilder();
					warn.append("l.").append(exception.getLineNumber()).append(" c.")
							.append(exception.getColumnNumber()).append(": ").append(exception.getMessage())
							.append("<br/>");
					warnings_ead.add(warn.toString());
				}
				return false;
			}
		} catch (SAXException e) {
			throw e;
		} catch (Exception e) {
			throw new APEnetException("Exception while validating an EAG file", e);
		}
		ArchivalInstitution institution = DAOFactory.instance().getArchivalInstitutionDAO().findById(aiId);
		String pattern = institution.getCountry().getIsoname() + "-[a-zA-Z0-9:/\\-]{1,11}";
		String repositoryCode = this.lookingForwardElementContent("/eag/control/recordId");
		boolean validRepositoryCode = Pattern.matches(pattern, repositoryCode);
		if (validRepositoryCode) {
			if (DAOFactory.instance().getArchivalInstitutionDAO().isRepositoryCodeAvailable(repositoryCode, aiId)) {
				return true;
			} else {
				warnings_ead.add("recordId: " + repositoryCode + " already used");
				return false;
			}

		} else {
			warnings_ead.add("recordId does not match pattern: " + pattern);
			return false;
		}
	}

	public Boolean convertToAPEnetEAG() throws APEnetException {
		// EAG file is stored temporally in the location defined in eagPath
		// attribute
		File file = new File(this.getEagPath());
		try {
			InputStream in;
			final String xslfilename = "changeNS.xsl";
			File outputfile = new File(file.getParentFile(), "converted_" + file.getName());
			String xslFilePath = APEnetUtilities.getDashboardConfig().getSystemXslDirPath()
					+ APEnetUtilities.FILESEPARATOR + xslfilename;
			in = new FileInputStream(file);
			TransformationTool.createTransformation(in, outputfile, FileUtils.openInputStream(new File(xslFilePath)),
					null, true, true, null, true, null);
			in.close();
			FileUtils.copyFile(outputfile, file);
		} catch (Exception e) {
			throw new APEnetException("Exception while converting in APEnet EAG", e);
		}
		return true;
	}

	public List<String> showWarnings() {
		return warnings_ead;
	}


	// This method stores a new EAG file or overwrites an existing one which has
	// been uploaded via HTTP
	public String saveEAGviaHTTP(String sourcePath) {

		String value = "";
		Integer overwrittingEAGProcess = 0;
		ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
		ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(this.aiId);

		// First, it is necessary to check if global Archival Landscape and
		// local Archival Landscape are ready for using
		// and check if the Archival Institution is within the local and global
		// Archival Landscape files
		File gArchivalLandscape = new File(APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR
				+ archivalInstitution.getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR + "AL"
				+ APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + "AL.xml");
		File lArchivalLandscape = new File(APEnetUtilities.getDashboardConfig().getArchivalLandscapeDirPath()
				+ APEnetUtilities.FILESEPARATOR + "AL.xml");

		if (gArchivalLandscape.exists() && lArchivalLandscape.exists()
				&& this.isArchivalInstitutionInArchivalLandscape()) {
			// It is necessary to build the path
			this.setEagPath(sourcePath);
			this.setId(this.extractAttributeFromEag("control/recordId", null, true));
			this.setName(this.extractAttributeFromEag("archguide/identity/autform", null, true));
			// It is necessary to check if this EAG has been updated before for
			// another archival institution
			if (this.isEagAlreadyUploaded()) {
				value = "error_eagalreadyuploaded";
			} else {
				this.setEagPath(APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname()
						+ APEnetUtilities.FILESEPARATOR + this.aiId.toString() + APEnetUtilities.FILESEPARATOR + "EAG"
						+ APEnetUtilities.FILESEPARATOR + this.getFilename());
				String storagePath = APEnetUtilities.getConfig().getRepoDirPath() + this.getEagPath();
				String oldEAGPath = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR
						+ archivalInstitution.getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR
						+ this.aiId.toString() + APEnetUtilities.FILESEPARATOR + "EAG" + APEnetUtilities.FILESEPARATOR
						+ "_remove" + getFilename();
				File source = new File(sourcePath);
				File destination = new File(storagePath);

				try {

					// Begin transaction with Database
					HibernateUtil.beginDatabaseTransaction();

					// The new path, autform and repositorycode are stored in
					// archival_institution table
					Date dateNow = new Date();
					archivalInstitution.setRegistrationDate(dateNow);
					archivalInstitution.setEagPath(this.getEagPath());
					archivalInstitution.setAutform(this.getName());
					archivalInstitution.setRepositorycode(this.getId());
					archivalInstitutionDao.insertSimple(archivalInstitution);

					overwrittingEAGProcess = 1;

					// / UPDATE FILE SYSTEM ///

					// Rename the old EAG to _remove...
					log.debug("Renaming the EAG to _remove for the archival institution with id: " + this.getAiId());
					ContentUtils.renameFileToRemove(storagePath);

					overwrittingEAGProcess = 2;

					// Copy the new EAG
					FileUtils.copyFile(source, destination);

					overwrittingEAGProcess = 3;

					// / FINAL COMMITS ///

					// Final commit in the File system
					// It is necessary to remove the old EAG
					ContentUtils.deleteFile(oldEAGPath);

					// Final commit in Database
					HibernateUtil.commitDatabaseTransaction();

					log.info(SecurityContext.get() + "The EAG " + this.getEagPath()
							+ " has been created and stored in repository");

					value = "correct";

				} catch (Exception e) {

					value = "error_eagnotstored";

					if (overwrittingEAGProcess == 0) {
						// There were errors during Database Transaction
						// It is necessary to make a Database rollback
						HibernateUtil.rollbackDatabaseTransaction();
						HibernateUtil.closeDatabaseSession();
						log.error(
								"There were errors during Database Transaction while uploading a new EAG via HTTP protocol for archival institution "
										+ this.getAiId(), e);
					}

					if (overwrittingEAGProcess == 1) {
						// There were errors during File System updating
						// It is necessary to make a Database rollback
						HibernateUtil.rollbackDatabaseTransaction();
						HibernateUtil.closeDatabaseSession();

						// It is necessary to make a File System rollback
						ContentUtils.rollbackRenameFileFromRemove(oldEAGPath);

						log.error(
								"There were errors during File System updating while uploading a new EAG via HTTP protocol for archival institution "
										+ this.getAiId(), e);
					}

					if (overwrittingEAGProcess == 2) {
						// There were errors during File System updating
						// It is necessary to make a Database rollback
						HibernateUtil.rollbackDatabaseTransaction();
						HibernateUtil.closeDatabaseSession();

						// It is necessary to make a File System rollback
						// Removing the new EAG for restoring the old one
						try {
							ContentUtils.deleteFile(destination.getAbsolutePath());
						} catch (Exception ex) {
							log.error(
									"There were errors during File System updating while uploading a new EAG via HTTP protocol for archival institution "
											+ this.getAiId() + ". Error removing the new EAG "
											+ destination.getAbsolutePath(), e);
						}

						ContentUtils.rollbackRenameFileFromRemove(oldEAGPath);

						log.error("There were errors during File System updating while uploading a new EAG via HTTP protocol for archival institution "
								+ this.getAiId());
					}

					if (overwrittingEAGProcess == 3) {
						// There were errors during Database, Index or File
						// system commit
						HibernateUtil.closeDatabaseSession();
						log.error(
								"FATAL ERROR. Error during Database or File System commits when an EAG file was uploading via HTTP protocol. Please, check inconsistencies in Database and File system for archival institution which id is: "
										+ this.getAiId(), e);
					}
					log.error(e.getMessage());
				}
			}
			if (value.equals("correct")) {
				// It is necessary to insert eagPath in the local Archival
				// Landscape for the current country and rebuild the global
				// Archival Landscape
				value = this.modifyArchivalLandscape();
			}
		} else {
			if (!gArchivalLandscape.exists()) {
				log.error("gArchivalLandscape does not exist: " + gArchivalLandscape.getAbsolutePath());
			}
			if (!lArchivalLandscape.exists()) {
				log.error("lArchivalLandscape does not exist: " + lArchivalLandscape.getAbsolutePath());
			}
			if (!isArchivalInstitutionInArchivalLandscape()) {
				log.error("No Archival institution in Archival Landscape.");
			}
			value = "error_archivallandscape";
		}
		return value;
	}

	// This method inserts the APEnet EAG reference into the local archival
	// landscape for
	// a specific country (country related to the archival institution) and
	// rebuilds the
	// global archival landscape
	public String modifyArchivalLandscape() {
		String value = "";
		ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
		ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(this.aiId);
		log.info("Modifying Archival Landscape because of EAG file modification for Archival Institution id: "
				+ this.aiId);

		// It is necessary to insert eagPath in the local Archival Landscape for
		// the current country
		// and rebuild the global Archival Landscape
		try {

			Boolean found = false;
			Boolean repositoryTagFound = false;
			int i;
			int j;
			Node parentNode = null;
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
				if (nodeList.item(i).getTextContent().trim().equals(archivalInstitution.getAiname())) {
					parentNode = nodeList.item(i).getParentNode();
					parentNodeList = parentNode.getChildNodes();
					j = 0;
					while ((!repositoryTagFound) && (j < parentNodeList.getLength())) {
						if ((parentNodeList.item(j).getNodeName().trim().equals("repository"))
								&& (parentNodeList.item(j).getNodeType() == Node.ELEMENT_NODE)) {
							repositoryTagFound = true;
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
				log.info("<repository> tag found within the AL file");
				// Everything is OK and the archival institution has been found
				// within the archival landscape
				if (repositoryTagFound) {
					// The local archival landscape already has a repository tag
					// so it is necessary to change its value
					Element extrefNode = null;
					Node repository = parentNodeList.item(j);
					NodeList repositoryChildren = repository.getChildNodes();
					Boolean extrefFound = false;
					for (int k = 0; k < repositoryChildren.getLength(); k++) {

						if (repositoryChildren.item(k).getNodeName().equals("extref")) {
							// There is already a extref tag and it is only
							// needed to change the values
							log.info("<extref> tag found within the AL file");
							extrefFound = true;
							if (repositoryChildren.item(k).hasAttributes()) {
								// extref has children
								NamedNodeMap attributes = repositoryChildren.item(k).getAttributes();
								Node attributeHref = attributes.getNamedItem("xlink:href");
								Node attributeTitle = attributes.getNamedItem("xlink:title");

								if (attributeHref != null && attributeTitle != null) {
									// xlink:href attribute exits and
									// xlink:title attribute exits
									log.info("xlink:href and xlink:title attributes exist in <repository> tag");
									attributeHref.setTextContent(APEnetUtilities.getConfig().getRepoDirPath()
											+ APEnetUtilities.FILESEPARATOR
											+ archivalInstitution.getCountry().getIsoname()
											+ APEnetUtilities.FILESEPARATOR + aiId.toString()
											+ APEnetUtilities.FILESEPARATOR + "EAG" + APEnetUtilities.FILESEPARATOR
											+ getFilename());
									attributeTitle.setTextContent(archivalInstitution.getAiname() + " EAG");
									log.info("Changing xlink:href content to "
											+ APEnetUtilities.getConfig().getRepoDirPath()
											+ APEnetUtilities.FILESEPARATOR
											+ archivalInstitution.getCountry().getIsoname()
											+ APEnetUtilities.FILESEPARATOR + aiId.toString()
											+ APEnetUtilities.FILESEPARATOR + "EAG" + APEnetUtilities.FILESEPARATOR
											+ getFilename());
									log.info("Changing xlink:title content to " + archivalInstitution.getAiname()
											+ " EAG");

								} else {
									// xlink:href attribute exits or xlink:title
									// attribute exits
									// Removing old repository and adding a new
									// one
									log.info("xlink:href or xlink:title attributes don't exist. Removing old <repository> tag and adding a new one");
									Node parent = repositoryChildren.item(k).getParentNode();
									Node extrefOld = repositoryChildren.item(k);
									extrefNode = doc.createElement("extref");
									extrefNode.setAttribute("xlink:href", APEnetUtilities.getConfig().getRepoDirPath()
											+ APEnetUtilities.FILESEPARATOR
											+ archivalInstitution.getCountry().getIsoname()
											+ APEnetUtilities.FILESEPARATOR + aiId.toString()
											+ APEnetUtilities.FILESEPARATOR + "EAG" + APEnetUtilities.FILESEPARATOR
											+ getFilename());
									extrefNode.setAttribute("xlink:title", archivalInstitution.getAiname() + " EAG");
									parent.replaceChild(extrefNode, extrefOld);
									log.info("xlink:href attribute added with content "
											+ APEnetUtilities.getConfig().getRepoDirPath()
											+ APEnetUtilities.FILESEPARATOR
											+ archivalInstitution.getCountry().getIsoname()
											+ APEnetUtilities.FILESEPARATOR + aiId.toString()
											+ APEnetUtilities.FILESEPARATOR + "EAG" + APEnetUtilities.FILESEPARATOR
											+ getFilename());
									log.info("xlink:title attribute added with content "
											+ archivalInstitution.getAiname() + " EAG");
								}
							}
						}
					}

					if (!extrefFound) {
						// If there is not a extref tag, it is needed to create
						// a new one
						extrefNode = doc.createElement("extref");
						extrefNode.setAttribute("xlink:href", APEnetUtilities.getConfig().getRepoDirPath()
								+ APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname()
								+ APEnetUtilities.FILESEPARATOR + aiId.toString() + APEnetUtilities.FILESEPARATOR
								+ "EAG" + APEnetUtilities.FILESEPARATOR + getFilename());
						extrefNode.setAttribute("xlink:title", archivalInstitution.getAiname() + " EAG");
						repository.appendChild(extrefNode);
					}

				} else {
					log.info("<repository> tag not found within the AL file. A new <repository> tag will be inserted");
					// It is necessary to add a new repository tag within did
					// tag
					Element repositoryNode = null;
					Element extrefNode = null;
					Node did = nodeList.item(i).getParentNode();

					repositoryNode = doc.createElement("repository");
					did.appendChild(repositoryNode);
					extrefNode = doc.createElement("extref");
					extrefNode.setAttribute("xlink:href", APEnetUtilities.getConfig().getRepoDirPath()
							+ APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname()
							+ APEnetUtilities.FILESEPARATOR + aiId.toString() + APEnetUtilities.FILESEPARATOR + "EAG"
							+ APEnetUtilities.FILESEPARATOR + getFilename());
					extrefNode.setAttribute("xlink:title", archivalInstitution.getAiname() + " EAG");
					repositoryNode.appendChild(extrefNode);

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

				// Finally, it is necessary to rebuild the global Archival
				// Landscape
				log.info("Rebuilding the whole AL");
				ArchivalLandscape archivalLandscape = new ArchivalLandscape(archivalInstitution.getCountry());
				archivalLandscape.changeAL();

				result = null;
				source = null;
				transformer = null;

				value = "correct";
			} else {
				// The archival institution doesn't exist in the archival
				// landscape
				log.error("The archival institution doesn't exist within the archical landscape");
				value = "error";
			}

			nodeList = null;
			parentNode = null;
			parentNodeList = null;
			dbFactory = null;
			dBuilder = null;
			sfile = null;
			doc = null;

		} catch (TransformerConfigurationException e) {
			log.error("Error configuring Transformer during the creation of " + this.getEagPath() + " file. "
					+ e.getMessage());
			value = "error";
		} catch (TransformerFactoryConfigurationError e) {
			log.error("Error configuring Transformer Factory during the creation of " + this.getEagPath() + " file. "
					+ e.getMessage());
			value = "error";
		} catch (TransformerException e) {
			log.error("Transformer error during the creation of " + this.getEagPath() + " file. " + e.getMessage());
			value = "error";
		} catch (IOException e) {
			log.error("The file " + this.getEagPath() + " could not be removed. " + e.getMessage());
			value = "error";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			value = "error";
		}

		archivalInstitutionDao = null;
		archivalInstitution = null;

		return value;
	}

	// This method check the existence of the current Archival Institution
	// within
	// the Global and Local (for its country) Archival Landscape
	private Boolean isArchivalInstitutionInArchivalLandscape() {

		Boolean value = false;
		Boolean found = false;
		ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
		ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(this.aiId);
		int i = 0;
		NodeList nodeList = null;

		log.debug("Archival Institution AI NAME: '" + archivalInstitution.getAiname() + "'");

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			String alPath = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR
					+ archivalInstitution.getCountry().getIsoname() + APEnetUtilities.FILESEPARATOR + "AL"
					+ APEnetUtilities.FILESEPARATOR + archivalInstitution.getCountry().getIsoname() + "AL.xml";
			InputStream sfile = new FileInputStream(alPath);
			Document doc = dBuilder.parse(sfile);
			doc.getDocumentElement().normalize();

			nodeList = doc.getElementsByTagName("unittitle");
			while ((!found) && (i < nodeList.getLength())) {
				log.debug("unittitle: '" + nodeList.item(i).getTextContent() + "'");
				if (nodeList.item(i).getTextContent().trim().equals(archivalInstitution.getAiname())) {
					found = true;
				} else {
					i = i + 1;
				}

			}

			if (found) {
				String archivalLandscapePath = APEnetUtilities.getDashboardConfig().getArchivalLandscapeDirPath()
						+ APEnetUtilities.FILESEPARATOR + "AL.xml";
				sfile = new FileInputStream(archivalLandscapePath);
				doc = dBuilder.parse(sfile);
				doc.getDocumentElement().normalize();
				found = false;
				i = 0;

				nodeList = doc.getElementsByTagName("unittitle");
				while ((!found) && (i < nodeList.getLength())) {
					log.debug("unittitle: '" + nodeList.item(i).getTextContent() + "'");
					if (nodeList.item(i).getTextContent().trim().equals(archivalInstitution.getAiname())) {
						found = true;
					} else {
						i = i + 1;
					}

				}

				if (found) {
					value = true;
				} else {
					log.info("Archival institution '" + archivalInstitution.getAiname()
							+ "' was not found in archival landscape ('" + archivalLandscapePath + "')");
					value = false;
				}
			} else {
				log.info("Archival institution '" + archivalInstitution.getAiname()
						+ "' was not found in archival landscape ('" + alPath + "')");
				value = false;
			}
		} catch (Exception e) {
			log.error("The Archival Landscape (local, global or both) doesn't include this Archival Institution "
					+ archivalInstitution.getAiname(), e);
			value = false;
		}

		return value;
	}

	private Boolean isEagAlreadyUploaded() {
		// It will be necessary to check eagid and autform values in
		// archival_institution table
		Boolean eagAlreadyUploaded = false;

		ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
		List<ArchivalInstitution> archivalInstitutionList = archivalInstitutionDao
				.getArchivalInstitutionsByAutform(this.getName());
		if (archivalInstitutionList.size() > 0) {
			// There is at least one institution with the same EAG uploaded
			for (int i = 0; i < archivalInstitutionList.size(); i++) {
				// If the institution found is the same that the institution
				// which wants to change the EAG file, then the operation is
				// permitted
				if (archivalInstitutionList.get(i).getAiId() != this.getAiId()) {
					log.warn(SecurityContext.get() + "EAG uploaded that contains the name " + this.getName() + " that already exist in another archival institution with id: " + archivalInstitutionList.get(i).getAiId());
					eagAlreadyUploaded = true;
				}
			}
		}
		warnings_ead.add("Archival institution name already used: " + this.getName() + " already used");
		
		return eagAlreadyUploaded;
	}

	//This method extracts the content within a tag inside an EAG (the first one) or
	//the content within an attribute for the tag
	public String extractAttributeFromEag(String element, String attribute, boolean isReturningFirstInstance) {
        final String CONVERTED_FLAG = "Converted_APEnet_EAG_version_";
        XMLStreamReader input = null;
	    InputStream sfile = null;
        XMLInputFactory xmlif = (XMLInputFactory) XMLInputFactory.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);

        try {
            sfile = new FileInputStream(this.eagPath);
            input = (XMLStreamReader) xmlif.createXMLStreamReader(sfile);

            boolean abort = false;
            boolean isInsideElement = false;
            boolean isInsidePath = false;
            boolean wasInsidePath = false;
		    String importantData = "";

            String[] pathElements = element.split("/");
            int lenghtPath = pathElements.length;
            int pointerPath = 0;
            
            log.debug("Checking EAG file, looking for element " + element + ", and attribute " + ((attribute==null)?"null":attribute) + ", path begins with " + pathElements[0]);
            while (!abort && input.hasNext()) {
                switch (input.getEventType()) {
					case XMLEvent.START_DOCUMENT:
						break;
					case XMLEvent.START_ELEMENT:
                        if(input.getLocalName().equals(pathElements[pointerPath])){
                            isInsidePath = true;
                            wasInsidePath = true;
                            if(pointerPath == lenghtPath-1){
                                isInsideElement = true;
                                if(attribute != null){
                                    for(int attributeNb = 0; attributeNb < input.getAttributeCount(); attributeNb++){
                                        if(input.getAttributeLocalName(attributeNb).equals(attribute)){
                                            log.debug("Returning " + input.getAttributeValue(attributeNb));
                                            return input.getAttributeValue(attributeNb);
                                        }
                                    }
                                    log.debug("Returning error");
                                    return "error";
                                }
                            }
                            pointerPath++;
                        }
						break;
					case XMLEvent.CHARACTERS:
						if (isInsideElement) {
							importantData = input.getText();
							if (importantData.startsWith(CONVERTED_FLAG)) {
								return "true";
							} else if(isReturningFirstInstance){
                                return importantData;
                            }
						}
						break;
					case XMLEvent.CDATA:
						break;
					case XMLEvent.END_ELEMENT:
                        if(isInsidePath && input.getLocalName().equals(pathElements[pointerPath-1])){
                            pointerPath--;
                            isInsideElement = false;
                            if(pointerPath == 0)
                                isInsidePath = false;
                        }
                        if(!isInsidePath && wasInsidePath)
                            abort = true;    
						break;
                }
                if (input.hasNext())
                    input.next();
            }
        } catch (Exception e) {
            log.error("Error parsing StAX for file " + this.eagPath, e);
        } finally {
            try {
                input.close();
                sfile.close();
            } catch (Exception e) {
                log.error("Error closing streams" + e.getMessage(), e);
            }
        }
        log.debug("Returning error");
        return "error";
	}
	
	//This method returns all the same elements of an EAG xml file found
	public String lookingForward(String element,String attribute,String value){
		XMLStreamReader input = null;
	    InputStream sfile = null;
	    XMLInputFactory xmlif = (XMLInputFactory) XMLInputFactory.newInstance();
	    xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
	    xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
	    xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
	    xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
	    try {
	        sfile = new FileInputStream(this.eagPath);
	        input = (XMLStreamReader) xmlif.createXMLStreamReader(sfile);
	        String[] pathElements = element.split("/");
	        while (input.hasNext()) {
	            switch (input.getEventType()) {
					case XMLEvent.START_ELEMENT:
						for(int i=0;i<pathElements.length;i++){
							if(input.getLocalName().equals(pathElements[i])){ //***********//
								for(int x=0;x<input.getAttributeCount();x++){
									if(input.getAttributeLocalName(x).equals(attribute) && input.getAttributeValue(x).equals(value)){
										input.close();
							            sfile.close();
										return value;
									}
								}
								
							}
						}
						break;
	            }
	            if (input.hasNext()){
	                input.next();
	            }
	        }
	        return "error"; //The element is not found
	    } catch (Exception e) {
	        log.error("Error parsing StAX for file " + this.eagPath, e);
	    } finally {
	        try {
	            input.close();
	            sfile.close();
	        } catch (Exception e) {
	            log.error("Error closing streams" + e.getMessage(), e);
	        }
	    }
	    log.debug("Returning error");
	    return "error"; //Error
	}
	public String lookingForwardElementContent(String element) {
		String text = null;
        XMLStreamReader input = null;
	    InputStream sfile = null;
        XMLInputFactory xmlif = (XMLInputFactory) XMLInputFactory.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);

        try {
            sfile = new FileInputStream(this.eagPath);
            input = xmlif.createXMLStreamReader(sfile);

            boolean exit = false;
            boolean found = true;
            String[] pathElements = null;
            if(input!=null && element!=null){
            	if(element.contains("/")){ //check input parameters
            		if(element.startsWith("/")){ 
            			element = element.substring(1);
            		}
            		if(element.endsWith("/") && element.length()>2){
            			element = element.substring(0,element.length()-2);
            		}
            		pathElements = element.split("/");
            	}else{
            		pathElements = new String[1];
            		pathElements[0] = element;
            	}
                List<String> currentElement = new ArrayList<String>();
                log.debug("Checking EAG file, looking for element " + element + ", path begins with " + pathElements[0]);
                while (!exit && input.hasNext()) {
                	switch (input.getEventType()) {
                	case XMLEvent.START_ELEMENT:
                		currentElement.add(input.getLocalName().toString());
                		if(currentElement.size()==pathElements.length){
                			found = true;
                			for(int i=0;i<pathElements.length && found;i++){
                				found = (pathElements[i].trim().equals(currentElement.get(i).trim()));
                			}
                			text = "";
                		}
                		break;
                	case XMLEvent.CHARACTERS:
                	case XMLEvent.CDATA:
                		if(found){
                			text += input.getText();
                		}
                		break;
                	case XMLEvent.END_ELEMENT:
                		currentElement.remove(currentElement.size()-1);
                		if(found){
                			exit = true;
                		}
                		break;
                	}
                	if (input.hasNext()){
    	                input.next();
    	            }
                }
            }
        }catch(Exception e){
        	log.error("Exception getting "+element,e);
        }
		return text;
	}
}
