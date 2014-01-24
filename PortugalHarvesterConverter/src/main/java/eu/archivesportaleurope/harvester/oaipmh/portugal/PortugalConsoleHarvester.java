package eu.archivesportaleurope.harvester.oaipmh.portugal;

import eu.archivesportaleurope.harvester.oaipmh.ConsoleHarvester;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.util.*;

/**
 * User: yoannmoranville
 * Date: 22/01/14
 *
 * @author yoannmoranville
 */
public class PortugalConsoleHarvester {
    private final static Logger LOG = Logger.getLogger(PortugalConsoleHarvester.class);

    public static void main(String[] args) {
        ConsoleHarvester consoleHarvester = new ConsoleHarvester(null, null);
        //1. Harvest files
        ConsoleHarvester.main(args);
        String set = consoleHarvester.getSet();

        //2. Check parameters
        Map<String, String> parameters = ConsoleHarvester.getParameters(args);
        String baseDirString = ".";
        if (parameters.containsKey(ConsoleHarvester.BASE_DIR_PARAMETER)) {
            baseDirString = parameters.get(ConsoleHarvester.BASE_DIR_PARAMETER);
        }
        File dataDir = new File(baseDirString, "data");


        //2. Use the DC files to import in DB
        File resultDir = getDataDir(dataDir);
        if(resultDir.getName().equals("DONE")) {
            resultDir = resultDir.getParentFile();
        }
        File delete = new File(resultDir, "DONE");
        if(delete.exists())
            delete.delete();

        HarvesterConverter converter = new HarvesterConverter(resultDir);
        try {
            converter.dublinCoreToEad();
            int size = converter.dbToEad("PT-" + set);
            LOG.info(size + " EADs were created!");
        } catch (Exception e) {
            LOG.error("Error...", e);
            throw new RuntimeException(e);
        }
    }

    private static File getDataDir(File presumedDataDir) {
        final File[] dirs = presumedDataDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory() && !file.getName().equals("errors");
            }
        });

        if(dirs == null || dirs.length == 0) {
            return presumedDataDir;
        }
        return getDataDir(dirs[0]);
    }

}
