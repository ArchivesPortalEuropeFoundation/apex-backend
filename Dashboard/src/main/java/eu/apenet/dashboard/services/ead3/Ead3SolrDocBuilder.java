/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3;

import eu.apenet.dashboard.services.ead3.publish.SolrDocTree;
import gov.loc.ead.Ead;
import gov.loc.ead.Head;
import gov.loc.ead.Titleproper;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.log4j.Logger;

/**
 *
 * @author mahbub
 */
public class Ead3SolrDocBuilder {

    protected static final Logger LOGGER = Logger.getLogger(Ead3SolrDocBuilder.class);

    private JXPathContext jXPathContext;
    private SolrDocTree solrDocTree;

    public void test() {
    }

//    public void parseEad(Ead ead) {
//        JXPathContext eadContext = JXPathContext.newContext(ead);
//        Iterator descs = eadContext.iterate("archdesc/accessrestrictOrAccrualsOrAcqinfo");
//        Did d = (Did) eadContext.getValue("archdesc/did");
//        LOGGER.info(d.getHead().getContent().toString());
//        for(Object o:d.getMDid()){
//            LOGGER.info(o.toString());
//        }
//        while (descs.hasNext()) {
//            Object obj =  descs.next();
//            LOGGER.info(obj.toString());
//            if(obj instanceof Did){
//                Did did = (Did) obj;
//                LOGGER.info(did.getMDid().get(0).toString());
//            }
//            else if(obj instanceof Dsc){
//                Dsc dsc = (Dsc) obj;
//                LOGGER.info(dsc.getHead().getContent().toString());
//                LOGGER.info(dsc.getC().size());
//            }
//        }
//    }
    public String getRecordId(Ead ead) {
        return ead.getControl().getRecordid().getContent();
    }

    public void buildDocTree(Ead ead3) {
        jXPathContext = JXPathContext.newContext(ead3);

        LOGGER.info("Titles: " + retriveTitleProper());
        Iterator it = jXPathContext.iterate("archdesc/*/head");
        while (it.hasNext()) {
            System.out.println(((Head) it.next()).getContent());
        }

    }

    private String retriveTitleProper() {
        StringBuilder stringBuilder = new StringBuilder();
        List<Titleproper> titlePropers = (List<Titleproper>) jXPathContext.getValue("control/filedesc/titlestmt/titleproper");
        for (Titleproper titleproper : titlePropers) {
            for (Serializable sr : titleproper.getContent()) {
                stringBuilder.append(sr);
                stringBuilder.append('\t');
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }
}
