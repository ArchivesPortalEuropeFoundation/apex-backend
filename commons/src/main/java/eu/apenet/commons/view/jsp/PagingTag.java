package eu.apenet.commons.view.jsp;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

public class PagingTag extends AbstractPagingTag {


	private static final String UL_END_TAG = "</ul>";

	private static final String UL_START_TAG = "<ul>";

	private static final String PAGE_NEXT = "&gt;";
	
	private static final String PAGE_LAST = PAGE_NEXT+PAGE_NEXT;

	private static final String PAGE_PREV = "&lt;";

	private static final String PAGE_FIRST = PAGE_PREV+PAGE_PREV;

	private Logger log = Logger.getLogger(PagingTag.class);
	
	private static int MAX_NUMBER_OF_VISIBLE_PAGES = 5;
	
	private static int FIRST_PAGE = 1;
	
	private String refreshUrl;

	private String pageNumberId;

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

	public void doTag() throws JspException, IOException {
		try {
			long numberOfItems =  toLong(this.getNumberOfItems());
			int pageNumber =  toInteger(this.getPageNumber());
			int pageSize =  toInteger(this.getPageSize());
		int numberOfPages = (int) Math.ceil((double)numberOfItems / (double) pageSize);
		if (pageNumber > numberOfPages){
			pageNumber = numberOfPages;
		}else if (pageNumber < FIRST_PAGE){
			pageNumber = FIRST_PAGE;
		}
		StringBuilder description = new StringBuilder();
		description.append(UL_START_TAG);
		if (pageNumber == FIRST_PAGE){
			addListItem(description, PAGE_FIRST);
			addListItem(description, PAGE_PREV);
		}else{
			addListItemWithLink(description,PAGE_FIRST, FIRST_PAGE);
			addListItemWithLink(description,PAGE_PREV, pageNumber-1);
		}
		description.append(" ");

		int firstVisiblePage = pageNumber - MAX_NUMBER_OF_VISIBLE_PAGES;
		int lastVisiblePage = pageNumber + MAX_NUMBER_OF_VISIBLE_PAGES;
		//when first page exceed first page
		if (firstVisiblePage < FIRST_PAGE){
			int difference = -1 * firstVisiblePage + FIRST_PAGE;
			if (lastVisiblePage + difference < numberOfPages){
				lastVisiblePage += difference;
			}else{
				lastVisiblePage = numberOfPages;
			}
			firstVisiblePage = FIRST_PAGE;
			
		}
		//when last page exceed number of pages
		if (lastVisiblePage > numberOfPages){
			int difference = lastVisiblePage - numberOfPages;
			if (firstVisiblePage - difference > FIRST_PAGE){
				firstVisiblePage -= difference;
			}
			lastVisiblePage = numberOfPages;
		}
		if (firstVisiblePage > FIRST_PAGE){
			addListItem(description,"... ");				
		}
		addActiveListItem(description, pageNumber);
		if (pageNumber == numberOfPages){
			addListItem(description, PAGE_NEXT);
			addListItem(description, PAGE_LAST);


		}else{
			addListItemWithLink(description,PAGE_NEXT, pageNumber+1);
			addListItemWithLink(description,PAGE_LAST, numberOfPages);
		}
		description.append(UL_END_TAG);
		this.getJspContext().getOut().print(description);
		} catch (Exception e) {
			log.error("Can't create page description: " + e.getMessage(), e);
			throw new JspException(e);
		}
	}

	private void addListItem(StringBuilder builder, String value){
		builder.append("<li><span>");
		builder.append(value);
		builder.append("</span></li>");
	}
	private void addListItemWithLink(StringBuilder builder, String linkName, long pageNumber){
		builder.append("<li>");
		builder.append("<a href=\"");
		HrefObject hrefObject = new HrefObject(refreshUrl);
		hrefObject.setParameter(this.getPageNumberId(), pageNumber);
		builder.append(hrefObject.toString());
		builder.append("\">");
		builder.append("<span>");
		builder.append(linkName);
		builder.append("</span>");
		builder.append("</a>");
		builder.append("</li>");
	}
	
	private void addActiveListItem(StringBuilder builder, int value){
		builder.append("<li class=\"active\"><span>");
		builder.append(value);
		builder.append("</span></li>");
	}

	
}
