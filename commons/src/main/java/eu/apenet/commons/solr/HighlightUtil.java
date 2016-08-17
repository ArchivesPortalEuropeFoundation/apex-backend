package eu.apenet.commons.solr;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class HighlightUtil {

    private static final String CLASSPATH_PREFIX = "classpath:";
    private static final String GT_TOKEN = ">";
    private static final String LT_TOKEN = "<";
    private static final String EM_END = "</em>";
    private static final String EM_START = "<em>";
    private static Logger LOGGER = Logger.getLogger(HighlightUtil.class);
    private Set<String> stopwords = new TreeSet<String>();
    private static final Pattern NO_WHITESPACE_PATTERN = Pattern.compile("\\S+");
    private static final Pattern WORD_PATTERN = Pattern.compile("([\\p{L}\\p{Digit}]+)");

    private static HighlightUtil instance = null;

    private HighlightUtil(String solrStopwordsUrl) {
        stopwords = retrieveStopwords(solrStopwordsUrl);
    }

    public static HighlightUtil getInstance(String solrStopwordsUrl) {
        if (instance == null) {
            instance = new HighlightUtil(solrStopwordsUrl);
        }
        return instance;
    }

    public String highlight(String search, String html, HighlightType type) {
        String newText = html;
        List<String> searchTerms = convertSearchTermToWords(search, type);
        if (html != null) {
            newText = doHighlight(searchTerms, newText, type);

        }
        return newText;
    }

    public List<String> convertSearchTermToWords(String searchTerm, HighlightType type) {
        List<String> results = new ArrayList<String>();
        boolean defaultType = HighlightType.DEFAULT.equals(type);
        Matcher matcher = NO_WHITESPACE_PATTERN.matcher(searchTerm);
        while (matcher.find()) {
            String word = matcher.group();
            if (defaultType) {
                if (!stopwords.contains(word.toLowerCase())) {
                    results.addAll(splitWordIntoSubwords(word));
                }
            } else {
                results.add(word.toLowerCase());
            }
        }
        return results;

    }

    protected List<String> splitWordIntoSubwords(String word) {
        List<String> results = new ArrayList<String>();
        Matcher matcher = WORD_PATTERN.matcher(word);
        while (matcher.find()) {
            results.add(matcher.group().toLowerCase());
        }
        return results;

    }

    private static String doHighlight(List<String> searchTerms, String html, HighlightType type) {
        String htmlLowercase = html.toLowerCase();
        String newText = "";
        int lastMatchIndex = 0;
        int lastTagIndex = -1;
        Pattern pattern = null;
        if (HighlightType.DEFAULT.equals(type)) {
            pattern = WORD_PATTERN;
        } else {
            pattern = NO_WHITESPACE_PATTERN;
        }
        Matcher matcher = pattern.matcher(htmlLowercase);

        while (matcher.find() && html.length() > 0) {
            int matchStartIndex = matcher.start();
            int matchEndIndex = matcher.end();
            String group = matcher.group();
            if (matchStartIndex > lastTagIndex && searchTerms.contains(group)) {
                // skip anything inside an HTML tag
                int gtIndex = html.lastIndexOf(GT_TOKEN, matchStartIndex);
                int ltIndex = html.lastIndexOf(LT_TOKEN, matchStartIndex);
                boolean withinTag = ltIndex >= gtIndex && ltIndex >= 0;
                if (withinTag) {
                    int endIndex = html.indexOf(GT_TOKEN, matchEndIndex);
                    newText += html.substring(lastMatchIndex, matchEndIndex);
                    lastTagIndex = endIndex;
                } else {
                    newText += html.substring(lastMatchIndex, matchStartIndex) + EM_START;
                    newText += html.substring(matchStartIndex, matchEndIndex) + EM_END;

                }
            } else {
                newText += html.substring(lastMatchIndex, matchEndIndex);
            }
            lastMatchIndex = matchEndIndex;
        }

        if (StringUtils.isBlank(newText)) {
            newText = html;
        } else {
            newText += html.substring(lastMatchIndex);
        }

        return newText;
    }

    private static Set<String> retrieveStopwords(String stopwordsFile) {
        String filename = stopwordsFile;
        Set<String> stopwords = new TreeSet<String>();
        try {
            InputStream inputStream = null;
            if (filename.startsWith(CLASSPATH_PREFIX)) {
                filename = stopwordsFile.substring(CLASSPATH_PREFIX.length());
                inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
            } else {
                inputStream = new FileInputStream(filename);
            }
            Reader r = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader input = new BufferedReader(r);
            boolean firstLine = true;

            String line = null; // not declared within while loop
            while ((line = input.readLine()) != null) {
                line = line.trim();
                if (!firstLine && line.length() > 0 && !line.startsWith("#")) {
                    stopwords.add(line);
                } else {
                    firstLine = false;
                }

            }
        } catch (Exception io) {
            LOGGER.error("Unable to read the stopwords file: " + filename);
        }
        return stopwords;
    }
}
