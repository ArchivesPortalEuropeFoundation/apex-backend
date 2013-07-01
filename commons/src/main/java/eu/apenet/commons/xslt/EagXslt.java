package eu.apenet.commons.xslt;

import java.io.File;
import java.io.Writer;

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
import eu.apenet.commons.ResourceBundleSource;
import eu.apenet.commons.xslt.extensions.ResourcebundleExtension;
import eu.apenet.commons.xslt.extensions.RetrieveRelatedAIIdExtension;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Lang;

public class EagXslt {
	private static XsltExecutable getXsltExecutable(String xslUrl, ResourceBundleSource resourceBundleSource, String currentAIRepositorCode, String requiredAIRepositorCode) throws SaxonApiException{
        ClassLoader classLoader = (ClassLoader) Thread.currentThread().getContextClassLoader();
        Source xsltSource = new StreamSource(classLoader.getResourceAsStream(xslUrl));	
        Processor processor = new Processor(false);
        ResourcebundleExtension resourcebundleExtension = new ResourcebundleExtension(resourceBundleSource);
        processor.registerExtensionFunction(resourcebundleExtension);
        RetrieveRelatedAIIdExtension relatedAIIdExtension = new RetrieveRelatedAIIdExtension(currentAIRepositorCode, requiredAIRepositorCode);
        processor.registerExtensionFunction(relatedAIIdExtension);
        XsltCompiler compiler = processor.newXsltCompiler();
        
        return compiler.compile(xsltSource);
	}
	@Deprecated
    public static void displayAiDetails(Writer writer, File xmlFile, ResourceBundleSource resourceBundleSource, String currentAIRepositorCode, String requiredAIRepositorCode) throws SaxonApiException{
		String language = resourceBundleSource.getLocale().getLanguage();
		String languageIso3 = "eng";
		Lang lang = DAOFactory.instance().getLangDAO().getLangByIso2Name(language);
		if (lang != null){
			languageIso3 = lang.getIsoname().toLowerCase();
		}
		displayAiDetails(writer, xmlFile, resourceBundleSource, languageIso3, currentAIRepositorCode, requiredAIRepositorCode);
    }
    public static void displayAiDetails(Writer writer, File xmlFile, ResourceBundleSource resourceBundleSource, String language, String currentAIRepositorCode, String requiredAIRepositorCode) throws SaxonApiException{
		Source xmlSource = new StreamSource(xmlFile);
    	XsltExecutable executable = getXsltExecutable("xsl/eag/aidetails.xsl", resourceBundleSource, currentAIRepositorCode, requiredAIRepositorCode);
        XsltTransformer transformer = executable.load();
        transformer.setParameter(new QName("language.selected"), new XdmAtomicValue(language));
        transformer.setSource(xmlSource);
        Serializer serializer = new Serializer();
        serializer.setOutputWriter(writer);
        transformer.setDestination(serializer);
        transformer.transform();
    }

	public static void convertAi(Writer writer, File eagPathFile, ResourceBundleSource resourceBundleSource) throws SaxonApiException {
		Source xmlSource = new StreamSource(eagPathFile);
    	XsltExecutable executable = getXsltExecutable("xsl/eag/eag2eag2012.xsl", resourceBundleSource, null, null);
        XsltTransformer transformer = executable.load();
        transformer.setSource(xmlSource);
        Serializer serializer = new Serializer();
        serializer.setOutputWriter(writer);
        transformer.setDestination(serializer);
        transformer.transform();
	}
}
