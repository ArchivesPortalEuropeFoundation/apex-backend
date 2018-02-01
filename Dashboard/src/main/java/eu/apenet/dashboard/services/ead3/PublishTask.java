/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.solr.Ead3SolrFields;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.ead3.publish.SolrDocTree;
import eu.apenet.dashboard.services.ead3.publish.SolrPublisher;
import eu.apenet.persistence.vo.Ead3;
import eu.apenet.persistence.vo.ValidatedState;
import gov.loc.ead.Ead;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.log4j.Logger;

/**
 *
 * @author kaisar
 */
public class PublishTask extends AbstractEad3Task {

    protected static final Logger LOGGER = Logger.getLogger(PublishTask.class);
    private final JAXBContext ead3Context;
    private final Unmarshaller ead3Unmarshaller;
    private Ead3SolrDocBuilder ead3SolrDocBuilder;

    public PublishTask() throws JAXBException {
        this.ead3Context = JAXBContext.newInstance(Ead.class);
        this.ead3Unmarshaller = ead3Context.createUnmarshaller();
        this.ead3SolrDocBuilder = new Ead3SolrDocBuilder();
    }

    public static boolean valid(Ead3 ead) {
        return ValidatedState.VALIDATED.equals(ead.getValidated()) && !ead.isPublished();
    }

    @Override
    protected void execute(Ead3 ead3Entity, Properties properties) throws Exception {
        if (valid(ead3Entity)) {
            FileInputStream fileInputStream = null;
            try {
                long startTime = System.currentTimeMillis();
                long solrTime = 0l;
                fileInputStream = getFileInputStream(ead3Entity.getPath());
                Ead ead3 = (Ead) ead3Unmarshaller.unmarshal(fileInputStream);
                ead3.setId(String.valueOf(ead3Entity.getId()));
                SolrDocTree tree = this.ead3SolrDocBuilder.buildDocTree(ead3, ead3Entity);
                SolrPublisher publisher = new SolrPublisher();
                long numberOfDocs = publisher.publish(tree);
                publisher.printTree(tree);

                ead3Entity.setTotalNumberOfDaos(Long.parseLong(tree.getRoot().getDataElement(Ead3SolrFields.NUMBER_OF_DAO).toString()));
                ead3Entity.setTotalNumberOfUnits(numberOfDocs);
                ead3Entity.setPublished(true);

                LOGGER.info("Ead3 Title: " + ead3.getControl().getFiledesc().getTitlestmt().getTitleproper().get(0).getContent().get(0));
                LOGGER.info("Time needed: " + (System.currentTimeMillis() - startTime));
//              
//                if (ead.getEadContent() == null) {
//                    message = "xml";
//                    solrTime = XmlEadParser.parseEadAndPublish(ead);
//                } else {
//                    message = "database";
//                    solrTime = DatabaseXmlEadParser.publish(ead);
//                }
//
//                logSolrAction(ead, message, solrTime, System.currentTimeMillis() - (startTime + solrTime));
            } catch (FileNotFoundException | JAXBException e) {
                throw new APEnetException(this.getActionName() + " " + e.getMessage(), e);
            } finally {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            }
        }
    }

    @Override
    protected String getActionName() {
        return "publish";
    }

    private static FileInputStream getFileInputStream(String path) throws FileNotFoundException {
        File file = new File(APEnetUtilities.getConfig().getRepoDirPath() + path);
//        File file = new File(path);
        return new FileInputStream(file);
    }

    public static void main(String[] args) throws JAXBException, Exception {
        PublishTask pTask = new PublishTask();
        Ead3 ead3 = new Ead3();
        ead3.setValidated(ValidatedState.VALIDATED);
        ead3.setPath(args[0]);
        pTask.execute(ead3, null);
    }
}
