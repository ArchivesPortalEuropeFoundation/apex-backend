package eu.apenet.commons.xslt;

import java.io.File;

import java.io.IOException;
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
import eu.apenet.commons.solr.SolrField;
import eu.apenet.commons.xslt.extensions.EadidCheckerExtension;
import eu.apenet.commons.xslt.extensions.HighlighterExtension;
import eu.apenet.commons.xslt.extensions.ResourcebundleExtension;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Lang;


public final class EacXslt {
    private static final Logger LOG = Logger.getLogger(EacXslt.class);

    
	private static XsltExecutable getXsltExecutable(String xslUrl, String searchTerms, List<SolrField> highlightFields, ResourceBundleSource resourceBundleSource, Integer aiId, boolean isPreview, String solrStopwordsUrl) throws SaxonApiException{
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

    public static void convertEacToHtml(String xslUrl, Writer writer, Source xmlSource, String searchTerms, List<SolrField> highlightFields, ResourceBundleSource resourceBundleSource,String secondDisplayUrl, Integer aiId,boolean isPreview, String solrStopwordsUrl) throws SaxonApiException{
		String language = resourceBundleSource.getLocale().getLanguage();
		String languageIso3 = "eng";
		Lang lang = DAOFactory.instance().getLangDAO().getLangByIso2Name(language);
		if (lang != null){
			languageIso3 = lang.getIsoname().toLowerCase();
		}
		convertEacToHtml(xslUrl, writer, xmlSource, searchTerms, highlightFields, resourceBundleSource, languageIso3, secondDisplayUrl, aiId, isPreview, solrStopwordsUrl);
    }

    public static void convertEacToHtml(String xslUrl, Writer writer, Source xmlSource, String searchTerms, List<SolrField> highlightFields, ResourceBundleSource resourceBundleSource, String language, String secondDisplayUrl, Integer aiId,boolean isPreview, String solrStopwordsUrl) throws SaxonApiException{
    	XsltExecutable executable = getXsltExecutable(xslUrl, searchTerms, highlightFields,resourceBundleSource, aiId, isPreview, solrStopwordsUrl);
        XsltTransformer transformer = executable.load();
        transformer.setParameter(new QName("eaccontent.extref.prefix"), new XdmAtomicValue(secondDisplayUrl));
        transformer.setParameter(new QName("language.selected"), new XdmAtomicValue(language));
        transformer.setSource(xmlSource);
        Serializer serializer = new Serializer();
        serializer.setOutputWriter(writer);
        transformer.setDestination(serializer);
        transformer.transform();
    }
    
}
