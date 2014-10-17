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

    /**
     * To be used with VM option: -DconsoleHarvesterClassName=eu.archivesportaleurope.harvester.oaipmh.portugal.PortugalConsoleHarvester
     */
    @Override
    public void start() {
        //For harvesting and converting, all together from the start
        super.start();
        String set = getSet();
        File resultDir = getOutputDir();

        //For continuing a conversion that was stopped: Need to delete (or move) the other directories like "digitarq.adavr.arquivos.pt" so only one is left and is the one you want to continue converting
        //You need to change the set below so it is the correct one that you harvested - it will continue converting from when it was stopped
//        String set = "MABF";
//        File resultDir = getDataDir(getDataDir());

        if(resultDir.getName().equals("DONE")) {
            resultDir = resultDir.getParentFile();
        }

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
