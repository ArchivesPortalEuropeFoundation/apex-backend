package eu.apenet.dashboard.services.eaccpf.publish;

import java.io.File;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.archivesportaleurope.commons.config.DashboardConfig;

public class TestClass {

	public static void main(String[] args) throws Exception{
		DashboardConfig config = new DashboardConfig();
		config.setSolrIndexUrl("http://localhost:8080/solr/eac-cpfs");
		config.finalizeConfigPhase();
		APEnetUtilities.setConfig(config);
		SolrPublisher solrPublisher = new SolrPublisher();
		File dir = new File("/home/bverhoef/Downloads/EAC-CPF");
		parse(dir, solrPublisher);
		solrPublisher.commitSolrDocuments();

	}
	public static void parse(File file ,SolrPublisher solrPublisher) throws Exception{
		if (file.isDirectory() && !file.getName().equalsIgnoreCase("hungary")){
			for (File child : file.listFiles()){
				parse(child, solrPublisher);

			}
		}else if (file.getName().endsWith(".xml")){
			try {
				solrPublisher.parse(file);
			}catch (Exception e){
				System.out.println(file.getCanonicalPath()+ " " + e.getMessage());
			}
		}
	}
}
