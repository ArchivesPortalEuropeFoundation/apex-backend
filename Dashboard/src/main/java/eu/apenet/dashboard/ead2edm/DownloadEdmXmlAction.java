package eu.apenet.dashboard.ead2edm;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.dashboard.utils.ZipManager;
import eu.apenet.dpt.utils.ead2edm.EdmFileUtils;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ese;
import java.util.ArrayList;

public class DownloadEdmXmlAction extends AbstractInstitutionAction {

    /**
     *
     */
    private static final long serialVersionUID = -4216154271715161521L;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String execute() throws IOException {
        EseDAO dao = DAOFactory.instance().getEseDAO();
        if (NumberUtils.isNumber(id)) {
            List<Ese> eses = dao.getEses(NumberUtils.toInt(id), getAiId());
            List<String> esesToZip = new ArrayList<>()         ;
            if (eses.size() > 0) {
                eses.stream().filter((ese) -> (ese.getPath() != null && !ese.getPath().matches(""))).forEachOrdered((ese) -> {
                    esesToZip.add(APEnetUtilities.getConfig().getRepoDirPath() + ese.getPath());
                });
            }
            if (esesToZip.size() > 0) {
                String outputFileName = esesToZip.get(0);
                outputFileName = outputFileName.substring(0, outputFileName.lastIndexOf("/"));
                String outputPath = outputFileName.substring(0, outputFileName.lastIndexOf("/")) + "/";
                outputFileName = outputFileName.substring(outputFileName.lastIndexOf("/") + 1);
                outputFileName = EdmFileUtils.encodeSpecialCharactersForFilename(outputFileName);
                outputFileName = outputPath + outputFileName + ".zip";
                File zipFile = new File(outputFileName);
                if (zipFile.exists()){
                    zipFile.delete();
                }
                ZipManager.zipMultiple(esesToZip, zipFile);
                ContentUtils.download(getServletRequest(), getServletResponse(), zipFile, "ZIP");
            }
        }
        return SUCCESS;
    }

}
