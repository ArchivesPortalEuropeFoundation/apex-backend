package eu.apenet.commons.infraestructure;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;

import eu.apenet.commons.exceptions.APEnetRuntimeException;
import eu.apenet.commons.utils.APEnetUtilities;

/**
 * Compose email messages.
 *
 * @author Bastiaan Verhoef
 *
 */
public class EmailComposer {

    private static Logger LOGGER = Logger.getLogger(EmailComposer.class);

    /**
     * Priority of an email Message
     *
     * @author Bastiaan Verhoef
     *
     */
    public enum Priority {
        HIGH("1"), NORMAL("3"), LOW("5");
        private String priority;

        private Priority(String priority) {
            this.priority = priority;
        }

        @Override
        public String toString() {
            return priority;
        }

    }
    private List<DataSource> attachments = new ArrayList<DataSource>();
    private String filename;
    private String subject;
    private boolean classpath;
    private boolean html;
    private Map<String, String> properties = new HashMap<String, String>();
    private static final String START_KEY = "\\$\\{";
    private static final String END_KEY = "\\}";
    private Priority priority = Priority.HIGH;

    public EmailComposer(String filename, String subject, boolean classpath, boolean html) {
        this.filename = filename;
        this.classpath = classpath;
        this.html = html;
        this.subject = subject;
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }

    public void addAttachment(String attachmentFilename, boolean classpath, String mimeType) {
        try {
            if (classpath) {
                InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(attachmentFilename);
                ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(inputStream, mimeType);
                int index = attachmentFilename.lastIndexOf("/");
                String name = attachmentFilename;
                if (index > 0) {
                    name = attachmentFilename.substring(index + 1);
                }
                byteArrayDataSource.setName(name);
                attachments.add(byteArrayDataSource);
            } else {
                attachments.add(new FileDataSource(attachmentFilename));
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new APEnetRuntimeException("Unable to add attachment " + attachmentFilename + " with classpath: " + classpath, e);
        }
    }

    public String getContent() throws IOException {
        String emailContent = retrieveTemplate();
        for (Map.Entry<String, String> property : properties.entrySet()) {
            emailContent = emailContent.replaceAll(START_KEY + property.getKey() + END_KEY, property.getValue());
        }
        return emailContent;
    }

    public String getSubject() {
        return APEnetUtilities.getConfig().getEmailPrefix() + subject;
    }

    public String getContentType() {
        if (html) {
            return "text/html";
        } else {
            return "text/plain";
        }
    }

    private String retrieveTemplate() throws IOException {
        StringBuilder emailContent = new StringBuilder();
        InputStreamReader fileReader = null;
        if (classpath) {
            InputStream emailInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
            fileReader = new InputStreamReader(emailInputStream, "UTF-8");
        } else {
            fileReader = new FileReader(filename);
        }
        BufferedReader bFileReader = new BufferedReader(fileReader);
        String line = null;
        while ((line = bFileReader.readLine()) != null) {
            emailContent.append(line + "\n");
        }
        bFileReader.close();
        return emailContent.toString();
    }

    public List<DataSource> getAttachments() {
        return attachments;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

}
