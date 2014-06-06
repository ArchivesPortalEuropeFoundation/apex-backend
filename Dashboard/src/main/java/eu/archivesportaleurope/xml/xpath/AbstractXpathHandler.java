package eu.archivesportaleurope.xml.xpath;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractXpathHandler implements XmlStreamHandler {

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


	private static void logXpathPosition(LinkedList<QName> xpathPosition, String message){
		String xpathString = "";
		for (QName xpath: xpathPosition){
			xpathString +="/" + xpath.getLocalPart();
		}
		System.out.println(xpathString + ": "+message);
	}
	
	@Override
	public void processCharacters(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		if (match){
			writeContent(xmlReader.getText());		
		}
		
	}
	@Override
	public void processStartElement(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
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
			if (allTextBelow && xpathLarger && xpathElementMatch){
				match = true;
			}else {
				match = false;
			}
		}
		
	}

	
	@Override
	public void processEndElement(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		boolean pastMatch = match;
		int xpathPositionSize = xpathPosition.size();
		int nextXpathPositionSize = xpathPositionSize -1;
		
		boolean equalXpathSize = xpathPositionSize == xpathQuery.getXpathQuerySize();
		if (equalXpathSize && xpathElementMatch){
			xpathElementMatch = false;
			match = false;
			
		}else if (match && nextXpathPositionSize > xpathQuery.getXpathQuerySize()){
			
		}else if (nextXpathPositionSize == xpathQuery.getXpathQuerySize()){

			/*
			 * element match
			 */
			LinkedList<QName> newXPathPosition = new LinkedList<QName>();
			for (int i =0; i < xpathPositionSize -1;i++){
				newXPathPosition.add(xpathPosition.get(i));
			}
			match = xpathQuery.match(newXPathPosition);
		}else {
			match = false;
		}
		if (pastMatch && !match){
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
}
