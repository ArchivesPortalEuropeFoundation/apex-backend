package eu.apenet.oaiserver.request;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import eu.apenet.oaiserver.config.other.Configuration;
import eu.apenet.oaiserver.config.main.MetadataFormats;
import eu.apenet.oaiserver.config.other.dao.impl.MetadataObjectDAOFrontImpl;
import eu.apenet.oaiserver.config.main.dao.ResumptionTokensDAOFront;
import eu.apenet.oaiserver.config.main.dao.MetadataObjectDAOFront;
import eu.apenet.oaiserver.config.other.dao.impl.ResumptionTokensDAOFrontImpl;
import eu.apenet.oaiserver.config.main.vo.MetadataObject;
import eu.apenet.oaiserver.config.main.vo.ResumptionTokens;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.apenet.oaiserver.response.ErrorResponse;
import eu.apenet.oaiserver.response.ListIdentifiersResponse;
import eu.apenet.oaiserver.response.ListRecordsResponse;
import eu.apenet.oaiserver.response.XMLStreamWriterHolder;
import eu.apenet.oaiserver.util.OAIUtils;

public class ListRecordsOrIdentifiers {

	private static Logger LOG = Logger.getLogger(ListRecordsOrIdentifiers.class);

	public static boolean execute(XMLStreamWriterHolder writer, Map<String, String> params, boolean showRecords) throws XMLStreamException, IOException {
        MetadataObjectDAOFront metadataObjectDAOFront = new MetadataObjectDAOFrontImpl();
        ResumptionTokensDAOFront resumptionTokensDAOFront = new ResumptionTokensDAOFrontImpl();

		String resumptionToken = params.get("resumptionToken");
		String from = params.get("from");
		String until = params.get("until");
		MetadataFormats metadataFormats = MetadataFormats.getMetadataFormats(params.get("metadataPrefix"));
		String set = params.get("set");
		int start = 0;
		Date fromDate = null;
		Date untilDate = null;
		ResumptionTokens oldResTokens = null;
		int limit = Configuration.EAD_RECORDS_LIMIT;
		if (!showRecords){
			limit = Configuration.IDENTIFIERS_LIMIT;
		} else {
            if(metadataFormats.equals(MetadataFormats.DC)) {
                limit = Configuration.DC_RECORDS_LIMIT;
            }
        }
		if (metadataFormats != null) {
			try {
				if (from != null) {
					fromDate = OAIUtils.parseStringToISO8601Date(from);
				} else {
					fromDate = OAIUtils.parseStringToISO8601Date("0001-01-01T00:00:00Z");
				}
			} catch (Exception ex) {
				LOG.error("Error trying to parse '" + from + "' dates, using default: " + ex.getCause());
				new ErrorResponse(ErrorResponse.ErrorCode.BAD_ARGUMENT).generateResponse(writer, params);
				return false;
			}
			try {
				if (until != null) {
					untilDate = OAIUtils.parseStringToISO8601Date(until);
				} else {
					untilDate = OAIUtils.parseStringToISO8601Date("9999-12-31T23:59:59Z");
				}
			} catch (Exception ex) {
				LOG.error("Error trying to parse '" + until + "' dates, using default: " + ex.getCause());
				new ErrorResponse(ErrorResponse.ErrorCode.BAD_ARGUMENT).generateResponse(writer, params);
				return false;
			}

		} else if (StringUtils.isNotBlank(resumptionToken)) {
			try {
				oldResTokens = resumptionTokensDAOFront.getResumptionToken(resumptionToken);
				if (oldResTokens == null) {
                    LOG.info("ResumptionToken: " + resumptionToken + " could not be found");
                    new ErrorResponse(ErrorResponse.ErrorCode.BAD_RESUMPTION_TOKEN).generateResponse(writer, params);
                    return false;
                } else {
                    if (oldResTokens.getExpirationDate().after(new Date())) {
                        fromDate = oldResTokens.getFromDate();
                        untilDate = oldResTokens.getUntilDate();
                        set = oldResTokens.getSet();
                        metadataFormats = oldResTokens.getMetadataFormats();
                        start = Integer.parseInt(oldResTokens.getLastRecordHarvested());
                    } else {
                        LOG.info("ResumptionToken: " + resumptionToken + " is expired");
                        new ErrorResponse(ErrorResponse.ErrorCode.BAD_RESUMPTION_TOKEN).generateResponse(writer, params);
                        return false;
                    }
				}

			} catch (Exception e) {
				LOG.error("Parse error, trying to retrieve resumptionToken in ListRecords: " + e.getCause());
				new ErrorResponse(ErrorResponse.ErrorCode.BAD_ARGUMENT).generateResponse(writer, params);
				return false;
			}
		} else {
			new ErrorResponse(ErrorResponse.ErrorCode.BAD_ARGUMENT).generateResponse(writer, params);
			return false;
		}
		List<MetadataObject> metadataObjects = metadataObjectDAOFront.getMetadataObjects(fromDate, untilDate, metadataFormats, set, start, limit);
		ResumptionTokens resToken = null;
		if (metadataObjects.isEmpty()) {
			metadataObjects = metadataObjectDAOFront.getMetadataObjects(fromDate, untilDate, metadataFormats, set + "-", start, limit);
			if (metadataObjects.isEmpty()) {
				new ErrorResponse(ErrorResponse.ErrorCode.NO_RECORDS_MATCH).generateResponse(writer, params);
				return false;
			}
		}
		if (metadataObjects.size() > limit) {
			if (oldResTokens == null) {
				resToken = OAIUtils.buildResumptionToken(params, start + limit);
			} else {
				resToken = OAIUtils.buildResumptionToken(oldResTokens, start + limit);
			}
			/*
			 * remove the last one, that is only for checking.
			 */
			int lastIndex = metadataObjects.size() - 1;
			metadataObjects.remove(lastIndex);
		}else {
			//TODO: add nicer locking, but without this, there is no locking when the items are below the max
			if (oldResTokens == null) {
				OAIUtils.buildResumptionToken(params, start + limit);
			} else {
				OAIUtils.buildResumptionToken(oldResTokens, start + limit);
			}			
		}
		if (showRecords) {
			new ListRecordsResponse(metadataObjects, resToken).generateResponse(writer, params);
		} else {
			new ListIdentifiersResponse(metadataObjects, resToken).generateResponse(writer, params);
		}
		return true;
	}

}
