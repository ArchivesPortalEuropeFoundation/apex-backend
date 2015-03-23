package eu.archivesportaleurope.xml.xpath.handler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

/**
 * Object representation of a XPATH
 * 
 * @author Bastiaan Verhoef
 *
 */
public class XpathQuery {
	private LinkedList<QName> xpathQueryList = new LinkedList<QName>();
	private List<QName> lastElement = new ArrayList<QName>();
	private int xpathQuerySize = 0;
	private boolean not = false;

	/**
	 * Contructs matchable object from a xpath query.
	 * 
	 * @param defaultNamespace
	 *            Default namespace
	 * @param xpathQueryArray
	 *            Array of xpath elements
	 */
	public XpathQuery(String defaultNamespace, String[] xpathQueryArray) {
		for (int i = 0; i < xpathQueryArray.length; i++) {
			if (i < xpathQueryArray.length - 1) {
				/*
				 * for all items before the last item, the item will be simply
				 * added to the list.
				 */
				this.xpathQueryList.add(new QName(defaultNamespace, xpathQueryArray[i]));
			} else {
				/*
				 * last item in the query could have some boolean operations
				 */
				String lastItem = xpathQueryArray[i];
				/*
				 * NOT implementation
				 */
				if (lastItem.startsWith("not(")) {
					not = true;
					lastItem = lastItem.trim().substring(4, lastItem.length() - 1);
				}
				/*
				 * OR implementation
				 */
				String[] splitted = lastItem.split("\\|");
				for (String item : splitted) {
					this.lastElement.add(new QName(defaultNamespace, item.trim()));
				}
			}
		}
		this.xpathQuerySize = this.xpathQueryList.size();
	}

	/**
	 * Gives the first item of the xpathQuery.
	 * 
	 * @return First QName of the xpathQuery
	 */
	protected QName getFirst() {
		if (xpathQueryList.size() > 0) {
			return xpathQueryList.getFirst();
		} else {
			return lastElement.get(0);
		}
	}

	/**
	 * Matches a list of QNames to the xpathQuery
	 * 
	 * @param xpathPosition
	 *            List of QNames
	 * @return true if match.
	 */
	protected boolean match(List<QName> xpathPosition) {
		int xpathPositionSize = xpathPosition.size();
		boolean equalXpathSize = xpathPositionSize == getXpathQuerySize();
		if (equalXpathSize) {
			/*
			 * if number of items are the same, so possibly a match
			 */
			boolean elementMatch = true;
			for (int current = 0; elementMatch && current < xpathPositionSize; current++) {
				if (current == xpathQuerySize) {
					/*
					 * if last item, support some boolean operations
					 */
					boolean or = false;
					/*
					 * OR operation
					 */
					for (QName item : lastElement) {
						or = or || item.equals(xpathPosition.get(current));
					}
					/*
					 * NOT operation
					 */
					if (not) {
						or = !or;
					}
					elementMatch = or;
				} else {
					elementMatch = xpathQueryList.get(current).equals(xpathPosition.get(current));
				}
			}
			return elementMatch;
		} else {
			/*
			 * sizes are different, so no match.
			 */
			return false;
		}
	}

	/**
	 * Gives the actual size of the xpath query
	 * 
	 * @return size of the xpath query
	 */
	protected int getXpathQuerySize() {
		return xpathQuerySize + 1;
	}

}
