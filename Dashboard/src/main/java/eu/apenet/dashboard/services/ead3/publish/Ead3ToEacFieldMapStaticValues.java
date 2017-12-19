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
public interface Ead3ToEacFieldMapStaticValues {

    String[] CPF_TYPE_PERSON = {"person"};
    String CPF_TYPE_CORPORATE_BODY = "corporateBody";
    String CPF_TYPE_FAMILY = "family";
    String PART_LOCAL_TYPE_FRIST_NAME = "firstname";
    String PART_LOCAL_TYPE_LAST_NAME = "lastname";
    String PART_LOCAL_TYPE_SUR_NAME = "surname";
    String PART_LOCAL_TYPE_ROLE = "role";
    String PART_LOCAL_TYPE_GENDER = "gender";
    String PART_LOCAL_TYPE_DEATH_DATE = "deathdate";
    String PART_LOCAL_TYPE_BIRTH_DATE = "birthdate";
    String PART_LOCAL_TYPE_PERS_NAME = "persname";
    String DATE_EXISTING_TYPE_UNKNOWN = "unknown";
    String DATE_EXISTING_TYPE_OPEN = "open";
    String SOURCE = "source";

}
