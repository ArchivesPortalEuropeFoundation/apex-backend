package eu.apenet.commons.types;

import eu.apenet.commons.solr.SolrValues;
import eu.apenet.persistence.vo.AbstractContent;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.Ead3;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;

/**
 * User: Yoann Moranville Date: 3/11/11
 *
 * @author Yoann Moranville
 */
public enum XmlType {
    EAD_FA(0, "Finding Aid", FindingAid.class, "fa", SolrValues.FA_PREFIX),
    EAD_HG(1, "Holdings Guide", HoldingsGuide.class, "hg", SolrValues.HG_PREFIX),
    EAC_CPF(2, "EAC-CPF", EacCpf.class, "ec", null),
    EAD_SG(3, "Source Guide", SourceGuide.class, "sg", SolrValues.SG_PREFIX),
    EAD_3(4, "EAD3", Ead3.class, "ead3", "");

    private final String resourceName;
    private final int identifier;
    private final String name;
    private final Class<? extends AbstractContent> clazz;
    private final String solrPrefix;

    XmlType(int identifier, String name, Class<? extends AbstractContent> clazz, String resourceName, String solrPrefix) {
        this.identifier = identifier;
        this.name = name;
        this.clazz = clazz;
        this.resourceName = resourceName;
        this.solrPrefix = solrPrefix;
    }

    public String getName() {
        return name;
    }

    public int getIdentifier() {
        return identifier;
    }

    public Class<? extends AbstractContent> getClazz() {
        return clazz;
    }

    public static XmlType getType(Class<? extends AbstractContent> clazz) {
        for (XmlType type : XmlType.values()) {
            if (type.getClazz() != null && type.getClazz().equals(clazz)) {
                return type;
            }
        }
        return null;
    }

    public static XmlType getType(int identifier) {
        for (XmlType type : XmlType.values()) {
            if (type.getIdentifier() == identifier) {
                return type;
            }
        }
        return null;
    }

    public static XmlType getTypeBySolrPrefix(String solrPrefix) {
        for (XmlType type : XmlType.values()) {
            if (type.getSolrPrefix() != null && type.getSolrPrefix().equals(solrPrefix)) {
                return type;
            }
        }
        return null;
    }

    public static XmlType getTypeByResourceName(String resourceName) {
        for (XmlType type : XmlType.values()) {
            if (type.getResourceName() != null && type.getResourceName().equalsIgnoreCase(resourceName)) {
                return type;
            }
        }
        return null;
    }

    public static XmlType getType(String name) {
        for (XmlType type : XmlType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }

    public static XmlType getContentType(AbstractContent content) {
        if (content instanceof FindingAid) {
            return XmlType.EAD_FA;
        }
        if (content instanceof HoldingsGuide) {
            return XmlType.EAD_HG;
        }
        if (content instanceof SourceGuide) {
            return XmlType.EAD_SG;
        }
        if (content instanceof EacCpf) {
            return XmlType.EAC_CPF;
        }
        return null;
    }

    public Class<? extends Ead> getEadClazz() {
        if (this.identifier == 0) {
            return FindingAid.class;
        }
        if (this.identifier == 1) {
            return HoldingsGuide.class;
        }
        if (this.identifier == 3) {
            return SourceGuide.class;
        }
        return null;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getSolrPrefix() {
        return solrPrefix;
    }

}
