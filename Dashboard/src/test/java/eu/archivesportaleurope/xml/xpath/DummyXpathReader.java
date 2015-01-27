package eu.archivesportaleurope.xml.xpath;

import java.util.LinkedList;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

public class DummyXpathReader implements XpathReader<Object> {

	boolean inited = false;
	private int numberOfStartElements = 0;
	private int numberOfEndElements = 0;
	private int numberOfCharacters = 0;
	@Override
	public void processCharacters(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		if (inited){
			numberOfCharacters++;
		}
		
	}

	@Override
	public void processStartElement(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		if (inited){
			numberOfStartElements++;
		}
	}

	@Override
	public void processEndElement(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		if (inited){
			numberOfEndElements++;
		}
	}

	@Override
	public void init() throws Exception {
		inited=true;
	}

	@Override
	public Object getData() {
		return null;
	}

	public boolean isInited() {
		return inited;
	}

	public int getNumberOfStartElements() {
		return numberOfStartElements;
	}

	public int getNumberOfEndElements() {
		return numberOfEndElements;
	}

	public int getNumberOfCharacters() {
		return numberOfCharacters;
	}

}
