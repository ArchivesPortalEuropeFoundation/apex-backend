package eu.apenet.oaiserver.request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.apenet.oaiserver.response.AbstractResponse;
import eu.apenet.oaiserver.response.ErrorResponse;
import eu.apenet.oaiserver.response.IdentifyResponse;
import eu.apenet.oaiserver.response.ListMetadataFormatsResponse;
import eu.apenet.oaiserver.response.XMLStreamWriterHolder;
import eu.apenet.oaiserver.util.OAIUtils;
import eu.apenet.persistence.vo.MetadataFormat;

public class RequestProcessor {
	private static Logger LOGGER = Logger.getLogger(RequestProcessor.class);
	public static final String VERB_IDENTIFY = "Identify";
	public static final String VERB_LIST_RECORDS = "ListRecords";
	public static final String VERB_LIST_IDENTIFIERS = "ListIdentifiers";
	public static final String VERB_LIST_SETS = "ListSets";
	public static final String VERB_GET_RECORD = "GetRecord";
	public static final String VERB_LIST_METADATAFORMATS = "ListMetadataFormats";
	public static final String METADATA_PREFIX = "metadataPrefix";

	public static void process(Map<String, String[]> originalParams, String url, XMLStreamWriterHolder writer)
			throws XMLStreamException, IOException {
		Map<String, String> params = new HashMap<String, String>();
		boolean badArguments = false;
		for (Entry<String, String[]> entry : originalParams.entrySet()) {
			if (entry.getValue() != null && entry.getValue().length > 1) {
				badArguments = true;
			} else if (entry.getValue() != null && entry.getValue().length == 1) {
				params.put(entry.getKey(), entry.getValue()[0]);
			}
		}
		params.put(AbstractResponse.REQUEST_URL, url);
		String verb = params.get(OAIUtils.VERB);
		try {
			if (StringUtils.isBlank(verb)) {
				new ErrorResponse(ErrorResponse.ErrorCode.BAD_VERB).generateResponse(writer, params);

			} else {
				String metadataPrefix = params.get(METADATA_PREFIX);
				if (StringUtils.isNotBlank(metadataPrefix) && MetadataFormat.getMetadataFormat(metadataPrefix) == null) {
					new ErrorResponse(ErrorResponse.ErrorCode.CANNOT_DISSEMINATE_FORMAT).generateResponse(writer,
							params);
				} else if (!OAIUtils.validateRequestAttributes(params)) {
					new ErrorResponse(ErrorResponse.ErrorCode.BAD_ARGUMENT).generateResponse(writer, params);
				} else if (!badArguments) {
					if (VERB_LIST_RECORDS.equals(verb)) {
						if (checkListIdentifiersAndListRecords(params)) {
							ListRecordsOrIdentifiers.execute(writer, params, true);
						} else {
							badArguments = true;
						}
					}
					if (VERB_LIST_IDENTIFIERS.equals(verb)) {
						if (checkListIdentifiersAndListRecords(params)) {
							ListRecordsOrIdentifiers.execute(writer, params, false);
						} else {
							badArguments = true;
						}
					} else if (VERB_IDENTIFY.equals(verb)) {
						if (checkIdentifyArguments(params)) {
							new IdentifyResponse().generateResponse(writer, params);
						} else {
							badArguments = true;
						}
					} else if (VERB_LIST_METADATAFORMATS.equals(verb)) {
						if (checkListMetadataFormats(params)) {
							new ListMetadataFormatsResponse().generateResponse(writer, params);
						} else {
							badArguments = true;
						}
					} else if (VERB_GET_RECORD.equals(verb)) {
						if (checkGetRecordArguments(params)) {
							GetRecord.execute(writer, params);
						} else {
							badArguments = true;
						}
					} else if (VERB_LIST_SETS.equals(verb)) {
						if (checkListSetsArguments(params)) {
							ListSets.execute(writer, params);
						} else {
							badArguments = true;
						}
					}else {
						new ErrorResponse(ErrorResponse.ErrorCode.BAD_VERB).generateResponse(writer, params);
					}
				}
				if (badArguments) {
					new ErrorResponse(ErrorResponse.ErrorCode.BAD_ARGUMENT).generateResponse(writer, params);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Could not process the request: "  + e.getMessage(), e);
			new ErrorResponse(ErrorResponse.ErrorCode.BAD_ARGUMENT).generateResponse(writer, params);
		}
		writer.close();
	}

	private static boolean checkListMetadataFormats(Map<String, String> params) {
		int size = params.size() - 1;
		if (size == 1) {
			return true;
		} else if (size == 2 && params.containsKey("identifier")) {
			return true;
		}
		return false;
	}

	private static boolean checkListSetsArguments(Map<String, String> params) {
		int size = params.size() - 1;
		if (size == 1) {
			return true;
		} else if (size == 2 && params.containsKey("resumptionToken")) {
			return true;
		}
		return false;
	}

	private static boolean checkListIdentifiersAndListRecords(Map<String, String> params) {
		int size = params.size()-1;
		if (size <= 5) {
			if (!params.containsKey("resumptionToken") && params.containsKey("metadataPrefix")) {
				switch (size) {
				case 2:
					return true;
				case 3:
					if (params.containsKey("from") || params.containsKey("until") || params.containsKey("set")) {
						return true;
					}
				case 4:
					if ((params.containsKey("from") && params.containsKey("until") && !params.containsKey("set"))
							|| (params.containsKey("from") && !params.containsKey("until") && params.containsKey("set"))
							|| (!params.containsKey("from") && params.containsKey("until") && params.containsKey("set"))) {
						return true;
					}
				case 5:
					if (params.containsKey("from") && params.containsKey("until") && params.containsKey("set")) {
						return true;
					}
				}
			} else if (params.containsKey("resumptionToken") && !params.containsKey("metadataPrefix")
					&& size == 2) {
				return true;
			}
		}
		return false;
	}

	private static boolean checkIdentifyArguments(Map<String, String> params) {
		if ((params.size() - 1) == 1) {
			return true;
		}
		return false;
	}

	private static boolean checkGetRecordArguments(Map<String, String> params) {
		int size = params.size() - 1;
		if (size == 3) {
			if (params.containsKey("identifier") && params.containsKey("metadataPrefix")) {
				return true;
			}
		}
		return false;
	}
}
