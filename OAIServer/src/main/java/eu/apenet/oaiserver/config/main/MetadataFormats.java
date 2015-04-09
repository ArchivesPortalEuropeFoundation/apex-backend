package eu.apenet.oaiserver.config.main;

/**
 * Created by yoannmoranville on 08/04/15.
 */
public enum MetadataFormats {
    EDM("edm", "http://www.w3.org/1999/02/22-rdf-syntax-ns#", "http://www.europeana.eu/schemas/edm/EDM.xsd"),
    EAD("ead", "urn:isbn:1-931666-22-9", "http://www.loc.gov/ead/ead.xsd"),
    APE_EAD("ape_ead", "urn:isbn:1-931666-22-9", "http://www.archivesportaleurope.net/Portal/profiles/apeEAD.xsd"),
    OAI_DC("oai_dc", "http://www.openarchives.org/OAI/2.0/oai_dc/", "http://www.openarchives.org/OAI/2.0/oai_dc.xsd"),
//    NOMINA("nomina", "", ""),
    DC("dc", "http://purl.org/dc/elements/1.1/", "http://dublincore.org/schemas/xmls/qdc/2008/02/11/dc.xsd");

    private String name;
    private String namespace;
    private String schemaLocation;

    private MetadataFormats(String name, String namespace, String schemaLocation) {
        this.name = name;
        this.namespace = namespace;
        this.schemaLocation = schemaLocation;
    }

    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getSchemaLocation() {
        return schemaLocation;
    }

    @Override
    public String toString() {
        return name + ", namespace: " + namespace + ", schemaLocation: " + schemaLocation;
    }

    public static MetadataFormats getMetadataFormats(String name) {
        for (MetadataFormats metadataFormat : MetadataFormats.values()) {
            if (metadataFormat.name.equals(name))
                return metadataFormat;
        }
        return null;
    }
}
