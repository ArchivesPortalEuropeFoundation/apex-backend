/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.manual.eaccpf.util;

import eu.apenet.dpt.utils.eaccpf.Date;
import eu.apenet.dpt.utils.eaccpf.DateRange;
import eu.apenet.dpt.utils.eaccpf.Occupation;
import eu.apenet.dpt.utils.eaccpf.P;
import eu.apenet.dpt.utils.eaccpf.PlaceEntry;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author papp
 */
public class OccupationType {

    private String occupationName;
    private String languageCode;
    private String vocabLink;
    private String description;
    private List<String> places;
    private List<String> countryCodes;
    private List<DateType> dates;

    public String getOccupationName() {
        return occupationName;
    }

    public void setOccupationName(String occupationName) {
        this.occupationName = occupationName;
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

    public OccupationType fillDataWith(Occupation occupation) {
        if (occupation.getTerm() != null
                && occupation.getTerm().getContent() != null
                && !occupation.getTerm().getContent().isEmpty()){
            this.occupationName = occupation.getTerm().getContent();

            if(occupation.getTerm().getVocabularySource() != null
                    && !occupation.getTerm().getVocabularySource().isEmpty()){
                this.vocabLink = occupation.getTerm().getVocabularySource();
            }
            if(occupation.getTerm().getLang() != null
                    && !occupation.getTerm().getLang().isEmpty()){
                this.languageCode = occupation.getTerm().getLang();
            }
        }
        if (occupation.getDescriptiveNote() != null
                && occupation.getDescriptiveNote().getP() != null
                && !occupation.getDescriptiveNote().getP().isEmpty()){
            List<P> paragraphs = occupation.getDescriptiveNote().getP();
            for (P p : paragraphs) {
                if(p.getContent() != null && !p.getContent().isEmpty()){
                    this.description = p.getContent();
                }
            }
        }

        if (occupation.getPlaceEntry() != null
                && !occupation.getPlaceEntry().isEmpty()){
            this.countryCodes = new ArrayList<String>();
            this.places = new ArrayList<String>();
            List<PlaceEntry> placeEntries = occupation.getPlaceEntry();
            for (PlaceEntry placeEntry : placeEntries) {
                if(placeEntry.getCountryCode() != null
                        && !placeEntry.getCountryCode().isEmpty()){
                    this.countryCodes.add(placeEntry.getCountryCode());
                } else {
                    this.countryCodes.add("");
                }
                if(placeEntry.getContent() != null
                        && !placeEntry.getContent().isEmpty()){
                    this.places.add(placeEntry.getContent());
                }
            }
        }

        if (occupation.getDateSet() != null || occupation.getDateRange() != null || occupation.getDate() != null) {
            this.dates = new ArrayList<DateType>();

            if (occupation.getDateSet() != null
                    && occupation.getDateSet().getDateOrDateRange() != null
                    && !occupation.getDateSet().getDateOrDateRange().isEmpty()) {
                for (Object object : occupation.getDateSet().getDateOrDateRange()) {
                    if (object instanceof Date) {
                        DateType dateType = new DateType(new SimpleDate(0));
                        this.dates.add(dateType.fillDataWith((Date) object));
                    }
                    if (object instanceof DateRange) {
                        DateType dateType = new DateType(new SimpleDate(0), new SimpleDate(0));
                        this.dates.add(dateType.fillDataWith((DateRange) object));
                    }
                }
            } else if (occupation.getDate() != null) {
                DateType dateType = new DateType(new SimpleDate(0));
                dates.add(dateType.fillDataWith(occupation.getDate()));
            } else if (occupation.getDateRange() != null) {
                DateType dateType = new DateType(new SimpleDate(0), new SimpleDate(0));
                dates.add(dateType.fillDataWith(occupation.getDateRange()));
            }
        }
        return this;
    }
}
