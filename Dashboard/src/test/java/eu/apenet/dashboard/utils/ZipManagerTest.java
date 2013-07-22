package eu.apenet.dashboard.utils;

import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import eu.apenet.commons.utils.APEnetUtilities;

/**
 * User: Yoann Moranville
 * Date: Sep 6, 2010
 *
 * @author Yoann Moranville
 */
@Ignore
public class ZipManagerTest {

    private ZipManager zipManager;
    private String path;

    @Before
    public void doBefore(){
        zipManager = new ZipManager();

        URL url = ZipManagerTest.class.getResource("/zip.zip");
        path = url.getPath();
    }

    @Test
    public void A_unzip(){
        zipManager.unzip(path);
    }

    @After
    @Deprecated
    public void eraseUnzippedFiles(){
        new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + "untitled.txt").delete();
        new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + "untitled2.txt").delete();
    }

}
