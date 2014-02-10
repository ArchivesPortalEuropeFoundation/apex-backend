package eu.apenet.commons.xslt;

import java.io.Writer;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

import org.apache.log4j.Logger;

import eu.apenet.commons.ResourceBundleSource;
import eu.apenet.commons.solr.eads.EadSolrField;
import eu.apenet.commons.xslt.extensions.EadidCheckerExtension;
import eu.apenet.commons.xslt.extensions.HighlighterExtension;
import eu.apenet.commons.xslt.extensions.ResourcebundleExtension;

public final class EadXslt {
    private static final Logger LOG = Logger.getLogger(EadXslt.class);

    
	private static XsltExecutable getXsltExecutable(String xslUrl, String searchTerms, List<EadSolrField> highlightFields, ResourceBundleSource resourceBundleSource, Integer aiId, boolean isPreview, String solrStopwordsUrl) throws SaxonApiException{
        ClassLoader classLoader = (ClassLoader) Thread.currentThread().getContextClassLoader();
        Source xsltSource = new StreamSource(classLoader.getResourceAsStream(xslUrl));	
        Processor processor = new Processor(false);
        HighlighterExtension highLighter = new HighlighterExtension (solrStopwordsUrl, searchTerms, highlightFields);
        ResourcebundleExtension  resourcebundleRetriever = new ResourcebundleExtension (resourceBundleSource);
        EadidCheckerExtension  eadidChecker = new EadidCheckerExtension (aiId, isPreview);
        processor.registerExtensionFunction(highLighter);
        processor.registerExtensionFunction(resourcebundleRetriever);
        processor.registerExtensionFunction(eadidChecker);
        XsltCompiler compiler = processor.newXsltCompiler();
        compiler.setURIResolver(new ClasspathURIResolver(xslUrl));
        return compiler.compile(xsltSource);
	}

    public static void convertEadToHtml(String xslUrl, Writer writer, Source xmlSource, String searchTerms, List<EadSolrField> highlightFields, ResourceBundleSource resourceBundleSource,String secondDisplayUrl, Integer aiId,boolean isPreview, String solrStopwordsUrl) throws SaxonApiException{
    	XsltExecutable executable = getXsltExecutable(xslUrl, searchTerms, highlightFields,resourceBundleSource, aiId, isPreview, solrStopwordsUrl);
        XsltTransformer transformer = executable.load();
        transformer.setParameter(new QName("eadcontent.extref.prefix"), new XdmAtomicValue(secondDisplayUrl));
        transformer.setSource(xmlSource);
        Serializer serializer = new Serializer();
        serializer.setOutputWriter(writer);
        transformer.setDestination(serializer);
        transformer.transform();
    }
    
}
