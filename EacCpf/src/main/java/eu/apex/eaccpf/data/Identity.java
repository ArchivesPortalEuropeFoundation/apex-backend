//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.06.11 at 12:41:53 PM CEST 
//


package eu.apex.eaccpf.data;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:isbn:1-931666-33-4}entityId" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{urn:isbn:1-931666-33-4}entityType"/>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element ref="{urn:isbn:1-931666-33-4}nameEntryParallel"/>
 *           &lt;element name="nameEntry">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element ref="{urn:isbn:1-931666-33-4}part" maxOccurs="unbounded"/>
 *                     &lt;element ref="{urn:isbn:1-931666-33-4}useDates" minOccurs="0"/>
 *                     &lt;group ref="{urn:isbn:1-931666-33-4}m.nameEntryAuthorizedorAlternative"/>
 *                   &lt;/sequence>
 *                   &lt;attGroup ref="{urn:isbn:1-931666-33-4}m.entryLanguageAttributes"/>
 *                   &lt;attribute name="localType">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *                         &lt;enumeration value="authorized"/>
 *                         &lt;enumeration value="alternative"/>
 *                         &lt;enumeration value="preferred"/>
 *                         &lt;enumeration value="abbreviation"/>
 *                         &lt;enumeration value="other"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/attribute>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/choice>
 *         &lt;element ref="{urn:isbn:1-931666-33-4}descriptiveNote" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="identityType">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             &lt;enumeration value="given"/>
 *             &lt;enumeration value="acquired"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "entityId",
    "entityType",
    "nameEntryParallelOrNameEntry",
    "descriptiveNote"
})
@XmlRootElement(name = "identity")
public class Identity {

    protected List<EntityId> entityId;
    @XmlElement(required = true)
    protected EntityType entityType;
    @XmlElements({
        @XmlElement(name = "nameEntryParallel", type = NameEntryParallel.class),
        @XmlElement(name = "nameEntry", type = Identity.NameEntry.class)
    })
    protected List<Object> nameEntryParallelOrNameEntry;
    protected DescriptiveNote descriptiveNote;
    @XmlAttribute(name = "identityType")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String identityType;

    /**
     * Gets the value of the entityId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entityId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntityId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EntityId }
     * 
     * 
     */
    public List<EntityId> getEntityId() {
        if (entityId == null) {
            entityId = new ArrayList<EntityId>();
        }
        return this.entityId;
    }

    /**
     * Gets the value of the entityType property.
     * 
     * @return
     *     possible object is
     *     {@link EntityType }
     *     
     */
    public EntityType getEntityType() {
        return entityType;
    }

    /**
     * Sets the value of the entityType property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntityType }
     *     
     */
    public void setEntityType(EntityType value) {
        this.entityType = value;
    }

    /**
     * Gets the value of the nameEntryParallelOrNameEntry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nameEntryParallelOrNameEntry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNameEntryParallelOrNameEntry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NameEntryParallel }
     * {@link Identity.NameEntry }
     * 
     * 
     */
    public List<Object> getNameEntryParallelOrNameEntry() {
        if (nameEntryParallelOrNameEntry == null) {
            nameEntryParallelOrNameEntry = new ArrayList<Object>();
        }
        return this.nameEntryParallelOrNameEntry;
    }

    /**
     * Gets the value of the descriptiveNote property.
     * 
     * @return
     *     possible object is
     *     {@link DescriptiveNote }
     *     
     */
    public DescriptiveNote getDescriptiveNote() {
        return descriptiveNote;
    }

    /**
     * Sets the value of the descriptiveNote property.
     * 
     * @param value
     *     allowed object is
     *     {@link DescriptiveNote }
     *     
     */
    public void setDescriptiveNote(DescriptiveNote value) {
        this.descriptiveNote = value;
    }

    /**
     * Gets the value of the identityType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentityType() {
        return identityType;
    }

    /**
     * Sets the value of the identityType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentityType(String value) {
        this.identityType = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element ref="{urn:isbn:1-931666-33-4}part" maxOccurs="unbounded"/>
     *         &lt;element ref="{urn:isbn:1-931666-33-4}useDates" minOccurs="0"/>
     *         &lt;group ref="{urn:isbn:1-931666-33-4}m.nameEntryAuthorizedorAlternative"/>
     *       &lt;/sequence>
     *       &lt;attGroup ref="{urn:isbn:1-931666-33-4}m.entryLanguageAttributes"/>
     *       &lt;attribute name="localType">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
     *             &lt;enumeration value="authorized"/>
     *             &lt;enumeration value="alternative"/>
     *             &lt;enumeration value="preferred"/>
     *             &lt;enumeration value="abbreviation"/>
     *             &lt;enumeration value="other"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "part",
        "useDates",
        "authorizedFormOrAlternativeForm"
    })
    public static class NameEntry {

        @XmlElement(required = true)
        protected List<Part> part;
        protected UseDates useDates;
        @XmlElements({
            @XmlElement(name = "authorizedForm", type = AuthorizedForm.class),
            @XmlElement(name = "alternativeForm", type = AlternativeForm.class)
        })
        protected List<Object> authorizedFormOrAlternativeForm;
        @XmlAttribute(name = "localType")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        protected String localType;
        @XmlAttribute(name = "scriptCode")
        protected String scriptCode;
        @XmlAttribute(name = "lang", namespace = "http://www.w3.org/XML/1998/namespace")
        protected String lang;
        @XmlAttribute(name = "transliteration")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "NMTOKEN")
        protected String transliteration;

        /**
         * Gets the value of the part property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the part property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPart().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Part }
         * 
         * 
         */
        public List<Part> getPart() {
            if (part == null) {
                part = new ArrayList<Part>();
            }
            return this.part;
        }

        /**
         * Gets the value of the useDates property.
         * 
         * @return
         *     possible object is
         *     {@link UseDates }
         *     
         */
        public UseDates getUseDates() {
            return useDates;
        }

        /**
         * Sets the value of the useDates property.
         * 
         * @param value
         *     allowed object is
         *     {@link UseDates }
         *     
         */
        public void setUseDates(UseDates value) {
            this.useDates = value;
        }

        /**
         * Gets the value of the authorizedFormOrAlternativeForm property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the authorizedFormOrAlternativeForm property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAuthorizedFormOrAlternativeForm().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AuthorizedForm }
         * {@link AlternativeForm }
         * 
         * 
         */
        public List<Object> getAuthorizedFormOrAlternativeForm() {
            if (authorizedFormOrAlternativeForm == null) {
                authorizedFormOrAlternativeForm = new ArrayList<Object>();
            }
            return this.authorizedFormOrAlternativeForm;
        }

        /**
         * Gets the value of the localType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLocalType() {
            return localType;
        }

        /**
         * Sets the value of the localType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLocalType(String value) {
            this.localType = value;
        }

        /**
         * Gets the value of the scriptCode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getScriptCode() {
            return scriptCode;
        }

        /**
         * Sets the value of the scriptCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setScriptCode(String value) {
            this.scriptCode = value;
        }

        /**
         * Gets the value of the lang property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLang() {
            return lang;
        }

        /**
         * Sets the value of the lang property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLang(String value) {
            this.lang = value;
        }

        /**
         * Gets the value of the transliteration property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTransliteration() {
            return transliteration;
        }

        /**
         * Sets the value of the transliteration property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTransliteration(String value) {
            this.transliteration = value;
        }

    }

}
