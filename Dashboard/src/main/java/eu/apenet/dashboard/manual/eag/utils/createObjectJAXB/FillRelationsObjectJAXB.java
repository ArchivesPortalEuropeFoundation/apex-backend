package eu.apenet.dashboard.manual.eag.utils.createObjectJAXB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dpt.utils.eag2012.DescriptiveNote;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.EagRelation;
import eu.apenet.dpt.utils.eag2012.P;
import eu.apenet.dpt.utils.eag2012.RelationEntry;
import eu.apenet.dpt.utils.eag2012.Relations;
import eu.apenet.dpt.utils.eag2012.Repository;
import eu.apenet.dpt.utils.eag2012.ResourceRelation;

/**
 * Class for fill "Relations" element of object JAXB
 */
public class FillRelationsObjectJAXB extends AbstractObjectJAXB implements ObjectJAXB {

    /**
     * EAG2012 {@link Eag2012} internal object.
     */
    protected Eag2012 eag2012;
    /**
     * Eag {@link Eag} JAXB object.
     */
    private final Logger log = Logger.getLogger(getClass());
    protected Eag eag;
    private ResourceRelation resourceRelation;
    private Repository mainRepository;
    private EagRelation eagRelation;

    @Override
    public Eag ObjectJAXB(Eag2012 eag2012, Eag eag) {
        this.eag2012 = eag2012;
        this.eag = eag;
        main();
        return this.eag;
    }

    /**
     * Method main with method for fill "relations" element of object JAXB
     */
    private void main() {
        this.log.debug("Method start: \"Main of class FillRelationsObjectJAXB\"");
        // eag/relations/resourceRelation/relationEntry
        if (this.eag2012.getRelationEntryValue() != null) {
            for (int i = 0; i < this.eag2012.getRelationEntryValue().size(); i++) {
                Map<String, List<String>> sectionValueMap = this.eag2012.getRelationEntryValue().get(i);
                Map<String, List<String>> sectionLangMap = this.eag2012.getRelationEntryLang().get(i);
                Iterator<String> sectionValueIt = sectionValueMap.keySet().iterator();
                Iterator<String> sectionLangIt = sectionLangMap.keySet().iterator();
                while (sectionValueIt.hasNext()) {
                    String sectionValueKey = sectionValueIt.next();
                    String sectionLangKey = (sectionLangIt.hasNext()) ? sectionLangIt.next() : null;
                    List<String> valueList = sectionValueMap.get(sectionValueKey);
                    List<String> langList = (sectionLangKey != null) ? sectionLangMap.get(sectionLangKey) : null;
                    if (Eag2012.RESOURCE_RELATION.equalsIgnoreCase(sectionValueKey)
                            || Eag2012.TAB_YOUR_INSTITUTION.equalsIgnoreCase(sectionValueKey)) {
                        createResourceRelation(valueList, langList, sectionValueKey);

                    }
                    getFirstPossition();

                    parseRelationEntry(valueList, langList, sectionValueKey, sectionLangKey);

                }
            }
        }
        this.log.debug("End method: \"Main of class FillRelationsObjectJAXB\"");
    }

    /**
     * Method to fill resourceRelationType element
     *
     * @param valueList {@link List<String>} the valueList
     * @param langList {@link List<String>>} the langList
     * @param sectionValueKey {@link String} the sectionValueKey
     */
    private void createResourceRelation(List<String> valueList, List<String> langList, String sectionValueKey) {
        this.log.debug("Method start: \"createResourceRelation\"");
        for (int j = 0; j < valueList.size(); j++) {
            resourceRelation = new ResourceRelation();
            getResourceRelationType(sectionValueKey, j);
            RelationEntry relationEntry = new RelationEntry();
            relationEntry.setContent(valueList.get(j));
            relationEntry.setLang((langList != null && langList.size() > j && !langList.get(j).equalsIgnoreCase(Eag2012.OPTION_NONE)) ? langList.get(j) : null);
            // eag/relations/resourceRelation/descriptiveNote/P
            if (this.eag2012.getDescriptiveNotePValue() != null
                    && this.eag2012.getDescriptiveNotePLang() != null) {
                for (int k = 0; k < this.eag2012.getDescriptiveNotePValue().size(); k++) {
                    Map<String, Map<String, List<String>>> tabsPValueMap = this.eag2012.getDescriptiveNotePValue().get(k);
                    Map<String, Map<String, List<String>>> tabsPLangMap = this.eag2012.getDescriptiveNotePLang().get(k);
                    Iterator<String> tabsPValueIt = tabsPValueMap.keySet().iterator();
                    Iterator<String> tabsPLangIt = tabsPLangMap.keySet().iterator();
                    while (tabsPValueIt.hasNext()) {
                        String tabPValueKey = tabsPValueIt.next();
                        String tabPLangKey = tabsPLangIt.next();
                        if (Eag2012.TAB_RELATION.equalsIgnoreCase(tabPValueKey)
                                && Eag2012.TAB_RELATION.equalsIgnoreCase(tabPLangKey)) {
                            Map<String, List<String>> sectionsPValueMap = tabsPValueMap.get(tabPValueKey);
                            Map<String, List<String>> sectionsPLangMap = tabsPLangMap.get(tabPLangKey);
                            Iterator<String> sectionsPValueIt = sectionsPValueMap.keySet().iterator();
                            Iterator<String> sectionsPLangIt = sectionsPLangMap.keySet().iterator();
                            while (sectionsPValueIt.hasNext()) {
                                String sectionPValuesKey = sectionsPValueIt.next();
                                String sectionPLangKey = sectionsPLangIt.next();
                                List<String> valuesPList = sectionsPValueMap.get(sectionPValuesKey);
                                List<String> langPList = sectionsPLangMap.get(sectionPLangKey);
                                for (int l = 0; l < valuesPList.size(); l++) {
                                    if (l == j) {
                                        if (valuesPList.get(l) != null && !valuesPList.get(l).isEmpty()) {
                                            P p = new P();
                                            p.setContent(valuesPList.get(l));
                                            if (!Eag2012.OPTION_NONE.equalsIgnoreCase(langPList.get(l))) {
                                                p.setLang(langPList.get(l));
                                            }
                                            if (Eag2012.RESOURCE_RELATION.equalsIgnoreCase(sectionPValuesKey)
                                                    && Eag2012.RESOURCE_RELATION.equalsIgnoreCase(sectionPLangKey)) {
                                                DescriptiveNote descriptiveNote = null;
                                                if (resourceRelation.getDescriptiveNote() == null) {
                                                    descriptiveNote = new DescriptiveNote();
                                                } else {
                                                    descriptiveNote = resourceRelation.getDescriptiveNote();
                                                }
                                                descriptiveNote.getP().add(p);
                                                resourceRelation.setDescriptiveNote(descriptiveNote);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (relationEntry.getContent() != null && !relationEntry.getContent().isEmpty()) {
                resourceRelation.setRelationEntry(relationEntry);
            }
            boolean found = false;
            //if (Eag2012.RESOURCE_RELATION.equalsIgnoreCase(sectionValueKey)){ //TODO: current situation from ticket 543 
            if (this.eag.getRelations() != null && this.eag.getRelations().getResourceRelation() != null) {
                for (int x = 0; x < this.eag.getRelations().getResourceRelation().size(); x++) {
                    if (this.eag.getRelations().getResourceRelation().get(x) != null
                            && (this.eag.getRelations().getResourceRelation().get(x).getHref() != null
                            && this.eag.getRelations().getResourceRelation().get(x).getHref().equalsIgnoreCase(resourceRelation.getHref())
                            || (this.eag.getRelations().getResourceRelation().get(x).getHref() == null
                            && resourceRelation.getHref() == null))
                            && this.eag.getRelations().getResourceRelation().get(x).getRelationEntry() != null
                            && ((this.eag.getRelations().getResourceRelation().get(x).getRelationEntry().getLang() != null
                            && this.eag.getRelations().getResourceRelation().get(x).getRelationEntry().getLang().equalsIgnoreCase(relationEntry.getLang()))
                            || (this.eag.getRelations().getResourceRelation().get(x).getRelationEntry().getLang() == null
                            && (relationEntry.getLang() == null
                            || relationEntry.getLang().equalsIgnoreCase(Eag2012.OPTION_NONE)))
                            || (this.eag.getRelations().getResourceRelation().get(x).getRelationEntry().getLang() == null
                            && (relationEntry.getContent() == null
                            || relationEntry.getContent().isEmpty())))) {
                        found = true;
                        if (resourceRelation.getDescriptiveNote() != null) { //priority for descriptive note
                            //TODO: current situation from ticket 543 
                            this.eag.getRelations().getResourceRelation().set(x, resourceRelation);
                        }
                    }
                }
            }
            if (!found) {
                //TODO: current situation from ticket 543
                if (resourceRelation.getRelationEntry() != null || (resourceRelation.getHref() != null && !resourceRelation.getHref().isEmpty())) {
                    if (this.eag.getRelations() == null){
                        this.eag.setRelations(new Relations());
                    }
                    this.eag.getRelations().getResourceRelation().add(resourceRelation);
                }
            }
            //}
        }
        this.log.debug("End method: \"createResourceRelation\"");
    }

    /**
     * Method to get resourceRelationType element
     *
     * @param sectionValueKey {@link String} the sectionValueKey
     * @param j {@link int} the j
     */
    private void getResourceRelationType(String sectionValueKey, int j) {
        this.log.debug("Method start: \"getResourceRelationType\"");
        if (Eag2012.TAB_YOUR_INSTITUTION.equalsIgnoreCase(sectionValueKey)) {
            resourceRelation.setResourceRelationType(Eag2012.OPTION_CREATOR_TEXT); //default text creator, it's mandatory and into tab-your_institution there is not a combobox for it
        } else if (this.eag2012.getResourceRelationResourceRelationType() != null && !this.eag2012.getResourceRelationResourceRelationType().isEmpty()) {
            if (Eag2012.RESOURCE_RELATION.equalsIgnoreCase(sectionValueKey)) {
                if (Eag2012.OPTION_CREATOR.equalsIgnoreCase(this.eag2012.getResourceRelationResourceRelationType().get(j))) {
                    resourceRelation.setResourceRelationType(Eag2012.OPTION_CREATOR_TEXT);
                }
                if (Eag2012.OPTION_SUBJECT.equalsIgnoreCase(this.eag2012.getResourceRelationResourceRelationType().get(j))) {
                    resourceRelation.setResourceRelationType(Eag2012.OPTION_SUBJECT_TEXT);
                }
                if (Eag2012.OPTION_OTHER.equalsIgnoreCase(this.eag2012.getResourceRelationResourceRelationType().get(j))) {
                    resourceRelation.setResourceRelationType(Eag2012.OPTION_OTHER_TEXT);
                }
                // eag/relations/resourceRelation/href
                if (this.eag2012.getResourceRelationHref() != null && this.eag2012.getResourceRelationHref().get(Eag2012.TAB_RELATION) != null && !this.eag2012.getResourceRelationHref().get(Eag2012.TAB_RELATION).isEmpty()
                        && this.eag2012.getResourceRelationHref().get(Eag2012.TAB_RELATION).get(j) != null
                        && !this.eag2012.getResourceRelationHref().get(Eag2012.TAB_RELATION).get(j).isEmpty()) {
                    String href = this.eag2012.getResourceRelationHref().get(Eag2012.TAB_RELATION).get(j);
                    //	href = (href!=null && href.length()>0 && (href.toLowerCase().startsWith("http://") || href.toLowerCase().startsWith("ftp://") || href.toLowerCase().startsWith("https://")) )?href:((href!=null && !href.isEmpty())?((href!=null && !href.isEmpty())?("http://"+href):href):null);
                    resourceRelation.setHref(href);
                    if (resourceRelation.getRelationEntry() == null) {
                        resourceRelation.setRelationEntry(new RelationEntry());
                    }
                }
            }
        }
        if (Eag2012.TAB_YOUR_INSTITUTION.equalsIgnoreCase(sectionValueKey)) {
            if (this.eag2012.getResourceRelationHref() != null && this.eag2012.getResourceRelationHref().get(Eag2012.TAB_YOUR_INSTITUTION) != null && !this.eag2012.getResourceRelationHref().get(Eag2012.TAB_YOUR_INSTITUTION).isEmpty()
                    && this.eag2012.getResourceRelationHref().get(Eag2012.TAB_YOUR_INSTITUTION).get(j) != null
                    && !this.eag2012.getResourceRelationHref().get(Eag2012.TAB_YOUR_INSTITUTION).get(j).isEmpty()) {
                String href = this.eag2012.getResourceRelationHref().get(Eag2012.TAB_YOUR_INSTITUTION).get(j);
                //	href = (href!=null && href.length()>0 && (href.toLowerCase().startsWith("http://") || href.toLowerCase().startsWith("ftp://") || href.toLowerCase().startsWith("https://")) )?href:((href!=null && !href.isEmpty())?((href!=null && !href.isEmpty())?("http://"+href):href):null);
                resourceRelation.setHref(href);
                if (resourceRelation.getRelationEntry() == null) {
                    resourceRelation.setRelationEntry(new RelationEntry());
                }
            }
        } else if (Eag2012.RESOURCE_RELATION.equalsIgnoreCase(sectionValueKey)) {
            if (this.eag2012.getResourceRelationHref() != null && this.eag2012.getResourceRelationHref().get(Eag2012.RESOURCE_RELATION) != null && !this.eag2012.getResourceRelationHref().get(Eag2012.RESOURCE_RELATION).isEmpty()
                    && this.eag2012.getResourceRelationHref().get(Eag2012.RESOURCE_RELATION).get(j) != null
                    && !this.eag2012.getResourceRelationHref().get(Eag2012.RESOURCE_RELATION).get(j).isEmpty()) {
                String href = this.eag2012.getResourceRelationHref().get(Eag2012.RESOURCE_RELATION).get(j);
                //	href = (href!=null && href.length()>0 && (href.toLowerCase().startsWith("http://") || href.toLowerCase().startsWith("ftp://") || href.toLowerCase().startsWith("https://")) )?((href!=null && !href.isEmpty())?("http://"+href):null):null;
                resourceRelation.setHref(href);
                if (resourceRelation.getRelationEntry() == null) {
                    resourceRelation.setRelationEntry(new RelationEntry());
                }
            }
        }
        this.log.debug("End method: \"getResourceRelationType\"");
    }

    /**
     * Method to get FirstPossition: Pass to first possition all relations with
     * type "Creator of".
     */
    private void getFirstPossition() {
        this.log.debug("Method start: \"getFirstPossition\"");
        // Pass to first possition all relations with type "Creator of".
        if (this.eag.getRelations() != null
                && !this.eag.getRelations().getResourceRelation().isEmpty()) {
            List<ResourceRelation> resourceRelationsListCreator = new ArrayList<ResourceRelation>();
            List<ResourceRelation> resourceRelationsListRest = new ArrayList<ResourceRelation>();

            for (int j = 0; j < this.eag.getRelations().getResourceRelation().size(); j++) {
                ResourceRelation relation = this.eag.getRelations().getResourceRelation().get(j);

                if (relation.getResourceRelationType() != null
                        && !relation.getResourceRelationType().isEmpty()
                        && Eag2012.OPTION_CREATOR_TEXT.equalsIgnoreCase(relation.getResourceRelationType())) {
                    resourceRelationsListCreator.add(relation);
                } else {
                    resourceRelationsListRest.add(relation);
                }
            }
            // Clear the resource reelations old list.
            this.eag.getRelations().getResourceRelation().clear();
            // Add new values.
            this.eag.getRelations().getResourceRelation().addAll(resourceRelationsListCreator);
            this.eag.getRelations().getResourceRelation().addAll(resourceRelationsListRest);
        }
        this.log.debug("End method: \"getFirstPossition\"");
    }

    /**
     * Method to fill relationEntry element
     *
     * @param valueList {@link List<String>} the valueList
     * @param langList {@link List<String>} the langList
     * @param sectionValueKey {@link String} the sectionValueKey
     * @param sectionLangKey {@link String} the sectionLangKey
     */
    private void parseRelationEntry(List<String> valueList, List<String> langList, String sectionValueKey, String sectionLangKey) {
        this.log.debug("Method start: \"parseRelationEntry\"");
        // eag/relations/eagRelation/relationEntry
        if (Eag2012.INSTITUTION_RELATIONS.equalsIgnoreCase(sectionValueKey)) {
            for (int j = 0; j < valueList.size(); j++) {
                eagRelation = new EagRelation();
                getEagRelationType(j, valueList);
                RelationEntry relationEntry = new RelationEntry();
                relationEntry.setContent(valueList.get(j));
                relationEntry.setLang((langList != null && langList.size() > j && !langList.get(j).equalsIgnoreCase(Eag2012.OPTION_NONE)) ? langList.get(j) : null);
                getDescriptiveNote(sectionLangKey, j);
                if ((relationEntry.getContent() == null || relationEntry.getContent().isEmpty())
                        && (eagRelation.getHref() == null || eagRelation.getHref().isEmpty())) {
                    eagRelation.setEagRelationType(null);
                } else {
                    eagRelation.getRelationEntry().add(relationEntry);
                }
                setEagRelation(eagRelation, relationEntry);
            }

            if ((this.eag.getRelations().getEagRelation().isEmpty() && this.eag.getRelations().getResourceRelation().isEmpty())) {
                this.eag.setRelations(null);
            }
        }
        this.log.debug("End method: \"parseRelationEntry\"");
    }

    /**
     * Method to Fill EagRelationType element
     *
     * @param valueList {@link List<String>> the valueList
     * @param j null null     {@link int} the j
	 */
    private void getEagRelationType(int j, List<String> valueList) {
        this.log.debug("Method start: \"getEagRelationType\"");
        // eag/relations/eagRelation/eagRelationType
        if (this.eag2012.getEagRelationEagRelationType() != null
                && !this.eag2012.getEagRelationEagRelationType().isEmpty()
                && !this.eag2012.getEagRelationEagRelationType().equals("none")) {
            if (Eag2012.OPTION_CHILD.equalsIgnoreCase(this.eag2012.getEagRelationEagRelationType().get(j))) {
                eagRelation.setEagRelationType(Eag2012.OPTION_CHILD_TEXT);
            }
            if (Eag2012.OPTION_PARENT.equalsIgnoreCase(this.eag2012.getEagRelationEagRelationType().get(j))) {
                eagRelation.setEagRelationType(Eag2012.OPTION_PARENT_TEXT);
            }
            if (Eag2012.OPTION_EARLIER.equalsIgnoreCase(this.eag2012.getEagRelationEagRelationType().get(j))) {
                eagRelation.setEagRelationType(Eag2012.OPTION_EARLIER_TEXT);
            }
            if (Eag2012.OPTION_LATER.equalsIgnoreCase(this.eag2012.getEagRelationEagRelationType().get(j))) {
                eagRelation.setEagRelationType(Eag2012.OPTION_LATER_TEXT);
            }
            if (Eag2012.OPTION_ASSOCIATIVE.equalsIgnoreCase(this.eag2012.getEagRelationEagRelationType().get(j))) {
                eagRelation.setEagRelationType(Eag2012.OPTION_ASSOCIATIVE_TEXT);
            }

            // eag/relations/eagRelation/href
            if (this.eag2012.getEagRelationHref() != null && !this.eag2012.getEagRelationHref().isEmpty()) {
                if (this.eag2012.getEagRelationHref().size() > valueList.size()) {
                    String href = this.eag2012.getEagRelationHref().get(j + 1);
                    //	href = (href!=null && href.length()>0 && (href.toLowerCase().startsWith("http://") || href.toLowerCase().startsWith("ftp://") || href.toLowerCase().startsWith("https://")) )?href:((href!=null && !href.isEmpty())?((href!=null && !href.isEmpty())?("http://"+href):href):null);
                    eagRelation.setHref(href);
                } else {
                    String href = this.eag2012.getEagRelationHref().get(j);
                    //		href = (href!=null && href.length()>0 && (href.toLowerCase().startsWith("http://") || href.toLowerCase().startsWith("ftp://") || href.toLowerCase().startsWith("https://")) )?href:((href!=null && !href.isEmpty())?((href!=null && !href.isEmpty())?("http://"+href):href):null);
                    eagRelation.setHref(href);
                }
            }
        }
        this.log.debug("End method: \"getEagRelationType\"");

    }

    /**
     * Method to get resourceRelationType element
     *
     * @param sectionLangKey {@link String} the sectionLangKey
     * @param j {@link int} the j
     */
    private void getDescriptiveNote(String sectionLangKey, int j) {
        this.log.debug("Method start: \"getDescriptiveNote\"");
        // eag/relations/eagRelation/descriptiveNote/P
        if (this.eag2012.getDescriptiveNotePValue() != null
                && this.eag2012.getDescriptiveNotePLang() != null) {
            for (int k = 0; k < this.eag2012.getDescriptiveNotePValue().size(); k++) {
                Map<String, Map<String, List<String>>> tabsValueMap = this.eag2012.getDescriptiveNotePValue().get(k);
                Map<String, Map<String, List<String>>> tabsLangMap = this.eag2012.getDescriptiveNotePLang().get(k);
                Iterator<String> tabsValueIt = tabsValueMap.keySet().iterator();
                Iterator<String> tabsLangIt = tabsLangMap.keySet().iterator();
                while (tabsValueIt.hasNext()) {
                    String tabValueKey = tabsValueIt.next();
                    String tabLangKey = tabsLangIt.next();
                    if (Eag2012.TAB_RELATION.equalsIgnoreCase(tabValueKey)
                            && Eag2012.TAB_RELATION.equalsIgnoreCase(tabLangKey)) {
                        Map<String, List<String>> sectionsValueMap = tabsValueMap.get(tabValueKey);
                        Map<String, List<String>> sectionsLangMap = tabsLangMap.get(tabLangKey);
                        Iterator<String> sectionsValueIt = sectionsValueMap.keySet().iterator();
                        Iterator<String> sectionsLangIt = sectionsLangMap.keySet().iterator();
                        while (sectionsValueIt.hasNext()) {
                            String sectionValuesKey = sectionsValueIt.next();
                            sectionLangKey = sectionsLangIt.next();
                            List<String> valuesList = sectionsValueMap.get(sectionValuesKey);
                            List<String> langPList = sectionsLangMap.get(sectionLangKey);
                            for (int l = 0; l < valuesList.size(); l++) {
                                if (l == j) {
                                    if (valuesList.get(l) != null && !valuesList.get(l).isEmpty()) {
                                        P p = new P();
                                        p.setContent(valuesList.get(l));
                                        if (!Eag2012.OPTION_NONE.equalsIgnoreCase(langPList.get(l))) {
                                            p.setLang(langPList.get(l));
                                        }

                                        if (Eag2012.INSTITUTION_RELATIONS.equalsIgnoreCase(sectionValuesKey)
                                                && Eag2012.INSTITUTION_RELATIONS.equalsIgnoreCase(sectionLangKey)) {
                                            DescriptiveNote descriptiveNote = null;
                                            if (eagRelation.getDescriptiveNote() == null) {
                                                descriptiveNote = new DescriptiveNote();
                                            } else {
                                                descriptiveNote = eagRelation.getDescriptiveNote();
                                            }
                                            descriptiveNote.getP().add(p);
                                            eagRelation.setDescriptiveNote(descriptiveNote);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        this.log.debug("End method: \"getDescriptiveNote\"");
    }

    /**
     * Method to set {@link EagRelation} EagRelation object
     *
     * @param eagRelation {@link EagRelation} the eagRelation
     * @param relationEntry {@link RelationEntry} the relationEntry
     */
    private void setEagRelation(EagRelation eagRelation, RelationEntry relationEntry) {
        this.log.debug("Method start: \"setEagRelation\"");
        if (eagRelation.getDate() != null
                || eagRelation.getDateRange() != null
                || eagRelation.getDateSet() != null
                || eagRelation.getEagRelationType() != null
                || (eagRelation.getHref() != null && !eagRelation.getHref().isEmpty())
                || eagRelation.getLastDateTimeVerified() != null
                || (!eagRelation.getRelationEntry().isEmpty()
                && eagRelation.getRelationEntry().get(0) != null
                && eagRelation.getRelationEntry().get(0).getContent() != null
                && !eagRelation.getRelationEntry().get(0).getContent().isEmpty())) {
            boolean found = false;
            for (int x = 0; x < this.eag.getRelations().getEagRelation().size(); x++) {
                if (this.eag.getRelations().getEagRelation().get(x) != null
                        && ((this.eag.getRelations().getEagRelation().get(x).getHref() != null
                        && this.eag.getRelations().getEagRelation().get(x).getHref().equals(eagRelation.getHref()))
                        || (this.eag.getRelations().getEagRelation().get(x).getHref() == null
                        && eagRelation.getHref() == null))) {
                    if (!this.eag.getRelations().getEagRelation().get(x).getRelationEntry().isEmpty()) {
                        for (int k = 0; k < this.eag.getRelations().getEagRelation().get(x).getRelationEntry().size(); k++) {
                            RelationEntry relationEntryCheck = this.eag.getRelations().getEagRelation().get(x).getRelationEntry().get(k);
                            if (relationEntryCheck != null
                                    && (relationEntryCheck.getLang() != null && relationEntryCheck.getLang().equalsIgnoreCase(relationEntry.getLang()))
                                    || (relationEntryCheck.getLang() == null && (relationEntry.getLang() == null || relationEntry.getLang().equalsIgnoreCase(Eag2012.OPTION_NONE)))
                                    || (relationEntryCheck.getLang() == null && (relationEntry.getContent() == null || relationEntry.getContent().isEmpty()))
                                    && relationEntryCheck.getContent() != null
                                    && relationEntry.getContent() != null
                                    && relationEntryCheck.getContent().equalsIgnoreCase(relationEntry.getContent())) {
                                found = true;
                                if (eagRelation.getDescriptiveNote() != null) { //priority for descriptive note
                                    this.eag.getRelations().getEagRelation().set(x, eagRelation);
                                }
                            }
                        }
                    } else {
                        found = true;
                        if (eagRelation.getDescriptiveNote() != null) { //priority for descriptive note
                            this.eag.getRelations().getEagRelation().set(x, eagRelation);
                        }
                    }
                }
            }
            if (!found) {
                if (eagRelation.getRelationEntry() != null || (eagRelation.getHref() != null && !eagRelation.getHref().isEmpty())) {
                    this.eag.getRelations().getEagRelation().add(eagRelation);
                }
            }
        }
        this.log.debug("End method: \"setEagRelation\"");
    }
}
