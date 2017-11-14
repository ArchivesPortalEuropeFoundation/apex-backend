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
    LASTNAME("lastname"),
    AGE("age"),
    ALIAS("alias"),
    BAPTISMDATE("baptismdate"),
    BAPTISMPLACE("baptismplace"),
    BITHDATE("birthdate"),
    BIRTHPLACE("birthplace"),
    BURIALDATE("burialdate"),
    BURIALPLACE("burialplace"),
    DEATHDATE("deathdate"),
    DEATHPLACE("deathplace"),
    DIVOCEDATE("divorcedate"),
    FIRSTNAME("firstname"),
    GENDER("gender"),
    INITIALS("initials"),
    LITERAL("literal"),
    MARITALSTATUS("maritalstatus"),
    MARRIAGEDATE("marriagedate"),
    MARRIAGEPLACE("marriageplace"),
    OCCUPATION("occupation"),
    PATRONYM("patronym"),
    ROLE("role"),
    TITLE("title");
    private String value;

    private ApeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
