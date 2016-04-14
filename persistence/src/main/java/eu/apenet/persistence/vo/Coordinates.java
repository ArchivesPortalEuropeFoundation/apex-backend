package eu.apenet.persistence.vo;
// Generated 19-nov-2013 17:31:30 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Coordinates class
 */
@Entity
@Table(name = "coordinates")
public class Coordinates implements java.io.Serializable {

	private static final long serialVersionUID = -3368116414252165182L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ai_id", foreignKey = @ForeignKey(name="coordinates_archival_institution_fkey"), nullable = false)
	private ArchivalInstitution archivalInstitution;
	@Column(name = "ai_id", insertable=false, updatable=false)
	private int aiId;
	@Column(name = "name_institution", nullable = false)
	private String nameInstitution;
	@Column(name = "street", nullable = false)
	private String street;
	@Column(name = "postalcity", nullable = false)
	private String postalCity;
	@Column(name = "country", nullable = false)
	private String country;
	@Column(name = "lat", precision = 131089, scale = 0)
	private double lat;
	@Column(name = "lon", precision = 131089, scale = 0)
	private double lon;

	public Coordinates() {
	}

	/***
	 * Coordinates object without coordinates
	 * 
	 * @param id {@link int} object id
	 * @param archivalInstitution {@link ArchivalInstitution} archivalInstitution object
	 * @param nameInstitution {@link String} name of the institution
	 * @param street {@link String} street of the institution
	 * @param postalcity {@link String} postal code
	 * @param country {@link String} country
	 */
	public Coordinates(int id, ArchivalInstitution archivalInstitution,
			String nameInstitution, String street, String postalcity, String country) {
		this.id = id;
		this.archivalInstitution = archivalInstitution;
		this.nameInstitution = nameInstitution;
		this.street = street;
		this.postalCity = postalcity;
		this.country = country;
	}

	/***
	 * Coordinates object with coordinates
	 * 
	 * @param id {@link int} object id
	 * @param archivalInstitution {@link ArchivalInstitution} archivalInstitution object
	 * @param nameInstitution {@link String} name of the institution
	 * @param street {@link String} street of the institution
	 * @param postalcity {@link String} postal code
	 * @param country {@link String} country
	 * @param lat {@link double} latitude
	 * @param lon {@link double} longitude
	 */
	public Coordinates(int id, ArchivalInstitution archivalInstitution,
			String nameInstitution, String street, String postalcity, String country, double lat,
			double lon) {
		this.id = id;
		this.archivalInstitution = archivalInstitution;
		this.nameInstitution = nameInstitution;
		//this.address = address;
		this.street = street;
		this.postalCity = postalcity;
		this.country = country;
		this.lat = lat;
		this.lon = lon;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArchivalInstitution getArchivalInstitution() {
		return this.archivalInstitution;
	}

	public void setArchivalInstitution(ArchivalInstitution archivalInstitution) {
		this.archivalInstitution = archivalInstitution;
	}

	public String getNameInstitution() {
		return this.nameInstitution;
	}

	public void setNameInstitution(String nameInstitution) {
		this.nameInstitution = nameInstitution;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPostalCity() {
		return postalCity;
	}

	public void setPostalCity(String postalCity) {
		this.postalCity = postalCity;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public double getLat() {
		return this.lat;
	}

	public void setLat(double co_lat) {
		this.lat = co_lat;
	}

	public double getLon() {
		return this.lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

}
