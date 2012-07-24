package eu.apenet.solr.stopwords;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import eu.apenet.solr.stopwords.parser.Stopwords;
import eu.apenet.solr.stopwords.writer.CSVStopwordsWriter;
import eu.apenet.solr.stopwords.writer.ForbiddenCSVStopwordsWriter;
import eu.apenet.solr.stopwords.writer.SolrStopwordsWriter;

public class StopwordsIntegrator {
	private static final Logger LOGGER = Logger.getLogger(StopwordsIntegrator.class);
	private Map<String, Stopword> stopwordsByValue = new HashMap<String, Stopword>();

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args == null || args.length != 2) {
			System.err.println("Source and destination directory are required");
			System.exit(1);
		}
		try {
			StopwordsIntegrator integrator = new StopwordsIntegrator();
			integrator.generateNewStopwords(args[0], args[1]);
		}catch (Exception e){
			LOGGER.fatal(e.getMessage(), e);
			System.exit(1);
		}
	}

	public void generateNewStopwords(String sourceDirname, String targetDirname) throws IOException {
		File sourceDir = new File(sourceDirname);
		for (File file : sourceDir.listFiles()) {
			Stopwords stopwords = new Stopwords(file);
			LOGGER.info("read file: " + file.getName());
			for (Stopword stopword : stopwords) {
				if (stopwordsByValue.containsKey(stopword.getValue())) {
					stopwordsByValue.get(stopword.getValue()).merge(stopword);
				} else {
					stopwordsByValue.put(stopword.getValue(), stopword);
				}
			}
		}
		File targetDir = new File(targetDirname);
		File solrStopwordsFile = new File(targetDir, "stopwords.txt");
		new SolrStopwordsWriter().write(solrStopwordsFile, stopwordsByValue.values());
		File csvSolrStopwordsFile = new File(targetDir, "stopwords.csv");
		new CSVStopwordsWriter().write(csvSolrStopwordsFile, stopwordsByValue.values());		
		File forbiddenCsvSolrStopwordsFile = new File(targetDir, "stopwords-forbidden.csv");
		new ForbiddenCSVStopwordsWriter().write(forbiddenCsvSolrStopwordsFile, stopwordsByValue.values());	
		LOGGER.info("Number of stopwords: " + stopwordsByValue.size());

	}

}
