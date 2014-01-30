package eu.archivesportaleurope.harvester.parser.other;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 16/10/2013
 *
 * @author Yoann Moranville
 */
public class OaiPmhElements {
    private List<OaiPmhElement> elements = new ArrayList<OaiPmhElement>();
    private List<String> errors;
    private String resumptionToken;


    public List<OaiPmhElement> getElements() {
        return elements;
    }

    public void setElements(List<OaiPmhElement> elements) {
        this.elements = elements;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public String getResumptionToken() {
        return resumptionToken;
    }

    public void setResumptionToken(String resumptionToken) {
        this.resumptionToken = resumptionToken;
    }
}
