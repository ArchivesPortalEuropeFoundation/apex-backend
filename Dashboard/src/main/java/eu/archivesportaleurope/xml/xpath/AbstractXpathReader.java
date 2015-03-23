package eu.archivesportaleurope.xml.xpath;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import eu.archivesportaleurope.xml.xpath.handler.XpathHandler;

/**
 * Abstract class for xpath and xml parsing.
 * 
 * @author Bastiaan Verhoef
 *
 * @param <T>
 */
public abstract class AbstractXpathReader<T> implements XpathReader<T> {

	/**
	 * Contains the the xpath handlers, that performs the reall xpath parsing.
	 */
	private List<XpathHandler> xpathHandlers = new ArrayList<XpathHandler>();
	/**
	 * Boolean if the class is initialized.
	 */
	private boolean initialized = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.archivesportaleurope.xml.xpath.XpathReader#init()
	 */
	@Override
	public final void init() throws Exception {
		internalInit();
		initialized = true;

	}

	/**
	 * Init to be overwritten by the subclasses.
	 * 
	 * @throws Exception
	 */
	protected abstract void internalInit() throws Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.archivesportaleurope.xml.xpath.XpathReader#processCharacters(java.
	 * util.LinkedList, javax.xml.stream.XMLStreamReader)
	 */
	@Override
	public void processCharacters(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		if (initialized) {
			for (XpathHandler handler : xpathHandlers) {
				handler.processCharacters(xpathPosition, xmlReader);
			}
		}
	}

	@Override
	public void processStartElement(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		if (initialized) {
			for (XpathHandler handler : xpathHandlers) {
				handler.processStartElement(xpathPosition, xmlReader);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.archivesportaleurope.xml.xpath.XpathReader#processEndElement(java.
	 * util.LinkedList, javax.xml.stream.XMLStreamReader)
	 */
	@Override
	public void processEndElement(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		if (initialized) {
			for (XpathHandler handler : xpathHandlers) {
				handler.processEndElement(xpathPosition, xmlReader);
			}
		}
	}

	/**
	 * Returns all the xpath handlers.
	 * 
	 * @return
	 */
	protected List<XpathHandler> getXpathHandlers() {
		return xpathHandlers;
	}

	@Override
	public T getData() throws Exception {
		return null;
	}

}
