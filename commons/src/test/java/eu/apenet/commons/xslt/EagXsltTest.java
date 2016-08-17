package eu.apenet.commons.xslt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import net.sf.saxon.s9api.SaxonApiException;

public class EagXsltTest {

    public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, NoSuchAlgorithmException, SaxonApiException {
        convert("commons/src/test/resources/APE_EAD_eag_35.xml");
        convert("commons/src/test/resources/EAG2012_NL-HaNA.xml");
    }

    public static void convert(String eagPath) throws SaxonApiException, FileNotFoundException {
        long startTime = System.currentTimeMillis();

        File eagPathFile = new File(eagPath);
        File destinationFile = new File(eagPathFile.getParentFile(), eagPathFile.getName() + ".html");
        PrintWriter writer = new PrintWriter(destinationFile);

        TestResourceBundleSource resourceBundleSource = new TestResourceBundleSource();
        long xslTreeStartTime = System.currentTimeMillis();
        EagXslt.displayAiDetails(false, writer, eagPathFile, resourceBundleSource, null, null);

        System.out.println("Duration (xslt(" + eagPathFile.getName() + "): " + (System.currentTimeMillis() - startTime) + " - " + +(System.currentTimeMillis() - xslTreeStartTime));
    }
}
