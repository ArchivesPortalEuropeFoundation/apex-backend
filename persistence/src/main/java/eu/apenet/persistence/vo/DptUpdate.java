package eu.apenet.persistence.vo;

import java.io.Serializable;

/**
 * User: Yoann Moranville
 * Date: 22/05/2012
 *
 * @author Yoann Moranville
 */
public class DptUpdate implements Serializable {
    private static final long serialVersionUID = 675878788290605885L;

    private Long id;
    private String version;

    public DptUpdate() {
    }

    public DptUpdate(String version) {
        this.version = version;
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
}
