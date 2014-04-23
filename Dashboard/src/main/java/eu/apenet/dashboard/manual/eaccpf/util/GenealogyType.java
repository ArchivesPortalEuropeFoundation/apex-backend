/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.manual.eaccpf.util;

import eu.apenet.dpt.utils.eaccpf.BiogHist;
import eu.apenet.dpt.utils.eaccpf.P;
import eu.apenet.dpt.utils.eaccpf.StructureOrGenealogy;
import java.util.List;

/**
 *
 * @author papp
 */
public class GenealogyType {

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

    public GenealogyType fillDataWith(P p) {
        if (p.getLang() != null
                && !p.getLang().isEmpty()) {
            this.languageCode = p.getLang();
        }
        if (p.getContent() != null
                && !p.getContent().isEmpty()) {
            this.paragraph = p.getContent();
        }
        return this;
    }
}
