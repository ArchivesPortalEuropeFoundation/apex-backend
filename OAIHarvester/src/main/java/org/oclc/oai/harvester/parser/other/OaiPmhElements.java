package org.oclc.oai.harvester.parser.other;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 16/10/2013
 *
 * @author Yoann Moranville
 */
public class OaiPmhElements {
    private List<String> sets;
    private List<String> metadataFormats;

    public OaiPmhElements() {
        this.sets = new ArrayList<String>();
        this.metadataFormats = new ArrayList<String>();
    }

    public List<String> getSets() {
        return sets;
    }

    public void setSets(List<String> sets) {
        this.sets = sets;
    }

    public List<String> getMetadataFormats() {
        return metadataFormats;
    }

    public void setMetadataFormats(List<String> metadataFormats) {
        this.metadataFormats = metadataFormats;
    }
}
