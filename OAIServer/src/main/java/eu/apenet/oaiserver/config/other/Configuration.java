package eu.apenet.oaiserver.config.other;

/**
 * Created by yoannmoranville on 09/04/15.
 */
public abstract class Configuration {
    public static final String REPOSITORY_NAME = "Somebody's OAI-PMH Repository";
    public static final String ADMIN_EMAIL = "info@someone.com";
    public static final String DELETED_RECORD_MANNER = "persistent";
    public static final String GRANULARITY = "YYYY-MM-DDThh:mm:ssZ";
    public static final String PROTOCOL_VERSION = "2.0";
    public static final String COMPRESSION = "gzip";
    public static final String XML_DIR_PATH = "";

    public static final int IDENTIFIERS_LIMIT = 100;
    public static final int EAD_RECORDS_LIMIT = 2;
    public static final int DC_RECORDS_LIMIT = 100;
    public static final int NOMINA_RECORDS_LIMIT = 100;


    public static final long EXPIRATION_TIME_IN_MILLISECONDS = 1000*60*30; //30 minutes
}
