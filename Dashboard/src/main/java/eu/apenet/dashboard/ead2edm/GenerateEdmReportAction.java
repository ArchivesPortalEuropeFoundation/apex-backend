/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.ead2edm;

import static com.opensymphony.xwork2.Action.SUCCESS;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dpt.utils.ead2edm.EdmFileUtils;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.util.extendxsl.EdmQualityCheckerCall;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ese;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.xml.sax.SAXException;

/**
 *
 * @author papp
 */
public class GenerateEdmReportAction extends AbstractInstitutionAction {

    private String id;
    private String noUnitidNumber;
    private String duplicateUnitidsNumber;
    private String duplicateUnitids;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoUnitidNumber() {
        return noUnitidNumber;
    }

    public void setNoUnitidNumber(String noUnitidNumber) {
        this.noUnitidNumber = noUnitidNumber;
    }

    public String getDuplicateUnitidsNumber() {
        return duplicateUnitidsNumber;
    }

    public void setDuplicateUnitidsNumber(String duplicateUnitidsNumber) {
        this.duplicateUnitidsNumber = duplicateUnitidsNumber;
    }

    public String getDuplicateUnitids() {
        return duplicateUnitids;
    }

    public void setDuplicateUnitids(String duplicateUnitids) {
        this.duplicateUnitids = duplicateUnitids;
    }

    @Override
    public String execute() {
        EseDAO dao = DAOFactory.instance().getEseDAO();
        if (NumberUtils.isNumber(id)) {

            List<Ese> eses = dao.getEses(NumberUtils.toInt(id), getAiId());
            if (eses.size() > 0) {
                Ese ese = eses.get(0);
                File file = EdmFileUtils.getRepoFile(APEnetUtilities.getConfig().getRepoDirPath(), ese.getPath());
                File reportScript = new File(APEnetUtilities.getDashboardConfig().getXslDirPath()
                        + APEnetUtilities.FILESEPARATOR + "report/edmQuality.xsl");
                InputStream is2;
                try {
                    is2 = FileUtils.openInputStream(file);
                    EdmQualityCheckerCall edmQualityCheckerCall = new EdmQualityCheckerCall();
                    TransformationTool.createTransformation(is2, null, reportScript, null, true, true, null, false, edmQualityCheckerCall);
                    is2.close();

                    noUnitidNumber = Integer.toString(edmQualityCheckerCall.getCounterNoUnitid());
                    int duplicateElements = 0;
                    
                    StringWriter duplicates = new StringWriter();
                    Map<String, Integer> unitids = edmQualityCheckerCall.getIdentifiers();
                    for (Map.Entry<String, Integer> unitid : unitids.entrySet()) {
                        if (unitid.getValue() > 1) {
                            if (duplicates.getBuffer().length() > 0) {
                                duplicates.append(", ");
                            }
                            duplicates.append(unitid.getKey());
                            duplicateElements += unitid.getValue();
                        }
                    }
                    duplicateUnitidsNumber = Integer.toString(duplicateElements);
                    duplicateUnitids = scapeCharacters(duplicates.toString());
                } catch (IOException e) {
                    LOG.error(e.toString());
                } catch (SAXException e) {
                    LOG.error(e.toString());
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
