package eu.apenet.commons.view.jsp;

import java.io.IOException;
import java.text.NumberFormat;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

public class PageDescriptionTag extends AbstractPagingTag {

    private Logger log = Logger.getLogger(PageDescriptionTag.class);
    private Object numberFormat;

    public void doTag() throws JspException, IOException {
        try {
            long numberOfItems = toLong(this.getNumberOfItems());
            int pageNumber = toInteger(this.getPageNumber());
            int pageSize = toInteger(this.getPageSize());
            StringBuilder description = new StringBuilder();
            description.append(formatNumber((pageNumber - 1) * pageSize + 1));
            description.append(StringEscapeUtils.escapeHtml(" - "));
            if (pageNumber * pageSize < numberOfItems) {
                description.append(formatNumber(pageNumber * pageSize));
            } else {
                description.append(formatNumber(numberOfItems));
            }

            description.append(StringEscapeUtils.escapeHtml(" / "));
            description.append(formatNumber(numberOfItems));
            this.getJspContext().getOut().print(description);
        } catch (Exception e) {
            log.error("Can't create page description: " + e.getMessage(), e);
            throw new JspException(e);
        }
    }

    private String formatNumber(long longValue) {
        if (numberFormat != null) {
            NumberFormat numberFormatter = (NumberFormat) numberFormat;
            return numberFormatter.format(longValue);
        }
        return longValue + "";
    }

    private String formatNumber(int intValue) {
        if (numberFormat != null) {
            NumberFormat numberFormatter = (NumberFormat) numberFormat;
            return numberFormatter.format(intValue);
        }
        return intValue + "";
    }

    public Object getNumberFormat() {
        return numberFormat;
    }

    public void setNumberFormat(Object numberFormat) {
        this.numberFormat = numberFormat;
    }

}
