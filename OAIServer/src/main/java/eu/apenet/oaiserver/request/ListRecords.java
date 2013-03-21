package eu.apenet.oaiserver.request;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.apenet.oaiserver.response.ErrorResponse;
import eu.apenet.oaiserver.response.ListRecordsResponse;
import eu.apenet.oaiserver.response.XMLStreamWriterHolder;
import eu.apenet.oaiserver.util.OAIUtils;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.MetadataFormat;
import eu.apenet.persistence.vo.ResumptionToken;

public class ListRecords {

	private static final int LIMIT = 2;
	private static Logger LOG = Logger.getLogger(ListRecords.class);

	public static boolean execute(XMLStreamWriterHolder writer, Map<String, String> params) throws XMLStreamException, IOException {
		String resumptionToken = params.get("resumptionToken");
		String from = params.get("from");
		String until = params.get("until");
		MetadataFormat metadataFormat = MetadataFormat.getMetadataFormat(params.get("metadataPrefix"));
		String set = params.get("set");
		int start = 0;
		Date fromDate = null;
		Date untilDate = null;
		ResumptionToken oldResToken = null;
		if (metadataFormat != null) {
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
				oldResToken = DAOFactory.instance().getResumptionTokenDAO()
						.findById(Integer.parseInt(resumptionToken));
				if (oldResToken == null){
					new ErrorResponse(ErrorResponse.ErrorCode.BAD_RESUMPTION_TOKEN).generateResponse(writer, params);
					return false;
				}else if (oldResToken.getExpirationDate().after(new Date())) {
					fromDate = oldResToken.getFromDate();
					untilDate = oldResToken.getUntilDate();
					set = oldResToken.getSet();
					metadataFormat = oldResToken.getMetadataFormat();
					start = Integer.parseInt(oldResToken.getLastRecordHarvested());
				}else {
					new ErrorResponse(ErrorResponse.ErrorCode.BAD_RESUMPTION_TOKEN).generateResponse(writer, params);
					return false;

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
		List<Ese> eses = DAOFactory.instance().getEseDAO()
				.getEsesByArguments(fromDate, untilDate, metadataFormat, set, start, LIMIT);
		ResumptionToken resToken = null;
		if (eses.isEmpty()) {
			eses = DAOFactory.instance().getEseDAO()
					.getEsesByArguments(fromDate, untilDate, metadataFormat, set + ":", start, LIMIT);
			if (eses.isEmpty()) {
				new ErrorResponse(ErrorResponse.ErrorCode.NO_RECORDS_MATCH).generateResponse(writer, params);
				return false;
			}
		}
		if (eses.size() > LIMIT) {
			if (oldResToken == null){
				resToken = OAIUtils.buildResumptionToken(params, start+LIMIT);
			}else {
				resToken = OAIUtils.buildResumptionToken(oldResToken, start+LIMIT);
			}
			/*
			 * remove the last one, that is only for checking.
			 */
			int lastIndex = eses.size() - 1;
			eses.remove(lastIndex);
		}
		new ListRecordsResponse(eses, resToken).generateResponse(writer, params);
		return true;
	}


}
