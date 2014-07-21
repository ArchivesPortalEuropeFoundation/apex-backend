/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.manual.eaccpf.util;

import eu.apenet.dpt.utils.eaccpf.Date;
import eu.apenet.dpt.utils.eaccpf.DateRange;

/**
 *
 * @author papp
 */
public class DateType {

    private SimpleDate standardDate1;
    private SimpleDate standardDate2;
    private String dateContent1;
    private String dateContent2;
    private String localType;

    public DateType(SimpleDate date1) {
        this.standardDate1 = date1;
        this.standardDate2 = null;
    }

    public DateType(SimpleDate date1, SimpleDate date2) {
        this.standardDate1 = date1;
        this.standardDate2 = date2;
    }

    public SimpleDate getStandardDate1() {
        return standardDate1;
    }

    public void setStandardDate1(SimpleDate date1) {
        this.standardDate1 = date1;
    }

    public SimpleDate getStandardDate2() {
        return standardDate2;
    }

    public void setStandardDate2(SimpleDate date2) {
        this.standardDate2 = date2;
    }

    public String getDateContent1() {
        return dateContent1;
    }

    public void setDateContent1(String dateContent1) {
        this.dateContent1 = dateContent1;
    }

    public String getDateContent2() {
        return dateContent2;
    }

    public void setDateContent2(String dateContent2) {
        this.dateContent2 = dateContent2;
    }

    public String getLocalType() {
        return localType;
    }

    public void setLocalType(String localType) {
        this.localType = localType;
    }

    public DateType fillDataWith(Date date) {
        if (date.getContent() != null && !date.getContent().isEmpty()){
            this.setDateContent1(date.getContent());
        }
        if (date.getStandardDate() != null && !date.getStandardDate().isEmpty()) {
            String[] dateContent = date.getStandardDate().split("-", 3);
            standardDate1.setYear(Integer.parseInt(dateContent[0]));
            if (dateContent.length > 1 && dateContent[1] != null) {
                standardDate1.setMonth(Integer.parseInt(dateContent[1]));
            }
            if (dateContent.length > 2 && dateContent[2] != null) {
                standardDate1.setDay(Integer.parseInt(dateContent[2]));
            }
        }
        return this;
    }

    public DateType fillDataWith(DateRange dateRange) {
        if (dateRange.getFromDate() != null && dateRange.getFromDate().getContent() != null && !dateRange.getFromDate().getContent().isEmpty()){
            this.setDateContent1(dateRange.getFromDate().getContent());
        }
        if (dateRange.getFromDate() != null && dateRange.getFromDate().getStandardDate() != null
                && !dateRange.getFromDate().getStandardDate().isEmpty()) {
            String[] dateContent = dateRange.getFromDate().getStandardDate().split("-", 3);
            standardDate1.setYear(Integer.parseInt(dateContent[0]));
            if (dateContent.length > 1 && dateContent[1] != null) {
                standardDate1.setMonth(Integer.parseInt(dateContent[1]));
            }
            if (dateContent.length > 2 && dateContent[2] != null) {
                standardDate1.setDay(Integer.parseInt(dateContent[2]));
            }
        }
        if (dateRange.getToDate() != null && dateRange.getToDate().getContent() != null && !dateRange.getToDate().getContent().isEmpty()){
            this.setDateContent2(dateRange.getToDate().getContent());
        }
        if (dateRange.getToDate() != null && dateRange.getToDate().getStandardDate() != null
                && !dateRange.getToDate().getStandardDate().isEmpty()) {
            String[] dateContent = dateRange.getToDate().getStandardDate().split("-", 3);
            standardDate2.setYear(Integer.parseInt(dateContent[0]));
            if (dateContent.length > 1 && dateContent[1] != null) {
                standardDate2.setMonth(Integer.parseInt(dateContent[1]));
            }
            if (dateContent.length > 2 && dateContent[2] != null) {
                standardDate2.setDay(Integer.parseInt(dateContent[2]));
            }
        }
        return this;
    }

    @Override
    public String toString(){
        if(standardDate2 == null)
            return standardDate1.toString();
        else
            return standardDate1.toString() + " - " + standardDate2.toString();
    }
}
