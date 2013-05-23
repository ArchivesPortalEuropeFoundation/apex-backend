package eu.apenet.dashboard.actions.ajax;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Writer;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.io.FileUtils;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import org.json.JSONObject;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

/**
 * User: Yoann Moranville
 * Date: 21/11/2011
 *
 * @author Yoann Moranville
 */
public class EagOperationsAjax extends AjaxControllerAbstractAction {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1678759998515305296L;
	private static final String EAG_XMLNS_NEW = "http://www.archivesportaleurope.eu/profiles/APEnet_EAG/";

    public String execute() {
        try {
            Writer writer = openOutputWriter();
            String fileIdStr = getServletRequest().getParameter("fileId");
            if(fileIdStr != null) {
                int fileId = Integer.parseInt(fileIdStr.replace("eagLink_", ""));
                ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().findById(fileId);
                String namespace = checkFileNamespace(APEnetUtilities.getConfig().getRepoDirPath() + archivalInstitution.getEagPath());
                if(namespace != null) {
                    if(namespace.equals(EAG_XMLNS_NEW))
                        writer.append(new JSONObject().put("old", "false").toString());
                    else {
                        if(convertToAPEnetEAG(APEnetUtilities.getConfig().getRepoDirPath() + archivalInstitution.getEagPath())) {
                            namespace = checkFileNamespace(APEnetUtilities.getConfig().getRepoDirPath() + archivalInstitution.getEagPath());
                            if(namespace.equals(EAG_XMLNS_NEW))
                                writer.append(new JSONObject().put("old", "false").toString());
                            else
                                writer.append(new JSONObject().put("old", "true").toString());
                        } else
                            writer.append(new JSONObject().put("old", "true").toString());
                    }
                }
            }
            writer.close();
        } catch (Exception e){
            LOG.error("Error", e);
        }
        return null;
    }

    private static String checkFileNamespace(String path) throws XMLStreamException {
        XMLInputFactory2 xmlif = (XMLInputFactory2) XMLInputFactory2.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        xmlif.configureForSpeed();

        File file = new File(path);
        XMLStreamReader2 input = xmlif.createXMLStreamReader(file);

        while(input.hasNext()){
            input.next();
            switch (input.getEventType()) {
                case XMLEvent.START_ELEMENT:
                    return input.getNamespaceURI();
            }
        }
        return null;
    }

    public static Boolean convertToAPEnetEAG(String filepath) throws APEnetException {
        //EAG file is stored temporally in the location defined in eagPath attribute
        File file = new File(filepath);
        try {
            final String xslfilename = "changeNS.xsl";
            String outputFilePath = filepath + "_converted.xml";
            File outputfile = new File(outputFilePath);
            String xslFilePath = APEnetUtilities.getDashboardConfig().getSystemXslDirPath() + APEnetUtilities.FILESEPARATOR + xslfilename;
            InputStream in = new FileInputStream(file);
            TransformationTool.createTransformation(in, outputfile, FileUtils.openInputStream(new File(xslFilePath)), null, true, true, null, true, null);
            in.close();
            FileUtils.copyFile(outputfile, file);
            outputfile.delete();
        } catch (Exception e){
            throw new APEnetException("Exception while converting in APEnet EAG at '" + filepath + "'", e);
        }
		return true;
	}
}
