package eu.archivesportaleurope.harvester.oaipmh.portugal;

import java.io.File;
import java.io.FileFilter;
import java.util.Properties;

import org.apache.log4j.Logger;

import eu.archivesportaleurope.harvester.oaipmh.ConsoleHarvester;

/**
 * User: yoannmoranville
 * Date: 22/01/14
 *
 * @author yoannmoranville
 */
public class PortugalConsoleHarvester extends ConsoleHarvester{
    private final static Logger LOG = Logger.getLogger(PortugalConsoleHarvester.class);


    public PortugalConsoleHarvester(File dataDir, Properties properties) {
		super(dataDir, properties);
	}

	@Override
	public void start() {
		super.start();
        String set = getSet();
//        String set = "AMCTC";

        //2. Use the DC files to import in DB
        File resultDir = getOutputDir();
//        File resultDir = getDataDir(getDataDir());
        if(resultDir.getName().equals("DONE")) {
            resultDir = resultDir.getParentFile();
        }
        File delete = new File(resultDir, "DONE");
        if(delete.exists())
            delete.delete();

        long start = System.currentTimeMillis();
        HarvesterConverter converter = new HarvesterConverter(resultDir);
        try {
            converter.dublinCoreToEad();
            int size = converter.dbToEadNew("PT-" + set);
            LOG.info(size + " EADs were created!");
            long end = System.currentTimeMillis();
            LOG.info("===============================================");
            super.calcHMS(end, start);
            LOG.info("===============================================");
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
