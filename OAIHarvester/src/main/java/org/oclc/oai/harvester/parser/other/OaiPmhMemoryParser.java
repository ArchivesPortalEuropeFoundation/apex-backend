package org.oclc.oai.harvester.parser.other;

import javax.xml.namespace.QName;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 16/10/2013
 *
 * @author Yoann Moranville
 */
public class OaiPmhMemoryParser {
    protected static final String UTF8 = "UTF-8";
    protected static final String OAI_PMH = "http://www.openarchives.org/OAI/2.0/";

    protected static final QName METADATA_FORMAT = new QName(OAI_PMH, "metadataFormat");
    protected static final QName SET = new QName(OAI_PMH, "set");

    private List<String> elements;

    public OaiPmhMemoryParser(){
        this.elements = new ArrayList<String>();
    }

    protected List<String> getElements() {
        return elements;
    }
}
