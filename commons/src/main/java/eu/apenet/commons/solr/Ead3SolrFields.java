/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.commons.solr;

/**
 *
 * @author mahbub
 */
public final class Ead3SolrFields {
    public final static String ID = "id";
    public final static String UNIT_TITLE = "unitTitle";
    public final static String SCOPE_CONTENT = "scopeContent";
    public final static String OTHER = "other";
    public final static String UNIT_DATE = "unitDate";
    public final static String UNIT_ID = "unitId";
    public final static String OTHER_UNIT_ID = "otherUnitId";
    public final static String TITLE_PROPER = "titleProper";
    public final static String COUNTRY_ID = "countryId";
    public final static String COUNTRY_NAME = "countryName";
    public final static String AI_ID = "aiId";
    public final static String AI_NAME = "aiName";
    public final static String REPOSITORY_CODE = "repositoryCode";
    public final static String NUMBER_OF_DECENDENTS = "numberOfDecendents";
    public final static String NUMBER_OF_ANCESTORS = "numberOfAncestors";
    
    public final static String LEVEL_NAME = "levelName"; //archdes or clevel
    public final static String START_DATE = "startDate";
    public final static String END_DATE = "endDate";
    public final static String DATE_TYPE = "dateType"; //normal or noDate or otherDate
    
    public final static String LANGUAGE = "language";
    public final static String LANG_MATERIAL = "langMaterial";
    public final static String RECORD_ID = "recordId";
    public final static String RECORD_TYPE = "recordType"; //fa, sg, hg - generated
    
    public final static String DAO = "dao";
    public final static String NUMBER_OF_DAO = "numberOfDao";
    public final static String DAO_LINKS = "daoLinks";
    public final static String DAO_TYPE = "daoType";
    public final static String TIMESTAMP = "timestamp";
    
    public final static String TOPIC = "topic";
    public final static String PARENT_ID = "parentId";
    public final static String PARENT_UNIT_ID = "parentUnitId";
    public final static String DUPLICATE_UNIT_ID = "duplicateParentId";
    public final static String SIBLING_POSITION = "siblingPosition";
    public final static String OPEN_DATA = "openData";
    public final static String SPELL = "spell";
    public final static String OPEN_DATA_SPELL = "openDataSpell";
    
    
    public final static String SORTABLE_RECORD_ID = "unitRecordIDSort";
    public final static String SORTABLE_UNIT_ID = "unitIdSort";
    public final static String SORTABLE_UNIT_TITLE = "unitTitleSort";
}
