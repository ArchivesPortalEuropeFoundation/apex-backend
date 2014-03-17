/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apex.eaccpf.util;

import eu.apenet.dpt.utils.eaccpf.Date;
import eu.apenet.dpt.utils.eaccpf.DateRange;
import eu.apenet.dpt.utils.eaccpf.Function;
import eu.apenet.dpt.utils.eaccpf.P;
import eu.apenet.dpt.utils.eaccpf.PlaceEntry;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author papp
 */
public class FunctionType {

    private String functionName;
    private String languageCode;
    private String vocabLink;
    private String description;
    private List<String> places = null;
    private List<String> countryCodes = null;
    private List<DateType> dates;

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getVocabLink() {
        return vocabLink;
    }

    public void setVocabLink(String vocabLink) {
        this.vocabLink = vocabLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPlaces() {
        return places;
    }

    public void setPlaces(List<String> places) {
        this.places = places;
    }

    public List<String> getCountryCodes() {
        return countryCodes;
    }

    public void setCountryCodes(List<String> countryCodes) {
        this.countryCodes = countryCodes;
    }

    public List<DateType> getDates() {
        return dates;
    }

    public void setDates(List<DateType> dates) {
        this.dates = dates;
    }

    public FunctionType fillDataWith(Function function) {
        if (function.getTerm() != null
                && function.getTerm().getContent() != null
                && !function.getTerm().getContent().isEmpty()) {
            this.functionName = function.getTerm().getContent();

            if (function.getTerm().getVocabularySource() != null
                    && !function.getTerm().getVocabularySource().isEmpty()) {
                this.vocabLink = function.getTerm().getVocabularySource();
            }
            if (function.getTerm().getLang() != null
                    && !function.getTerm().getLang().isEmpty()) {
                this.languageCode = function.getTerm().getLang();
            }
        }
        if (function.getDescriptiveNote() != null
                && function.getDescriptiveNote().getP() != null
                && !function.getDescriptiveNote().getP().isEmpty()) {
            List<P> paragraphs = function.getDescriptiveNote().getP();
            for (P p : paragraphs) {
                if (p.getContent() != null && !p.getContent().isEmpty()) {
                    this.description = p.getContent();
                }
            }
        }

        this.countryCodes = new ArrayList<String>();
        this.places = new ArrayList<String>();
        if (function.getPlaceEntry() != null
                && !function.getPlaceEntry().isEmpty()) {
            List<PlaceEntry> placeEntries = function.getPlaceEntry();
            for (PlaceEntry placeEntry : placeEntries) {
                if (placeEntry.getCountryCode() != null
                        && !placeEntry.getCountryCode().isEmpty()) {
                    this.countryCodes.add(placeEntry.getCountryCode());
                } else {
                    this.countryCodes.add("");
                }
                if (placeEntry.getContent() != null
                        && !placeEntry.getContent().isEmpty()) {
                    this.places.add(placeEntry.getContent());
                }
            }
        }

        this.dates = new ArrayList<DateType>();
        if (function.getDateSet() != null || function.getDateRange() != null || function.getDate() != null) {

            if (function.getDateSet() != null
                    && function.getDateSet().getDateOrDateRange() != null
                    && !function.getDateSet().getDateOrDateRange().isEmpty()) {
                for (Object object : function.getDateSet().getDateOrDateRange()) {
                    if (object instanceof Date) {
                        DateType dateType = new DateType(new SimpleDate(0));
                        this.dates.add(dateType.fillDataWith((Date) object));
                    }
                    if (object instanceof DateRange) {
                        DateType dateType = new DateType(new SimpleDate(0), new SimpleDate(0));
                        this.dates.add(dateType.fillDataWith((DateRange) object));
                    }
                }
            } else if (function.getDate() != null) {
                DateType dateType = new DateType(new SimpleDate(0));
                dates.add(dateType.fillDataWith(function.getDate()));
            } else if (function.getDateRange() != null) {
                DateType dateType = new DateType(new SimpleDate(0), new SimpleDate(0));
                dates.add(dateType.fillDataWith(function.getDateRange()));
            }
        }
        return this;
    }
}
