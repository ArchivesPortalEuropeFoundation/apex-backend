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
    public final static String ID = "id"; //indexed+stored - unique
    public final static String PARENT_ID = "parentId";//indexed+stored -
    
    public final static String TITLE_PROPER = "titleProper"; //indexed+stored - global
    public final static String LANGUAGE = "language"; //indexed+stored - global
    public final static String LANG_MATERIAL = "langMaterial"; //stored - global
    public final static String RECORD_ID = "recordId"; //indexed+stored - global
    public final static String RECORD_TYPE = "recordType"; //indexed+stored - fa, sg, hg - generated
    
    public final static String UNIT_ID = "unitId"; //indexed+stored - 
    public final static String OTHER_UNIT_ID = "otherUnitId"; //indexed+stored - array
    public final static String PARENT_UNIT_ID = "parentUnitId";//indexed+stored - 
    
    public final static String UNIT_TITLE = "unitTitle"; //indexed+stored - 
    public final static String UNIT_DATE = "unitDate"; //indexed+stored - 
    public final static String SCOPE_CONTENT = "scopeContent"; //indexed+stored - 
    public final static String OTHER = "other";  //indexed+stored - other fields valaues, excluding unitTitle, scopeContent, titleProper, etc already stored data fields
    
    public final static String COUNTRY_ID = "countryId"; //indexed+stored - 
    public final static String COUNTRY_NAME = "countryName"; //indexed+stored - 
    public final static String AI_ID = "aiId"; //indexed+stored - db value
    public final static String AI_NAME = "aiName"; //indexed+stored - db value
    public final static String REPOSITORY_CODE = "repositoryCode"; //indexed+stored - db value
    public final static String NUMBER_OF_DESCENDENTS = "numberOfDescendents"; //stored - 
    public final static String NUMBER_OF_ANCESTORS = "numberOfAncestors"; //stored - 
    
    public final static String LEVEL_NAME = "levelName";  //indexed+stored - archdes, clevel
    public final static String START_DATE = "startDate"; //indexed+stored - 
    public final static String END_DATE = "endDate"; //indexed+stored - 
    public final static String DATE_TYPE = "dateType"; //indexed+stored - normal, noDate, otherDate
    
    public final static String DAO = "dao";//indexed+stored - true, false
    public final static String NUMBER_OF_DAO = "numberOfDao";//stored
    public final static String DAO_LINKS = "daoLinks"; //stored - array of objects, object = link and clevel's Id
    public final static String DAO_TYPE = "daoType";//indexed+stored
    public final static String TIMESTAMP = "timestamp";//indexed+stored
    
    public final static String TOPIC = "topic";//indexed+stored
    public final static String DUPLICATE_UNIT_ID = "duplicateUnitId";//stored
    public final static String SIBLING_POSITION = "siblingPosition";//stored
    public final static String OPEN_DATA = "openData";//indexed+stored
    
    public final static String SPELL = "spell";//indexed+stored
    public final static String OPEN_DATA_SPELL = "openDataSpell"; //indexed+stored
    
    public final static String DYNAMIC_STRING_SUFFIX = "_s";
    public final static String DYNAMIC_NUMBER_SUFFIX = "_i";
    
    public final static String SORTABLE_RECORD_ID = "unitRecordIDSort"; //indexed
    public final static String SORTABLE_UNIT_ID = "unitIdSort"; //indexed
    public final static String SORTABLE_UNIT_TITLE = "unitTitleSort"; //indexed
}
