package org.oclc.oai.harvester.parser.record;

import java.io.File;

import javax.xml.namespace.QName;


public abstract class AbstractOaiPmhParser {
	protected static final String OAI_PMH = "http://www.openarchives.org/OAI/2.0/";
	protected static final QName RECORD = new QName(OAI_PMH, "record");
	protected static final QName METADATA = new QName(OAI_PMH, "metadata");
	protected static final String UTF8 = "UTF-8";
	private File outputDirectory;
	public AbstractOaiPmhParser(File outputDirectory){
		this.outputDirectory = outputDirectory;
	}
	protected File getOutputDirectory() {
		return outputDirectory;
	}
	
}
