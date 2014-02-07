package eu.apenet.dashboard.services;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;

public class AbstractSolrPublisher {
	private static final Logger LOGGER = Logger.getLogger(AbstractSolrPublisher.class);
	protected static final String WHITE_SPACE = " ";
	public static final String COLON = ":";
	protected static String removeUnusedCharacters(String input) {
		if (input != null) {
			String result = input.replaceAll("[\t ]+", " ");
			result = result.replaceAll("[\n\r]+", "");
			return result.trim();
		} else {
			return null;
		}

	}

	protected static String getText(NodeList nodeList) {
		String result = "";
		for (int i = 0; i < nodeList.getLength(); i++) {
			result += WHITE_SPACE + nodeList.item(i).getTextContent();
		}
		return removeUnusedCharacters(result);
	}

	protected static Set<String> getTextsWithoutMultiplity(NodeList nodeList) {
		Set<String> results = new TreeSet<String>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			String text = removeUnusedCharacters(nodeList.item(i).getTextContent());
			if (StringUtils.isNotBlank(text)) {
				results.add(text.toUpperCase());
			}
		}
		return results;
	}
	protected static String obtainDate(String onedate, boolean isStartDate) {
		String year = null;
		String month = null;
		String day = null;
		try {
			if (onedate.contains("-")) {
				String[] list = onedate.split("-");
				if (list.length >= 1) {
					year = list[0];
				}
				if (list.length >= 2) {
					month = list[1];
				}
				if (list.length >= 3) {
					day = list[2];
					if (day != null && day.contains("T")){
						day = day.substring(0,day.indexOf("T"));
					}
				}
			} else {
				if (onedate.length() >= 4) {
					year = onedate.substring(0, 4);
				}
				if (onedate.length() >= 6) {
					month = onedate.substring(4, 6);
				}
				if (onedate.length() >= 8) {
					day = onedate.substring(6, 8);
				}
			}
			return obtainDate(year, month, day, isStartDate);
		} catch (Exception ex) {
			LOGGER.error("Error trying to obtain Date in Indexer: " + ex.getMessage());
		}
		return null;
	}

	private static String obtainDate(String yearString, String monthString, String dayString, boolean isStartDate) {
		if (yearString != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Integer year = new Integer(yearString);
			Integer month = null;
			Integer day = null;
			if (monthString != null) {
				month = new Integer(monthString);
			}
			if (dayString != null) {
				day = new Integer(dayString);
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
			calendar.setTimeInMillis(0);
			calendar.set(Calendar.YEAR, year);
			if (isStartDate) {
				if (month == null) {
					calendar.set(Calendar.MONTH, 0);
				} else {
					calendar.set(Calendar.MONTH, month - 1);
				}
				if (day == null) {
					calendar.set(Calendar.DAY_OF_MONTH, 1);
				} else {
					calendar.set(Calendar.DAY_OF_MONTH, day);
				}
				return dateFormat.format(calendar.getTime()) + "T00:00:01Z";
			} else {
				if (month == null) {
					calendar.set(Calendar.MONTH, 11);
				} else {
					calendar.set(Calendar.MONTH, month - 1);
				}
				if (day == null) {
					calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
				} else {
					calendar.set(Calendar.DAY_OF_MONTH, day);
				}
				return dateFormat.format(calendar.getTime()) + "T23:59:59Z";
			}
		}
		return null;
	}

}
