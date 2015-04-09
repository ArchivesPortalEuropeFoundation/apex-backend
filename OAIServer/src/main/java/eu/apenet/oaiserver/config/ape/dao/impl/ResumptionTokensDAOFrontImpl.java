package eu.apenet.oaiserver.config.ape.dao.impl;

import eu.apenet.oaiserver.config.main.MetadataFormats;
import eu.apenet.oaiserver.config.main.dao.ResumptionTokensDAOFront;
import eu.apenet.oaiserver.config.main.vo.ResumptionTokens;
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
public class ResumptionTokensDAOFrontImpl implements ResumptionTokensDAOFront {
    private ResumptionTokenDAO resumptionTokenDAO;

    public ResumptionTokensDAOFrontImpl() {
        resumptionTokenDAO = DAOFactory.instance().getResumptionTokenDAO();
    }

    @Override
    public ResumptionTokens getResumptionToken(String resumptionTokenStr) {
        ResumptionToken resumptionToken = resumptionTokenDAO.findById(Integer.parseInt(resumptionTokenStr));
        if(resumptionToken == null) {
            return null;
        }
        return createResumptionTokens(resumptionToken);
    }

    @Override
    public ResumptionTokens saveResumptionTokens(ResumptionTokens resumptionTokens) {
        ResumptionToken resToken = new ResumptionToken();
        resToken.setFromDate(resumptionTokens.getFromDate());
        resToken.setUntilDate(resumptionTokens.getUntilDate());
        if(resumptionTokens.getSet() != null && !resumptionTokens.getSet().isEmpty()) {
            resToken.setSet(resumptionTokens.getSet().trim());
        }
        resToken.setLastRecordHarvested(resumptionTokens.getLastRecordHarvested());
        resToken.setExpirationDate(resumptionTokens.getExpirationDate());
        MetadataFormat metadataFormat = MetadataFormat.getMetadataFormat(resumptionTokens.getMetadataFormats().getName());
        resToken.setMetadataFormat(metadataFormat);

        ResumptionToken resumptionToken = resumptionTokenDAO.store(resToken);
        return createResumptionTokens(resumptionToken);
    }

    @Override
    public void removeOldResumptionToken() {
        ResumptionTokenDAO resumptionTokenDao = DAOFactory.instance().getResumptionTokenDAO();
        List<ResumptionToken> resumptionTokenList = resumptionTokenDao.getOldResumptionTokensThan(new Date());
        Iterator<ResumptionToken> iterator = resumptionTokenList.iterator();
        while(iterator.hasNext()){
            resumptionTokenDao.delete(iterator.next());
        }
    }

    private static ResumptionTokens createResumptionTokens(ResumptionToken resumptionToken) {
        ResumptionTokens resumptionTokens = new ResumptionTokens();
        resumptionTokens.setId(resumptionToken.getRtId());
        resumptionTokens.setMetadataFormats(MetadataFormats.getMetadataFormats(resumptionToken.getMetadataFormat().name()));
        resumptionTokens.setExpirationDate(resumptionToken.getExpirationDate());
        resumptionTokens.setFromDate(resumptionToken.getFromDate());
        resumptionTokens.setUntilDate(resumptionToken.getUntilDate());
        resumptionTokens.setSet(resumptionToken.getSet());
        resumptionTokens.setLastRecordHarvested(resumptionToken.getLastRecordHarvested());
        return resumptionTokens;
    }
}
