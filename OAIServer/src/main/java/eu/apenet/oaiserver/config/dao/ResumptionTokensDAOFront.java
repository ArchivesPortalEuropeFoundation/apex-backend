package eu.apenet.oaiserver.config.dao;

import eu.apenet.oaiserver.config.MetadataFormats;
import eu.apenet.oaiserver.config.vo.ResumptionTokens;
import eu.apenet.persistence.dao.ResumptionTokenDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.MetadataFormat;
import eu.apenet.persistence.vo.ResumptionToken;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yoannmoranville on 08/04/15.
 */
public interface ResumptionTokensDAOFront {
    public abstract ResumptionTokens getResumptionToken(String resumptionTokenStr);

    public abstract ResumptionTokens saveResumptionTokens(ResumptionTokens resumptionTokens);

    public abstract void removeOldResumptionToken();
}
