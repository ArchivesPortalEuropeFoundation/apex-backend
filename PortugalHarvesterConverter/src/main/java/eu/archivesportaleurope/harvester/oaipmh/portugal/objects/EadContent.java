package eu.archivesportaleurope.harvester.oaipmh.portugal.objects;

/**
 * User: yoannmoranville
 * Date: 27/01/14
 *
 * @author yoannmoranville
 */

import java.io.Serializable;

public class EadContent implements Serializable {
    private static final long serialVersionUID = 7231712945692944551L;

    private Long ecId;
    private String eadid;
    private String xml;

    public String getXml() {
        return xml;
    }
    public void setXml(String xml) {
        this.xml = xml;
    }
    public String getEadid() {
        return eadid;
    }
    public void setEadid(String eadid) {
        this.eadid = eadid;
    }
    public Long getEcId() {
        return ecId;
    }
    public void setEcId(Long ecId) {
        this.ecId = ecId;
    }
}

