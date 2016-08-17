package eu.apenet.commons.xslt;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

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
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.xslt.extensions.ResourcebundleExtension;
import eu.apenet.commons.xslt.extensions.RetrieveRelatedAIIdExtension;

public class EagXslt {

    private static Logger LOGGER = Logger.getLogger(EagXslt.class);

    private static XsltExecutable getXsltExecutable(String xslUrl, ResourceBundleSource resourceBundleSource, String requiredAIRepositorCode) throws SaxonApiException {
        ClassLoader classLoader = (ClassLoader) Thread.currentThread().getContextClassLoader();
        Source xsltSource = new StreamSource(classLoader.getResourceAsStream(xslUrl));
        Processor processor = new Processor(false);
        ResourcebundleExtension resourcebundleExtension = new ResourcebundleExtension(resourceBundleSource);
        processor.registerExtensionFunction(resourcebundleExtension);
        RetrieveRelatedAIIdExtension relatedAIIdExtension = new RetrieveRelatedAIIdExtension(requiredAIRepositorCode);
        processor.registerExtensionFunction(relatedAIIdExtension);
        XsltCompiler compiler = processor.newXsltCompiler();
        compiler.setURIResolver(new ClasspathURIResolver(xslUrl));

        return compiler.compile(xsltSource);
    }

    public static void displayAiDetails(boolean preview, Writer writer, File xmlFile, ResourceBundleSource resourceBundleSource, String currentAIRepositorCode, String requiredAIRepositorCode) throws SaxonApiException {
        String language = resourceBundleSource.getLocale().getLanguage().toLowerCase();
        String languageIso3 = "eng";
        Map<String, String> langMap = APEnetUtilities.getIso2ToIso3LanguageCodesMap();

        //recover the iso3 language 
        if (langMap.get(language) != null && !langMap.get(language).isEmpty()) {
            languageIso3 = langMap.get(language);
        }
        displayAiDetails(preview, writer, xmlFile, resourceBundleSource, languageIso3, currentAIRepositorCode, requiredAIRepositorCode);
    }

    public static void displayAiDetails(boolean preview, Writer writer, File xmlFile, ResourceBundleSource resourceBundleSource, String language, String currentAIRepositorCode, String requiredAIRepositorCode) throws SaxonApiException {
        if (xmlFile.exists()) {
            Source xmlSource = new StreamSource(xmlFile);
            String xslt = "xsl/eag/aidetails.xsl";
            if (preview) {
                xslt = "xsl/eag/aidetails-preview.xsl";
            }
            XsltExecutable executable = getXsltExecutable(xslt, resourceBundleSource, requiredAIRepositorCode);
            XsltTransformer transformer = executable.load();
            transformer.setParameter(new QName("language.selected"), new XdmAtomicValue(language));
            transformer.setSource(xmlSource);
            Serializer serializer = new Serializer();
            serializer.setOutputWriter(writer);
            transformer.setDestination(serializer);
            transformer.transform();
        } else {
            try {
                LOGGER.error("No file: " + xmlFile.getCanonicalPath());
            } catch (IOException e) {
            }
        }
    }

    public static void convertAi(Writer writer, File eagPathFile, ResourceBundleSource resourceBundleSource) throws SaxonApiException {
        Source xmlSource = new StreamSource(eagPathFile);
        XsltExecutable executable = getXsltExecutable("xsl/eag/eag2eag2012.xsl", resourceBundleSource, null);
        XsltTransformer transformer = executable.load();
        transformer.setSource(xmlSource);
        Serializer serializer = new Serializer();
        serializer.setOutputWriter(writer);
        transformer.setDestination(serializer);
        transformer.transform();
    }
}
