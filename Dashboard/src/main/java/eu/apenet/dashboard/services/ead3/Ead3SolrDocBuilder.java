/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3;

import eu.apenet.commons.solr.Ead3SolrFields;
import eu.apenet.commons.solr.SolrFields;
import eu.apenet.commons.solr.SolrValues;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.exception.ItemNotFoundException;
import eu.apenet.dashboard.services.ead3.publish.DateUtil;
import eu.apenet.dashboard.services.ead3.publish.Ead3ToEacFieldMapKeys;
import eu.apenet.dashboard.services.ead3.publish.Ead3ToEacFieldMapStaticValues;
import eu.apenet.dashboard.services.ead3.publish.SolrDocNode;
import eu.apenet.dashboard.services.ead3.publish.SolrDocTree;
import eu.apenet.dashboard.services.ead3.publish.StoreEacFromEad3;
import eu.apenet.dashboard.types.ead3.ApeType;
import eu.apenet.dashboard.types.ead3.LocalTypeMap;
import eu.apenet.dashboard.utils.FileDownloader;
import eu.apenet.dpt.utils.eaccpf.Control;
import eu.apenet.dpt.utils.eaccpf.RecordId;
import eu.apenet.dpt.utils.eaccpf.Sources;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.Ead3;
import gov.loc.ead.Chronlist;
import gov.loc.ead.Corpname;
import gov.loc.ead.Dao;
import gov.loc.ead.Daoset;
import gov.loc.ead.Did;
import gov.loc.ead.Dsc;
import gov.loc.ead.Ead;
import gov.loc.ead.Event;
import gov.loc.ead.Famname;
import gov.loc.ead.Item;
import gov.loc.ead.MMixedBasic;
import gov.loc.ead.Langmaterial;
import gov.loc.ead.Language;
import gov.loc.ead.Languageset;
import gov.loc.ead.Localtypedeclaration;
import gov.loc.ead.MCBase;
import gov.loc.ead.MMixedBasicPlusAccess;
import gov.loc.ead.Maintenanceagency;
import gov.loc.ead.Origination;
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
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import net.archivesportaleurope.apetypes.LocalTypes;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author mahbub
 */
public class Ead3SolrDocBuilder {

    protected static final Logger LOGGER = Logger.getLogger(Ead3SolrDocBuilder.class);

    private JXPathContext jXPathContext;
    private Ead ead3;
    private Ead3 ead3Entity;
    private LocalTypeMap localTypeMap = new LocalTypeMap();
    private SolrDocNode archdescNode = new SolrDocNode();
    private boolean openDataEnable = false;

//    private final JAXBContext ead3Context;
    private final JAXBContext localTypeContext;
    private final Unmarshaller localTypeUnmarshaller;

    public String getRecordId(Ead ead) {
        return ead.getControl().getRecordid().getContent();
    }

    public Ead3SolrDocBuilder() throws JAXBException {
//        this.ead3Context = JAXBContext.newInstance(Ead.class);
        this.localTypeContext = JAXBContext.newInstance(LocalTypes.class);
        this.localTypeUnmarshaller = localTypeContext.createUnmarshaller();
    }

    public SolrDocTree buildDocTree(Ead ead3, Ead3 ead3Entity) throws JAXBException {
        this.ead3Entity = ead3Entity;
        this.openDataEnable = ead3Entity.getArchivalInstitution().isOpenDataEnabled();
        this.ead3 = ead3;
        jXPathContext = JXPathContext.newContext(this.ead3);
        try {
            Localtypedeclaration localTypeDeclaration = this.retrieveLocaltypedeclaration();

            if (null != localTypeDeclaration && null != localTypeDeclaration.getCitation()
                    && null != localTypeDeclaration.getCitation().getHref()) {
                String localTypeFileLink = localTypeDeclaration.getCitation().getHref();
                LOGGER.debug("Localtype link:" + localTypeFileLink);
                Path tempFilePath = FileDownloader.downloadFile(localTypeFileLink, APEnetUtilities.getDashboardConfig().getTempAndUpDirPath());
                LocalTypes localTypes = (LocalTypes) this.localTypeUnmarshaller.unmarshal(tempFilePath.toFile());
                this.localTypeMap.add(localTypes);
            } else {
                LOGGER.debug("No localtype declaration was found");
            }
        } catch (ItemNotFoundException | IOException ex) {
            LOGGER.debug(ex.getMessage());
        }
        this.retrieveArchdescMain();
        SolrDocTree solrDocTree = new SolrDocTree(archdescNode);

        return solrDocTree;
    }

    private void retrieveArchdescMain() {
        if (this.ead3 == null || this.jXPathContext == null) {
            throw new IllegalStateException("Not initialized properly");
        }

        this.archdescNode.setDataElement(Ead3SolrFields.AI_ID, ead3Entity.getAiId());
        this.archdescNode.setDataElement(Ead3SolrFields.AI_NAME, ead3Entity.getArchivalInstitution().getAiname());
        this.archdescNode.setDataElement(Ead3SolrFields.AI, ead3Entity.getArchivalInstitution().getAiname() + "|" + ead3Entity.getAiId());

        this.archdescNode.setDataElement(Ead3SolrFields.COUNTRY_ID, ead3Entity.getArchivalInstitution().getCountry().getId());
        this.archdescNode.setDataElement(Ead3SolrFields.COUNTRY_NAME, ead3Entity.getArchivalInstitution().getCountry().getCname());
        this.archdescNode.setDataElement(Ead3SolrFields.COUNTRY, ead3Entity.getArchivalInstitution().getCountry().getCname() + "|" + ead3Entity.getArchivalInstitution().getCountry().getId());

        this.archdescNode.setDataElement(Ead3SolrFields.REPOSITORY_CODE, ead3Entity.getArchivalInstitution().getRepositorycode());
        this.archdescNode.setDataElement(Ead3SolrFields.ID, ead3.getId());
        this.archdescNode.setDataElement(Ead3SolrFields.EAD_ID, ead3.getId());
        this.archdescNode.setDataElement(Ead3SolrFields.NUMBER_OF_ANCESTORS, 0);
        this.archdescNode.setDataElement(Ead3SolrFields.NUMBER_OF_DESCENDENTS, 0);
        this.archdescNode.setDataElement(Ead3SolrFields.TITLE_PROPER, this.retrieveTitleProper());
        this.archdescNode.setDataElement(Ead3SolrFields.LANGUAGE, this.retrieveControlLanguage());
        this.archdescNode.setDataElement(Ead3SolrFields.RECORD_ID, this.retrieveRecordId());
        this.archdescNode.setDataElement(Ead3SolrFields.OPEN_DATA, this.openDataEnable);
        retrieveAgency();

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

        Map<String, Object> didMap = this.processDid(aDid);
        if (this.archdescNode.getDataElement(Ead3SolrFields.ID) == null) {
            this.archdescNode.setDataElement(Ead3SolrFields.ID, UUID.randomUUID());
        }

        for (Map.Entry entry : didMap.entrySet()) {
            this.archdescNode.setDataElement(entry.getKey().toString(), entry.getValue());
        }
//        this.archdescNode.setDataElement(Ead3SolrFields.UNIT_TITLE, didMap.get(Ead3SolrFields.UNIT_TITLE));
//        this.archdescNode.setDataElement(Ead3SolrFields.UNIT_ID, didMap.get(Ead3SolrFields.UNIT_ID));
//        this.archdescNode.setDataElement(Ead3SolrFields.UNIT_DATE, didMap.get(Ead3SolrFields.UNIT_DATE));
//        this.archdescNode.setDataElement(Ead3SolrFields.LANG_MATERIAL, didMap.get(Ead3SolrFields.LANG_MATERIAL));
//        this.archdescNode.setDataElement(Ead3SolrFields.OTHER, didMap.get(Ead3SolrFields.OTHER));
        this.archdescNode.setDataElement(Ead3SolrFields.LEVEL_NAME, "archdesc");
//        this.archdescNode.setDataElement(Ead3SolrFields.NUMBER_OF_DAO, Integer.parseInt(didMap.get(Ead3SolrFields.NUMBER_OF_DAO)));

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
        int currentNumberOfDescendents = 0;

        while (it.hasNext()) {
            Object element = it.next();
            if (element instanceof MCBase) {
                //ToDo update based on child
                SolrDocNode child = processC((MCBase) element, this.archdescNode);
                this.archdescNode.setChild(child);
                currentNumberofDao += Integer.parseInt(child.getDataElement(Ead3SolrFields.NUMBER_OF_DAO).toString());
                currentNumberOfDescendents += Integer.parseInt(child.getDataElement(Ead3SolrFields.NUMBER_OF_DESCENDENTS).toString()) + 1;
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
        this.archdescNode.setDataElement(Ead3SolrFields.NUMBER_OF_DESCENDENTS, currentNumberOfDescendents);

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

        Map<String, Object> didMap = this.processDid(cDid);
        //ToDo gen currentNodeId
        cRoot.setDataElement(Ead3SolrFields.AI_ID, this.archdescNode.getDataElement(Ead3SolrFields.AI_ID));
        cRoot.setDataElement(Ead3SolrFields.AI_NAME, this.archdescNode.getDataElement(Ead3SolrFields.AI_NAME));
        cRoot.setDataElement(Ead3SolrFields.AI, this.archdescNode.getDataElement(Ead3SolrFields.AI));

        cRoot.setDataElement(Ead3SolrFields.COUNTRY_ID, this.archdescNode.getDataElement(Ead3SolrFields.COUNTRY_ID));
        cRoot.setDataElement(Ead3SolrFields.COUNTRY_NAME, this.archdescNode.getDataElement(Ead3SolrFields.COUNTRY_NAME));
        cRoot.setDataElement(Ead3SolrFields.COUNTRY, this.archdescNode.getDataElement(Ead3SolrFields.COUNTRY));

        cRoot.setDataElement(Ead3SolrFields.REPOSITORY_CODE, this.archdescNode.getDataElement(Ead3SolrFields.REPOSITORY_CODE));
        cRoot.setDataElement(Ead3SolrFields.ID, UUID.randomUUID());

        //global fields
        cRoot.setDataElement(Ead3SolrFields.LANGUAGE, this.archdescNode.getDataElement(Ead3SolrFields.LANGUAGE));
        cRoot.setDataElement(Ead3SolrFields.LANG_MATERIAL, this.archdescNode.getDataElement(Ead3SolrFields.LANG_MATERIAL));
        cRoot.setDataElement(Ead3SolrFields.RECORD_ID, this.archdescNode.getDataElement(Ead3SolrFields.RECORD_ID));
        cRoot.setDataElement(Ead3SolrFields.TITLE_PROPER, this.archdescNode.getDataElement(Ead3SolrFields.TITLE_PROPER));

        cRoot.setDataElement(Ead3SolrFields.PARENT_ID, parent.getDataElement(Ead3SolrFields.ID));
        cRoot.setDataElement(Ead3SolrFields.EAD_ID, this.archdescNode.getDataElement(Ead3SolrFields.EAD_ID));
        cRoot.setDataElement(Ead3SolrFields.UNIT_TITLE, didMap.get(Ead3SolrFields.UNIT_TITLE));
        cRoot.setDataElement(Ead3SolrFields.UNIT_ID, this.archdescNode.getDataElement(Ead3SolrFields.RECORD_ID) + "-" + didMap.get(Ead3SolrFields.UNIT_ID));
//        cRoot.setDataElement(Ead3SolrFields.UNIT_ID, parent.getDataElement(Ead3SolrFields.UNIT_ID) + "-" + didMap.get(Ead3SolrFields.UNIT_ID));
        cRoot.setDataElement(Ead3SolrFields.UNIT_DATE, didMap.get(Ead3SolrFields.UNIT_DATE));
        cRoot.setDataElement(Ead3SolrFields.START_DATE, didMap.get(Ead3SolrFields.START_DATE));
        cRoot.setDataElement(Ead3SolrFields.END_DATE, didMap.get(Ead3SolrFields.END_DATE));
        cRoot.setDataElement(Ead3SolrFields.DATE_TYPE, didMap.get(Ead3SolrFields.DATE_TYPE));
        cRoot.setDataElement(Ead3SolrFields.OTHER, didMap.get(Ead3SolrFields.OTHER));
        cRoot.setDataElement(Ead3SolrFields.DAO_LINKS, didMap.get(Ead3SolrFields.DAO_LINKS));
        cRoot.setDataElement(Ead3SolrFields.DAO_TYPE, didMap.get(Ead3SolrFields.DAO_TYPE));
        cRoot.setDataElement(Ead3SolrFields.DAO, didMap.get(Ead3SolrFields.DAO));
        cRoot.setDataElement(Ead3SolrFields.NUMBER_OF_ANCESTORS, (Integer) parent.getDataElement(Ead3SolrFields.NUMBER_OF_ANCESTORS) + 1);
        cRoot.setDataElement(Ead3SolrFields.PARENT_UNIT_ID, parent.getDataElement(Ead3SolrFields.PARENT_UNIT_ID));

        int currentNumberofDao = Integer.parseInt(didMap.get(Ead3SolrFields.NUMBER_OF_DAO).toString());
        int currentNumberOfDescendents = 0;
        Iterator it = context.iterate("*");

        StringBuilder otherStrBuilder = new StringBuilder();

        while (it.hasNext()) {
            Object element = it.next();
            if (element instanceof Scopecontent) {
                Scopecontent scopecontent = (Scopecontent) element;
//                cRoot.setEacData(this.buildEacData(cRoot, ((Chronlist) scopecontent.getChronlistOrListOrTable().get(0)).getChronitem().get(0).getEvent()));
                Map<String, String> eventMap = buildEvent(((Chronlist) scopecontent.getChronlistOrListOrTable().get(0)).getChronitem().get(0).getEvent());
                this.buildAndStoreEacData(cRoot, ((Chronlist) scopecontent.getChronlistOrListOrTable().get(0)).getChronitem().get(0).getEvent());
                for (Map.Entry entry : eventMap.entrySet()) {
                    cRoot.setDataElement(entry.getKey().toString() + "_s", entry.getValue().toString());
                }
                cRoot.setDataElement(Ead3SolrFields.SCOPE_CONTENT, retrieveScopeContentAsText(element));
            } else if (element instanceof Did) {
            } else if (element instanceof MCBase) {
                //ToDo update based on child
                SolrDocNode child = processC((MCBase) element, cRoot);
                cRoot.setChild(child);
                currentNumberofDao += Integer.parseInt(child.getDataElement(Ead3SolrFields.NUMBER_OF_DAO).toString());
                currentNumberOfDescendents += Integer.parseInt(child.getDataElement(Ead3SolrFields.NUMBER_OF_DESCENDENTS).toString()) + 1;
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

        cRoot.setDataElement(Ead3SolrFields.OPEN_DATA, this.openDataEnable);
        cRoot.setDataElement(Ead3SolrFields.LEVEL_NAME, "clevel");
        cRoot.setDataElement(Ead3SolrFields.NUMBER_OF_DAO, currentNumberofDao);
        cRoot.setDataElement(Ead3SolrFields.NUMBER_OF_DESCENDENTS, currentNumberOfDescendents);

        if (otherStrBuilder.length() > 0) {
            cRoot.setDataElement(Ead3SolrFields.OTHER, otherStrBuilder.toString());
        }

        return cRoot;
    }

    private String retrieveScopeContentAsText(Object scopecontent) {
        return getContentText(scopecontent, " ");

    }

    private String retrieveTitleProper() {
        StringBuilder stringBuilder = new StringBuilder();
        List<Titleproper> titlePropers = (List<Titleproper>) jXPathContext.getValue("control/filedesc/titlestmt/titleproper");
        for (Titleproper titleproper : titlePropers) {
            for (Serializable sr : titleproper.getContent()) {
                stringBuilder.append(sr);
                stringBuilder.append(" ");
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString().trim();
    }

    private String getPlainText(Object obj) {
        if (obj == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder("");

        if (!(obj instanceof JAXBElement) && (obj instanceof String)) {
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
        return stringBuilder.toString().trim();
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
            } else if (object instanceof Item) {
                tmpStr = this.getPlainText(object);
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
        return stringBuilder.toString().trim();
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

    private String retrieveRecordId() {
        if (this.ead3 == null || this.jXPathContext == null) {
            throw new IllegalStateException("Not initialized properly");
        }
        try {
            Recordid recordId = (Recordid) jXPathContext.getValue("control/recordid");
            return recordId.getContent();
        } catch (Exception e) {
            return "";
        }
    }

    private void retrieveAgency() {
        if (this.ead3 == null || this.jXPathContext == null) {
            throw new IllegalStateException("Not initialized properly");
        }
        try {
            Maintenanceagency maintenanceagency = (Maintenanceagency) jXPathContext.getValue("control/maintenanceagency");
            this.archdescNode.setTransientDataElement(Ead3SolrFields.AGENCY_CODE, maintenanceagency.getAgencycode().getContent());
            this.archdescNode.setTransientDataElement(Ead3SolrFields.AGENCY_NAME, maintenanceagency.getAgencyname().get(0).getContent());
        } catch (Exception e) {

        }
    }

    private Localtypedeclaration retrieveLocaltypedeclaration() throws ItemNotFoundException {
        if (this.ead3 == null || this.jXPathContext == null) {
            throw new IllegalStateException("Not initialized properly");
        }
        try {
            ArrayList<Localtypedeclaration> localtypedeclarations = (ArrayList<Localtypedeclaration>) jXPathContext.getValue("control/localtypedeclaration");
            if (null == localtypedeclarations || localtypedeclarations.isEmpty()) {
                throw new ItemNotFoundException("control/localtypedeclaration is empty");
            }
            return localtypedeclarations.get(0);
        } catch (Exception ex) {
            throw new ItemNotFoundException("control/localtypedeclaration Not found", ex);
        }

    }

    private String retrieveControlLanguage() {
        if (this.ead3 == null || this.jXPathContext == null) {
            throw new IllegalStateException("Not initialized properly");
        }
        try {
            Language language = (Language) jXPathContext.getValue("control/languagedeclaration/language");
            return language.getLangcode();
        } catch (Exception e) {
            return "";
        }
    }

    private String retrieveSubtitle() {
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

    private Map<String, Object> processDid(Did did) {
        Map<String, Object> didInfoMap = new HashMap<>();

        didInfoMap.put(Ead3SolrFields.NUMBER_OF_DAO, "0");

        if (did != null) {
            didInfoMap.put(Ead3SolrFields.DAO, false);
            for (Object obj : did.getMDid()) {
                if (obj instanceof Unittitle) {
                    didInfoMap.put(Ead3SolrFields.UNIT_TITLE, this.getContent((MMixedBasicPlusAccess) obj));
                    didInfoMap.put(Ead3SolrFields.UNIT_TITLE_LOCALTYPE, ((Unittitle) obj).getLocaltype());
                } else if (obj instanceof Daoset) {
                    int count = 0;
                    List<JAXBElement<?>> daos = ((Daoset) obj).getContent();
                    List<String> daoFieldValues = new ArrayList<>();
                    Set<String> daoTypeValues = new HashSet<>();
                    for (JAXBElement jAXBElement : daos) {
                        if (jAXBElement.getDeclaredType().equals(Dao.class)) {

                            count++;
                            if (count <= 10) {
                                Dao dao = (Dao) jAXBElement.getValue();
                                if (!dao.getLocaltype().equals("fullsize")) {
                                    daoFieldValues.add(dao.getHref());
                                    daoTypeValues.add(dao.getDaotype());
                                }
                            }
                        }
                    }
                    didInfoMap.put(Ead3SolrFields.DAO_LINKS, daoFieldValues);
                    didInfoMap.put(Ead3SolrFields.DAO_TYPE, daoTypeValues);
                    didInfoMap.put(Ead3SolrFields.NUMBER_OF_DAO, count + "");
                    if (count > 0) {
                        didInfoMap.put(Ead3SolrFields.DAO, true);
                    } else {
                        didInfoMap.put(Ead3SolrFields.DAO, false);
                    }

                } else if (obj instanceof Unitid) {
                    if (didInfoMap.get(Ead3SolrFields.UNIT_ID) == null) {
                        didInfoMap.put(Ead3SolrFields.UNIT_ID, this.getContent((MMixedBasic) obj));
                        didInfoMap.put(Ead3SolrFields.OTHER_UNIT_ID, "");
                    } else {
                        didInfoMap.put(Ead3SolrFields.OTHER_UNIT_ID, didInfoMap.get(Ead3SolrFields.OTHER_UNIT_ID) + " " + this.getContent((MMixedBasic) obj));
                    }
                } else if (obj instanceof Unitdate) {
                    didInfoMap.put(Ead3SolrFields.UNIT_DATE, this.getContent((MMixedBasic) obj));
                    Unitdate unitdate = (Unitdate) obj;
                    String dateNormal = unitdate.getNormal();

                    List<String> dates = DateUtil.getDates(didInfoMap.get(Ead3SolrFields.UNIT_DATE).toString(), dateNormal);
                    didInfoMap.put(Ead3SolrFields.START_DATE, dates.get(0));
                    didInfoMap.put(Ead3SolrFields.END_DATE, dates.get(1));
                    if (StringUtils.isBlank(didInfoMap.get(Ead3SolrFields.UNIT_DATE).toString())) {
                        didInfoMap.put(Ead3SolrFields.DATE_TYPE, SolrValues.DATE_TYPE_NO_DATE_SPECIFIED);
                    } else if (StringUtils.isBlank(dates.get(0))) {
                        didInfoMap.put(Ead3SolrFields.DATE_TYPE, SolrValues.DATE_TYPE_OTHER_DATE);
                    } else {
                        didInfoMap.put(Ead3SolrFields.DATE_TYPE, SolrValues.DATE_TYPE_NORMALIZED);
                    }
                } else if (obj instanceof Langmaterial) {
                    didInfoMap.put(Ead3SolrFields.LANG_MATERIAL, processLangmaterial((Langmaterial) obj));
                } else if (obj instanceof Origination) {
                    Map<String, String> originationMap = buildOriginationMap((Origination) obj);
                    for (Map.Entry entry : originationMap.entrySet()) {
                        didInfoMap.put(entry.getKey().toString() + "_s", entry.getValue().toString());
                    }
                } else {
                    Object other = didInfoMap.get(Ead3SolrFields.OTHER);
                    if (other != null) {
                        didInfoMap.put(Ead3SolrFields.OTHER, other.toString() + " " + getPlainText(obj));
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

    private Map<String, String> buildEvent(Event event) {
        Map<String, String> eventMap = new HashMap<>();
        List<Serializable> events = event.getContent();

        for (Serializable eacEvent : events) {
            if (eacEvent instanceof JAXBElement) {
                JAXBElement eacElement = (JAXBElement) eacEvent;
                if (eacElement.getDeclaredType().equals(Persname.class)) {
                    buildPersnameOrFamOrCorpMap(((Persname) eacElement.getValue()).getPart(), "persname", eventMap);
                } else if (eacElement.getDeclaredType().equals(Corpname.class)) {
                    buildPersnameOrFamOrCorpMap(((Corpname) eacElement.getValue()).getPart(), "corpname", eventMap);
                } else if (eacElement.getDeclaredType().equals(Famname.class)) {
                    buildPersnameOrFamOrCorpMap(((Famname) eacElement.getValue()).getPart(), "famname", eventMap);
                }
            }
        }
        return eventMap;
    }

    private List<Map<String, Object>> buildAndStoreEacData(SolrDocNode cRoot, Event event) {
        List<Map<String, Object>> listOfEacs = new ArrayList<>();
        List<Serializable> events = event.getContent();
        int count = 0;
        for (Serializable eacEvent : events) {
            if (eacEvent instanceof JAXBElement) {
                JAXBElement eacElement = (JAXBElement) eacEvent;
                if (eacElement.getDeclaredType().equals(Persname.class)) {
                    count++;
                    Map<String, Object> personMap = this.generateEacCpfDataMap(cRoot, ((Persname) eacElement.getValue()).getPart(), count);
                    personMap.put(Ead3ToEacFieldMapKeys.CPF_TYPE, Ead3ToEacFieldMapStaticValues.CPF_TYPE_PERSON);

                    try {
                        new StoreEacFromEad3(personMap, ead3Entity.getArchivalInstitution().getCountry().getIsoname(), ead3Entity.getAiId(), ead3Entity.getIdentifier()).storeEacCpf();
                    } catch (Exception ex) {
                        java.util.logging.Logger.getLogger(Ead3SolrDocBuilder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    listOfEacs.add(personMap);
                } else if (eacElement.getDeclaredType().equals(Corpname.class)) {
                    count++;
                    Map<String, Object> corpMap = generateEacCpfDataMap(cRoot, ((Corpname) eacElement.getValue()).getPart(), count);
                    corpMap.put(Ead3ToEacFieldMapKeys.CPF_TYPE, Ead3ToEacFieldMapStaticValues.CPF_TYPE_CORPORATE_BODY);
                    listOfEacs.add(corpMap);
                } else if (eacElement.getDeclaredType().equals(Famname.class)) {
                    count++;
                    Map<String, Object> famMap = generateEacCpfDataMap(cRoot, ((Famname) eacElement.getValue()).getPart(), count);
                    famMap.put(Ead3ToEacFieldMapKeys.CPF_TYPE, Ead3ToEacFieldMapStaticValues.CPF_TYPE_FAMILY);
                    listOfEacs.add(famMap);
                }
            }
        }
        return listOfEacs;
    }

    private Map<String, String> buildOriginationMap(Origination origination) {
        Map<String, String> eventMap = new HashMap<>();
        for (Object obj : origination.getCorpnameOrFamnameOrName()) {
            if (obj instanceof Persname) {
                buildPersnameOrFamOrCorpMap(((Persname) obj).getPart(), "persname", eventMap);
            } else if (obj instanceof Corpname) {
                buildPersnameOrFamOrCorpMap(((Corpname) obj).getPart(), "corpname", eventMap);
            } else if (obj instanceof Famname) {
                buildPersnameOrFamOrCorpMap(((Famname) obj).getPart(), "famname", eventMap);
            }
        }
        return eventMap;
    }

    private Map<String, Object> generateEacCpfDataMap(SolrDocNode cRoot, List<Part> parts, int count) {
        Map<String, Object> eacMap = new HashMap<>();
//        EacCpf eacCpf = new EacCpf();

        Control control = new Control();
        RecordId recordId = new RecordId();
        recordId.setValue(cRoot.getDataElement(Ead3SolrFields.RECORD_ID).toString());
        control.setRecordId(recordId);
        Sources sources = new Sources();
//        sources.getSource().add(new Source())
//        control.setSources();
//        eacCpf.setControl(control);
//        Cpf
//        eacMap.put(SolrFields.ID, UUID.randomUUID().toString());
//        
//        eacCpf.
//        eacMap.put(SolrFields.COUNTRY, cRoot.getDataElement(Ead3SolrFields.COUNTRY));
//        eacMap.put(SolrFields.AI, cRoot.getDataElement(Ead3SolrFields.AI));
        eacMap.put(SolrFields.REPOSITORY_CODE, returnAsArray(cRoot.getDataElement(Ead3SolrFields.REPOSITORY_CODE).toString()));
        eacMap.put(Ead3SolrFields.RECORD_ID, returnAsArray(cRoot.getDataElement(Ead3SolrFields.RECORD_ID).toString()));

        int partNameCount = 1;
        int partGenealogyDescriptionCount = 0;
        int partDateCount = 0;
        String firstName = "";
        String lastName = "";
        String birthDate = "";
        String deathDate = "";
        for (Part part : parts) {
            //ToDo:
            try {
                ApeType apeType = ApeType.get(this.localTypeMap.getApeType(part.getLocaltype().toLowerCase()));
                if (apeType == null) {
                    LOGGER.debug("Unsupported ApeType: " + part.getLocaltype());
                    continue;
                }
                switch (apeType) {
                    case FIRSTNAME:
                        partNameCount++;
                        firstName = getContent(part);
                        eacMap.put(Ead3ToEacFieldMapKeys.IDENTITY_PERSON_NAME_ + "1_part_" + partNameCount, returnAsArray(firstName));
                        eacMap.put(Ead3ToEacFieldMapKeys.IDENTITY_PERSON_NAME_ + "1_comp_" + partNameCount, returnAsArray(Ead3ToEacFieldMapStaticValues.PART_LOCAL_TYPE_FRIST_NAME));
                        break;
                    case LASTNAME:
                        partNameCount++;
                        lastName = getContent(part);
                        eacMap.put(Ead3ToEacFieldMapKeys.IDENTITY_PERSON_NAME_ + "1_part_" + partNameCount, returnAsArray(lastName));
                        eacMap.put(Ead3ToEacFieldMapKeys.IDENTITY_PERSON_NAME_ + "1_comp_" + partNameCount, returnAsArray(Ead3ToEacFieldMapStaticValues.PART_LOCAL_TYPE_SUR_NAME));
                        break;
                    case GENDER:
                    case ROLE:
                        partGenealogyDescriptionCount++;
                        eacMap.put(Ead3ToEacFieldMapKeys.GENEALOGY_DESCRIPTION_ + partGenealogyDescriptionCount, returnAsArray(getContent(part)));
                        //need to add language form default arcdes lang
                        eacMap.put(Ead3ToEacFieldMapKeys.DEFAULT_LANGUAGE, returnAsArray("dut"));
                        eacMap.put(Ead3ToEacFieldMapKeys.DEFAULT_SCRIPT, returnAsArray("Latn"));
                        eacMap.put("controlLanguage", returnAsArray("dut"));
                        eacMap.put("controlScript", returnAsArray("Latn"));
                        break;
                    case DEATHDATE:
                        deathDate = getContent(part);
                        if (StringUtils.isEmpty(deathDate)) {
                            deathDate = Ead3ToEacFieldMapStaticValues.DATE_EXISTING_TYPE_UNKNOWN;
                        }
                        partDateCount++;
                        eacMap.put(Ead3ToEacFieldMapKeys.DATE_EXISTENCE_TABLE_DATE_ + "2_" + partDateCount, returnAsArray(deathDate));
                        break;
                    case BITHDATE:
                        birthDate = getContent(part);
                        if (StringUtils.isEmpty(birthDate)) {
                            birthDate = Ead3ToEacFieldMapStaticValues.DATE_EXISTING_TYPE_UNKNOWN;
                        }
                        eacMap.put(Ead3ToEacFieldMapKeys.DATE_EXISTENCE_TABLE_DATE_ + "1_" + partDateCount, returnAsArray(birthDate));
                        break;
                    default:
                        break;
                }
            } catch (ItemNotFoundException ex) {
                LOGGER.debug(ex.getMessage());
            }
        }

        if (StringUtils.isEmpty(deathDate)) {
            eacMap.put(Ead3ToEacFieldMapKeys.DATE_EXISTENCE_TABLE_DATE_ + "2_1", returnAsArray(Ead3ToEacFieldMapStaticValues.DATE_EXISTING_TYPE_UNKNOWN));
        }
        if (StringUtils.isEmpty(birthDate)) {
            eacMap.put(Ead3ToEacFieldMapKeys.DATE_EXISTENCE_TABLE_DATE_ + "1_1", returnAsArray(Ead3ToEacFieldMapStaticValues.DATE_EXISTING_TYPE_UNKNOWN));
        }

        partNameCount++;
        eacMap.put(Ead3ToEacFieldMapKeys.IDENTITY_PERSON_NAME_ + "1_part_1", returnAsArray(firstName + " " + lastName));
        eacMap.put(Ead3ToEacFieldMapKeys.IDENTITY_PERSON_NAME_ + "1_comp_1", returnAsArray(Ead3ToEacFieldMapStaticValues.PART_LOCAL_TYPE_PERS_NAME));

        //test build map for ead3 to eac
        EacCpfDAO cpfDAO = DAOFactory.instance().getEacCpfDAO();
        EacCpf eacCpf = cpfDAO.getEacCpfByIdentifier(ead3Entity.getAiId(), cRoot.getDataElement(Ead3SolrFields.UNIT_ID).toString() + "_" + count);
        if (null != eacCpf) {
            eacMap.put(Ead3ToEacFieldMapKeys.APE_ID, returnAsArray(cRoot.getDataElement(Ead3SolrFields.UNIT_ID).toString() + "_" + count));
        } else {
            eacMap.put(Ead3ToEacFieldMapKeys.TEXT_LOCAL_ID_ + "1", returnAsArray(cRoot.getDataElement(Ead3SolrFields.UNIT_ID).toString() + "_" + count));
        }

        //adding relation to resource 
        eacMap.put("textResRelationName_1", returnAsArray("See more"));
        eacMap.put("textResRelationId_1", returnAsArray(cRoot.getDataElement(Ead3SolrFields.UNIT_ID).toString()));
        eacMap.put("textResRelationLink_1", returnAsArray("simple"));
        eacMap.put("resRelationType_1", returnAsArray("subjectOf"));
        eacMap.put("textareaResRelationDescription_1", returnAsArray(cRoot.getDataElement(Ead3SolrFields.UNIT_TITLE).toString()));

        //for test
        eacMap.put("dateExistenceTable_rows", returnAsArray("1"));
        eacMap.put("dateExistenceTable_date_1_radio_1", returnAsArray("unknown"));
        eacMap.put("dateExistenceTable_date_2_radio_1", returnAsArray("unknown"));

        eacMap.put("identityPersonName_1_rows", returnAsArray("0"));

        //identify automatic eac generation from ead3
        eacMap.put("agent", "Ead3");
        return eacMap;
    }

    private String[] returnAsArray(String... input) {
        return input;
    }

    private void buildPersnameOrFamOrCorpMap(List<Part> parts, String type, Map<String, String> map) {
        parts.stream().forEach((part) -> {
            if (map.get(type + "_" + part.getLocaltype()) == null) {
                map.put(type + "_" + part.getLocaltype(), getContent(part));
            } else {
                map.put(type + "_" + part.getLocaltype(), map.get(type + "_" + part.getLocaltype()) + " " + getContent(part));
            }
        });
    }

    private static FileInputStream getFileInputStream(String path) throws FileNotFoundException {
        File file = new File(APEnetUtilities.getConfig().getRepoDirPath() + path);
//        File file = new File(path);
        return new FileInputStream(file);
    }
}
