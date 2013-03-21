package eu.apenet.oaiserver.response;

import java.util.Map;

import javax.xml.stream.XMLStreamException;

public class ErrorResponse extends AbstractResponse {
	private static final String BAD_VERB_MESSAGE = "Value of the verb argument is not a legal OAI-PMH verb, the verb argument is missing, or the verb argument is repeated.";
	private static final String BAD_ARGUMENT_MESSAGE = "The request includes illegal arguments, is missing required arguments, includes a repeated argument, or values for arguments have an illegal syntax.";
	private static final String BAD_RESUMPTION_TOKEN_MESSAGE = "The value of the resumptionToken argument is invalid or expired.";
	private static final String CANNOT_DISSEMINATE_FORMAT_MESSAGE = "The metadata format identified by the value given for the metadataPrefix argument is not supported by the item or by the repository.";
	private static final String ID_DOES_NOT_EXIST_MESSAGE = "The value of the identifier argument is unknown or illegal in this repository.";
	private static final String NO_RECORDS_MATCH_MESSAGE = "The combination of the values of the from, until, set and metadataPrefix arguments results in an empty list.";
	private static final String NO_METADATA_FORMATS_MESSAGE = "There are no metadata formats available for the specified item.";
	private static final String NO_SET_HIERARCHY_MESSAGE = "The repository does not support sets.";

	public enum ErrorCode {
		BAD_VERB("badVerb", BAD_VERB_MESSAGE), BAD_ARGUMENT("badArgument",BAD_ARGUMENT_MESSAGE), BAD_RESUMPTION_TOKEN("badResumptionToken",BAD_RESUMPTION_TOKEN_MESSAGE),
		CANNOT_DISSEMINATE_FORMAT("cannotDisseminateFormat",CANNOT_DISSEMINATE_FORMAT_MESSAGE), ID_DOES_NOT_EXIST("idDoesNotExist",ID_DOES_NOT_EXIST_MESSAGE), NO_RECORDS_MATCH("noRecordsMatch",NO_RECORDS_MATCH_MESSAGE),
		NO_METADATA_FORMATS("noMetadataFormats",NO_METADATA_FORMATS_MESSAGE),NO_SET_HIERARCHY("noSetHierarchy",NO_SET_HIERARCHY_MESSAGE) ;
		private String errorCode;
		private String errorMessage;
		ErrorCode (String errorCode, String errorMessage){
			this.errorCode = errorCode;
			this.errorMessage = errorMessage;
		}
		
	};

	private ErrorCode errorCode;

	public ErrorResponse(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	protected void generateResponseInternal(XMLStreamWriterHolder writer, Map<String, String> params) throws XMLStreamException {
		writer.writeTextElementWithAttribute("error", errorCode.errorMessage, "code", errorCode.errorCode);
	}

}
