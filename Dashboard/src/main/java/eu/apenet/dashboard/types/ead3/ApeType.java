/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.types.ead3;

/**
 *
 * @author kaisar
 */
public enum ApeType {
    CORPNAME("corpname"),
    DATES("dates"),
    REMARK("remark"),
    RESIDENCE("residence"),
    INFIX("infix"),
    INFIXTITLE("infixtitle"),
    LASTNAME("lastname"),
    AGE("age"),
    ALIAS("alias"),
    BAPTISMDATE("baptismdate"),
    BAPTISMPLACE("baptismplace"),
    BITHDATE("birthdate"),
    BIRTHNAME("birthname"),
    BIRTHPLACE("birthplace"),
    BURIALDATE("burialdate"),
    BURIALPLACE("burialplace"),
    BUSINESS_RESIDENCE("business-residence"),
    DEATHDATE("deathdate"),
    DEATHPLACE("deathplace"),
    DIVOCEDATE("divorcedate"),
    FAMNAME("famname"),
    FIRSTNAME("firstname"),
    LEGALFORM("legalform"),
    GENDER("gender"),
    INITIALS("initials"),
    LITERAL("literal"),
    MARITALSTATUS("maritalstatus"),
    MARRIAGEDATE("marriagedate"),
    MARRIAGEPLACE("marriageplace"),
    OCCUPATION("occupation"),
    PATRONYM("patronym"),
    PERSNAME("persname"),
    POSTTITLE("posttitle"),
    PRETITLE("pretitle"),
    PRIVATE_RESIDENCE("private-residence"),
    ROLE("role"),
    SUFFIX("suffix"),
    TITLE("title");
    private final String value;

    private ApeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
