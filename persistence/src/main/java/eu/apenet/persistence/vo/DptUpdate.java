package eu.apenet.persistence.vo;

import java.io.Serializable;
import javax.persistence.*;

/**
 * User: Yoann Moranville
 * Date: 22/05/2012
 *
 * @author Yoann Moranville
 */
@Entity
@Table(name = "dpt_update")
public class DptUpdate implements Serializable {
    private static final long serialVersionUID = 675878788290605885L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String version;
    @Column(name = "new_version")
    private String newVersion;

    public DptUpdate() {
    }

    public DptUpdate(String version, String newVersion) {
        this.version = version;
        this.newVersion = newVersion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }
}
