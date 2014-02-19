/**
 * 
 */
package eu.apenet.dashboard.manual.eag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderGeometry;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.GeocoderStatus;
import com.google.code.geocoder.model.LatLng;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.CoordinatesDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Coordinates;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 * @author fernando
 * 
 */
public class Eag2012GeoCoordinatesAction extends AbstractInstitutionAction {
	// Logger.
	private final Logger log = Logger.getLogger(getClass());

	// Attributes for Coordinates
	private double co_archId;	// Archival institution ID.
	private String co_name;		// Institution or repository name.
	private String co_street;   // Institution or repository address (street).
	private String co_postalCity;   // Institution or repository address (municipalityPostalcode).
	private String co_country;   // Institution or repository address (country).
	private double co_lat;		// Institution or repository latitude.
	private double co_lon;		// Institution or repository longitude.

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public Eag2012GeoCoordinatesAction() {
		this.setCo_archId(0.0);
		this.setCo_name("");
		this.setCo_street("");
		this.setCo_postalCity("");
		this.setCo_country("");
		this.setCo_lat(0.0);
		this.setCo_lon(0.0);
	}

	// Methods
	public String execute() throws Exception {
		log.debug("Start process to fill Coordinates table.");
		ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
		List<ArchivalInstitution> archivalInstitution = archivalInstitutionDao.findAll();
		
		for (int i = 0; i < archivalInstitution.size(); i++) {
			this.insertCoordinates(archivalInstitution.get(i));
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				log.error("Error trying to sleep thread for action 'eag2012GeoCoordinates' at iteration: " + (i + 1));
			}
		}// for I
		log.debug("End process to fill Coordinates table.");
		return SUCCESS;
	}

	/**
	 * Method to add the coordinates to the coordinates table.
	 *
	 * @param archivalInstitution Archival institution to recover the EAG file.
	 */
	public void insertCoordinates(final ArchivalInstitution archivalInstitution) {
		String strPath = "";
		try {
			if (archivalInstitution.getEagPath() != null) {
				strPath = APEnetUtilities.getConfig().getRepoDirPath() + archivalInstitution.getEagPath();
				File  EAGfile = new File(strPath);
				if (EAGfile.exists()){
					this.setCo_archId(archivalInstitution.getAiId()); //Co_archId
					this.log.debug("Process institution with id: " + this.getCo_archId());
					this.log.debug("Process EAG in path: " + strPath);
					NodeList nodeRepositoryList = null;
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					dbFactory.setNamespaceAware(true);
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					InputStream sfile = new FileInputStream(strPath);
					Document doc = dBuilder.parse(sfile);
			
					doc.getDocumentElement().normalize();
					nodeRepositoryList = doc.getElementsByTagNameNS("http://www.archivesportaleurope.net/Portal/profiles/eag_2012/", "repository");
					
					if (nodeRepositoryList != null && nodeRepositoryList.getLength() > 0) {
						for (int j = 0; j < nodeRepositoryList.getLength(); j++) {
							Node repositoryNode = nodeRepositoryList.item(j);
							NodeList repositoryChildsList = repositoryNode.getChildNodes();
							boolean isVisitorAddress = false;
							// Reset values.
							boolean isNameRecovered = false;
							boolean isLocationRecovered = false;
							this.setCo_name("");
							//this.setCo_address("");
							this.setCo_street("");
							this.setCo_postalCity("");
							this.setCo_country("");
							this.setCo_lat(0.0);
							this.setCo_lon(0.0);
							for (int k=0; k< repositoryChildsList.getLength() && (!isNameRecovered || !isLocationRecovered); k++) {
								Node repositoryChildNode = repositoryChildsList.item(k);
			
								if (repositoryChildNode.getNodeName().equalsIgnoreCase("repositoryName")) {
									//repositoryName
									this.setCo_name(repositoryChildNode.getTextContent()); //Co_name
									isNameRecovered = true;
								} else if (repositoryChildNode.getNodeName().equalsIgnoreCase("location")) {
									NamedNodeMap repositoryChildAttributesMap = repositoryChildNode.getAttributes();
									isVisitorAddress = false;
			
									for (int l = 0; l < repositoryChildAttributesMap.getLength(); l++) {
										Node attributeNode = repositoryChildAttributesMap.item(l);
										if (attributeNode.getNodeName().equalsIgnoreCase("localType")) {
											if (attributeNode.getTextContent().equalsIgnoreCase("visitors address")) {
												isVisitorAddress = true;
											}
										} else if (attributeNode.getNodeName().equalsIgnoreCase("latitude")) {
											String latitude = attributeNode.getTextContent();
											try {
												this.setCo_lat(Double.parseDouble(latitude)); //Co_lat
											} catch (Exception e) {
												log.debug(strPath + " Error: " + this.getCo_name() + ": " + e.toString());
											}
										} else if (attributeNode.getNodeName().equalsIgnoreCase("longitude")) {
											String longitude = attributeNode.getTextContent();
											try {
												this.setCo_lon(Double.parseDouble(longitude)); //Co_lon
											} catch (Exception e) {
												log.debug(strPath + " Error: " + this.getCo_name() + ": " + e.toString());
											}
										}
									}// for L
									
									if (isVisitorAddress) {
										boolean isStreetRecovered = false;
										boolean isMunicipalityRecovered = false;
										NodeList visitorsAddressChildsList = repositoryChildNode.getChildNodes();
										String municipalityPostalcode = "";
										String street = "";
										String country = "";
										for (int l=0; l< visitorsAddressChildsList.getLength() && (!isStreetRecovered || !isMunicipalityRecovered); l++) {
											Node visitorsAddressChildNode = visitorsAddressChildsList.item(l);
			
											if (visitorsAddressChildNode.getNodeName().equals("municipalityPostalcode")) {
												//municipalityPostalcode
												municipalityPostalcode = visitorsAddressChildNode.getTextContent().trim();
												isMunicipalityRecovered = true;
											} else if (visitorsAddressChildNode.getNodeName().equals("street")) {
												//street
												street = visitorsAddressChildNode.getTextContent().trim();
												isStreetRecovered = true;
											} else if (visitorsAddressChildNode.getNodeName().equals("country")) {
												//country
												country = visitorsAddressChildNode.getTextContent().trim();
											}
										}// for L
										this.setCo_street(street); //setCo_street
										this.setCo_postalCity(municipalityPostalcode); //setCo_postalCity
										this.setCo_country(country); //setCo_country
										if (isStreetRecovered || isMunicipalityRecovered) {
											isLocationRecovered = true;
										}
									}// if isVisitorAddress
								}//child.getLocalName().equals("location")
			
								if(this.getCo_name() == null){
									this.setCo_name(archivalInstitution.getAiname());
									isNameRecovered = true;
								} else if (this.getCo_name().trim().isEmpty()) {
									this.setCo_name(archivalInstitution.getAiname());
									isNameRecovered = true;
								}
			
								if (isNameRecovered && isLocationRecovered) {
									log.debug("Start to fill the new cell for Coordinates table.");
									CoordinatesDAO coordinatesDAO = DAOFactory.instance().getCoordinatesDAO();
									Coordinates coordinates = new Coordinates();
									// Archival institution.
									coordinates.setArchivalInstitution(archivalInstitution);
									// Name of the current archival institution/repository.
									coordinates.setNameInstitution(this.getCo_name());
									// street for the current archival institution/repository.
									coordinates.setStreet(this.getCo_street());
									// municipalityPostalCode for the current archival institution/repository.
									coordinates.setPostalCity(this.getCo_postalCity());
									//country for the current archival institution/repository.
									coordinates.setCountry(this.getCo_country());
			
									// Check the coordinates or recover it if necessary.
									if (this.getCo_lat() == 0.0
											|| this.getCo_lon() == 0.0) {
										Geocoder geocoder = new Geocoder();
										String address = this.getCo_street() + ", " + this.getCo_postalCity() + ", " + this.getCo_country();
										GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(address).getGeocoderRequest();
										GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
										if (geocoderResponse.getStatus().equals(GeocoderStatus.OK)) {
											List<GeocoderResult> geocoderResultList = geocoderResponse.getResults();
			
											// Always recover the first result.
											if (geocoderResultList.size() > 0) {
												GeocoderResult geocoderResult = geocoderResultList.get(0);
			
												// get Geometry Object
												GeocoderGeometry geocoderGeometry = geocoderResult.getGeometry();
												// get Location Object
												LatLng latLng = geocoderGeometry.getLocation();
												this.setCo_lat(latLng.getLat().doubleValue());
												this.setCo_lon(latLng.getLng().doubleValue());
											}
										}
									}

									double latTrunc = (double) Math.round(this.getCo_lat() * 10000000) / 10000000;
									double longTrunc = (double) Math.round(this.getCo_lon() * 10000000) / 10000000;

									this.setCo_lat(latTrunc);
									this.setCo_lon(longTrunc);
									
									if (this.getCo_lat() != 0.0 && this.getCo_lon() != 0.0) {
										//do not loop blanks and compare actual with existing
										List<Coordinates> coordinatesList = DAOFactory.instance().getCoordinatesDAO().getCoordinates();
										if (coordinatesList != null && !coordinatesList.isEmpty()) {
											// Check if current coordinates exist in database.
											while(this.loopCoord(coordinatesList));
										}
									}

									// Latitude (if exists) for the current archival institution/repository.
									if (this.getCo_lat() != 0.0) {
										coordinates.setLat(this.getCo_lat());
									}
									// Longitude (if exists) for the current archival institution/repository.
									if (this.getCo_lon() != 0.0) {
										coordinates.setLon(this.getCo_lon());
									}

									// Try to add the new value to coordinates table.
									try {
										JpaUtil.beginDatabaseTransaction();
										coordinatesDAO.insertSimple(coordinates);
										JpaUtil.commitDatabaseTransaction();
										log.info("insert: " + coordinates.getNameInstitution() + " with (" + coordinates.getLat() + "," + coordinates.getLon() + ")");
									} catch (Exception e) {
										// Rollback current database transaction.
										JpaUtil.rollbackDatabaseTransaction();
										log.error("Error trying to insert: " + coordinates.getNameInstitution());
										log.error(e.getCause());
									}
								}
							}// for K
						}//for J
					}// if nodeAutList
				}//if EAGfile.exists
			}// if eagPath() != null
		} catch (ParserConfigurationException pcEx) {
			log.error("Error when creating the parser configuration." );
			log.error(pcEx.getCause());
		} catch (FileNotFoundException fnfEx) {
			log.error("Error file " + strPath + " not found in system.");
			log.error(fnfEx.getCause());
		} catch (IOException ioEx) {
			log.error("Input/Outpur error with file " + strPath);
			log.error(ioEx.getCause());
		} catch (SAXException saxEx) {
			log.error("SAX exception with file " + strPath);
			log.error(saxEx.getCause());
		}
	}

	/**
	 * Method to check if the current coordinates exist in database.
	 *
	 * @param coordinatesList List of elements in coordinates table.
	 * @return Process should end (false) or start again (true).
	 */
	private boolean loopCoord(List<Coordinates> coordinatesList) {
		Iterator<Coordinates> coordinatesIt = coordinatesList.iterator();
		while (coordinatesIt.hasNext()) {
			Coordinates coordinatesCurrent = coordinatesIt.next();
			double currentLatTrunc = (double) Math.round(coordinatesCurrent.getLat() * 10000000) / 10000000;
			double currentLongTrunc = (double) Math.round(coordinatesCurrent.getLon() * 10000000) / 10000000;	

			if (this.getCo_lat()==currentLatTrunc && this.getCo_lon()==currentLongTrunc){
				this.setCo_lon(this.getCo_lon()+0.00005);
				double newLongTrunc = (double) Math.round(this.getCo_lon() * 10000000) / 10000000;
				this.setCo_lon(newLongTrunc);
				return true;
			}
		}
		return false;
	}

	public double getCo_archId() {
		return co_archId;
	}

	public void setCo_archId(double co_archId) {
		this.co_archId = co_archId;
	}

	public String getCo_name() {
		return co_name;
	}

	public void setCo_name(String co_name) {
		this.co_name = co_name;
	}

	public String getCo_street() {
		return co_street;
	}

	public void setCo_street(String co_street) {
		this.co_street = co_street;
	}

	public String getCo_postalCity() {
		return co_postalCity;
	}

	public void setCo_postalCity(String co_postalCity) {
		this.co_postalCity = co_postalCity;
	}

	public String getCo_country() {
		return co_country;
	}

	public void setCo_country(String co_country) {
		this.co_country = co_country;
	}

	public double getCo_lat() {
		return co_lat;
	}

	public void setCo_lat(double co_lat) {
		this.co_lat = co_lat;
	}

	public double getCo_lon() {
		return co_lon;
	}

	public void setCo_lon(double co_lon) {
		this.co_lon = co_lon;
	}

}
