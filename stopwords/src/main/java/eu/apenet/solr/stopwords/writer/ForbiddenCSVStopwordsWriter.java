package eu.apenet.solr.stopwords.writer;

import java.io.IOException;
import java.io.Writer;

import eu.apenet.solr.stopwords.Stopword;

public class ForbiddenCSVStopwordsWriter extends AbstractWriter{

	private static final String SEPARATOR = ";";

	@Override
	protected void addHeaderLine(Writer writer) throws IOException {
		writer.append("Stopword"+ SEPARATOR + "Language" + SEPARATOR + "English translation"+ SEPARATOR + "Source" + SEPARATOR + "Reason"+ "\n");
		
	}

	@Override
	protected void addStopwordLine(Writer writer, Stopword stopword) throws IOException {
		if (stopword.isForbidden()){
			writer.append(stopword.getValue());
			writer.append(SEPARATOR + stopword.getLanguage() + SEPARATOR + stopword.getDescription()+ SEPARATOR + stopword.getForbiddenSource() +SEPARATOR + stopword.getForbiddenReason()+ "\n");
		}
		
	}

}
