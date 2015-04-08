package eu.apenet.oaiserver.config.vo;

import eu.apenet.oaiserver.config.MetadataFormats;
import eu.apenet.persistence.vo.Ese;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yoannmoranville on 08/04/15.
 */
public class MetadataObject {

    private Integer id;
    private String oaiIdentifier;

    private String xmlPath;

    private String set;

    private Date creationDate;
    private Date modificationDate;

    private State state;

    private MetadataFormats metadataFormats;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOaiIdentifier() {
        return oaiIdentifier;
    }

    public void setOaiIdentifier(String oaiIdentifier) {
        this.oaiIdentifier = oaiIdentifier;
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public MetadataFormats getMetadataFormats() {
        return metadataFormats;
    }

    public void setMetadataFormats(MetadataFormats metadataFormats) {
        this.metadataFormats = metadataFormats;
    }

    public enum State {
        PUBLISHED("Published"),
        REMOVED("Removed");

        private String name;

        private State(String name) {
            this.name = name;
        }

        public static State getStateFromName(String name) {
            for(State state : State.values()) {
                if(state.name.equals(name)) {
                    return state;
                }
            }
            return null;
        }
    }
}
