package eu.apenet.persistence.vo;
// Generated 19-nov-2013 17:31:30 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Coordinates generated by hbm2java
 */
@Entity
@Table(name = "coordinates", schema = "public")
public class Coordinates implements java.io.Serializable {

	private static final long serialVersionUID = -3368116414252165182L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ai_id", nullable = false)
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

	public Coordinates(int id, ArchivalInstitution archivalInstitution,
			String nameInstitution, String street, String postalcity, String country) {
		this.id = id;
		this.archivalInstitution = archivalInstitution;
		this.nameInstitution = nameInstitution;
		this.street = street;
		this.postalCity = postalcity;
		this.country = country;
	}

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
