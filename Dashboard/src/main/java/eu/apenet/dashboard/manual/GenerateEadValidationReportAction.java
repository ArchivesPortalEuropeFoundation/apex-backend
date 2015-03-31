/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.manual;

import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.util.extendxsl.XmlQualityCheckerCall;
import eu.apenet.persistence.dao.EadContentDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.ValidatedState;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.xml.sax.SAXException;

/**
 *
 * @author papp
 */
public class GenerateEadValidationReportAction extends AbstractInstitutionAction {

    private String id;
    protected Integer xmlTypeId;
    private String counterDao;
    private String counterUnitdate;
    private String counterUnittitle;
    private String counterWrongHref;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getXmlTypeId() {
        return xmlTypeId;
    }

    public void setXmlTypeId(Integer xmlTypeId) {
        this.xmlTypeId = xmlTypeId;
    }

    public String getCounterDao() {
        return counterDao;
    }

    public void setCounterDao(String counterDao) {
        this.counterDao = counterDao;
    }

    public String getCounterUnitdate() {
        return counterUnitdate;
    }

    public void setCounterUnitdate(String counterUnitdate) {
        this.counterUnitdate = counterUnitdate;
    }

    public String getCounterUnittitle() {
        return counterUnittitle;
    }

    public void setCounterUnittitle(String counterUnittitle) {
        this.counterUnittitle = counterUnittitle;
    }

    public String getCounterWrongHref() {
        return counterWrongHref;
    }

    public void setCounterWrongHref(String counterWrongHref) {
        this.counterWrongHref = counterWrongHref;
    }

    protected XmlType getXmlType() {
        return XmlType.getType(xmlTypeId);
    }

    @Override
    public String execute() throws Exception {
        EadDAO dao = DAOFactory.instance().getEadDAO();
        if (NumberUtils.isNumber(id)) {

            Ead ead = dao.findById(Integer.parseInt(id), getXmlType().getClazz());
            if (valid(ead)) {
                File file = new File(APEnetUtilities.getConfig().getRepoDirPath() + ead.getPath());
                File reportScript = new File(APEnetUtilities.getDashboardConfig().getXslDirPath()
                        + APEnetUtilities.FILESEPARATOR + "report/xmlQuality.xsl");
                InputStream is2;
                try {
                    is2 = FileUtils.openInputStream(file);
                    XmlQualityCheckerCall xmlQualityCheckerCall = new XmlQualityCheckerCall();
                    TransformationTool.createTransformation(is2, null, reportScript, null, true, true, null, false, xmlQualityCheckerCall);
                    is2.close();

                    counterDao = Integer.toString(xmlQualityCheckerCall.getCounterDao());
                    counterUnitdate = Integer.toString(xmlQualityCheckerCall.getCounterUnitdate());
                    counterUnittitle = Integer.toString(xmlQualityCheckerCall.getCounterUnittitle());
                    counterWrongHref = Integer.toString(xmlQualityCheckerCall.getCounterWrongHref());
                } catch (IOException e) {
                    LOG.error(e.toString());
                } catch (SAXException e) {
                    LOG.error(e.toString());
                }
            }

        }
        return SUCCESS;
    }

    private static boolean valid(Ead ead) {
        return ValidatedState.VALIDATED.equals(ead.getValidated());
    }

}
