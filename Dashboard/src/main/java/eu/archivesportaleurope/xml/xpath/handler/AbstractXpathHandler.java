package eu.archivesportaleurope.xml.xpath.handler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;

/**
 * Abstract class that contains most of the logic for Xpath processing.
 * 
 * @author Bastiaan Verhoef
 *
 */
public abstract class AbstractXpathHandler implements XpathHandler {

	/**
	 * Contains the xpath query.
	 */
	private XpathQuery xpathQuery;
	private int position = 0;
	private boolean xpathElementMatch = false;
	private boolean match = false;
	private boolean allTextBelow = false;
	private boolean onlyFirst = false;
	private String attributeNamespace;
	private String attributeName;
	private String attributeValue;
	private boolean attributeValueNot = false;
	private boolean relative = false;

	public AbstractXpathHandler(String defaultNamespace, String[] xpathQueryArray) {
		xpathQuery = new XpathQuery(defaultNamespace, xpathQueryArray);
	}

	/**
	 * Use only the first occurrence
	 * 
	 * @param onlyFirst
	 */
	public void setOnlyFirst(boolean onlyFirst) {
		this.onlyFirst = onlyFirst;
	}

	/**
	 * Specify the xml attribute including the namespace
	 * 
	 * @param namespace
	 * @param localPart
	 * @param value
	 * @param not
	 *            if true, the attribute should not contain the value.
	 */
	public void setAttribute(String namespace, String localPart, String value, boolean not) {
		this.attributeNamespace = namespace;
		this.attributeName = localPart;
		this.attributeValue = value;
		this.attributeValueNot = not;
	}

	/**
	 * Specify the xml attribute
	 * 
	 * @param namespace
	 * @param localPart
	 * @param value
	 * @param not
	 *            if true, the attribute should not contain the value.
	 */
	public void setAttribute(String localPart, String value, boolean not) {
		this.attributeName = localPart;
		this.attributeValue = value;
		this.attributeValueNot = not;
	}

	/**
	 * Include all the text below the matching xml element. (xpath equivalent is
	 * //text()).
	 * 
	 * @param allTextBelow
	 */
	public void setAllTextBelow(boolean allTextBelow) {
		this.allTextBelow = allTextBelow;
	}

	/**
	 * Set the xpath query as relative. (xpath equivalent is //c)
	 * 
	 * @param relative
	 */
	public void setRelative(boolean relative) {
		this.relative = relative;
	}

	public boolean isAllTextBelow() {
		return allTextBelow;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.archivesportaleurope.xml.xpath.handler.XpathHandler#processCharacters
	 * (java.util.List, javax.xml.stream.XMLStreamReader)
	 */
	@Override
	public final void processCharacters(List<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		if (match) {
			/*
			 * if there is a match, process the characters
			 */
			internalProcessCharacters(xmlReader);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.archivesportaleurope.xml.xpath.handler.XpathHandler#processStartElement
	 * (java.util.List, javax.xml.stream.XMLStreamReader)
	 */
	@Override
	public final void processStartElement(List<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		if (relative) {
			/*
			 * if the xpath is relative, generate relative xpath position list
			 */
			internalProcessStartElement(generateRelativeXpath(xpathPosition), xmlReader);
		} else {
			internalProcessStartElement(xpathPosition, xmlReader);
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.archivesportaleurope.xml.xpath.handler.XpathHandler#processEndElement
	 * (java.util.List, javax.xml.stream.XMLStreamReader)
	 */
	@Override
	public final void processEndElement(List<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		if (relative) {
			/*
			 * if the xpath is relative, parse the xpath position if the 
			 */
			internalProcessEndElement(generateRelativeXpath(xpathPosition), xmlReader);
		} else {
			internalProcessEndElement(xpathPosition, xmlReader);
		}
	}
	/**
	 * Generate new relative xpath position
	 * 
	 * @param xpathPosition Current xpath position
	 * @return Relative xpath position
	 */
	private List<QName> generateRelativeXpath(List<QName> xpathPosition){
		List<QName> xpathPositionTemp = new ArrayList<QName>();
		QName first = xpathQuery.getFirst();
		boolean found = false;
		for (QName xpathItem : xpathPosition) {
			if (found) {
				xpathPositionTemp.add(xpathItem);
			} else if (first.equals(xpathItem)) {
				xpathPositionTemp.add(xpathItem);
				found = true;
			}
		}
		return xpathPositionTemp;
	}
	/**
	 * Process the start element and look if there is a match
	 * @param xpathPosition
	 * @param xmlReader
	 * @throws Exception
	 */
	private void internalProcessStartElement(List<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		int xpathPositionSize = xpathPosition.size();
		boolean equalXpathSize = xpathPositionSize == xpathQuery.getXpathQuerySize();
		/*
		 * if the xpath size is equal to the xpath query size, there could be a match.
		 */
		if (equalXpathSize) {
			boolean elementMatch = true;
			/*
			 * match the xpath query to the xpath position
			 */
			elementMatch = xpathQuery.match(xpathPosition);
			/*
			 * if there is a match based on the xpath query, try to match it at the attributes.
			 */
			if (elementMatch) {
				if (StringUtils.isNotBlank(attributeName) && StringUtils.isNotBlank(attributeValue)) {
					String namespace = null;
					/*
					 * if attribute namespace is specified, use it.
					 */
					if (StringUtils.isNotBlank(attributeNamespace)) {
						namespace = attributeNamespace;
					}
					String value = xmlReader.getAttributeValue(namespace, attributeName);
					/*
					 * if the not operator is specified, use it
					 */
					if (attributeValueNot) {
						elementMatch = !attributeValue.equals(value);
					} else {
						elementMatch = attributeValue.equals(value);
					}

				}
			}
			/*
			 * if there is a match based on the xpath query an the attribute, look at the position.
			 */
			if (elementMatch) {
				position++;
				/*
				 * if only the first item should be parse, and the position is is higher than 1, it is not a match.
				 */
				if (onlyFirst && position > 1) {
					elementMatch = false;
				}
			}

			xpathElementMatch = elementMatch;
			match = elementMatch;
			if (elementMatch) {
				/*
				 * there is an exact match, so the start element should be processed.
				 */
				processExactStartElementMatch(xmlReader);
			}
		} else {
			if (isAllTextBelow() && xpathElementMatch) {
				/*
				 * if there is a match of a parent, and all information below the match should be used, then there is a match.
				 */
				match = true;
				processChildStartElementMatch(xmlReader);
			} else {
				/*
				 * in all other cases there is no match.
				 */
				match = false;
			}
		}
	}

	/**
	 * Process the start element and look if there is a match
	 * @param xpathPosition
	 * @param xmlReader
	 * @throws Exception
	 */
	private void internalProcessEndElement(List<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		boolean pastMatch = xpathElementMatch;
		int xpathPositionSize = xpathPosition.size();
		int nextXpathPositionSize = xpathPositionSize - 1;

		boolean equalXpathSize = xpathPositionSize == xpathQuery.getXpathQuerySize();
		if (equalXpathSize) {
			/*
			 * if the xpath size is the same, the next xpath position is no match anymore. 
			 */
			xpathElementMatch = false;
			match = false;
		} else if (match && nextXpathPositionSize > xpathQuery.getXpathQuerySize()) {
			/*
			 * if there is a match, and the size of the next xpath position is higher than xpath query size. It should be an end element of a child.
			 */
			processChildEndElementMatch(xmlReader);
		} else if (nextXpathPositionSize == xpathQuery.getXpathQuerySize()) {
			/*
			 * if there is a match, and the size of the next xpath position is the same as xpath query size.
			 * Check of the new xpath position match the xpath query. 
			 */
			LinkedList<QName> newXPathPosition = new LinkedList<QName>();
			for (int i = 0; i < xpathPositionSize - 1; i++) {
				newXPathPosition.add(xpathPosition.get(i));
			}
			match = xpathQuery.match(newXPathPosition);
			if (match) {
				processChildEndElementMatch(xmlReader);
			}
		} else {
			match = false;
		}

		if (pastMatch && !xpathElementMatch) {
			/*
			 * end of the match, if the past match was true and the current false.
			 */
			processExactEndElementMatch();
		}
	}
	
	/**
	 * Process xml start element, based on an match of a xml element parent.
	 * @param xmlReader
	 * @throws Exception
	 */
	public void processChildStartElementMatch(XMLStreamReader xmlReader) throws Exception {

	}
	
	/**
	 * Process xml end element, based on an match of a xml element parent.
	 * @param xmlReader
	 * @throws Exception
	 */
	public void processChildEndElementMatch(XMLStreamReader xmlReader) throws Exception {

	}



	/**
	 * Process xml start element, based on an exact xml element match.
	 * @param xmlReader
	 */
	protected void processExactStartElementMatch(XMLStreamReader xmlReader) {
	}

	/**
	 * Process xml end element, based on an exact xml element match.
	 * @param xmlReader
	 */
	protected void processExactEndElementMatch() {
	}

	/**
	 * Process xml characters when there is a match
	 * @param xmlReader
	 * @throws Exception
	 */
	protected void internalProcessCharacters(XMLStreamReader xmlReader) throws Exception {
		writeContent(xmlReader.getText());
	}
	
	/**
	 *  
	 * @param content
	 */
	protected void writeContent(String content) {
	}

}
