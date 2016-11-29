/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3;

import eu.apenet.commons.solr.Ead3SolrFields;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.ead3.publish.SolrDocNode;
import eu.apenet.dashboard.services.ead3.publish.SolrDocTree;
import eu.apenet.persistence.vo.Ead3;
import gov.loc.ead.Chronitem;
import gov.loc.ead.Chronlist;
import gov.loc.ead.Dao;
import gov.loc.ead.Daoset;
import gov.loc.ead.Did;
import gov.loc.ead.Dsc;
import gov.loc.ead.Ead;
import gov.loc.ead.MMixedBasic;
import gov.loc.ead.Langmaterial;
import gov.loc.ead.Language;
import gov.loc.ead.Languageset;
import gov.loc.ead.MCBase;
import gov.loc.ead.MMixedBasicPlusAccess;
import gov.loc.ead.Part;
import gov.loc.ead.Persname;
import gov.loc.ead.Recordid;
import gov.loc.ead.Scopecontent;
import gov.loc.ead.Subtitle;
import gov.loc.ead.Titleproper;
import gov.loc.ead.Unitdate;
import gov.loc.ead.Unitid;
import gov.loc.ead.Unittitle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
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
    private Ead3 persistantEad3;
    private SolrDocNode archdescNode = new SolrDocNode();

    private final JAXBContext ead3Context;
    private final Unmarshaller ead3Unmarshaller;

    public String getRecordId(Ead ead) {
        return ead.getControl().getRecordid().getContent();
    }

    public Ead3SolrDocBuilder() throws JAXBException {
        this.ead3Context = JAXBContext.newInstance(Ead.class);
        this.ead3Unmarshaller = ead3Context.createUnmarshaller();
    }

    public SolrDocTree buildDocTree(Ead3 ead3) throws JAXBException {
        this.persistantEad3 = ead3;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = getFileInputStream(ead3.getPath());
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ead3SolrDocBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.ead3 = (Ead) ead3Unmarshaller.unmarshal(fileInputStream);
        this.ead3.setId(String.valueOf(persistantEad3.getId()));
        jXPathContext = JXPathContext.newContext(this.ead3);

        this.retrieveArchdescMain();
        SolrDocTree solrDocTree = new SolrDocTree(archdescNode);

        return solrDocTree;
    }

    private void retrieveArchdescMain() {
        if (this.ead3 == null || this.jXPathContext == null) {
            throw new IllegalStateException("Not initialized properly");
        }

        this.archdescNode.setDataElement(Ead3SolrFields.AI_ID, persistantEad3.getAiId());
        this.archdescNode.setDataElement(Ead3SolrFields.AI_NAME, persistantEad3.getArchivalInstitution().getAiname());
        this.archdescNode.setDataElement(Ead3SolrFields.ID, ead3.getId());
        this.archdescNode.setDataElement(Ead3SolrFields.EAD_ID, ead3.getId());
        this.archdescNode.setDataElement(Ead3SolrFields.TITLE_PROPER, this.retriveTitleProper());
        this.archdescNode.setDataElement(Ead3SolrFields.LANGUAGE, this.retriveControlLanguage());
        this.archdescNode.setDataElement(Ead3SolrFields.RECORD_ID, this.retriveRecordId());

        this.processArchdesc();

    }

    private void processArchdesc() {
        if (this.ead3 == null || this.jXPathContext == null) {
            throw new IllegalStateException("Not initialized properly");
        }
        Did aDid = (Did) this.jXPathContext.getValue("archdesc/did", Did.class);
        if (aDid == null) {
            return; //ToDo: invalid c exception?
        }

        Map<String, String> didMap = this.processDid(aDid);
        if (this.archdescNode.getDataElement(Ead3SolrFields.ID) == null) {
            this.archdescNode.setDataElement(Ead3SolrFields.ID, UUID.randomUUID());
        }
        this.archdescNode.setDataElement(Ead3SolrFields.UNIT_TITLE, didMap.get(Ead3SolrFields.UNIT_TITLE));
        this.archdescNode.setDataElement(Ead3SolrFields.UNIT_ID, didMap.get(Ead3SolrFields.UNIT_ID));
        this.archdescNode.setDataElement(Ead3SolrFields.UNIT_DATE, didMap.get(Ead3SolrFields.UNIT_DATE));
        this.archdescNode.setDataElement(Ead3SolrFields.LANG_MATERIAL, didMap.get(Ead3SolrFields.LANG_MATERIAL));
        this.archdescNode.setDataElement(Ead3SolrFields.OTHER, didMap.get(Ead3SolrFields.OTHER));
        this.archdescNode.setDataElement(Ead3SolrFields.LEVEL_NAME, "archdesc");
        this.archdescNode.setDataElement(Ead3SolrFields.NUMBER_OF_DAO, Integer.parseInt(didMap.get(Ead3SolrFields.NUMBER_OF_DAO)));

        Iterator it = this.jXPathContext.iterate("archdesc/*");

        while (it.hasNext()) {
            Object element = it.next();
            if (element instanceof Did) {
            } else if (element instanceof Dsc) {
                this.processDsc((Dsc) element, this.archdescNode);
            }
        }
    }

    private void processDsc(Dsc dsc, SolrDocNode parent) {
        if (dsc == null) {
            return;
        }
        JXPathContext context = JXPathContext.newContext(dsc);
        Iterator it = context.iterate("*");

        StringBuilder otherStrBuilder;
        if (this.archdescNode.getDataElement(Ead3SolrFields.OTHER) != null) {
            otherStrBuilder = new StringBuilder((String) this.archdescNode.getDataElement(Ead3SolrFields.OTHER));
        } else {
            otherStrBuilder = new StringBuilder();
        }

        int currentNumberofDao = 0;

        while (it.hasNext()) {
            Object element = it.next();
            if (element instanceof MCBase) {
                //ToDo update based on child
                SolrDocNode child = processC((MCBase) element, this.archdescNode);
                this.archdescNode.setChild(child);
                currentNumberofDao += Integer.parseInt(child.getDataElement(Ead3SolrFields.NUMBER_OF_DAO).toString());
            } else {
                String str = this.getPlainText(element);
                if (!str.isEmpty()) {
                    if (otherStrBuilder.length() > 0) {
                        otherStrBuilder.append(" ");
                    }
                    otherStrBuilder.append(str);
                }
            }
        }

        this.archdescNode.setDataElement(Ead3SolrFields.NUMBER_OF_DAO, currentNumberofDao);

        if (otherStrBuilder.length() > 0) {
            this.archdescNode.setDataElement(Ead3SolrFields.OTHER, otherStrBuilder.toString());
        }
    }

    private SolrDocNode processC(MCBase cElement, SolrDocNode parent) {
        if (cElement == null) {
            return null;
        }
        SolrDocNode cRoot = new SolrDocNode();
        JXPathContext context = JXPathContext.newContext(cElement);
        Did cDid = (Did) context.getValue("did", Did.class);
        if (cDid == null) {
            return null; //ToDo: invalid c exception?
        }

        Map<String, String> didMap = this.processDid(cDid);
        //ToDo gen currentNodeId
        cRoot.setDataElement(Ead3SolrFields.AI_ID, persistantEad3.getAiId());
        cRoot.setDataElement(Ead3SolrFields.AI_NAME, persistantEad3.getArchivalInstitution().getAiname());
        cRoot.setDataElement(Ead3SolrFields.ID, UUID.randomUUID());
        cRoot.setDataElement(Ead3SolrFields.PARENT_ID, parent.getDataElement(Ead3SolrFields.ID));
        cRoot.setDataElement(Ead3SolrFields.EAD_ID, this.archdescNode.getDataElement(Ead3SolrFields.EAD_ID));
        cRoot.setDataElement(Ead3SolrFields.UNIT_TITLE, didMap.get(Ead3SolrFields.UNIT_TITLE));
        cRoot.setDataElement(Ead3SolrFields.UNIT_ID, didMap.get(Ead3SolrFields.UNIT_ID));
        cRoot.setDataElement(Ead3SolrFields.UNIT_DATE, didMap.get(Ead3SolrFields.UNIT_DATE));
        cRoot.setDataElement(Ead3SolrFields.OTHER, didMap.get(Ead3SolrFields.OTHER));
        int currentNumberofDao = Integer.parseInt(didMap.get(Ead3SolrFields.NUMBER_OF_DAO));
        Iterator it = context.iterate("*");

        StringBuilder otherStrBuilder = new StringBuilder();

        while (it.hasNext()) {
            Object element = it.next();
            if (element instanceof Did) {
            } else if (element instanceof MCBase) {
                //ToDo update based on child
                SolrDocNode child = processC((MCBase) element, cRoot);
                cRoot.setChild(child);
                currentNumberofDao += Integer.parseInt(child.getDataElement(Ead3SolrFields.NUMBER_OF_DAO).toString());
            } else {
                String str = this.getPlainText(element);
                if (!str.isEmpty()) {
                    if (otherStrBuilder.length() > 0) {
                        otherStrBuilder.append(" ");
                    }
                    otherStrBuilder.append(str);
                }
            }
        }

        cRoot.setDataElement(Ead3SolrFields.LEVEL_NAME, "clevel");
        cRoot.setDataElement(Ead3SolrFields.NUMBER_OF_DAO, currentNumberofDao);

        if (otherStrBuilder.length() > 0) {
            cRoot.setDataElement(Ead3SolrFields.OTHER, otherStrBuilder.toString());
        }

        return cRoot;
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
                stringBuilder.append(" ");
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
            String str = ((String) obj);
            stringBuilder.append(str);
            if (stringBuilder.length() > 0) {
                stringBuilder.append(" ");
            }
        } else {
            JXPathContext context = JXPathContext.newContext(obj);
            Iterator it = context.iterate("*");

            while (it.hasNext()) {
                Object object = it.next();
                if (object instanceof MMixedBasic) {
                    MMixedBasic contentHolder = (MMixedBasic) object;

                    for (Serializable sr : contentHolder.getContent()) {
                        String str1 = (String) sr;
                        if (!str1.isEmpty()) {
                            if (stringBuilder.length() > 0) {
                                stringBuilder.append(" ");
                            }
                            stringBuilder.append(str1);
                        }
                    }
                } else {
                    String str = getPlainText(object);
                    if (!str.isEmpty()) {
                        stringBuilder.append(str);
                    }
                }
            }
        }
        return stringBuilder.toString().replaceAll("(\r\n|\n|\t)", "");
    }

    private String getContentText(Object obj, String delimiter) {
        if (obj == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder("");
        JXPathContext context = JXPathContext.newContext(obj);
        Iterator it = context.iterate("*");

        String tmpStr = "";

        while (it.hasNext()) {
            Object object = it.next();
            if (object instanceof MMixedBasic) {
                tmpStr = this.getContent((MMixedBasic) object);
            } else if (object instanceof MMixedBasicPlusAccess) {
                tmpStr = this.getContent((MMixedBasicPlusAccess) obj);
            } else {
                tmpStr = this.getContentText(object, delimiter);
            }
            tmpStr = tmpStr.trim();

            if (!tmpStr.isEmpty()) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(delimiter);
                }
                stringBuilder.append(tmpStr);
            }
        }
        return stringBuilder.toString();
    }

    private String getContent(MMixedBasic obj) {
        return this.getContentText(obj.getContent());
    }

    private String getContent(MMixedBasicPlusAccess obj) {
        return this.getContentText(obj.getContent());
    }

    private String getContentText(List<Serializable> contents) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Serializable sr : contents) {
            if (sr instanceof String) {
                String str = ((String) sr).trim();
                if (!str.isEmpty()) {
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append(' ');
                    }
                    stringBuilder.append(str);
                }
            }
        }
        return stringBuilder.toString();
    }

    private String retriveRecordId() {
        if (this.ead3 == null || this.jXPathContext == null) {
            throw new IllegalStateException("Not initialized properly");
        }
        Recordid recordId = (Recordid) jXPathContext.getValue("control/recordid");
        return recordId.getContent();
    }

    private String retriveControlLanguage() {
        if (this.ead3 == null || this.jXPathContext == null) {
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

    private Map<String, String> processDid(Did did) {
        Map<String, String> didInfoMap = new HashMap<>();

        didInfoMap.put(Ead3SolrFields.NUMBER_OF_DAO, "0");

        if (did != null) {
            for (Object obj : did.getMDid()) {
                if (obj instanceof Unittitle) {
                    didInfoMap.put(Ead3SolrFields.UNIT_TITLE, this.getContent((MMixedBasicPlusAccess) obj));
                } else if (obj instanceof Daoset) {
                    int count = 0;
                    List<JAXBElement<?>> daos = ((Daoset) obj).getContent();
                    for (JAXBElement jAXBElement : daos) {
                        if (jAXBElement.getDeclaredType().equals(Dao.class)) {
                            count++;
                        }
                    }
                    didInfoMap.put(Ead3SolrFields.NUMBER_OF_DAO, count + "");

                } else if (obj instanceof Unitid) {
                    if (didInfoMap.get(Ead3SolrFields.UNIT_ID) == null) {
                        didInfoMap.put(Ead3SolrFields.UNIT_ID, this.getContent((MMixedBasic) obj));
                        didInfoMap.put(Ead3SolrFields.OTHER_UNIT_ID, "");
                    } else {
                        didInfoMap.put(Ead3SolrFields.OTHER_UNIT_ID, didInfoMap.get(Ead3SolrFields.OTHER_UNIT_ID) + " " + this.getContent((MMixedBasic) obj));
                    }
                } else if (obj instanceof Unitdate) {
                    didInfoMap.put(Ead3SolrFields.UNIT_DATE, this.getContent((MMixedBasic) obj));

                    didInfoMap.put("unitdateCalenderType", ((Unitdate) obj).getCalendar());
                } else if (obj instanceof Langmaterial) {
                    didInfoMap.put(Ead3SolrFields.LANG_MATERIAL, processLangmaterial((Langmaterial) obj));
                } else {
                    String other = didInfoMap.get(Ead3SolrFields.OTHER);
                    if (other != null) {
                        didInfoMap.put(Ead3SolrFields.OTHER, other + " " + getPlainText(obj));
                    } else {
                        didInfoMap.put(Ead3SolrFields.OTHER, getPlainText(obj));
                    }
                }

            }
        }
        return didInfoMap;
    }

    private String processLangmaterial(Langmaterial langmaterial) {
        StringBuilder langcodes = new StringBuilder();
        if (langmaterial != null) {
            for (Object obj : langmaterial.getLanguageOrLanguageset()) {
                if (obj instanceof Language) {
                    langcodes.append(((Language) obj).getLangcode()).append(" ");
                } else if (obj instanceof Languageset) {
                    Languageset languageset = (Languageset) obj;
                    for (Language language : languageset.getLanguage()) {
                        langcodes.append(language.getLangcode()).append(" ");
                    }
                }
            }
        }
        return langcodes.toString().trim();
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

    private static FileInputStream getFileInputStream(String path) throws FileNotFoundException {
        File file = new File(APEnetUtilities.getConfig().getRepoDirPath() + path);
//        File file = new File(path);
        return new FileInputStream(file);
    }
}
