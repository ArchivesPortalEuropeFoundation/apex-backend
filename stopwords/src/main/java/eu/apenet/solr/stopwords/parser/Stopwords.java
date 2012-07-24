package eu.apenet.solr.stopwords.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.apenet.solr.stopwords.Stopword;

public class Stopwords implements Iterable<Stopword> {
	private static final Logger LOGGER = Logger.getLogger(Stopwords.class);
	private static final String SEPARATOR = ";";
	private List<Stopword> stopwords = new ArrayList<Stopword>();
	private int lineNumber = 0;

	public Stopwords(File stopwordsFile) {
		boolean forbiddenStopwords = stopwordsFile.getName().startsWith("stopwords-forbidden");
		try {
			if (stopwordsFile.getName().endsWith(".csv") && stopwordsFile.isFile()) {
				Reader r = new InputStreamReader(new FileInputStream(stopwordsFile), "UTF-8");
				BufferedReader input = new BufferedReader(r);
				boolean firstLine = true;
				try {
					String line = null; // not declared within while loop
					while ((line = input.readLine()) != null) {
						line = line.trim();
						lineNumber++;
						if (!firstLine && line.length() > 0) {
							// String resultLine = "";
							String[] lineArray = line.split(SEPARATOR);
							if (lineArray.length < 2) {
								LOGGER.error(stopwordsFile.getName()
										+ ": number of columns should be at least 2. at line: " + lineNumber);
							} else {
								Stopword stopword = new Stopword();
								stopword.setValue(lineArray[0].trim());
								stopword.setLanguage(lineArray[1].trim());
								if (lineArray.length >= 3) {
									stopword.setDescription(lineArray[2].trim());
									if (lineArray.length >= 4) {
										stopword.setForbidden(StringUtils.isNotBlank(lineArray[3]));
									}
									if (lineArray.length >= 5) {
										stopword.setForbidden(StringUtils.isNotBlank(lineArray[4])
												|| stopword.isForbidden());
									}
									if (stopword.isForbidden() && lineArray.length >= 6 && StringUtils.isNotBlank(lineArray[5])) {
										stopword.setForbiddenReason(lineArray[5].trim());
									}
								}
								if (stopword.isForbidden()) {
									stopword.setForbiddenSource(stopwordsFile.getName());
								} else {
									stopword.setValueSource(stopwordsFile.getName());
								}
								if (StringUtils.isNotBlank(stopword.getValue())
										&& StringUtils.isNotBlank(stopword.getLanguage())) {
									if (!forbiddenStopwords || (forbiddenStopwords && stopword.isForbidden())) {
										stopwords.add(stopword);
									}
								} else {
									LOGGER.error(stopwordsFile.getName()
											+ ": stopword or language should not be empty.");
								}
							}
						} else if (firstLine && line.length() > 0) {
							firstLine = false;
						}

					}
				} finally {
					input.close();
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public Iterator<Stopword> iterator() {
		return stopwords.iterator();
	}

}
