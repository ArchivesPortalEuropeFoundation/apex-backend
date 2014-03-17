/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apex.eaccpf.actions;

import eu.apex.eaccpf.CreateEacCpf;
import eu.apenet.dpt.utils.eaccpf.EacCpf;
import eu.apex.eaccpf.util.ContentUtils;
import java.util.Enumeration;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author papp
 */
public class StoreEacCpfAction extends EacCpfAction {

    private String content = new String();
    //private final Logger log = Logger.getLogger(StoreEacCpfAction.class);

    @Override
    public void validate() {

    }

    @Override
    public String execute() throws Exception {
        EacCpf eac = null;
        CreateEacCpf creator = new CreateEacCpf(getServletRequest());
        eac = creator.getEacCpf();
        String filename = "apeEACCPF_" + eac.getControl().getMaintenanceAgency().getAgencyCode().getValue() + "_" + eac.getControl().getRecordId().getValue() + ".xml";

        // Save XML.
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(EacCpf.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(eac, ContentUtils.getWriterToDownload(getServletRequest(), getServletResponse(), filename, ContentUtils.MIME_TYPE_APPLICATION_XML));
        } catch (JAXBException jaxbe) {
            jaxbe.printStackTrace();
        } finally {
        }

        StringBuilder builder = new StringBuilder();
        Enumeration<String> paramNames = getServletRequest().getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            builder.append(paramName);
            builder.append(": ");
            String[] paramValues = getServletRequest().getParameterValues(paramName);
            for (String string : paramValues) {
                builder.append(string);
                builder.append(", ");
            }
        }
        content = builder.toString();
        return SUCCESS;
    }

    public String addStuff() {
        return INPUT;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
