package eu.apenet.oaiserver.verb;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import eu.apenet.oaiserver.util.OAIResponse;
import eu.apenet.oaiserver.util.OAIUtils;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.EseState;
import eu.apenet.persistence.vo.MetadataFormat;
@Deprecated
public class ListIdentifiers extends OAIVerb{
	
	private InputStream inputStream;
	private static Logger LOG = Logger.getLogger(ListIdentifiers.class);
	private String from;
	private String until;
	private String metadataPrefix;
	private String set;
	private String resumptionToken;
	
	public ListIdentifiers(Map<String, String> params) {
		this.from = params.get("from");
		this.until = params.get("until");
		this.metadataPrefix = params.get("metadataPrefix");
		this.set = params.get("set");
		this.resumptionToken = params.get("resumptionToken");
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getUntil() {
		return until;
	}

	public void setUntil(String until) {
		this.until = until;
	}

	public String getMetadataPrefix() {
		return metadataPrefix;
	}

	public void setMetadataPrefix(String metadataPrefix) {
		this.metadataPrefix = metadataPrefix;
	}

	public String getSet() {
		return set;
	}

	public void setSet(String set) {
		this.set = set;
	}

	public String getResumptionToken() {
		return resumptionToken;
	}

	public void setResumptionToken(String resumptionToken) {
		this.resumptionToken = resumptionToken;
	}

	public boolean execute(){
		try{
			if((this.metadataPrefix!=null && this.metadataPrefix.length()>0)||(this.resumptionToken!=null && this.resumptionToken.length()>0)){
				Map<String,String> map = new HashMap<String,String>();
				map.put("metadataPrefix",this.metadataPrefix);
				if(this.from!=null && this.from.length()>0){
					map.put("from",this.from);
				}
				if(this.until!=null && this.until.length()>0){
					map.put("until",this.until);
				}
				if(this.set!=null && this.set.length()>0){
					map.put("set",this.set);
				}
				if(this.resumptionToken!=null && this.resumptionToken.length()>0){
					map.put("resumptionToken",this.resumptionToken);
				}
				this.inputStream = OAIResponse.getVerbInfo("ListIdentifiers",map,this.getUrl());
				if(this.inputStream!=null){
					return true;
				}
			}
			Document emptyDocument = OAIResponse.getVerbResponse("ListIdentifiers",null,this.getUrl());
			this.inputStream = OAIUtils.getInputStreamOfDocument(OAIUtils.getResponseError(emptyDocument,"cannotDisseminateFormat"));
		}catch(Exception e){
			LOG.error("Params are NOT right in URL: ",e.getCause());
		}
		return false;
	}
	
	public static Document listIdentifierEngine(Date fromDate,Date untilDate,String set,String metadataPrefix,Document doc, String eseAndRecord) throws SAXException, IOException, ParserConfigurationException, ParseException {
		List<Ese> esesToBeShown = null;
		int recordNumber = 0;
		int eseNumber = 0;
		MetadataFormat metadataFormat = null;
		if(fromDate!=null || untilDate!=null || set !=null){
			if(eseAndRecord!=null && !eseAndRecord.isEmpty() && eseAndRecord.contains(OAIUtils.SPECIAL_KEY)){
				eseNumber = new Integer(eseAndRecord.substring(0,eseAndRecord.lastIndexOf(OAIUtils.SPECIAL_KEY)));
				recordNumber = new Integer(eseAndRecord.substring(eseAndRecord.indexOf(OAIUtils.SPECIAL_KEY)+1));
			}
			metadataFormat = MetadataFormat.getMetadataFormat(metadataPrefix);
			esesToBeShown = DAOFactory.instance().getEseDAO().getEsesByArguments(fromDate,untilDate,metadataFormat,set,eseNumber,OAIResponse.LIMIT_PER_RESPONSE);
		}
		if(esesToBeShown!=null && esesToBeShown.size()>0){
			doc = listIdentifierLogic(doc,esesToBeShown,eseNumber,recordNumber,metadataPrefix,set,fromDate,untilDate);
		}else{
			esesToBeShown = DAOFactory.instance().getEseDAO().getEsesByArguments(fromDate,untilDate,metadataFormat,set+":",eseNumber,OAIResponse.LIMIT_PER_RESPONSE);
			if(esesToBeShown!=null && esesToBeShown.size()>0){
				doc = listIdentifierLogic(doc,esesToBeShown,eseNumber,recordNumber,metadataPrefix,set,fromDate,untilDate);
			}
			else{
				doc = OAIUtils.getResponseError(doc,"noRecordsMatch");
			}
		}
		return doc;
	}
	
	private static Document listIdentifierLogic(Document doc,List<Ese> esesToBeShown,int eseNumber,int recordNumber,String metadataPrefix,String set,Date fromDate,Date untilDate) throws ParseException, DOMException, ParserConfigurationException{
		Node verbNode = doc.createElementNS(OAIResponse.NAMESPACE,"ListIdentifiers");
		Iterator<Ese> esesIterator = esesToBeShown.iterator();
		int numberOfIdentifiers = 0;
		int numberOfEse = eseNumber-1;
		int numberOfObjects = 0;
		String lastMetadataFormatEse = null;
		Ese ese = null;
		while(esesIterator.hasNext() && (numberOfObjects<OAIResponse.LIMIT_PER_RESPONSE)){
			ese = esesIterator.next();
			numberOfEse++;
			lastMetadataFormatEse  = ese.getMetadataFormat().toString();
			if(!ese.getEseState().getState().equals(EseState.NOT_PUBLISHED) && (metadataPrefix==null || metadataPrefix.isEmpty() || metadataPrefix.equals(lastMetadataFormatEse))){
				for(int i=0;i<ese.getNumberOfRecords() && numberOfObjects<OAIResponse.LIMIT_PER_RESPONSE;i++,numberOfIdentifiers=i){
					Element headerNode = doc.createElementNS(OAIResponse.NAMESPACE,"header");
					if(ese.getEseState().getState().equals(EseState.REMOVED)){
						headerNode.setAttribute("status","deleted");
					}
					if(recordNumber<=0){
						numberOfObjects++; //It counts if the number of objects is the correct 
						Node identifierNode = doc.createElementNS(OAIResponse.NAMESPACE, "identifier");
						identifierNode.setTextContent(ese.getOaiIdentifier()+OAIUtils.SPECIAL_KEY+(i+1));
						headerNode.appendChild(identifierNode);
						Node nodeDatestamp = doc.createElementNS(OAIResponse.NAMESPACE, "datestamp");
						nodeDatestamp.setTextContent(OAIUtils.parseDateToISO8601(ese.getModificationDate()));
						headerNode.appendChild(nodeDatestamp);
						Node nodeSetSpec = doc.createElementNS(OAIResponse.NAMESPACE, "setSpec");
						nodeSetSpec.setTextContent(ese.getEset());
						headerNode.appendChild(nodeSetSpec);
						verbNode.appendChild(headerNode);
					}else{
						recordNumber--;
					}
				}
			}
		}
		if(numberOfObjects>0){
			doc.getDocumentElement().appendChild(verbNode);
		}
		if(numberOfObjects==OAIResponse.LIMIT_PER_RESPONSE){
			if(ese.getNumberOfRecords()>numberOfIdentifiers || esesIterator.hasNext()){
				Map<String,String> arguments = new HashMap<String, String>();
				if(fromDate.after(OAIUtils.parseStringToISO8601Date("0001-01-01T00:00:00Z"))){
					arguments.put("from",OAIUtils.parseDateToISO8601(fromDate));
				}
				if(untilDate.before(OAIUtils.parseStringToISO8601Date("9999-12-31T23:59:59Z"))){
					arguments.put("until",OAIUtils.parseDateToISO8601(untilDate));
				}
				arguments.put("set",set);
				arguments.put("metadataPrefix", metadataPrefix);
				doc = OAIUtils.buildResumptionToken(doc,arguments,numberOfEse+OAIUtils.SPECIAL_KEY+numberOfIdentifiers);
			}
		}else if(numberOfObjects==0){
			if(metadataPrefix.equals(lastMetadataFormatEse)){
				doc = OAIUtils.getResponseError(doc,"noRecordsMatch");
			}else{
				doc = OAIUtils.getResponseError(doc,"cannotDisseminateFormat");
			}
		}
		return doc;
	}
	
	public static Document getListIdentifiersLogic(Map<String, String> arguments,String requestURL) throws Exception {
		Document doc = OAIResponse.getVerbResponse("ListIdentifiers",arguments,requestURL);
		if(!(doc.getElementsByTagName("error")!=null && doc.getElementsByTagName("error").getLength()>0)){
			Date fromDate = null;
			Date untilDate = null;
			String from = arguments.get("from");
			try{
				if(from!=null){
					fromDate = OAIUtils.parseStringToISO8601Date(from);
				}else{
					fromDate = OAIUtils.parseStringToISO8601Date("0001-01-01T00:00:00Z");
				}
			}catch(Exception ex){
				LOG.error("Error trying to parse '"+from+"' dates, using default: "+ex.getCause());
				return OAIUtils.getResponseError(doc,"badArgument");
			}
			String until = arguments.get("until");
			try{
				if(until!=null){
					untilDate = OAIUtils.parseStringToISO8601Date(until);
				}else{
					untilDate = OAIUtils.parseStringToISO8601Date("9999-12-31T23:59:59Z");
				}
			}catch(Exception ex){
				LOG.error("Error trying to parse '"+until+"' dates, using default: "+ex.getCause());
				return OAIUtils.getResponseError(doc,"badArgument");
			}
			String metadataPrefix = arguments.get("metadataPrefix"); 
			String set = arguments.get("set");
			String resumptionToken = arguments.get("resumptionToken");
			if(metadataPrefix!=null && !metadataPrefix.isEmpty()){ 
				doc = ListIdentifiers.listIdentifierEngine(fromDate,untilDate,set,metadataPrefix,doc,null);
			}else{
				if(resumptionToken!=null && resumptionToken.length()>5 && resumptionToken.contains("/")){
					String[] resumptionTokenArguments = resumptionToken.split("/");
					from=resumptionTokenArguments[0];
					until=resumptionTokenArguments[1];
					fromDate = null;
					untilDate = null;
					try{
						if(from!=null){
							fromDate = OAIUtils.parseStringToISO8601Date(from);
						}else{
							fromDate = OAIUtils.parseStringToISO8601Date("0001-01-01T00:00:00Z");
						}
					}catch(Exception ex){
						LOG.error("Error trying to parse '"+from+"' date, using default: "+ex.getCause());
						return OAIUtils.getResponseError(doc,"badResumptionToken");
					}
					try{
						if(until!=null){
							untilDate = OAIUtils.parseStringToISO8601Date(until);
						}else{
							untilDate = OAIUtils.parseStringToISO8601Date("9999-12-31T23:59:59Z");
						}
					}catch(Exception ex){
						LOG.error("Error trying to parse '"+until+"' date, using default: "+ex.getCause());
						return OAIUtils.getResponseError(doc,"badResumptionToken");
					}
					set=resumptionTokenArguments[2];
					metadataPrefix=resumptionTokenArguments[3];
					String eseAndRecord = resumptionTokenArguments[4];
					doc = ListIdentifiers.listIdentifierEngine(fromDate,untilDate,set,metadataPrefix,doc,eseAndRecord);
				}else{
					return OAIUtils.getResponseError(doc,"badResumptionToken");
				}
			}
		}
		return doc;
	}
}
