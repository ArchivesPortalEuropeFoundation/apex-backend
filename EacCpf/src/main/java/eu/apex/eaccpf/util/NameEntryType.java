/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apex.eaccpf.util;

import eu.apenet.dpt.utils.eaccpf.Date;
import eu.apenet.dpt.utils.eaccpf.DateRange;
import eu.apenet.dpt.utils.eaccpf.Identity;
import eu.apenet.dpt.utils.eaccpf.NameEntry;
import eu.apenet.dpt.utils.eaccpf.NameEntryParallel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author papp
 */
public class NameEntryType {

    private String name;
    private String language;
    private String form;
    private String component;
    private List<DateType> dates;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public List<DateType> getDates() {
        return dates;
    }

    public void setDates(List<DateType> dates) {
        this.dates = dates;
    }

    public NameEntryType fillDataWith(Identity.NameEntry nameEntry) {
        if (nameEntry.getLocalType() != null && !nameEntry.getLocalType().isEmpty()) {
            this.setForm(nameEntry.getLocalType());
        }
        if (nameEntry.getLang() != null && !nameEntry.getLang().isEmpty()) {
            this.setLanguage(nameEntry.getLang());
        }
        if (nameEntry.getPart() != null && nameEntry.getPart().get(0) != null
                && nameEntry.getPart().get(0).getLocalType() != null
                && !nameEntry.getPart().get(0).getLocalType().isEmpty()) {
            this.setComponent(nameEntry.getPart().get(0).getLocalType());
        }
        if (nameEntry.getPart() != null && nameEntry.getPart().get(0) != null
                && nameEntry.getPart().get(0).getContent() != null
                && !nameEntry.getPart().get(0).getContent().isEmpty()) {
            this.setName(nameEntry.getPart().get(0).getContent());
        }

        this.dates = new ArrayList<DateType>();
        if (nameEntry.getUseDates() != null) {
            if (nameEntry.getUseDates().getDateSet() != null || nameEntry.getUseDates().getDateRange() != null || nameEntry.getUseDates().getDate() != null) {
                if (nameEntry.getUseDates().getDateSet() != null
                        && nameEntry.getUseDates().getDateSet().getDateOrDateRange() != null
                        && !nameEntry.getUseDates().getDateSet().getDateOrDateRange().isEmpty()) {
                    for (Object object : nameEntry.getUseDates().getDateSet().getDateOrDateRange()) {
                        if (object instanceof Date) {
                            DateType dateType = new DateType(new SimpleDate(0));
                            this.dates.add(dateType.fillDataWith((Date) object));
                        }
                        if (object instanceof DateRange) {
                            DateType dateType = new DateType(new SimpleDate(0), new SimpleDate(0));
                            this.dates.add(dateType.fillDataWith((DateRange) object));
                        }
                    }
                } else if (nameEntry.getUseDates().getDate() != null) {
                    DateType dateType = new DateType(new SimpleDate(0));
                    dates.add(dateType.fillDataWith(nameEntry.getUseDates().getDate()));
                } else if (nameEntry.getUseDates().getDateRange() != null) {
                    DateType dateType = new DateType(new SimpleDate(0), new SimpleDate(0));
                    dates.add(dateType.fillDataWith(nameEntry.getUseDates().getDateRange()));
                }
            }
        }
        return this;
    }

    public NameEntryType fillDataWith(NameEntry nameEntry, NameEntryParallel nameEntryParallel) {
        if (nameEntry.getLocalType() != null && !nameEntry.getLocalType().isEmpty()) {
            this.setForm(nameEntry.getLocalType());
        }
        if (nameEntry.getLang() != null && !nameEntry.getLang().isEmpty()) {
            this.setLanguage(nameEntry.getLang());
        }
        if (nameEntry.getPart() != null && nameEntry.getPart().get(0) != null
                && nameEntry.getPart().get(0).getLocalType() != null
                && !nameEntry.getPart().get(0).getLocalType().isEmpty()) {
            this.setComponent(nameEntry.getPart().get(0).getLocalType());
        }
        if (nameEntry.getPart() != null && nameEntry.getPart().get(0) != null
                && nameEntry.getPart().get(0).getContent() != null
                && !nameEntry.getPart().get(0).getContent().isEmpty()) {
            this.setName(nameEntry.getPart().get(0).getContent());
        }
//        if (nameEntryParallel.getUseDates() != null) {
//            if (nameEntries.getUseDates().getDateSet() != null || nameEntries.getUseDates().getDateRange() != null || nameEntries.getUseDates().getDate() != null) {
//                this.dates = new ArrayList<DateType>();
//
//                if (nameEntries.getUseDates().getDateSet() != null
//                        && nameEntries.getUseDates().getDateSet().getDateOrDateRange() != null
//                        && nameEntries.getUseDates().getDateSet().getDateOrDateRange().isEmpty()) {
//                    for (Object object : nameEntries.getUseDates().getDateSet().getDateOrDateRange()) {
//                        if (object instanceof Date) {
//                            DateType dateType = new DateType(new SimpleDate(0));
//                            this.dates.add(dateType.fillDataWith((Date) object));
//                        }
//                        if (object instanceof DateRange) {
//                            DateType dateType = new DateType(new SimpleDate(0), new SimpleDate(0));
//                            this.dates.add(dateType.fillDataWith((DateRange) object));
//                        }
//                    }
//                } else if (nameEntries.getUseDates().getDate() != null) {
//                    DateType dateType = new DateType(new SimpleDate(0));
//                    dates.add(dateType.fillDataWith(nameEntries.getUseDates().getDate()));
//                } else if (nameEntries.getUseDates().getDateRange() != null) {
//                    DateType dateType = new DateType(new SimpleDate(0), new SimpleDate(0));
//                    dates.add(dateType.fillDataWith(nameEntries.getUseDates().getDateRange()));
//                }
//            }
//        }
        return this;
    }
}
