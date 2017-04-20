package eu.apenet.dashboard.services.ead.xml.stream.publish;

import java.util.HashSet;
import java.util.Set;

import eu.apenet.dashboard.services.ead.publish.PublishData;

public class EadPublishData extends PublishData {

    private String unitid;
    private String firstOtherUnitid;
    private Set<String> otherUnitid;
    private String firstUnittitle;
    private String scopecontent;
    private String otherinfo;
    private String unitdate;
    private String unitdateNormal;
    private String langmaterial;
    private int numberOfDaos = 0;
    private int numberOfDaosBelow = 0;
    private int numberOfTotalDaos = 0;
    private int numberOfDescendents = 0;
    private Set<String> roledao = new HashSet<String>();
    private String globalLanguage;
    private Set<String> controlAccessSubjectsOccupationsGenreforms = new HashSet<String>();

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }

    public Set<String> getOtherUnitid() {
        return otherUnitid;
    }

    public void setOtherUnitid(Set<String> otherUnitid) {
        this.otherUnitid = otherUnitid;
    }

    public String getFirstUnittitle() {
        return firstUnittitle;
    }

    public void setFirstUnittitle(String firstUnittitle) {
        this.firstUnittitle = firstUnittitle;
    }

    public String getScopecontent() {
        return scopecontent;
    }

    public void setScopecontent(String scopecontent) {
        this.scopecontent = scopecontent;
    }

    public String getOtherinfo() {
        return otherinfo;
    }

    public void setOtherinfo(String otherinfo) {
        this.otherinfo = otherinfo;
    }

    public String getUnitdate() {
        return unitdate;
    }

    public void setUnitdate(String unitdate) {
        this.unitdate = unitdate;
    }

    public String getUnitdateNormal() {
        return unitdateNormal;
    }

    public void setUnitdateNormal(String unitdateNormal) {
        this.unitdateNormal = unitdateNormal;
    }

    public int getNumberOfDaos() {
        return numberOfDaos;
    }

    public void setNumberOfDaos(int numberOfDaos) {
        this.numberOfDaos = numberOfDaos;
    }

    public int getNumberOfTotalDaos() {
        return numberOfTotalDaos;
    }

    public void setNumberOfTotalDaos(int numberOfTotalDaos) {
        this.numberOfTotalDaos = numberOfTotalDaos;
    }

    public int getNumberOfDaosBelow() {
        return numberOfDaosBelow;
    }

    public void setNumberOfDaosBelow(int numberOfDaosBelow) {
        this.numberOfDaosBelow = numberOfDaosBelow;
    }

    public int getNumberOfDescendents() {
        return numberOfDescendents;
    }

    public void setNumberOfDescendents(int numberOfDescendents) {
        this.numberOfDescendents = numberOfDescendents;
    }

    public String getLangmaterial() {
        return langmaterial;
    }

    public void setLangmaterial(String langmaterial) {
        this.langmaterial = langmaterial;
    }

    public Set<String> getRoledao() {
        return roledao;
    }

    public void setRoledao(Set<String> roledao) {
        this.roledao = roledao;
    }

    public String getGlobalLanguage() {
        return globalLanguage;
    }

    public void setGlobalLanguage(String globalLanguage) {
        this.globalLanguage = globalLanguage;
    }

    public String getFirstOtherUnitid() {
        return firstOtherUnitid;
    }

    public void setFirstOtherUnitid(String firstOtherUnitid) {
        this.firstOtherUnitid = firstOtherUnitid;
    }

    public Set<String> getControlAccessSubjectsOccupationsGenreforms() {
        return controlAccessSubjectsOccupationsGenreforms;
    }

    public void setControlAccessSubjectsOccupationsGenreforms(Set<String> controlAccessSubjectsFunctionsGenreforms) {
        this.controlAccessSubjectsOccupationsGenreforms = controlAccessSubjectsFunctionsGenreforms;
    }

    @Override
    public String toString() {
        return "EadPublishData [unitid=" + unitid + ", otherUnitid=" + otherUnitid + ", firstUnittitle="
                + firstUnittitle + ", scopecontent=" + scopecontent + ", otherinfo=" + otherinfo + ", unitdate="
                + unitdate + ", unitdateNormal=" + unitdateNormal + ", langmaterial=" + langmaterial
                + ", numberOfDaos=" + numberOfDaos + ", roledao=" + roledao + ", globalLanguage="
                + globalLanguage + "]";
    }

}
