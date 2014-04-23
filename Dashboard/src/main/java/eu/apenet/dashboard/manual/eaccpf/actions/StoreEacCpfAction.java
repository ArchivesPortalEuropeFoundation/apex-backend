/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.manual.eaccpf.actions;

import eu.apenet.dashboard.manual.eaccpf.CreateEacCpf;
import eu.apenet.dpt.utils.eaccpf.EacCpf;
import eu.apenet.dashboard.manual.eaccpf.util.ContentUtils;
import eu.apenet.dpt.utils.eaccpf.namespace.EacCpfNamespaceMapper;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.log4j.Logger;

/**
 *
 * @author papp
 */
public class StoreEacCpfAction extends EacCpfAction {

    private final Logger log = Logger.getLogger(StoreEacCpfAction.class);

    @Override
    public void validate() {

    }

    @Override
    public String execute() throws Exception {
        CreateEacCpf creator = new CreateEacCpf(getServletRequest());
        EacCpf eac = creator.getEacCpf();
        String filename = eac.getControl().getRecordId().getValue() + ".xml";

        // Save XML.
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(EacCpf.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "urn:isbn:1-931666-33-4 http://www.archivesportaleurope.net/Portal/profiles/apeEAC-CPF.xsd");
            jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new EacCpfNamespaceMapper());
            jaxbMarshaller.marshal(eac, ContentUtils.getWriterToDownload(getServletRequest(), getServletResponse(), filename, ContentUtils.MIME_TYPE_APPLICATION_XML));
        } catch (JAXBException jaxbe) {
            log.error("Error during marshalling of file: " + filename + "--" + jaxbe.getCause());
            log.error(jaxbe.fillInStackTrace());
        } finally {
        }

        return SUCCESS;
    }
}
