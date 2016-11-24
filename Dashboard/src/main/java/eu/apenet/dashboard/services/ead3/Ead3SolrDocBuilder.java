/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3;

import eu.apenet.commons.solr.Ead3SolrFields;
import eu.apenet.dashboard.services.ead3.publish.SolrDocNode;
import gov.loc.ead.C;
import gov.loc.ead.C01;
import gov.loc.ead.Chronitem;
import gov.loc.ead.Chronlist;
import gov.loc.ead.Did;
import gov.loc.ead.Ead;
import gov.loc.ead.Eventdatetime;
import gov.loc.ead.Head;
import gov.loc.ead.MMixedBasic;
import gov.loc.ead.Langmaterial;
import gov.loc.ead.Language;
import gov.loc.ead.Languageset;
import gov.loc.ead.MCBase;
import gov.loc.ead.P;
import gov.loc.ead.Part;
import gov.loc.ead.Persname;
import gov.loc.ead.Recordid;
import gov.loc.ead.Scopecontent;
import gov.loc.ead.Subtitle;
import gov.loc.ead.Titleproper;
import gov.loc.ead.Unitdate;
import gov.loc.ead.Unitid;
import gov.loc.ead.Unittitle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBElement;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.log4j.Logger;

/**
 *
 * @author mahbub
 */
public class Ead3SolrDocBuilder {

    protected static final Logger LOGGER = Logger.getLogger(Ead3SolrDocBuilder.class);

    private JXPathContext jXPathContext;
    private Ead ead3;
    private SolrDocNode archdescNode = new SolrDocNode();

    public String getRecordId(Ead ead) {
        return ead.getControl().getRecordid().getContent();
    }

    public void buildDocTree(Ead ead3) {
        this.ead3 = ead3;
        jXPathContext = JXPathContext.newContext(this.ead3);
        
    }
    
    private void retrieveArchdescMain () {
        if (this.ead3==null || this.jXPathContext == null) {
            throw new IllegalStateException("Not initialized properly");
        }
        
        this.archdescNode.setDataElement(Ead3SolrFields.ID, ead3.getId());
        this.archdescNode.setDataElement(Ead3SolrFields.TITLE_PROPER, this.retriveTitleProper());
        this.archdescNode.setDataElement(Ead3SolrFields.LANGUAGE, this.retriveControlLanguage());
        this.archdescNode.setDataElement(Ead3SolrFields.RECORD_ID, this.retriveRecordId());
    }

    private List<Map<String, String>> processScopeContent(Scopecontent scopecontent) {
        List<Map<String, String>> scopeMapList = new ArrayList<>();
        for (Object obj : scopecontent.getChronlistOrListOrTable()) {
            if (obj instanceof Chronlist) {
                Chronlist chronlist = (Chronlist) obj;
                for (Chronitem chi : chronlist.getChronitem()) {
                    for (Object sr : chi.getEvent().getContent()) {
                        if (sr instanceof JAXBElement) {
                            JAXBElement j = (JAXBElement) sr;
                            if (j.getDeclaredType().equals(Persname.class)) {
                                JAXBElement<Persname> pr = (JAXBElement<Persname>) sr;
                                scopeMapList.add(getPersName(pr.getValue()));
                            }
                        }
                    }
                }
            }
        }
        return scopeMapList;
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

    private String getPlainText(Object obj) {
        if (obj == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder("");

        if (obj instanceof String) {
            stringBuilder.append(obj);
            if (stringBuilder.length() > 0) {
                stringBuilder.append(" ");
            }
        } else {
            JXPathContext context = JXPathContext.newContext(obj);
            Iterator it = context.iterate("*");
            boolean added = false;

            while (it.hasNext()) {
                Object object = it.next();
                if (object instanceof MMixedBasic) {
                    MMixedBasic contentHolder = (MMixedBasic) object;

                    for (Serializable sr : contentHolder.getContent()) {
                        String str1 = (String) sr;
                        if (!str1.isEmpty()) {
                            stringBuilder.append(str1);
                            added = true;
                        }
                    }
                    if (added) {
                        stringBuilder.append(" ");
                    }
                    added = false;
                } else {
                    String str = getPlainText(object);
                    if (!str.isEmpty()) {
                        stringBuilder.append(str);
                    }
                }
            }
        }
        return stringBuilder.toString();
    }
    
    private String retriveRecordId() {
        if (this.ead3==null || this.jXPathContext == null) {
            throw new IllegalStateException("Not initialized properly");
        }
        Recordid recordId = (Recordid) jXPathContext.getValue("control/recordid");
        return recordId.getContent();
    }
    
    private String retriveControlLanguage() {
        if (this.ead3==null || this.jXPathContext == null) {
            throw new IllegalStateException("Not initialized properly");
        }
        Language language = (Language) jXPathContext.getValue("control/languagedeclaration/language");
        return language.getLangcode();
    }

    private String retriveSubtitle() {
        StringBuilder stringBuilder = new StringBuilder();
        List<Subtitle> subtitles = (List<Subtitle>) jXPathContext.getValue("control/filedesc/titlestmt/subtitle");
        for (Subtitle subtitle : subtitles) {
            for (Serializable sr : subtitle.getContent()) {
                stringBuilder.append(sr);
                stringBuilder.append('\t');
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    private String retriveArchdescUnitTitle() {
        StringBuilder stringBuilder = new StringBuilder();
        Unittitle unittitle = (Unittitle) jXPathContext.getValue("archdesc/did/unittitle");
        for (Serializable sr : unittitle.getContent()) {
            stringBuilder.append(sr);
        }
        stringBuilder.append('\t');
        return stringBuilder.toString();
    }

    private Map<String, String> parseDid(Did did) {
        Map<String, String> didInfoMap = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (Serializable sr : did.getHead().getContent()) {
            stringBuilder.append(sr);
        }
        stringBuilder.append('\t');
        didInfoMap.put("head", stringBuilder.toString());
        for (Object obj : did.getMDid()) {
            if (obj instanceof Unittitle) {
                stringBuilder = new StringBuilder();

                for (Serializable sr : ((Unittitle) obj).getContent()) {
                    stringBuilder.append(sr);
                }
                stringBuilder.append('\t');
                didInfoMap.put("unittitle", stringBuilder.toString());
            } else if (obj instanceof Unitid) {
                stringBuilder = new StringBuilder();

                for (Serializable sr : ((Unitid) obj).getContent()) {
                    stringBuilder.append(sr);
                }
                stringBuilder.append('\t');
                didInfoMap.put("unitid", stringBuilder.toString());
            } else if (obj instanceof Unitdate) {
                stringBuilder = new StringBuilder();

                for (Serializable sr : ((Unitdate) obj).getContent()) {
                    stringBuilder.append(sr);
                }
                stringBuilder.append('\t');
                didInfoMap.put("unitdate", stringBuilder.toString());

                didInfoMap.put("unitdateCalender", ((Unitdate) obj).getCalendar());
            } else if (obj instanceof Langmaterial) {
                didInfoMap.putAll(parseLangmateriall((Langmaterial) obj));
            }
        }

        return didInfoMap;
    }

    private Map<String, String> parseLangmateriall(Langmaterial langmaterial) {
        Map<String, String> langMap = new HashMap<>();
        for (Object obj : langmaterial.getLanguageOrLanguageset()) {
            if (obj instanceof Languageset) {
                Languageset languageset = (Languageset) obj;
                StringBuilder stringBuilder = new StringBuilder();
                for (Language language : languageset.getLanguage()) {
                    stringBuilder.append(language.getLangcode()).append(":").append(language.getContent()).append('\t').append('\n');
                }
                langMap.put("languages", stringBuilder.toString());

                stringBuilder = new StringBuilder();

                for (P p : languageset.getDescriptivenote().getP()) {
                    for (Serializable sr : p.getContent()) {
                        stringBuilder.append(sr);
                    }
                    stringBuilder.append('\n');
                }
                langMap.put("langDesc", stringBuilder.toString());
            }
        }
        return langMap;
    }

    private Map<String, String> getPersName(Persname persname) {
        Map<String, String> persNameMap = new HashMap<>();
        persNameMap.put("identifier", persname.getIdentifier());
        persNameMap.put("relator", persname.getRelator());
        persNameMap.put("rules", persname.getRules());
        for (Part part : persname.getPart()) {
            StringBuilder builder = new StringBuilder();
            for (Serializable sr : part.getContent()) {
                builder.append(sr);
            }
            persNameMap.put(part.getLocaltype(), builder.toString());
        }
        return persNameMap;
    }
}
