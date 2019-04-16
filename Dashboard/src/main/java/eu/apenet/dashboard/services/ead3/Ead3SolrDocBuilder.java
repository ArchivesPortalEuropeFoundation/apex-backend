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
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.Ead3;
import eu.apenet.persistence.vo.EadContent;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import gov.loc.ead3.Archdesc;
import gov.loc.ead3.C;
import gov.loc.ead3.Corpname;
import gov.loc.ead3.Dao;
import gov.loc.ead3.Daoset;
import gov.loc.ead3.Did;
import gov.loc.ead3.Dsc;
import gov.loc.ead3.Ead;
import gov.loc.ead3.Event;
import gov.loc.ead3.Famname;
import gov.loc.ead3.Genreform;
import gov.loc.ead3.Item;
import gov.loc.ead3.MMixedBasic;
import gov.loc.ead3.Langmaterial;
import gov.loc.ead3.Language;
import gov.loc.ead3.Languageset;
import gov.loc.ead3.Localtypedeclaration;
import gov.loc.ead3.MCBase;
import gov.loc.ead3.MMixedBasicDate;
import gov.loc.ead3.MMixedBasicPlusAccess;
import gov.loc.ead3.Maintenanceagency;
import gov.loc.ead3.Origination;
import gov.loc.ead3.Part;
import gov.loc.ead3.Persname;
import gov.loc.ead3.Recordid;
import gov.loc.ead3.Scopecontent;
import gov.loc.ead3.Script;
import gov.loc.ead3.Subtitle;
import gov.loc.ead3.Titleproper;
import gov.loc.ead3.Unitdate;
import gov.loc.ead3.Unitid;
import gov.loc.ead3.Unittitle;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.text.DecimalFormat;
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
import javax.xml.bind.Marshaller;
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
    private Set<CLevel> cLevelEntities = new HashSet<>();
    private LocalTypeMap localTypeMap = new LocalTypeMap();
    private SolrDocNode archdescNode = new SolrDocNode();
    private boolean openDataEnable = false;
    private boolean persistEac = false;
    private final JAXBContext cLevelContext = JAXBContext.newInstance(gov.loc.ead3.MCBase.class);
//    private final JAXBContext ead3Context;
    private final JAXBContext localTypeContext;
    private final Unmarshaller localTypeUnmarshaller;
    private boolean exists = false;

    public static final DecimalFormat NUMBERFORMAT = new DecimalFormat("00000000");
    private static final String DOC_TYPE = "ead3";

    public String getRecordId(Ead ead) {
        return ead.getControl().getRecordid().getContent();
    }

    public Ead3SolrDocBuilder() throws JAXBException {
//        this.ead3Context = JAXBContext.newInstance(Ead.class);
        this.localTypeContext = JAXBContext.newInstance(LocalTypes.class);
        this.localTypeUnmarshaller = localTypeContext.createUnmarshaller();
    }

    public SolrDocTree buildDocTree(Ead ead3, Ead3 ead3Entity, boolean persistEac) throws JAXBException, IOException {
        this.persistEac = persistEac;
        this.ead3Entity = ead3Entity;
        this.openDataEnable = ead3Entity.getArchivalInstitution().isOpenDataEnabled();
        this.ead3 = ead3;
        if (ead3Entity.getEadContent() != null) {
            exists = true;
        }
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

        //save updated ead3Entity
        try {
            JpaUtil.beginDatabaseTransaction();
            this.ead3Entity = DAOFactory.instance().getEad3DAO().getEad3ByIdentifier(this.ead3Entity.getAiId(), this.ead3Entity.getIdentifier());

            //Note: Some problem with hibernate, parent id don't get saved, so this following loop might seem redundant but it is need for now!
            if (!this.cLevelEntities.isEmpty()) {
                for (CLevel cls : this.cLevelEntities) {
                    if (cls.getParent() != null) {
                        cls.setParentId(cls.getParent().getId());
                    }
                }
                if (!exists) {
                    this.ead3Entity.setcLevels(this.cLevelEntities);
                }
            }
            this.ead3Entity = DAOFactory.instance().getEad3DAO().store(this.ead3Entity);
            JpaUtil.commitDatabaseTransaction();
        } catch (Exception ex) {
            LOGGER.error("DB exception!", ex);
        }
//            List<CLevel> levels = DAOFactory.instance().getCLevelDAO().getCLevelWithEad3Id(this.ead3Entity.getArchivalInstitution().getRepositorycode(), this.ead3Entity.getIdentifier(), null);
//            SolrDocNode processedDocNode = new SolrRootDocNodeInit(archdescNode, levels).getProcessedNode();
//            solrDocTree = new SolrDocTree(processedDocNode);

        return solrDocTree;

    }

    private void retrieveArchdescMain() throws IOException {
        if (this.ead3 == null || this.jXPathContext == null) {
            throw new IllegalStateException("Not initialized properly");
        }

        this.archdescNode.setDataElement(Ead3SolrFields.AI_ID, ead3Entity.getAiId());
        this.archdescNode.setDataElement(Ead3SolrFields.AI_NAME, ead3Entity.getArchivalInstitution().getAiname());
        this.archdescNode.setDataElement(Ead3SolrFields.AI, ead3Entity.getArchivalInstitution().getAiname() + ":" + ead3Entity.getAiId());

        this.archdescNode.setDataElement(Ead3SolrFields.COUNTRY_ID, ead3Entity.getArchivalInstitution().getCountry().getId());
        this.archdescNode.setDataElement(Ead3SolrFields.COUNTRY_NAME, ead3Entity.getArchivalInstitution().getCountry().getCname());
        this.archdescNode.setDataElement(Ead3SolrFields.COUNTRY, ead3Entity.getArchivalInstitution().getCountry().getCname() + ":" + SolrValues.TYPE_GROUP + ":" + ead3Entity.getArchivalInstitution().getCountry().getId());

        this.archdescNode.setDataElement(Ead3SolrFields.REPOSITORY_CODE, ead3Entity.getArchivalInstitution().getRepositorycode());
        this.archdescNode.setDataElement(Ead3SolrFields.ID, SolrValues.E3_FA_PREFIX + ead3.getId());
        this.archdescNode.setDataElement(Ead3SolrFields.ROOT_DOC_ID, SolrValues.E3_FA_PREFIX + ead3.getId());
        this.archdescNode.setDataElement(Ead3SolrFields.NUMBER_OF_ANCESTORS, 0);
        this.archdescNode.setDataElement(Ead3SolrFields.NUMBER_OF_DESCENDENTS, 0);
        this.archdescNode.setDataElement(Ead3SolrFields.TITLE_PROPER, this.retrieveTitleProper() + ":" + this.archdescNode.getDataElement(Ead3SolrFields.ROOT_DOC_ID));
        this.archdescNode.setDataElement(Ead3SolrFields.LANGUAGE, this.retrieveControlLanguage());
        this.archdescNode.setDataElement(Ead3SolrFields.RECORD_ID, this.retrieveRecordId());
        this.archdescNode.setDataElement(Ead3SolrFields.RECORD_TYPE, Ead3SolrDocBuilder.DOC_TYPE); //ToDo: auto find the type?
        this.archdescNode.setDataElement(Ead3SolrFields.OPEN_DATA, this.openDataEnable);

        this.archdescNode.setDataElement(SolrFields.FA_DYNAMIC_NAME, this.retrieveTitleProper() + ":" + SolrValues.TYPE_GROUP + ":" + this.archdescNode.getDataElement(Ead3SolrFields.ROOT_DOC_ID));
        this.archdescNode.setDataElement(SolrFields.FA_DYNAMIC_ID + "0" + SolrFields.DYNAMIC_STRING_SUFFIX, this.archdescNode.getDataElement(Ead3SolrFields.ROOT_DOC_ID));
        this.archdescNode.setDataElement(SolrFields.AI_DYNAMIC_ID + "0" + SolrFields.DYNAMIC_STRING_SUFFIX, SolrValues.AI_PREFIX + ead3Entity.getArchivalInstitution().getAiId());
        this.archdescNode.setDataElement(SolrFields.AI_DYNAMIC + "0" + SolrFields.DYNAMIC_STRING_SUFFIX, ead3Entity.getArchivalInstitution().getAiname() + ":" + SolrValues.TYPE_LEAF + ":" + SolrValues.AI_PREFIX + ead3Entity.getArchivalInstitution().getAiId());

        this.archdescNode.setTransientDataElement(Ead3SolrFields.LANGUAGE, this.retrieveControlLanguage());
        this.archdescNode.setTransientDataElement("script", this.retrieveControlScript());
        retrieveAgency();

        this.processArchdesc();

    }

    private void processArchdesc() throws IOException {
        if (this.ead3 == null || this.jXPathContext == null) {
            throw new IllegalStateException("Not initialized properly");
        }
        Did aDid = (Did) this.jXPathContext.getValue("archdesc/did", Did.class);
        if (aDid == null) {
            return; //ToDo: invalid c exception?
        }

        Map<String, Object> didMap = this.processDid(aDid, archdescNode);
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

        //Generate eadcontent xml
        Archdesc archdesc = this.ead3.getArchdesc();
        List<Serializable> dscBackup = new ArrayList<>();

        Iterator it = this.jXPathContext.iterate("archdesc/*");

        while (it.hasNext()) {
            Serializable element = (Serializable) it.next();
            if (element instanceof Dsc) {
                dscBackup.add(element);
                this.processDsc((Dsc) element, this.archdescNode);
                archdesc.getAccessrestrictOrAccrualsOrAcqinfo().remove(element);
            }
        }
        //Arcdesc don't have the dscs now. lets generate xml
        try {
            JAXBContext ead3Context = JAXBContext.newInstance(gov.loc.ead3.Ead.class);
            Marshaller marshaller = ead3Context.createMarshaller();
            ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
            marshaller.marshal(this.ead3, baos);
            String arcdescXml = baos.toString("UTF-8");
            EadContent eadContent = ead3Entity.getEadContent();
            if (eadContent == null) {
                eadContent = new EadContent();
                eadContent.setEad3(ead3Entity);
            }
            eadContent.setEadid(ead3.getId());
            eadContent.setUnittitle((String) this.archdescNode.getDataElement(Ead3SolrFields.UNIT_TITLE));
            String titleProper = (String) this.archdescNode.getDataElement(Ead3SolrFields.TITLE_PROPER);
            try {
                titleProper = titleProper.substring(0, titleProper.lastIndexOf(":"));
            } catch (Exception e) {
                LOGGER.debug("No : added in title proper" + e);
            }
            eadContent.setTitleproper(titleProper);
            eadContent.setXml(arcdescXml);
            this.ead3Entity.setEadContent(eadContent);
        } catch (JAXBException | UnsupportedEncodingException ex) {
            LOGGER.debug("Archdesc to xml fail", ex);
        }

        //add the removed dsc back.
        archdesc.getAccessrestrictOrAccrualsOrAcqinfo().addAll(dscBackup);
    }

    private void processDsc(Dsc dsc, SolrDocNode parent) throws IOException {
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
        int orderId = 0;
        while (it.hasNext()) {
            Object element = it.next();
            if (element instanceof MCBase) {
                //ToDo update based on child
                SolrDocNode child = processC((MCBase) element, this.archdescNode, null, orderId++);
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

    private SolrDocNode processC(MCBase cElement, SolrDocNode parent, CLevel parentC, int orderId) throws IOException {
        if (cElement == null) {
            return null;
        }
        SolrDocNode cRoot = new SolrDocNode();
        JXPathContext context = JXPathContext.newContext(cElement);
        Did cDid = (Did) context.getValue("did", Did.class);
        if (cDid == null) {
            return null; //ToDo: invalid c exception?
        }

        JpaUtil.beginDatabaseTransaction();
        CLevel cLevelEntity = new CLevel();
        cLevelEntity.setOrderId(orderId);
        //cLevelEntity.setEad3(ead3Entity);
        cLevelEntity.setParent(parentC);

        cLevelEntity.setEad3(ead3Entity);
        Map<String, Object> didMap = this.processDid(cDid, cRoot);
        cLevelEntity.setUnittitle((String) didMap.get(Ead3SolrFields.UNIT_TITLE));
        cLevelEntity.setUnitid((String) parent.getDataElement(Ead3SolrFields.UNIT_ID) + "-" + didMap.get(Ead3SolrFields.UNIT_ID));

        //ToDo gen currentNodeId
        cRoot.setDataElement(Ead3SolrFields.AI_ID, this.archdescNode.getDataElement(Ead3SolrFields.AI_ID));
        cRoot.setDataElement(Ead3SolrFields.AI_NAME, this.archdescNode.getDataElement(Ead3SolrFields.AI_NAME));
        cRoot.setDataElement(Ead3SolrFields.AI, this.archdescNode.getDataElement(Ead3SolrFields.AI));

        cRoot.setDataElement(Ead3SolrFields.COUNTRY_ID, this.archdescNode.getDataElement(Ead3SolrFields.COUNTRY_ID));
        cRoot.setDataElement(Ead3SolrFields.COUNTRY_NAME, this.archdescNode.getDataElement(Ead3SolrFields.COUNTRY_NAME));
        cRoot.setDataElement(Ead3SolrFields.COUNTRY, this.archdescNode.getDataElement(Ead3SolrFields.COUNTRY));

        cRoot.setDataElement(Ead3SolrFields.REPOSITORY_CODE, this.archdescNode.getDataElement(Ead3SolrFields.REPOSITORY_CODE));

        //global fields
        cRoot.setDataElement(Ead3SolrFields.LANGUAGE, this.archdescNode.getDataElement(Ead3SolrFields.LANGUAGE));
        cRoot.setDataElement(Ead3SolrFields.LANG_MATERIAL, this.archdescNode.getDataElement(Ead3SolrFields.LANG_MATERIAL));
        cRoot.setDataElement(Ead3SolrFields.RECORD_ID, this.archdescNode.getDataElement(Ead3SolrFields.RECORD_ID));
        cRoot.setDataElement(Ead3SolrFields.RECORD_TYPE, this.archdescNode.getDataElement(Ead3SolrFields.RECORD_TYPE));
        cRoot.setDataElement(Ead3SolrFields.TITLE_PROPER, this.archdescNode.getDataElement(Ead3SolrFields.TITLE_PROPER)); //ToDo: Recheck prefix

        cRoot.setDataElement(Ead3SolrFields.PARENT_ID, parent.getDataElement(Ead3SolrFields.ID));
        cRoot.setDataElement(Ead3SolrFields.ROOT_DOC_ID, this.archdescNode.getDataElement(Ead3SolrFields.ROOT_DOC_ID));
        cRoot.setDataElement(Ead3SolrFields.UNIT_TITLE, didMap.get(Ead3SolrFields.UNIT_TITLE));
        cRoot.setDataElement(Ead3SolrFields.UNIT_ID, parent.getDataElement(Ead3SolrFields.UNIT_ID) + "-" + didMap.get(Ead3SolrFields.UNIT_ID));
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
        cRoot.setDataElement(SolrFields.FA_DYNAMIC_NAME, this.archdescNode.getDataElement(SolrFields.FA_DYNAMIC_NAME));
        cRoot.setDataElement(SolrFields.FA_DYNAMIC_ID + "0" + SolrFields.DYNAMIC_STRING_SUFFIX, this.archdescNode.getDataElement(Ead3SolrFields.ROOT_DOC_ID));

        //need to change the implementation to retrieve the parent dynamic fs
        int numberOfAncestors = (int) cRoot.getDataElement(Ead3SolrFields.NUMBER_OF_ANCESTORS);

        for (int i = 1; i < numberOfAncestors - 1; i++) {
            cRoot.setDataElement(SolrFields.FA_DYNAMIC + i + SolrFields.DYNAMIC_STRING_SUFFIX, parent.getDataElement(SolrFields.FA_DYNAMIC + i + SolrFields.DYNAMIC_STRING_SUFFIX));
            cRoot.setDataElement(SolrFields.FA_DYNAMIC_ID + i + SolrFields.DYNAMIC_STRING_SUFFIX, parent.getDataElement(SolrFields.FA_DYNAMIC_ID + i + SolrFields.DYNAMIC_STRING_SUFFIX));
        }

        if (numberOfAncestors > 1) {
            cRoot.setDataElement(SolrFields.FA_DYNAMIC + (numberOfAncestors - 1) + SolrFields.DYNAMIC_STRING_SUFFIX, parent.getTransientDataElement(SolrFields.FA_DYNAMIC + (numberOfAncestors - 1) + SolrFields.DYNAMIC_STRING_SUFFIX));
            cRoot.setDataElement(SolrFields.FA_DYNAMIC_ID + (numberOfAncestors - 1) + SolrFields.DYNAMIC_STRING_SUFFIX, parent.getTransientDataElement(SolrFields.FA_DYNAMIC_ID + (numberOfAncestors - 1) + SolrFields.DYNAMIC_STRING_SUFFIX));
        }
        cRoot.setDataElement(SolrFields.AI_DYNAMIC_ID + "0" + SolrFields.DYNAMIC_STRING_SUFFIX, SolrValues.AI_PREFIX + ead3Entity.getArchivalInstitution().getAiId());
        cRoot.setDataElement(SolrFields.AI_DYNAMIC + "0" + SolrFields.DYNAMIC_STRING_SUFFIX, ead3Entity.getArchivalInstitution().getAiname() + ":" + SolrValues.TYPE_LEAF + ":" + SolrValues.AI_PREFIX + ead3Entity.getArchivalInstitution().getAiId());

        int currentNumberofDao = Integer.parseInt(didMap.get(Ead3SolrFields.NUMBER_OF_DAO).toString());
        int currentNumberOfDescendents = 0;

        if (!exists) {
            JpaUtil.getEntityManager().persist(cLevelEntity);
            JpaUtil.commitDatabaseTransaction();
        } else {
            cLevelEntity = DAOFactory.instance().getCLevelDAO().getCLevelByUnitId(cRoot.getDataElement(Ead3SolrFields.UNIT_ID).toString(),
                    cRoot.getDataElement(Ead3SolrFields.REPOSITORY_CODE).toString(),
                    cRoot.getDataElement(Ead3SolrFields.ROOT_DOC_ID).toString().substring(1));
            cRoot.setDataElement(Ead3SolrFields.ID, SolrValues.C_LEVEL_PREFIX + cLevelEntity.getId());
        }

        cRoot.setDataElement(Ead3SolrFields.ID, SolrValues.C_LEVEL_PREFIX + cLevelEntity.getId()); //ToDo: DB ID

        cRoot.setTransientDataElement(SolrFields.FA_DYNAMIC + numberOfAncestors + SolrFields.DYNAMIC_STRING_SUFFIX, NUMBERFORMAT.format(orderId) + ":" + didMap.get(Ead3SolrFields.UNIT_TITLE) + ":" + SolrValues.TYPE_GROUP + ":" + cRoot.getDataElement(Ead3SolrFields.ID));
        cRoot.setTransientDataElement(SolrFields.FA_DYNAMIC_ID + numberOfAncestors + SolrFields.DYNAMIC_STRING_SUFFIX, cRoot.getDataElement(Ead3SolrFields.ID));

        Iterator it = context.iterate("*");

        StringBuilder otherStrBuilder = new StringBuilder();
        int currentOrderId = 0;
        while (it.hasNext()) {
            Object element = it.next();
            if (element instanceof Scopecontent) {
                Scopecontent scopecontent = (Scopecontent) element;
//                cRoot.setEacData(this.buildEacData(cRoot, ((Chronlist) scopecontent.getChronlistOrListOrTable().get(0)).getChronitem().get(0).getEvent()));

                /* !Do not remove!
                for (Object o : scopecontent.getChronlistOrListOrTable()) {
                    if (o instanceof Chronlist) {
                        Chronlist cList = (Chronlist) o;
                        for (Chronitem co : cList.getChronitem()) {
                            Map<String, String> eventMap = buildEvent(co.getEvent());
                            this.buildAndStoreEacData(cRoot, co.getEvent());
                            
                            //in ead3 solrdocs, creating dynamic fields with all the i.e firstname from the document to make it searchable
                            for (Map.Entry entry : eventMap.entrySet()) {
                                cRoot.setDataElement(entry.getKey().toString() + "_s", entry.getValue().toString());
                            }
                        }
                    }
                }
                //*/
//                if (scopecontent.getChronlistOrListOrTable() instanceof List<?>) {
//                    Map<String, String> eventMap = buildEvent(((Chronlist) scopecontent.getChronlistOrListOrTable().get(0)).getChronitem().get(0).getEvent());
//                    this.buildAndStoreEacData(cRoot, ((Chronlist) scopecontent.getChronlistOrListOrTable().get(0)).getChronitem().get(0).getEvent());
//                    for (Map.Entry entry : eventMap.entrySet()) {
//                        cRoot.setDataElement(entry.getKey().toString() + "_s", entry.getValue().toString());
//                    }
//                }
                cRoot.setDataElement(Ead3SolrFields.SCOPE_CONTENT, retrieveScopeContentAsText(element));
            } else if (element instanceof Did) {
            } else if (element instanceof C) {
                //ToDo update based on child
                SolrDocNode child = processC((MCBase) element, cRoot, cLevelEntity, currentOrderId++);
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

        if (cRoot.getChild() == null) {
            cLevelEntity.setLeaf(true);
        }
        Marshaller marshaller = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(100);
        ((C) cElement).getTheadAndC().clear(); //remove all child Cs only to serialized current level

        //*
        JXPathContext context1 = JXPathContext.newContext(cElement);
        it = context1.iterate("//*");
        int count = 0;
        while (it.hasNext()) {
            Object element = it.next();

            if (element instanceof Persname) {
                Persname persname = (Persname) element;
                //System.out.println("Found Persname: "+ persname.getPart().size());
                Map<String, Object> personMap = this.generateEacCpfDataMap(cRoot, persname.getPart(), count);
                if (personMap == null) {
                    //System.out.println("blank person! "+this.getContentText(persname, " "));
                    continue;
                }
                count++;
                personMap.put(Ead3ToEacFieldMapKeys.CPF_TYPE, Ead3ToEacFieldMapStaticValues.CPF_TYPE_PERSON);
                personMap.put(Ead3ToEacFieldMapStaticValues.SOURCE, cRoot.getTransientDataElement(Ead3ToEacFieldMapStaticValues.SOURCE));

                if (this.persistEac) {
                    //System.out.println("Storing persname");
                    try {
                        new StoreEacFromEad3(personMap, ead3Entity.getArchivalInstitution().getCountry().getIsoname(), ead3Entity.getAiId(), ead3Entity.getIdentifier()).storeEacCpf();
                    } catch (Exception ex) {
                        java.util.logging.Logger.getLogger(Ead3SolrDocBuilder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        //*/
        //System.out.println("total persname: "+count);

        try {
            marshaller = this.cLevelContext.createMarshaller();
//            QName qName = new QName("c");
//            JAXBElement<MCBase> rootedC = new JAXBElement<>(qName, MCBase.class, cElement);

            marshaller.marshal(cElement, baos);
            String cLevelXml = baos.toString("UTF-8");
            cLevelEntity.setXml(cLevelXml);
            cLevelEntity.setcBinary(getBytesFromObjectStr((C) cElement));
//            System.out.println("Clevel xml: " + cLevelXml);

        } catch (JAXBException | UnsupportedEncodingException ex) {
            java.util.logging.Logger.getLogger(Ead3SolrDocBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }

        cRoot.setDataElement(Ead3SolrFields.OPEN_DATA, this.openDataEnable);
        cRoot.setDataElement(Ead3SolrFields.LEVEL_NAME, "clevel");
        cRoot.setDataElement(Ead3SolrFields.NUMBER_OF_DAO, currentNumberofDao);
        cRoot.setDataElement(Ead3SolrFields.NUMBER_OF_DESCENDENTS, currentNumberOfDescendents);

        if (otherStrBuilder.length() > 0) {
            cRoot.setDataElement(Ead3SolrFields.OTHER, otherStrBuilder.toString());
        }
        if (!exists) {
            JpaUtil.getEntityManager().persist(cLevelEntity);
            this.cLevelEntities.add(cLevelEntity);
        }
        //ead3Entity.addcLevel(cLevelEntity);

        return cRoot;
    }

    private static byte[] getBytesFromObjectStr(C cElement) throws JAXBException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput oos = new ObjectOutputStream(baos);
        oos.writeObject(cElement);
        oos.flush();
        byte[] clevelObjBytes = baos.toByteArray();
        oos.close();
        return clevelObjBytes;

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
            String str1 = "";
            while (it.hasNext()) {
                Object object = it.next();
                if (object instanceof MMixedBasic) {
                    MMixedBasic contentHolder = (MMixedBasic) object;

                    for (Serializable sr : contentHolder.getContent()) {
                        try {
                            str1 = (String) sr;
                        } catch (ClassCastException cx) {
                            Ead3SolrDocBuilder.LOGGER.debug("Failed to convert to string: " + sr.toString());
                        }
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
        try {
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
        } catch (Exception ex) {
            LOGGER.error("Failed to get content text: ", ex);
        }
        return stringBuilder.toString().trim();
    }

    private String getContent(MMixedBasic obj) {
        return this.getContentText(obj.getContent());
    }

    private String getContent(MMixedBasicPlusAccess obj) {
        return this.getContentText(obj.getContent());
    }

    private String getContent(MMixedBasicDate obj) {
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

    private String retrieveControlScript() {
        if (this.ead3 == null || this.jXPathContext == null) {
            throw new IllegalStateException("Not initialized properly");
        }
        try {
            Script script = (Script) jXPathContext.getValue("control/languagedeclaration/scriptType");
            return script.getScriptcode();
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

    private Map<String, Object> processDid(Did did, SolrDocNode cRoot) {
        Map<String, Object> didInfoMap = new HashMap<>();

        int daoCount = 0;
        List<String> daoFieldValues = new ArrayList<>();
        Set<String> daoTypeValues = new HashSet<>();

        if (did != null) {
            didInfoMap.put(Ead3SolrFields.DAO, false);
            for (Object obj : did.getMDid()) {
                if (obj instanceof Unittitle) {
                    String unitTitle = this.getContent((MMixedBasicPlusAccess) obj);
                    String composedUnitTitle = " ";

                    Unittitle unittitle = (Unittitle) obj;
                    for (Serializable innerObjs : unittitle.getContent()) {
                        if (innerObjs instanceof JAXBElement) {
                            JAXBElement genreform = (JAXBElement) innerObjs;
                            if (genreform.getDeclaredType().equals(Genreform.class)) {
                                String partValue = processSource((Genreform) genreform.getValue());
                                composedUnitTitle = generateUnitTitleFromGenreform((Genreform) genreform.getValue());
                                cRoot.setTransientDataElement(Ead3ToEacFieldMapStaticValues.SOURCE, partValue);

                            }

                        }
                    }
                    if (StringUtils.isEmpty(unitTitle)) {
                        unitTitle = composedUnitTitle;
                    }
                    didInfoMap.put(Ead3SolrFields.UNIT_TITLE, unitTitle);
                    didInfoMap.put(Ead3SolrFields.UNIT_TITLE_LOCALTYPE, ((Unittitle) obj).getLocaltype());
                } else if (obj instanceof Dao) {
                    daoCount++;
                    Dao dao = (Dao) obj;
                    if (StringUtils.isNotBlank(dao.getHref())) {
                        daoFieldValues.add(dao.getHref());
                    }
                    if (StringUtils.isNotBlank(dao.getDaotype())) {
                        daoTypeValues.add(dao.getDaotype());
                    }

                } else if (obj instanceof Daoset) {
                    List<JAXBElement<? extends Serializable>> daos = ((Daoset) obj).getContent();
                    daoCount++;
                    for (JAXBElement jAXBElement : daos) {
                        if (jAXBElement.getDeclaredType().equals(Dao.class)) {
                            Dao dao = (Dao) jAXBElement.getValue();
                            if (StringUtils.isNotBlank(dao.getHref())) {
                                daoFieldValues.add(dao.getHref());
                            }
                            if (StringUtils.isNotBlank(dao.getDaotype())) {
                                daoTypeValues.add(dao.getDaotype());
                            }
                        }
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
                    if (!StringUtils.isBlank(dates.get(0))) {
                        didInfoMap.put(Ead3SolrFields.START_DATE, dates.get(0));
                    }
                    if (!StringUtils.isBlank(dates.get(1))) {
                        didInfoMap.put(Ead3SolrFields.END_DATE, dates.get(1));
                    }
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

        didInfoMap.put(Ead3SolrFields.DAO_LINKS, daoFieldValues);
        didInfoMap.put(Ead3SolrFields.DAO_TYPE, daoTypeValues);
        didInfoMap.put(Ead3SolrFields.NUMBER_OF_DAO, daoCount + "");
        if (daoCount > 0) {
            didInfoMap.put(Ead3SolrFields.DAO, true);
        } else {
            didInfoMap.put(Ead3SolrFields.DAO, false);
        }
        return didInfoMap;
    }

    private String processSource(Genreform genreform) {
        String source = "";
        for (Part part : genreform.getPart()) {
            if (part.getLocaltype().equals(Ead3ToEacFieldMapStaticValues.SOURCE)) {
                source = getContent(part);
            }
        }
        return source;
    }

    private String generateUnitTitleFromGenreform(Genreform genreform) {
        StringBuilder title = new StringBuilder();
        for (Part part : genreform.getPart()) {
            title.append(getContent(part)).append(" ");
        }
        return title.toString();
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
                    Map<String, Object> personMap = this.generateEacCpfDataMap(cRoot, ((Persname) eacElement.getValue()).getPart(), count);
                    if (personMap == null) {
                        continue;
                    }
                    count++;
                    personMap.put(Ead3ToEacFieldMapKeys.CPF_TYPE, Ead3ToEacFieldMapStaticValues.CPF_TYPE_PERSON);
                    personMap.put(Ead3ToEacFieldMapStaticValues.SOURCE, cRoot.getTransientDataElement(Ead3ToEacFieldMapStaticValues.SOURCE));

                    if (this.persistEac) {
                        try {
                            new StoreEacFromEad3(personMap, ead3Entity.getArchivalInstitution().getCountry().getIsoname(), ead3Entity.getAiId(), ead3Entity.getIdentifier()).storeEacCpf();
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(Ead3SolrDocBuilder.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    listOfEacs.add(personMap);
                } else if (eacElement.getDeclaredType().equals(Corpname.class)) {
                    Map<String, Object> corpMap = generateEacCpfDataMap(cRoot, ((Corpname) eacElement.getValue()).getPart(), count);
                    if (corpMap == null) {
                        continue;
                    }
                    count++;
                    corpMap.put(Ead3ToEacFieldMapKeys.CPF_TYPE, Ead3ToEacFieldMapStaticValues.CPF_TYPE_CORPORATE_BODY);
                    listOfEacs.add(corpMap);
                } else if (eacElement.getDeclaredType().equals(Famname.class)) {
                    Map<String, Object> famMap = generateEacCpfDataMap(cRoot, ((Famname) eacElement.getValue()).getPart(), count);
                    if (famMap == null) {
                        continue;
                    }
                    count++;
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
        eacMap.put(Ead3SolrFields.REPOSITORY_CODE, returnAsArray(cRoot.getDataElement(Ead3SolrFields.REPOSITORY_CODE).toString()));
        eacMap.put(Ead3SolrFields.RECORD_ID, returnAsArray(cRoot.getDataElement(Ead3SolrFields.RECORD_ID).toString()));

        int partNameCount = 1;
        int partGenealogyDescriptionCount = 0;
        int partDateCount = 1;
        int partPlaceCount = 1;
        String partLang = "";
        String firstName = "";
        String lastName = "";
        String birthDate = "";
        String deathDate = "";
        String tmpStr;
        boolean valid = false; //ToDo: quick fix
        for (Part part : parts) {
            //ToDo:
            try {
                ApeType apeType = null;
                if (StringUtils.isNotBlank(part.getLocaltype())) {
                    apeType = ApeType.get(this.localTypeMap.getApeType(part.getLocaltype().toLowerCase()));
                }
                if (apeType == null) {
                    LOGGER.debug("Unsupported ApeType: " + part.getLocaltype());
                    continue;
                }
//                partLang="";
//                if (StringUtils.isNotBlank(part.getLang())) {
//                    partLang = part.getLang();
//                }
                switch (apeType) {
                    case FIRSTNAME:
                        firstName = getContent(part);
                        eacMap.put(Ead3ToEacFieldMapKeys.IDENTITY_PERSON_NAME_ + "1_part_" + partNameCount, returnAsArray(firstName));
                        eacMap.put(Ead3ToEacFieldMapKeys.IDENTITY_PERSON_NAME_ + "1_comp_" + partNameCount, returnAsArray(Ead3ToEacFieldMapStaticValues.PART_LOCAL_TYPE_FRIST_NAME));
                        valid = true;
                        partNameCount++;
                        break;
                    case LASTNAME:
                        lastName = getContent(part);
                        eacMap.put(Ead3ToEacFieldMapKeys.IDENTITY_PERSON_NAME_ + "1_part_" + partNameCount, returnAsArray(lastName));
                        eacMap.put(Ead3ToEacFieldMapKeys.IDENTITY_PERSON_NAME_ + "1_comp_" + partNameCount, returnAsArray(Ead3ToEacFieldMapStaticValues.PART_LOCAL_TYPE_SUR_NAME));
                        valid = true;
                        partNameCount++;
                        break;
                    case PERSNAME:
                        String persName = getContent(part);
                        eacMap.put(Ead3ToEacFieldMapKeys.IDENTITY_PERSON_NAME_ + "1_part_" + partNameCount, returnAsArray(persName));
                        eacMap.put(Ead3ToEacFieldMapKeys.IDENTITY_PERSON_NAME_ + "1_comp_" + partNameCount, returnAsArray(Ead3ToEacFieldMapStaticValues.PART_LOCAL_TYPE_PERS_NAME));
                        valid = true;
                        partNameCount++;
                        break;
                    case BIRTHNAME:
                        String birthName = getContent(part);
                        eacMap.put(Ead3ToEacFieldMapKeys.IDENTITY_PERSON_NAME_ + "1_part_" + partNameCount, returnAsArray(birthName));
                        eacMap.put(Ead3ToEacFieldMapKeys.IDENTITY_PERSON_NAME_ + "1_comp_" + partNameCount, returnAsArray(Ead3ToEacFieldMapStaticValues.PART_LOCAL_TYPE_BIRTH_NAME));
                        partNameCount++;
                        break;
                    case GENDER:
                        eacMap.put(ApeType.GENDER.getValue(), getContent(part));
                        break;
                    case ROLE:
                        eacMap.put(ApeType.ROLE.getValue(), getContent(part));
                        break;
                    case BITHDATE:
                        birthDate = getContent(part);
                        partDateCount++;
                        break;
                    case DEATHDATE:
                        deathDate = getContent(part);
                        partDateCount++;
                        break;
                    case BIRTHPLACE:
                        tmpStr = getContent(part);
                        eacMap.put(Ead3ToEacFieldMapKeys.PLACE_ + partPlaceCount, returnAsArray(tmpStr));
                        eacMap.put(Ead3ToEacFieldMapKeys.PLACE_ROLE_ + partPlaceCount, returnAsArray("birth")); // ToDo: change the hard code
//                        eacMap.put(Ead3ToEacFieldMapKeys.PLACE_LANGUAGE_+partPlaceCount, returnAsArray(partLang));
                        partPlaceCount++;
                        break;
                    case DEATHPLACE:
                        tmpStr = getContent(part);
                        eacMap.put(Ead3ToEacFieldMapKeys.PLACE_ + partPlaceCount, returnAsArray(tmpStr));
                        eacMap.put(Ead3ToEacFieldMapKeys.PLACE_ROLE_ + partPlaceCount, returnAsArray("death")); // ToDo: change the hard code
//                        eacMap.put(Ead3ToEacFieldMapKeys.PLACE_LANGUAGE_+partPlaceCount, returnAsArray(partLang));
                        partPlaceCount++;
                        break;
                    default:
                        break;
                }
            } catch (ItemNotFoundException ex) {
                LOGGER.debug(ex.getMessage());
            }
        }

        eacMap.put(Ead3ToEacFieldMapStaticValues.PART_LOCAL_TYPE_BIRTH_DATE, birthDate);
        eacMap.put(Ead3ToEacFieldMapStaticValues.PART_LOCAL_TYPE_DEATH_DATE, deathDate);

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

        eacMap.put("identityPersonName_1_rows", returnAsArray("1"));

        eacMap.put(Ead3ToEacFieldMapKeys.DEFAULT_LANGUAGE, archdescNode.getTransientDataElement(Ead3SolrFields.LANGUAGE).toString());
        eacMap.put(Ead3ToEacFieldMapKeys.DEFAULT_SCRIPT, archdescNode.getTransientDataElement("script").toString());
        eacMap.put("controlLanguage", returnAsArray(archdescNode.getTransientDataElement(Ead3SolrFields.LANGUAGE).toString()));
        eacMap.put("controlScript", returnAsArray(archdescNode.getTransientDataElement("script").toString()));

        //identify automatic eac generation from ead3
        eacMap.put("agent", "Ead3");
        if (!valid) {
            return null;
        }
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
