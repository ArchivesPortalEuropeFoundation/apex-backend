package eu.apenet.persistence.vo;

public class UserRole implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1872111744607561743L;
	public final static String ROLE_COUNTRY_MANAGER = "countryManager";
    public final static String ROLE_INSTITUTION_MANAGER = "institutionManager";
    public final static String ROLE_ADMIN = "admin";

	private int id;
	private String role;

	public UserRole() {
	}

	public UserRole(int roleId, String roletype) {
		this.id = roleId;
		this.role = roletype;
	}
	public UserRole(String roletype) {
		this.role = roletype;
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
