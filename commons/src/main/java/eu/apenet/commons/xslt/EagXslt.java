package eu.apenet.commons.xslt;

import java.io.File;
import java.io.Writer;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;
import eu.apenet.commons.ResourceBundleSource;
import eu.apenet.commons.xslt.extensions.ResourcebundleExtension;

public class EagXslt {
	private static XsltExecutable getXsltExecutable(String xslUrl, ResourceBundleSource resourceBundleSource) throws SaxonApiException{
        ClassLoader classLoader = (ClassLoader) Thread.currentThread().getContextClassLoader();
        Source xsltSource = new StreamSource(classLoader.getResourceAsStream(xslUrl));	
        Processor processor = new Processor(false);
        ResourcebundleExtension resourcebundleExtension = new ResourcebundleExtension(resourceBundleSource);
        processor.registerExtensionFunction(resourcebundleExtension);
        XsltCompiler compiler = processor.newXsltCompiler();
        return compiler.compile(xsltSource);
	}

    public static void displayAiDetails(Writer writer, File xmlFile, ResourceBundleSource resourceBundleSource) throws SaxonApiException{
		Source xmlSource = new StreamSource(xmlFile);
    	XsltExecutable executable = getXsltExecutable("xsl/eag/aidetails.xsl", resourceBundleSource);
        XsltTransformer transformer = executable.load();
        transformer.setSource(xmlSource);
        Serializer serializer = new Serializer();
        serializer.setOutputWriter(writer);
        transformer.setDestination(serializer);
        transformer.transform();
    }
}
