/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.persistence.vo.Ead3;
import eu.apenet.persistence.vo.ValidatedState;
import gov.loc.ead.Ead;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.log4j.Logger;

/**
 *
 * @author kaisar
 */
public class PublishTask extends AbstractEad3Task {
    protected static final Logger LOGGER = Logger.getLogger(PublishTask.class);
    private final JAXBContext ead3Context;
    private final Unmarshaller ead3Unmarshaller;
    
    public PublishTask() throws JAXBException {
        this.ead3Context = JAXBContext.newInstance(Ead.class);
        this.ead3Unmarshaller = ead3Context.createUnmarshaller();
    }
    public static boolean valid(Ead3 ead) {
        return ValidatedState.VALIDATED.equals(ead.getValidated()) && !ead.isPublished();
    }

    @Override
    protected void execute(Ead3 ead3, Properties properties) throws Exception {
        if (valid(ead3)) {
            FileInputStream fileInputStream=null;
            try {
                long startTime = System.currentTimeMillis();
                long solrTime = 0l;
                fileInputStream = getFileInputStream(ead3.getPath());
                Ead ead = (Ead) ead3Unmarshaller.unmarshal(fileInputStream);
                
                LOGGER.debug(ead.getControl().getFiledesc().getTitlestmt().getTitleproper().get(0));
//              
//                if (ead.getEadContent() == null) {
//                    message = "xml";
//                    solrTime = XmlEadParser.parseEadAndPublish(ead);
//                } else {
//                    message = "database";
//                    solrTime = DatabaseXmlEadParser.publish(ead);
//                }
//
//                logSolrAction(ead, message, solrTime, System.currentTimeMillis() - (startTime + solrTime));
            } catch (FileNotFoundException | JAXBException e) {
                throw new APEnetException(this.getActionName() + " " + e.getMessage(), e);
            } finally {
                if (fileInputStream!=null) {
                    fileInputStream.close();
                }
            }
        }
    }

    @Override
    protected String getActionName() {
        return "publish";
    }
    
    private static FileInputStream getFileInputStream(String path) throws FileNotFoundException {
        File file = new File(APEnetUtilities.getConfig().getRepoDirPath() + path);
        return new FileInputStream(file);
    }
}
