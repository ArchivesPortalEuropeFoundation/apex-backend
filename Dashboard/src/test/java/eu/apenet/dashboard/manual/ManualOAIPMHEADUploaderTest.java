package eu.apenet.dashboard.manual;

import eu.apenet.commons.utils.APEnetUtilities;
import org.junit.*;

import java.io.*;

/**
 * User: Yoann Moranville
 * Date: Aug 23, 2010
 *
 * @author Yoann Moranville
 */
public class ManualOAIPMHEADUploaderTest {
    private final String oaiServer = "http://archives.cantal.fr/oai_pmh.cgi";
    private final String oaiSet = "phototheque";
    private final String oaiFormat = "olac";
    private final String oaiTimeFrom = null;
    private final String oaiTimeTo = null;

    private String testFilePath;

    ManualOAIPMHEADUploader oaiUploader;
    OutputStream out;

//    @Before
    public void A_createUploader(){
        testFilePath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + "OAI_test.xml";
        oaiUploader = new ManualOAIPMHEADUploader(oaiServer, oaiFormat, oaiSet, oaiTimeFrom, oaiTimeTo);
        try {
            out =  new FileOutputStream(testFilePath);
            Assert.assertNotNull(out);
        } catch (FileNotFoundException e){
            Assert.fail();
        }
    }

//    @Test
    public void doNothing(){}

//    @After
    public void C_eraseTestFile(){
        Assert.assertTrue(new File(testFilePath).delete());
    }
}
