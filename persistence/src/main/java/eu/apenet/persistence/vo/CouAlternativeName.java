package eu.apenet.persistence.vo;

// Generated 25-ene-2011 16:20:03 by Hibernate Tools 3.2.4.GA

/**
 * CouAlternativeName generated by hbm2java
 */
public class CouAlternativeName implements java.io.Serializable {

	private int couAnId;
	private Lang lang;
	private Country country;
	private String couAnName;

	public CouAlternativeName() {
	}

	public CouAlternativeName(int couAnId, Lang lang, Country country,
			String couAnName) {
		this.couAnId = couAnId;
		this.lang = lang;
		this.country = country;
		this.couAnName = couAnName;
	}

	public int getCouAnId() {
		return this.couAnId;
	}

	public void setCouAnId(int couAnId) {
		this.couAnId = couAnId;
	}

	public Lang getLang() {
		return this.lang;
	}

	public void setLang(Lang lang) {
		this.lang = lang;
	}

	public Country getCountry() {
		return this.country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getCouAnName() {
		return this.couAnName;
	}

	public void setCouAnName(String couAnName) {
		this.couAnName = couAnName;
	}

}
