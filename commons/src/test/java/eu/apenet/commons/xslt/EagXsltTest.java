package eu.apenet.commons.xslt;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.NoSuchAlgorithmException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

import org.junit.Before;
import org.junit.Test;

import eu.apenet.commons.ResourceBundleSource;
import eu.apenet.commons.xslt.extensions.ResourcebundleExtension;

public class EagXsltTest {
	private static XsltExecutable getXsltExecutable(String xslUrl, ResourceBundleSource resourceBundleSource) throws SaxonApiException{
		File xsltFile = new File("/home/bastiaan/apex/apexv1/workspace/portal-project/commons/src/main/resources/xsl/eag/aidetails.xsl");
		Source xsltSource = new StreamSource(xsltFile);	
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
	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, NoSuchAlgorithmException, SaxonApiException {
		convert("/home/bastiaan/eag/APE_EAG_APEnet_EAG_eag_53.xml" );
		convert("/home/bastiaan/eag/DE-BA.xml" );
		convert("/home/bastiaan/eag/FR-SIAF.xml" );
		convert("/home/bastiaan/eag/NL-HaNA.xml" );
	}
	public static void convert(String eagPath) throws SaxonApiException, FileNotFoundException{
		long startTime = System.currentTimeMillis();

		File eagPathFile = new File(eagPath);
		File destinationFile = new File(eagPathFile.getParentFile(), eagPathFile.getName()+ ".html");
		PrintWriter writer = new PrintWriter(destinationFile);

		TestResourceBundleSource resourceBundleSource = new TestResourceBundleSource();	
		long xslTreeStartTime = System.currentTimeMillis();
		displayAiDetails(writer, eagPathFile, resourceBundleSource);
	
		System.out.println("Duration (xslt(" + eagPathFile.getName() + "): "  + (System.currentTimeMillis()-startTime) + " - " +   + (System.currentTimeMillis()-xslTreeStartTime));
	}
}
