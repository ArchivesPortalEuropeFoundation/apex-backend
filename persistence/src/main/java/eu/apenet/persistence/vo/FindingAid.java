package eu.apenet.persistence.vo;

import java.util.HashSet;
import java.util.Set;


public class FindingAid extends Ead {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8833722976902370936L;
	private Set<Ese> eses = new HashSet<Ese>(0);

    @Deprecated
	public int getFaId() {
		if (this.getId() == null){
			return -1;
		}
		return this.getId();
	}

    @Deprecated
	public String getFaTitle() {
		return this.getTitle();
	}


	@Deprecated
	public String getFaEadid() {
		return this.getEadid();
	}


	public Set<Ese> getEses() {
		return this.eses;
	}

	public void setEses(Set<Ese> eses) {
		this.eses = eses;
	}
	




}
