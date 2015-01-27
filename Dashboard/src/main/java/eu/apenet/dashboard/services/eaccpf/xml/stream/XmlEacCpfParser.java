package eu.apenet.dashboard.services.eaccpf.xml.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.eaccpf.xml.stream.publish.EacCpfPublishData;
import eu.apenet.dashboard.services.eaccpf.xml.stream.publish.EacCpfSolrPublisher;
import eu.apenet.dashboard.services.ead.xml.AbstractParser;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EacCpf;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import eu.archivesportaleurope.xml.XmlParser;

public class XmlEacCpfParser extends AbstractParser {

    private static Logger LOG = Logger.getLogger(XmlEacCpfParser.class);
    public static final String UTF_8 = "utf-8";

    public static long parseAndPublish(EacCpf eacCpf) throws Exception {
        EacCpfSolrPublisher solrPublisher = new EacCpfSolrPublisher();
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        FileInputStream fileInputStream = getFileInputStream(eacCpf.getPath());

        EacCpfXpathReader eacCpfParser = new EacCpfXpathReader();
        try {
            JpaUtil.beginDatabaseTransaction();
            XmlParser.parse(fileInputStream, eacCpfParser);
            fileInputStream.close();
            EacCpfPublishData publishData = new EacCpfPublishData();
            eacCpfParser.fillData(publishData, eacCpf);
            solrPublisher.publishEacCpf(eacCpf, publishData);
            solrPublisher.commitSolrDocuments();
            eacCpf.setCpfRelations(publishData.getNumberOfCpfRelations());
            eacCpf.setResourceRelations(publishData.getNumberOfArchivalMaterialRelations());
            eacCpf.setFunctionRelations(publishData.getNumberOfFunctionRelations());
			ContentUtils.changeSearchable(eacCpf, true);
			eacCpfDAO.insertSimple(eacCpf);
            JpaUtil.commitDatabaseTransaction();
        } catch (Exception de) {
            JpaUtil.rollbackDatabaseTransaction();
            if (solrPublisher != null) {
                LOG.error(eacCpf + ": rollback:", de);
                solrPublisher.unpublish(eacCpf);
    			eacCpfDAO.insertSimple(eacCpf);
                JpaUtil.commitDatabaseTransaction();
            }
            throw de;
        }
        return solrPublisher.getSolrTime();
    }

    private static FileInputStream getFileInputStream(String path) throws FileNotFoundException, XMLStreamException {
        File file = new File(APEnetUtilities.getConfig().getRepoDirPath() + path);
        return new FileInputStream(file);
    }


}
