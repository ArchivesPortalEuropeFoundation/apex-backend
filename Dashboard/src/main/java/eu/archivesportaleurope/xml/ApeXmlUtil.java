package eu.archivesportaleurope.xml;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

/**
 * Utility class for XML Parsing.
 * 
 * @author Bastiaan Verhoef
 *
 */
public final class ApeXmlUtil {
	public static final String NO_SEPARATOR = "";
	public static final String WHITE_SPACE = " ";

	/**
	 * Remove all (carriage) returns and remove to many tabs and whitespaces.
	 * 
	 * @param input
	 *            String
	 * @return String that is clean up
	 */
	public static String removeUnusedCharacters(String input) {
		if (input != null) {
			String result = input.replaceAll("[\t ]+", WHITE_SPACE);
			result = result.replaceAll("[\n\r]+", NO_SEPARATOR);
			return result;
		} else {
			return null;
		}

	}

	/**
	 * Convert a collection of strings to one string.
	 * 
	 * @param collectionOfStrings
	 *            Collection of strings to be used.
	 * @param start
	 *            Start position of the collection.
	 * @param separator
	 *            separator used to concatenate the strings.
	 * @return
	 */
	public static String convertToString(Collection<String> collectionOfStrings, int start, String separator) {
		if (collectionOfStrings == null)
			return null;
		if (collectionOfStrings.size() == 0)
			return null;
		StringBuilder builder = new StringBuilder();
		int i = 0;
		Iterator<String> iterator = collectionOfStrings.iterator();
		while (iterator.hasNext()) {
			String result = iterator.next();
			if (i >= start) {
				builder.append(result + separator);
			}
			i++;
		}
		return convertEmptyStringToNull(builder.toString());
	}

	/**
	 * Convert empty String to a null value
	 * 
	 * @param string
	 * @return
	 */
	public static String convertEmptyStringToNull(String string) {
		if (StringUtils.isBlank(string)) {
			return null;
		} else {
			return string.trim();
		}
	}
}
