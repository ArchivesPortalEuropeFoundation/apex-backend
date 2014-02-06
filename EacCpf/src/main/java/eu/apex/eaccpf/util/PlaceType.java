/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apex.eaccpf.util;

import eu.apex.eaccpf.data.AddressLine;
import eu.apex.eaccpf.data.Date;
import eu.apex.eaccpf.data.DateRange;
import eu.apex.eaccpf.data.Place;
import eu.apex.eaccpf.data.PlaceEntry;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author papp
 */
public class PlaceType {

    private String placeName;
    private String languageCode;
    private String vocabLink;
    private String countryCode;
    private List<String> addressDetails = null;
    private List<String> addressComponents = null;
    private String role;
    private List<DateType> dates;

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public List<String> getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(List<String> addressDetails) {
        this.addressDetails = addressDetails;
    }

    public List<String> getAddressComponents() {
        return addressComponents;
    }

    public void setAddressComponents(List<String> addressComponents) {
        this.addressComponents = addressComponents;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<DateType> getDates() {
        return dates;
    }

    public void setDates(List<DateType> dates) {
        this.dates = dates;
    }

    public PlaceType fillDataWith(Place place) {
        if (place.getPlaceEntry() != null
                && !place.getPlaceEntry().isEmpty()) {
            for (PlaceEntry placeEntry : place.getPlaceEntry()) {
                if (placeEntry.getCountryCode() != null
                        && !placeEntry.getCountryCode().isEmpty()) {
                    this.countryCode = placeEntry.getCountryCode();
                }
                if (placeEntry.getLocalType() != null
                        && !placeEntry.getLocalType().isEmpty()) {
                    this.role = placeEntry.getLocalType();
                }
                if (placeEntry.getVocabularySource() != null
                        && !placeEntry.getVocabularySource().isEmpty()) {
                    this.vocabLink = placeEntry.getVocabularySource();
                }
                if (placeEntry.getLang() != null
                        && !placeEntry.getLang().isEmpty()) {
                    this.languageCode = placeEntry.getLang();
                }
                if (placeEntry.getContent() != null
                        && !placeEntry.getContent().isEmpty()) {
                    this.placeName = placeEntry.getContent();
                }
            }
        }
        if (place.getAddress() != null
                && place.getAddress().getAddressLine() != null
                && !place.getAddress().getAddressLine().isEmpty()){
            this.addressComponents = new ArrayList<String>();
            this.addressDetails = new ArrayList<String>();
            List<AddressLine> addressLines = place.getAddress().getAddressLine();
            for (AddressLine addressLine : addressLines) {
                if(addressLine.getLocalType() != null
                        && !addressLine.getLocalType().isEmpty()){
                    this.addressComponents.add(addressLine.getLocalType());
                } else {
                    this.addressComponents.add("----");
                }
                if(addressLine.getContent() != null
                        && !addressLine.getContent().isEmpty()){
                    this.addressDetails.add(addressLine.getContent());
                }
            }
        }
        if (place.getDateSet() != null || place.getDateRange() != null || place.getDate() != null) {
            this.dates = new ArrayList<DateType>();

            if (place.getDateSet() != null
                    && place.getDateSet().getDateOrDateRange() != null
                    && place.getDateSet().getDateOrDateRange().isEmpty()) {
                for (Object object : place.getDateSet().getDateOrDateRange()) {
                    if (object instanceof Date) {
                        DateType dateType = new DateType(new SimpleDate(0));
                        this.dates.add(dateType.fillDataWith((Date) object));
                    }
                    if (object instanceof DateRange) {
                        DateType dateType = new DateType(new SimpleDate(0), new SimpleDate(0));
                        this.dates.add(dateType.fillDataWith((DateRange) object));
                    }
                }
            } else if (place.getDate() != null) {
                DateType dateType = new DateType(new SimpleDate(0));
                dates.add(dateType.fillDataWith(place.getDate()));
            } else if (place.getDateRange() != null) {
                DateType dateType = new DateType(new SimpleDate(0), new SimpleDate(0));
                dates.add(dateType.fillDataWith(place.getDateRange()));
            }
        }
        return this;
    }
}
