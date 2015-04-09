package eu.apenet.oaiserver.config.ape2;

/**
 * Created by yoannmoranville on 08/04/15.
 */
public abstract class Configuration {
    public static final String REPOSITORY_NAME = "Archives Portal Europe OAI-PMH Repository";
    public static final String ADMIN_EMAIL = "info@apex-project.eu";
    public static final String DELETED_RECORD_MANNER = "persistent";
    public static final String GRANULARITY = "YYYY-MM-DDThh:mm:ssZ";
    public static final String PROTOCOL_VERSION = "2.0";
    public static final String COMPRESSION = "gzip";
    public static final String XML_DIR_PATH = "";
//    public static final String XML_DIR_PATH = APEnetUtilities.getConfig().getRepoDirPath();

    public static final int RECORDS_LIMIT = 2;
    public static final int IDENTIFIERS_LIMIT = 100;
}
