package eu.apenet.dashboard.manual;

import com.opensymphony.xwork2.ActionSupport;
import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.Ead3;
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
                    abstract_data = this.scapeCharacters(warnings.getAbstract_());
                }
            }
        } else if (xmlType == XmlType.EAD_3) {
            Ead3 ead3 = DAOFactory.instance().getEad3DAO().findById(id, xmlType.getClazz());
            for (Warnings warnings : ead3.getWarningses()) {
                if (warnings.getIswarning() == iswarning) {
                    abstract_data = this.scapeCharacters(warnings.getAbstract_());
                }
            }
        } else {
            Ead ead = DAOFactory.instance().getEadDAO().findById(id, xmlType.getClazz());
            for (Warnings warnings : ead.getWarningses()) {
                if (warnings.getIswarning() == iswarning) {
                    abstract_data = this.scapeCharacters(warnings.getAbstract_());
                }
            }
        }
        return SUCCESS;
    }

    /**
     * Method to escape the content to display in order to prevent script
     * injection.
     *
     * @param abstractText Text that should be escaped.
     * @return Escaped text.
     */
    private String scapeCharacters(String abstractText) {
        String fixedAbstract = "";
        String tempAbstract = abstractText;

        // Checks if exists text that could be escaped.
        while (tempAbstract.indexOf("'") != -1) {
            String temp = tempAbstract.substring(tempAbstract.indexOf("'") + 1);
            fixedAbstract += tempAbstract.substring(0, tempAbstract.indexOf("'") + 1);

            // Escape the text between char "'".
            if (temp.indexOf("'") != -1) {
                temp = temp.substring(0, temp.indexOf("'") + 1);
                temp = temp.replaceAll(">", "&#62;").replaceAll("<", "&#60;");
            }

            fixedAbstract += temp;

            tempAbstract = tempAbstract.substring(tempAbstract.indexOf("'") + 1);
            if (tempAbstract.indexOf("'") != -1) {
                tempAbstract = tempAbstract.substring(tempAbstract.indexOf("'") + 1);
            }
        }

        fixedAbstract += tempAbstract;

        return fixedAbstract;
    }

}
