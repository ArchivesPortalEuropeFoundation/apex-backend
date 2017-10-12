/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3.publish;

/**
 *
 * @author kaisar
 */
public interface Ead3ToEacFieldMapKeys {

    /**
     * value can be a person, corporateBody or family
     */
    String CPF_TYPE = "cpfType";
    /**
     * //ead/control/languagedeclaration/language@langcode
     */
    String DEFAULT_LANGUAGE = "defaultLanguage";
    /**
     * // //ead/control/languagedeclaration/script@scriptcode
     */
    String DEFAULT_SCRIPT = "defaultScript";
    /**
     * // used in a form of "identityPersonName_1_part_1" to give the value and
     * "identityPersonName_1_comp_1" to give the type
     */
    String IDENTITY_PERSON_NAME_ = "identityPersonName_";
    /**
     * // used with counter like "identityFormOfName_1" will come from has to
     * ask wim from where //eac-cpf/cpfDescription/identity/nameEntry@localType
     * came
     */
    String IDENTITY_FORM_OF_NAME_ = "identityFormOfName_";
    /**
     * // used with a counter like "identityNameLanguage_1" from default
     * language
     */
    String IDENTITY_LANGUAGE_NAME_ = "identityNameLanguage_";
    /**
     * //used with a counter like "genealogyDescription_1" //gender, living
     * status for example
     */
    String GENEALOGY_DESCRIPTION_ = "genealogyDescription_";
    /**
     * // used with a counter like "genealogyLanguage_1" // from default
     * language
     */
    String GENEALOGY_LANGUAGE_ = "genealogyLanguage_";
    /**
     * // used with a counter like "textResRelationLink_1" have to ask wim how
     * to generate this link form ead3
     */
    String TEXT_RES_RELATION_LINK_ = "textResRelationLink_";
    /**
     * //used with a counter like "resRelationType_1" value would be
     * creatorOf,subjectOf or other
     */
    String RES_RELATION_TYPE_ = "resRelationType_";
    /**
     * //used with a counter like "textResRelationName_1" value like "See more"
     */
    String TEXT_RES_RELATION_NAME_ = "textResRelationName_";
    /**
     * //used with a counter like "textLocalId_1" identifier generated from
     * FA-UNITID-SERIAL_NUMBER
     */
    String TEXT_LOCAL_ID_ = "textLocalId_";
    /**
     * // used with counter like "dateExistenceTable_date_2_1" _1 means from _2
     * means to and last counter is number of date. Probable value can be known,
     * unknown and open in that case no need to provide standardised dates.
     */
    String DATE_EXISTENCE_TABLE_DATE_ = "dateExistenceTable_date_";
    /**
     * // used with a counter like "dateExistenceTable_date_1_Year_1" can be
     * composed with DATE_EXISTENCE_TABLE_DATE
     */
    String YEAR_ = "Year_";
    /**
     * // used with a counter like "dateExistenceTable_date_1_Month_1" can be
     * composed with DATE_EXISTENCE_TABLE_DATE
     */
    String MONTH_ = "Month_";
    /**
     * // used with a counter like "dateExistenceTable_date_1_Day_1" can be
     * composed with DATE_EXISTENCE_TABLE_DATE
     */
    String DAY_ = "Day_";
}
