/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apex.eaccpf.util;

import eu.apex.eaccpf.data.BiogHist;
import eu.apex.eaccpf.data.P;
import java.util.List;

/**
 *
 * @author papp
 */
public class BiographyType {

    private String paragraph;
    private String languageCode;

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public BiographyType fillDataWith(BiogHist biogHist) {
        if (biogHist.getChronListOrPOrCitation() != null
                && biogHist.getChronListOrPOrCitation().isEmpty()) {
            List<Object> paragraph = biogHist.getChronListOrPOrCitation();
            for (Object object : paragraph) {
                if (object instanceof P) {
                    P p = (P) object;
                    if (p.getLang() != null
                            && !p.getLang().isEmpty()) {
                        this.languageCode = p.getLang();
                    }
                    if (p.getContent()!= null
                            && !p.getContent().isEmpty()) {
                        this.paragraph = p.getContent();
                    }
                }
            }
        }
        return this;
    }
}
