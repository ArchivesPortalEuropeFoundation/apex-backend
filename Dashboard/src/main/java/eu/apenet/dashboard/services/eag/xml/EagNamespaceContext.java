package eu.apenet.dashboard.services.eag.xml;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;

/**
 *
 * @author bverhoef
 */
public class EagNamespaceContext implements NamespaceContext {
    public String getNamespaceURI(String prefix) {
		if ("xlink".equals(prefix)) {
            return "http://www.w3.org/1999/xlink";
        }
        return "http://www.archivesportaleurope.net/Portal/profiles/eag_2012/";
    }

    // This method isn't necessary for XPath processing.
    public String getPrefix(String uri) {
        throw new UnsupportedOperationException();
    }

    // This method isn't necessary for XPath processing either.
    public Iterator<?> getPrefixes(String uri) {
        throw new UnsupportedOperationException();
    }


}
