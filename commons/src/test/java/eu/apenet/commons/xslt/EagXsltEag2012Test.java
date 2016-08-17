package eu.apenet.commons.xslt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import net.sf.saxon.s9api.SaxonApiException;

public class EagXsltEag2012Test {

    public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, NoSuchAlgorithmException, SaxonApiException {
        convert(System.getProperty("user.home") + "/workspace_branch/portal-project-1.1.3-EAG2012/commons/src/test/resources/APE_EAD_eag_35.xml");
    }

    public static void convert(String eagPath) throws SaxonApiException, FileNotFoundException {
        long startTime = System.currentTimeMillis();

        File eagPathFile = new File(eagPath);
        File destinationFile = new File(eagPathFile.getParentFile(), eagPathFile.getName() + "_convertedToEAG2012.xml");
        PrintWriter writer = new PrintWriter(destinationFile);

        TestResourceBundleSource resourceBundleSource = new TestResourceBundleSource();
        long xslTreeStartTime = System.currentTimeMillis();
        EagXslt.convertAi(writer, eagPathFile, resourceBundleSource);

        System.out.println("Duration (xslt(" + eagPathFile.getName() + "): " + (System.currentTimeMillis() - startTime) + " - " + +(System.currentTimeMillis() - xslTreeStartTime));
    }
}
