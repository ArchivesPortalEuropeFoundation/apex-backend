package eu.apenet.dashboard.manual;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.EadContent;

/**
 * User: Yoann Moranville
 * Date: 30/09/2011
 *
 * @author Yoann Moranville
 */
public class ReconstructEadFile {
    public static void reconstructEadFile(EadContent eadContent, String filePath) throws Exception {
        EadCreator eadCreator = null;
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(filePath));

            CLevelDAO cLevelDAO = DAOFactory.instance().getCLevelDAO();

            eadCreator = new EadCreator(outputStream);
            eadCreator.writeEadContent(eadContent.getXml());


            List<CLevel> cLevels = cLevelDAO.findTopCLevels(eadContent.getEcId());
            for(CLevel cLevel : cLevels){
                eadCreator.writeEadContent(cLevel.getXml());
                writeChildren(cLevel.getClId(), eadCreator);
                eadCreator.closeCTag();
            }
            eadCreator.closeEndTags();
            eadCreator.closeWriter();
            outputStream.flush();
            outputStream.close();

        } catch (Exception e){
            if(eadCreator != null)
                eadCreator.closeWriter();
            if(outputStream != null){
                outputStream.flush();
                outputStream.close();
            }
            throw e;
        }
    }

    private static void writeChildren(Long parentId, EadCreator eadCreator) throws XMLStreamException, IOException {
        List<CLevel> children = DAOFactory.instance().getCLevelDAO().findChilds(parentId);
        for(CLevel child : children){
            eadCreator.writeEadContent(child.getXml());
            writeChildren(child.getClId(), eadCreator);
            eadCreator.closeCTag();
        }
    }
}
