package eu.apenet.persistence.vo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_role")
public class UserRole implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1872111744607561743L;
	public final static String ROLE_COUNTRY_MANAGER = "countryManager";
    public final static String ROLE_INSTITUTION_MANAGER = "institutionManager";
    public final static String ROLE_ADMIN = "admin";
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String role;

	public UserRole() {
	}
	public UserRole(String role) {
		this.role = role;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int roleId) {
		this.id = roleId;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String roletype) {
		this.role = roletype;
	}


}
