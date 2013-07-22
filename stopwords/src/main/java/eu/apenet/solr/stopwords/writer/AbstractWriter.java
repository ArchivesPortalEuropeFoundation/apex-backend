package eu.apenet.solr.stopwords.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collection;

import eu.apenet.solr.stopwords.Stopword;

public abstract class AbstractWriter {

	public void write(File outputFile, Collection<Stopword> stopwords) throws IOException {
		Writer writer = new OutputStreamWriter(new FileOutputStream(outputFile), Charset.forName("UTF-8"));
		addHeaderLine(writer);
		for (Stopword stopword: stopwords){
			addStopwordLine(writer, stopword);
		}
		writer.flush();
		writer.close();
		
	}
	protected abstract void addHeaderLine(Writer writer)  throws IOException ;
	
	protected abstract void addStopwordLine(Writer writer, Stopword stopword) throws IOException ;
}
