/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apex.eaccpf.util;

import eu.apenet.dpt.utils.eaccpf.EntityId;

/**
 *
 * @author papp
 */
public class IdentifierType {
    private String identifier;
    private String identifierType;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(String identifierType) {
        this.identifierType = identifierType;
    }

    public IdentifierType fillDataWith(EntityId entityId) {
        if(entityId.getLocalType() != null && !entityId.getLocalType().isEmpty()){
            this.identifierType = entityId.getLocalType();
        }
        if(entityId.getContent() != null && !entityId.getContent().isEmpty()){
            this.identifier = entityId.getContent();
        }
        return this;
    }
}
