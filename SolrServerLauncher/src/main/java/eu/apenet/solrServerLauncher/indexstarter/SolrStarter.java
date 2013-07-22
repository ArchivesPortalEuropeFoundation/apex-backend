package eu.apenet.solrServerLauncher.indexstarter;

import eu.apenet.commons.exceptions.APEnetRuntimeException;
import eu.apenet.commons.listener.APEnetConfigListener;
import eu.apenet.commons.listener.ApePortalAndDashboardConfigListener;
import org.apache.log4j.Logger;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SolrStarter {
    private static final Logger log = Logger.getLogger(SolrStarter.class);
    public static final int PORT = 8083;

    public static void main(String... args) throws Exception {
        if(args.length != 2)
            throw new APEnetRuntimeException("No argument specified. You have to specify the path to your own web override xml file in the pom file and the apache solr version");
        else
            new SolrStarter().startServer(args[0], args[1]);
    }

    public void startServer(String webXmlPath, String solrVersion) throws Exception {
        File presumedRoot = new File(".").getCanonicalFile();
        
        String root = getProjectRoot(presumedRoot, "persistence", "Dashboard");
        System.setProperty("solr.solr.home", root + "/SolrServerLauncher/src/test/resources/solr");

        WebAppContext webAppContext = new WebAppContext(root + "/SolrServerLauncher/src/test/resources/solr/apache-solr-" + solrVersion + ".war", "/solr");

        webAppContext.setOverrideDescriptor(root + webXmlPath);
        webAppContext.addEventListener(new ApePortalAndDashboardConfigListener());

        Server server = new Server(PORT);
        server.addHandler(webAppContext);
        server.start();
    }

    private static String getProjectRoot(File presumedRoot, String... moduleNames) throws Exception {
        final File[] dirs = presumedRoot.listFiles(new FileFilter(){
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        List<String> dirNames = new ArrayList<String>();
        for(File dir : dirs)
            dirNames.add(dir.getName());

        for(String moduleName : moduleNames){
            if(!dirNames.contains(moduleName)){
                log.error(presumedRoot.getAbsolutePath() + " is not the main root of the project, trying with its parent.");
                return getProjectRoot(presumedRoot.getParentFile(), moduleNames);
            }
        }
        return presumedRoot.getAbsolutePath();
    }
}
