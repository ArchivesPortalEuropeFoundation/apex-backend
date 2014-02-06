/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apex.eaccpf.util;

import eu.apex.eaccpf.data.CpfRelation;
import eu.apex.eaccpf.data.RelationEntry;
import eu.apex.eaccpf.data.FunctionRelation;
import eu.apex.eaccpf.data.ResourceRelation;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author papp
 */
public class RelationType {
    private String relationName;
    private String language;
    private String id;
    private String link;
    private String relationType;
    private String description;
    private List<String> agencyNames = null;
    private List<String> agencyCodes = null;

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAgencyNames() {
        return agencyNames;
    }

    public void setAgencyNames(List<String> agencyNames) {
        this.agencyNames = agencyNames;
    }

    public void addAgencyName(String agencyName){
        if(this.agencyNames == null){
            this.agencyNames = new ArrayList<String>();
        }
        this.agencyNames.add(agencyName);
    }

    public List<String> getAgencyCodes() {
        return agencyCodes;
    }

    public void setAgencyCodes(List<String> agencyCodes) {
        this.agencyCodes = agencyCodes;
    }

     public void addAgencyCode(String agencyCode){
        if(this.agencyCodes == null){
            this.agencyCodes = new ArrayList<String>();
        }
        this.agencyCodes.add(agencyCode);
    }

    public RelationType fillDataWith(CpfRelation cpfRelation) {
        this.language = "";
        if(cpfRelation.getCpfRelationType() != null
                && !cpfRelation.getCpfRelationType().isEmpty()){
            this.relationType = cpfRelation.getCpfRelationType();
        }
        if(cpfRelation.getHref() != null
                && !cpfRelation.getHref().isEmpty()){
            this.link = cpfRelation.getHref();
        }
        if(cpfRelation.getDescriptiveNote() != null
                && cpfRelation.getDescriptiveNote().getP() != null
                && !cpfRelation.getDescriptiveNote().getP().isEmpty()
                && cpfRelation.getDescriptiveNote().getP().get(0).getContent() != null
                && !cpfRelation.getDescriptiveNote().getP().get(0).getContent().isEmpty()){
            this.description = cpfRelation.getDescriptiveNote().getP().get(0).getContent();
        }
        if(cpfRelation.getRelationEntry() != null
                && !cpfRelation.getRelationEntry().isEmpty()){
            for (RelationEntry relationEntry : cpfRelation.getRelationEntry()) {
                if(this.language.isEmpty()){
                    if(relationEntry.getLang() != null && !relationEntry.getLang().isEmpty()){
                        this.language = relationEntry.getLang();
                    }
                }
                if(relationEntry.getLocalType().equals("title")
                        && !relationEntry.getContent().isEmpty()){
                    this.relationName = relationEntry.getContent();
                }
                if(relationEntry.getLocalType().equals("id")
                        && !relationEntry.getContent().isEmpty()){
                    this.id = relationEntry.getContent();
                }
                if(relationEntry.getLocalType().equals("agencyName")
                        && !relationEntry.getContent().isEmpty()){
                    this.addAgencyName(relationEntry.getContent());
                }
                if(relationEntry.getLocalType().equals("agencyCode")
                        && !relationEntry.getContent().isEmpty()){
                    this.addAgencyCode(relationEntry.getContent());
                }
            }
        }
        return this;
    }

    public RelationType fillDataWith(ResourceRelation resRelation) {
        this.language = "";
        if(resRelation.getResourceRelationType() != null
                && !resRelation.getResourceRelationType().isEmpty()){
            this.relationType = resRelation.getResourceRelationType();
        }
        if(resRelation.getHref() != null
                && !resRelation.getHref().isEmpty()){
            this.link = resRelation.getHref();
        }
        if(resRelation.getDescriptiveNote() != null
                && resRelation.getDescriptiveNote().getP() != null
                && !resRelation.getDescriptiveNote().getP().isEmpty()
                && resRelation.getDescriptiveNote().getP().get(0).getContent() != null
                && !resRelation.getDescriptiveNote().getP().get(0).getContent().isEmpty()){
            this.description = resRelation.getDescriptiveNote().getP().get(0).getContent();
        }
        if(resRelation.getRelationEntry() != null
                && !resRelation.getRelationEntry().isEmpty()){
            for (RelationEntry relationEntry : resRelation.getRelationEntry()) {
                if(this.language.isEmpty()){
                    if(relationEntry.getLang() != null && !relationEntry.getLang().isEmpty()){
                        this.language = relationEntry.getLang();
                    }
                }
                if(relationEntry.getLocalType().equals("title")
                        && !relationEntry.getContent().isEmpty()){
                    this.relationName = relationEntry.getContent();
                }
                if(relationEntry.getLocalType().equals("id")
                        && !relationEntry.getContent().isEmpty()){
                    this.id = relationEntry.getContent();
                }
                if(relationEntry.getLocalType().equals("agencyName")
                        && !relationEntry.getContent().isEmpty()){
                    this.addAgencyName(relationEntry.getContent());
                }
                if(relationEntry.getLocalType().equals("agencyCode")
                        && !relationEntry.getContent().isEmpty()){
                    this.addAgencyCode(relationEntry.getContent());
                }
            }
        }
        return this;
    }

    public RelationType fillDataWith(FunctionRelation fncRelation) {
        this.language = "";
        if(fncRelation.getFunctionRelationType() != null
                && !fncRelation.getFunctionRelationType().isEmpty()){
            this.relationType = fncRelation.getFunctionRelationType();
        }
        if(fncRelation.getHref() != null
                && !fncRelation.getHref().isEmpty()){
            this.link = fncRelation.getHref();
        }
        if(fncRelation.getDescriptiveNote() != null
                && fncRelation.getDescriptiveNote().getP() != null
                && !fncRelation.getDescriptiveNote().getP().isEmpty()
                && fncRelation.getDescriptiveNote().getP().get(0).getContent() != null
                && !fncRelation.getDescriptiveNote().getP().get(0).getContent().isEmpty()){
            this.description = fncRelation.getDescriptiveNote().getP().get(0).getContent();
        }
        if(fncRelation.getRelationEntry() != null
                && !fncRelation.getRelationEntry().isEmpty()){
            for (RelationEntry relationEntry : fncRelation.getRelationEntry()) {
                if(this.language.isEmpty()){
                    if(relationEntry.getLang() != null && !relationEntry.getLang().isEmpty()){
                        this.language = relationEntry.getLang();
                    }
                }
                if(relationEntry.getLocalType().equals("title")
                        && !relationEntry.getContent().isEmpty()){
                    this.relationName = relationEntry.getContent();
                }
                if(relationEntry.getLocalType().equals("id")
                        && !relationEntry.getContent().isEmpty()){
                    this.id = relationEntry.getContent();
                }
                if(relationEntry.getLocalType().equals("agencyName")
                        && !relationEntry.getContent().isEmpty()){
                    this.addAgencyName(relationEntry.getContent());
                }
                if(relationEntry.getLocalType().equals("agencyCode")
                        && !relationEntry.getContent().isEmpty()){
                    this.addAgencyCode(relationEntry.getContent());
                }
            }
        }
        return this;
    }
}
