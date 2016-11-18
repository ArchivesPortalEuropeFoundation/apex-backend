/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3;

import eu.apenet.dashboard.services.ead3.publish.SolrDocTree;
import gov.loc.ead.Archdesc;
import gov.loc.ead.Did;
import gov.loc.ead.Dsc;
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
    
    public void buildDocTree(Ead ead3){
        jXPathContext = JXPathContext.newContext(ead3);
        
        LOGGER.info("Titles: "+retriveTitleProper());
        Iterator it = jXPathContext.iterate("archdesc/*/head");
        while (it.hasNext()) {
            System.out.println(((Head)it.next()).getContent());
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
