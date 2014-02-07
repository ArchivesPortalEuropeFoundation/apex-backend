package eu.apenet.dashboard.services.eaccpf.xml;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;

/**
 *
 * @author bverhoef
 */
public class EacCpfNamespaceContext implements NamespaceContext {
    public String getNamespaceURI(String prefix) {
		if ("xlink".equals(prefix)) {
            return "http://www.w3.org/1999/xlink";
        }
        return "urn:isbn:1-931666-33-4";
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
