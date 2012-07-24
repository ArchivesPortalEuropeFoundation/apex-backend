package eu.apenet.solr.stopwords.writer;

import java.io.IOException;
import java.io.Writer;

import eu.apenet.solr.stopwords.Stopword;

public class SolrStopwordsWriter extends AbstractWriter{

	@Override
	protected void addHeaderLine(Writer writer) throws IOException {
		writer.append("#APEnet stopword list\n");
		
	}

	@Override
	protected void addStopwordLine(Writer writer, Stopword stopword) throws IOException {
		if (!stopword.isForbidden()){
			writer.append(stopword.getValue());
			writer.append("\n");			
		}
		
	}

}
