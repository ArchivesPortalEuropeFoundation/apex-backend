package eu.apenet.persistence.vo;

// Generated 09-sep-2010 13:50:04 by Hibernate Tools 3.2.4.GA

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * EseState generated by hbm2java
 */
@Entity
@Table(name = "ese_state")
public class EseState implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4300947370980070120L;
	public static final String NOT_PUBLISHED = "Not published";
	public static final String PUBLISHED = "Published";
	public static final String REMOVED = "Removed";
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String state;



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
