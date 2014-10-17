/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.ead2ese;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.FindingAid;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author papp
 */
public class CTableXMLHandler {

    protected Logger logger = Logger.getLogger(CTableXMLHandler.class);
    private String filename;

    private static final String PREFIX = "apehelp";
    private static final String NAMESPACE = "http://www.archivesportaleurope.net/functions/helper";

    public CTableXMLHandler() {
    }

    public void generateFile(String id, String aiId) {
        File tempDir = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + aiId + APEnetUtilities.FILESEPARATOR);
        if(!tempDir.exists()){
            try {
                FileUtils.forceMkdir(tempDir);
            } catch (IOException ex) {
                logger.error(ex);
            }
        }
        filename = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + aiId + APEnetUtilities.FILESEPARATOR + "clevels_" + id + ".xml";
        OutputStream outputStream = null;
        try {
            CLevelDAO dao = DAOFactory.instance().getCLevelDAO();

            outputStream = new FileOutputStream(new File(filename));
            XMLOutputFactory outFactory = XMLOutputFactory.newFactory();
            outFactory.setProperty(XMLOutputFactory.IS_REPAIRING_NAMESPACES, true);
            XMLStreamWriter out = outFactory.createXMLStreamWriter(
                    new OutputStreamWriter(outputStream, "utf-8"));
            out.writeStartDocument();
            out.setPrefix(PREFIX, NAMESPACE);
            out.writeStartElement(NAMESPACE, "cLevels");
            out.setDefaultNamespace(NAMESPACE);
            out.writeDefaultNamespace(NAMESPACE);
            List<CLevel> clevels = new ArrayList<CLevel>();
            clevels.addAll(dao.getCLevelsWithoutUnitid(Integer.parseInt(id)));

            for (CLevel cLevel : clevels) {
                out.writeStartElement(NAMESPACE, "cLevel");
                out.writeAttribute(NAMESPACE, "id", cLevel.getClId().toString());
                out.writeCData(cLevel.getXml());
                out.writeEndElement();
            }

            out.writeEndElement();
            out.flush();
            out.close();
        } catch (FileNotFoundException ex) {
            logger.error(ex);
        } catch (XMLStreamException ex) {
            logger.error(ex);
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex);
        } finally {
            try {
                outputStream.close();
            } catch (IOException ex) {
                logger.error(ex);
            }
        }

        //"POST-PRODUCTION": REMOVAL OF CDATA-TAGS FROM FILE WHILE KEEPING ORIGINAL CONTENT
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new FileReader(new File(filename)));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("<!\\[CDATA\\[", "");
                line = line.replaceAll("\\]\\]>", "");
                sb.append(line.trim());
                sb.append(System.getProperty("line.separator"));
            }
            sb.toString();
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(CTableXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(CTableXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(CTableXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(filename)));
            writer.write(sb.toString());
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(CTableXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                    logger.error(ex);
                }
            }
        }
    }

    public void deleteFile(String id, String aiId){
        File fileToDelete = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + aiId + APEnetUtilities.FILESEPARATOR + "clevels_" + id + ".xml");
        if(fileToDelete.exists() && !fileToDelete.isDirectory()){
            try {
                ContentUtils.deleteFile(fileToDelete, true);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(CTableXMLHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getFilename() {
        return filename;
    }
}
