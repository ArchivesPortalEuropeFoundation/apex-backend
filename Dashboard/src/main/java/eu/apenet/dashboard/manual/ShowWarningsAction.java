package eu.apenet.dashboard.manual;

import com.opensymphony.xwork2.ActionSupport;
import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.Warnings;

/**
 * User: Yoann Moranville Date: Oct 19, 2010
 *
 * @author Yoann Moranville
 */
public class ShowWarningsAction extends ActionSupport {

    private Integer id;
    private boolean iswarning;
    private Integer xmlTypeId;
    private String abstract_data;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isIswarning() {
        return iswarning;
    }

    public void setIswarning(boolean iswarning) {
        this.iswarning = iswarning;
    }

    public String getAbstract_data() {
        return abstract_data;
    }

    public void setAbstract_data(String abstract_data) {
        this.abstract_data = abstract_data;
    }

    public Integer getXmlTypeId() {
        return xmlTypeId;
    }

    public void setXmlTypeId(Integer xmlTypeId) {
        this.xmlTypeId = xmlTypeId;
    }

    @Override
    public String execute() {
        XmlType xmlType = XmlType.getType(xmlTypeId);
        if (xmlType == XmlType.EAC_CPF) {
            EacCpf eacCpf = DAOFactory.instance().getEacCpfDAO().findById(id, xmlType.getClazz());
            for (Warnings warnings : eacCpf.getWarningses()) {
                if (warnings.getIswarning() == iswarning) {
                    abstract_data = warnings.getAbstract_();
                }
            }
        } else {
            Ead ead = DAOFactory.instance().getEadDAO().findById(id, xmlType.getClazz());
            for (Warnings warnings : ead.getWarningses()) {
                if (warnings.getIswarning() == iswarning) {
                    abstract_data = warnings.getAbstract_();
                }
            }
        }
        return SUCCESS;
    }

}
