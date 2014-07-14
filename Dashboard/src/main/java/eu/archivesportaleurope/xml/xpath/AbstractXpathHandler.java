package eu.archivesportaleurope.xml.xpath;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractXpathHandler implements XmlStreamHandler {
	protected static final String NO_SEPARATOR = "";
	public static final String WHITE_SPACE = " ";
	private XpathQuery xpathQuery;
	private int position = 0;
	private boolean xpathElementMatch = false;
	private boolean match = false;
	private boolean allTextBelow =false;
	private boolean onlyFirst = false;
	private String attributeNamespace;
	private String attributeName;
	private String attributeValue;
	private boolean attributeValueNot = false;
	private boolean relative = false;
	public AbstractXpathHandler(String defaultNamespace, String[] xpathQueryArray){
		xpathQuery = new XpathQuery(defaultNamespace, xpathQueryArray);
	}
	
	
	
	public void setOnlyFirst(boolean onlyFirst) {
		this.onlyFirst = onlyFirst;
	}

	public void setAttribute(String namespace, String localPart, String value, boolean not){
		this.attributeNamespace = namespace;
		this.attributeName = localPart;
		this.attributeValue = value;
		this.attributeValueNot = not;
	}
	public void setAttribute( String localPart, String value, boolean not){
		this.attributeName = localPart;
		this.attributeValue = value;
		this.attributeValueNot = not;
	}

	public void setAllTextBelow(boolean allTextBelow) {
		this.allTextBelow = allTextBelow;
	}


	protected static void logXpathPosition(LinkedList<QName> xpathPosition, String message){
		String xpathString = "";
		for (QName xpath: xpathPosition){
			xpathString +="/" + xpath.getLocalPart();
		}
		System.out.println(xpathString + ": "+message);
	}
	
	@Override
	public final void processCharacters(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		if (match){
			internalProcessCharacters(xmlReader);
		}
		
	}
	protected void internalProcessCharacters(XMLStreamReader xmlReader) throws Exception {
		writeContent(xmlReader.getText());		
	}
	@Override
	public final void processStartElement(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		if (relative){
			LinkedList<QName> xpathPositionTemp = new LinkedList<QName>();
			QName first = xpathQuery.getFirst();
			boolean found = false;
			for (QName xpathItem: xpathPosition){
				if (found){
					xpathPositionTemp.add(xpathItem);
				}else if (first.equals(xpathItem)){
					xpathPositionTemp.add(xpathItem);
					found = true;
				}
			}
			internalProcessStartElement(xpathPositionTemp,xmlReader);
		}else {
			internalProcessStartElement(xpathPosition,xmlReader);
		}
	}
	@Override
	public final void processEndElement(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		if (relative){
			LinkedList<QName> xpathPositionTemp = new LinkedList<QName>();
			QName first = xpathQuery.getFirst();
			boolean found = false;
			for (QName xpathItem: xpathPosition){
				if (found){
					xpathPositionTemp.add(xpathItem);
				}else if (first.equals(xpathItem)){
					xpathPositionTemp.add(xpathItem);
					found = true;
				}
			}
			internalProcessEndElement(xpathPositionTemp,xmlReader);
		}else {
			internalProcessEndElement(xpathPosition, xmlReader);
		}
	}
	private void internalProcessStartElement(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		int xpathPositionSize = xpathPosition.size();
		boolean equalXpathSize = xpathPositionSize == xpathQuery.getXpathQuerySize();
		if (equalXpathSize){
			boolean elementMatch = true;
			/*
			 * element match
			 */
			elementMatch = xpathQuery.match(xpathPosition);
			/*
			 * attribute match
			 */
			if (elementMatch){
				if (StringUtils.isNotBlank(attributeName) && StringUtils.isNotBlank(attributeValue)){
					String namespace = null;
					if (StringUtils.isNotBlank(attributeNamespace)){
						namespace = attributeNamespace;
					}
					String value = xmlReader.getAttributeValue(namespace, attributeName);
					if (attributeValueNot){
						elementMatch = !attributeValue.equals(value);
					}else {
						elementMatch = attributeValue.equals(value);
					}
					
				}
			}
			/*
			 * position match
			 */
			if (elementMatch){
				position++;
				if (onlyFirst && position > 1){
					elementMatch = false;
				}
			}

			xpathElementMatch = elementMatch;
			match = elementMatch;
			if (elementMatch){
				processExactStartElementMatch(xmlReader);
			}
		}else {
			boolean xpathLarger = xpathPositionSize > xpathQuery.getXpathQuerySize();
			if (isAllTextBelow() && xpathLarger && xpathElementMatch){
				match = true;
				processChildStartElementMatch(xmlReader);
			}else {
				match = false;
			}
		}
	}

	public void processChildStartElementMatch(XMLStreamReader xmlReader)throws Exception {
		
	}
	public void processChildEndElementMatch(XMLStreamReader xmlReader)throws Exception {
		
	}
	protected static String removeUnusedCharacters(String input){
		if (input != null){
			String result = input.replaceAll("[\t ]+", " ");
			result = result.replaceAll("[\n\r]+", NO_SEPARATOR);
			return result;
		}else {
			return null;
		}
		
	}

	private void internalProcessEndElement(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		boolean pastMatch = xpathElementMatch;
		int xpathPositionSize = xpathPosition.size();
		int nextXpathPositionSize = xpathPositionSize -1;
		
		boolean equalXpathSize = xpathPositionSize == xpathQuery.getXpathQuerySize();
		if (equalXpathSize && xpathElementMatch){
			xpathElementMatch = false;
			match = false;
		}else if (match && nextXpathPositionSize > xpathQuery.getXpathQuerySize()){
			processChildEndElementMatch(xmlReader);
		}else if (nextXpathPositionSize == xpathQuery.getXpathQuerySize()){

			/*
			 * element match
			 */
			LinkedList<QName> newXPathPosition = new LinkedList<QName>();
			for (int i =0; i < xpathPositionSize -1;i++){
				newXPathPosition.add(xpathPosition.get(i));
			}
			match = xpathQuery.match(newXPathPosition);
			if (match){
				processChildEndElementMatch(xmlReader);
			}
		}else {
			match = false;
		}
		if (pastMatch && !xpathElementMatch){
			processExactEndElementMatch();
		}
	}

	protected void processExactStartElementMatch(XMLStreamReader xmlReader){
		
		
	}
	protected void processExactEndElementMatch(){
		
	}
	protected void writeContent(String content){
		
	}


	protected abstract void clear();

	static class XpathQuery {
		private LinkedList<QName> xpathQueryList = new LinkedList<QName>();
		private List<QName> lastElement = new ArrayList<QName>();
		private int xpathQuerySize = 0;
		private boolean not = false;
		public XpathQuery(String defaultNamespace, String[] xpathQueryArray) {
			for (int i = 0; i < xpathQueryArray.length;i++){
				if (i < xpathQueryArray.length-1){
					this.xpathQueryList.add(new QName(defaultNamespace, xpathQueryArray[i]));
				}else {
					String lastItem = xpathQueryArray[i];
					if (lastItem.startsWith("not(")){
						not = true;
						lastItem = lastItem.trim().substring(4, lastItem.length()-1);
					}
					String[] splitted = lastItem.split("\\|");
					for (String item: splitted){
						this.lastElement.add(new QName(defaultNamespace, item.trim()));
					}
				}
			}
			this.xpathQuerySize = this.xpathQueryList.size();
		}
		protected QName getFirst(){
			if (xpathQueryList.size() > 0){
				return xpathQueryList.getFirst();
			}else {
				return lastElement.get(0);
			}
		}

		protected boolean match(LinkedList<QName> xpathPosition){
			int xpathPositionSize = xpathPosition.size();
			boolean equalXpathSize = xpathPositionSize == getXpathQuerySize();
			if (equalXpathSize){
				boolean elementMatch = true;
				for (int current =0; elementMatch && current< xpathPositionSize;current++){
					if (xpathPositionSize > current){
						if (current == xpathQuerySize){
							boolean or = false;
							for (QName item: lastElement){
								or = or || item.equals(xpathPosition.get(current));
							}
							if (not){
								or = !or;
							}
							elementMatch = elementMatch && or;
						}else {
							elementMatch = elementMatch && xpathQueryList.get(current).equals(xpathPosition.get(current));
						}
					}
				}
				return elementMatch;
			}else{
				return false;
			}
		}
		protected int getXpathQuerySize(){
			return xpathQuerySize+1;
		}
	}

	public void setRelative(boolean relative) {
		this.relative = relative;
	}



	public boolean isAllTextBelow() {
		return allTextBelow;
	}
	protected String convertToString(Collection<String> results, int start, String separator){
		if (results.size() == 0){
			return null;
		}
		StringBuilder builder = new StringBuilder();
		int i = 0;
		Iterator<String> iterator = results.iterator();
		while (iterator.hasNext()){
			String result = iterator.next();
			if (i >= start){
				builder.append(result + separator);
			}
			i++;
		}
		return convertEmptyStringToNull(builder.toString());
	}
	protected String convertEmptyStringToNull(String string){
		if (StringUtils.isBlank(string)){
			return null;
		}else {
			return string.trim();
		}
	}
}
