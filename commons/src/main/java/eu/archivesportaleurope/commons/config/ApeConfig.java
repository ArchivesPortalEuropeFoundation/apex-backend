package eu.archivesportaleurope.commons.config;

import eu.apenet.commons.exceptions.BadConfigurationException;

public class ApeConfig {

    private String repoDirPath;
    private boolean configured;
    private String emailPrefix;

    public String getRepoDirPath() {
        return repoDirPath;
    }

    public void setRepoDirPath(String repoDirPath) {
        checkConfigured();
        this.repoDirPath = repoDirPath;
    }

    public final void finalizeConfigPhase() {
        checkConfigured();
        initBeforeFinalize();
        configured = true;
    }

    /**
     * method to set properties before finalize
     */
    protected void initBeforeFinalize() {

    }

    public String getEmailPrefix() {
        return emailPrefix;
    }

    public void setEmailPrefix(String emailPrefix) {
        checkConfigured();
        this.emailPrefix = emailPrefix;
    }

    protected void checkConfigured() {
        if (configured) {
            throw new BadConfigurationException("The config phase is already closed. Please configure this class before finalizing the config phase");
        }
    }
}
