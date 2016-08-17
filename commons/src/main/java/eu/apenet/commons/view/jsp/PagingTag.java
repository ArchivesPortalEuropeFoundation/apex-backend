package eu.apenet.commons.view.jsp;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

public class PagingTag extends AbstractPagingTag {

    private static final String UL_END_TAG = "</ul>";

    private static final String UL_START_TAG = "<ul>";

    private static final String PAGE_NEXT = "&gt;";

    private static final String PAGE_LAST = PAGE_NEXT + PAGE_NEXT;

    private static final String PAGE_PREV = "&lt;";

    private static final String PAGE_FIRST = PAGE_PREV + PAGE_PREV;

    private final static Logger LOGGER = Logger.getLogger(PagingTag.class);

    private static int MAX_NUMBER_OF_VISIBLE_PAGES = 3;

    private static int FIRST_PAGE = 1;

    private String refreshUrl;

    private String pageNumberId;

    private String liferayFriendlyUrl;

    private Object maxNumberOfItems;

    protected String getPageLast() {
        return PAGE_LAST;
    }

    protected String getPageFirst() {
        return PAGE_FIRST;
    }

    protected String getPageNext() {
        return PAGE_NEXT;
    }

    protected String getPagePrevious() {
        return PAGE_PREV;
    }

    public String getRefreshUrl() {
        return refreshUrl;
    }

    public void setRefreshUrl(String refreshUrl) {
        this.refreshUrl = refreshUrl;
    }

    public String getPageNumberId() {
        return pageNumberId;
    }

    public void setPageNumberId(String pageNumberId) {
        this.pageNumberId = pageNumberId;
    }

    public String getLiferayFriendlyUrl() {
        return liferayFriendlyUrl;
    }

    public void setLiferayFriendlyUrl(String liferayFriendlyUrl) {
        this.liferayFriendlyUrl = liferayFriendlyUrl;
    }

    public void doTag() throws JspException, IOException {
        try {

            this.getJspContext().getOut().print(buildPagination());
        } catch (Exception e) {
            LOGGER.error("Can't create page description: " + e.getMessage(), e);
            throw new JspException(e);
        }
    }

    protected StringBuilder buildPagination() {
        Long maxNumberOfItems = toLong(this.getMaxNumberOfItems());
        long numberOfItems = toLong(this.getNumberOfItems());
        int pageNumber = toInteger(this.getPageNumber());
        int pageSize = toInteger(this.getPageSize());
        int numberOfPages = (int) Math.ceil((double) numberOfItems / (double) pageSize);
        boolean fullPagination = maxNumberOfItems == null || (maxNumberOfItems != null && numberOfItems <= maxNumberOfItems);
        if (pageNumber > numberOfPages) {
            pageNumber = numberOfPages;
        } else if (pageNumber < FIRST_PAGE) {
            pageNumber = FIRST_PAGE;
        }
        StringBuilder description = new StringBuilder();
        description.append(UL_START_TAG);
        if (pageNumber == FIRST_PAGE || numberOfPages == 0) {
            if (fullPagination) {
                addListItem(description, getPageFirst());
            }
            addListItem(description, getPagePrevious());
        } else {
            if (fullPagination) {
                addListItemWithLink(description, getPageFirst(), FIRST_PAGE);
            }
            addListItemWithLink(description, getPagePrevious(), pageNumber - 1);
        }
        description.append(" ");

        int firstVisiblePage = pageNumber - MAX_NUMBER_OF_VISIBLE_PAGES;
        int lastVisiblePage = pageNumber + MAX_NUMBER_OF_VISIBLE_PAGES;
        // when first page exceed first page
        if (firstVisiblePage < FIRST_PAGE) {
            int difference = -1 * firstVisiblePage + FIRST_PAGE;
            if (lastVisiblePage + difference < numberOfPages) {
                lastVisiblePage += difference;
            } else {
                lastVisiblePage = numberOfPages;
            }
            firstVisiblePage = FIRST_PAGE;

        }
        // when last page exceed number of pages
        if (lastVisiblePage > numberOfPages) {
            int difference = lastVisiblePage - numberOfPages;
            if (firstVisiblePage - difference > FIRST_PAGE) {
                firstVisiblePage -= difference;
            }
            lastVisiblePage = numberOfPages;
        }
//		if (firstVisiblePage > FIRST_PAGE) {
//			addListItem(description, "... ");
//		}
        for (int i = firstVisiblePage; i < pageNumber; i++) {
            addListItemWithLink(description, i + "", i);
        }
        addActiveListItem(description, pageNumber);
        if (pageNumber == numberOfPages) {
            addListItem(description, getPageNext());
            if (fullPagination) {
                addListItem(description, getPageLast());
            }

        } else {
            for (int i = pageNumber + 1; i <= lastVisiblePage; i++) {
                addListItemWithLink(description, i + "", i);
            }
//			if (lastVisiblePage < numberOfPages) {
//				addListItem(description, "... ");
//			}
            addListItemWithLink(description, getPageNext(), pageNumber + 1);
            if (fullPagination) {
                addListItemWithLink(description, getPageLast(), numberOfPages);
            }
        }
        description.append(UL_END_TAG);
        return description;
    }

    protected void addListItem(StringBuilder builder, String value) {
        builder.append("<li class=\"nolink\">");
        builder.append(value);
        builder.append("</li>");
    }

    protected void addListItemWithLink(StringBuilder builder, String linkName, long pageNumber) {
        builder.append("<li>");
        builder.append("<a href=\"");
        if (Boolean.parseBoolean(liferayFriendlyUrl)) {
            String temp = refreshUrl.replaceAll("%7B" + this.getPageNumberId() + "%7D", pageNumber + "");
            temp = temp.replaceAll("\\{" + this.getPageNumberId() + "\\}", pageNumber + "");
            builder.append(temp);
        } else {
            HrefObject hrefObject = new HrefObject(refreshUrl);
            hrefObject.setParameter(this.getPageNumberId(), pageNumber);
            builder.append(hrefObject.toString());
        }
        builder.append("\">");
        builder.append(linkName);
        builder.append("</a>");
        builder.append("</li>");
    }

    protected void addActiveListItem(StringBuilder builder, int value) {
        builder.append("<li class=\"active\">");
        builder.append(value);
        builder.append("</li>");
    }

    public Object getMaxNumberOfItems() {
        return maxNumberOfItems;
    }

    public void setMaxNumberOfItems(Object maxNumberOfItems) {
        this.maxNumberOfItems = maxNumberOfItems;
    }

}
