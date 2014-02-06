/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apex.eaccpf.actions;

import static com.opensymphony.xwork2.Action.SUCCESS;
import eu.apex.eaccpf.EacCpfLoader;
import eu.apex.eaccpf.util.DateType;
import eu.apex.eaccpf.util.IdentifierType;
import eu.apex.eaccpf.util.NameEntryType;
import eu.apex.eaccpf.util.RelationType;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;

/**
 *
 * @author papp
 */
public class IndexAction extends EacCpfAction {

    private File upload;
    private String uploadContentType;
    private String uploadFileName;
    private String content;
    private EacCpfLoader loader;
    private String useMode;
    private String defaultLanguage;
    private String defaultScript;

    @Override
    public String execute() {
        this.loader = new EacCpfLoader();
        if (upload != null) {
            boolean result = this.loader.fillEacCpf(upload);
        } else {
            Random random = new Random();
            int fakeId = random.nextInt(1000000000);
            this.loader.setRecordId(Integer.toString(fakeId));
            this.loader.setAgencyCode("EU-00000000001");
        }
        if (this.loader.getIdentifiers()== null) {
            this.loader.setIdentifiers(new ArrayList<IdentifierType>());
        }
        if (this.loader.getNameEntries()== null) {
            this.loader.setNameEntries(new ArrayList<NameEntryType>());
        }
        if (this.loader.getExistDates()== null) {
            this.loader.setExistDates(new ArrayList<DateType>());
        }
        if (this.loader.getCpfRelations() == null) {
            this.loader.setCpfRelations(new ArrayList<RelationType>());
        }
        if (this.loader.getResRelations() == null) {
            this.loader.setResRelations(new ArrayList<RelationType>());
        }
        if (this.loader.getOtherRecordIds() == null) {
            this.loader.setOtherRecordIds(new ArrayList<String>());
        }
        if (getServletRequest().getParameter("useMode") != null) {
            useMode = getServletRequest().getParameter("useMode");
        }
        if (getServletRequest().getParameter("defaultLanguage") != null) {
            defaultLanguage = getServletRequest().getParameter("defaultLanguage");
        }
        if (getServletRequest().getParameter("defaultScript") != null) {
            defaultScript = getServletRequest().getParameter("defaultScript");
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

    public void setUpload(File file) {
        this.upload = file;
    }

    public void setUploadContentType(String contentType) {
        this.uploadContentType = contentType;
    }

    public void setUploadFileName(String filename) {
        this.uploadFileName = filename;
    }

    public File getUpload() {
        return upload;
    }

    public String getUploadContentType() {
        return uploadContentType;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public EacCpfLoader getLoader() {
        return loader;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUseMode() {
        return useMode;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public String getDefaultScript() {
        return defaultScript;
    }
}
