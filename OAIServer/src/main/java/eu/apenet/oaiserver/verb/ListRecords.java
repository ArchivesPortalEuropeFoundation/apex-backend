package eu.apenet.oaiserver.verb;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import eu.apenet.oaiserver.util.OAIResponse;
import eu.apenet.oaiserver.util.OAIUtils;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.MetadataFormat;

public class ListRecords extends OAIVerb{
	
	private InputStream inputStream;
	private static Logger LOG = Logger.getLogger(ListRecords.class);
	private String from;
	private String until;
	private String set;
	private String resumptionToken;
	private String metadataPrefix;

	public ListRecords(Map<String, String> params) {
		this.from = params.get("from");
		this.until = params.get("until");
		this.set = params.get("set");
		this.resumptionToken = params.get("resumptionToken");
		this.metadataPrefix = params.get("metadataPrefix");
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setUntil(String until) {
		this.until = until;
	}

	public void setSet(String set) {
		this.set = set;
	}

	public void setResumptionToken(String resumptionToken) {
		this.resumptionToken = resumptionToken;
	}

	public void setMetadataPrefix(String metadataPrefix) {
		this.metadataPrefix = metadataPrefix;
	}

	public boolean execute(){
		Map<String,String> map = new HashMap<String, String>();
		try{
			if(this.metadataPrefix!=null && !this.metadataPrefix.isEmpty()){
				map.put("metadataPrefix",this.metadataPrefix);
			}
			if(this.from!=null && !this.from.isEmpty()){
				map.put("from",this.from);
			}
			if(this.until!=null && !this.until.isEmpty()){
				map.put("until",this.until);
			}
			if(this.set!=null && !this.set.isEmpty()){
				map.put("set",this.set);
			}
			if(this.resumptionToken!=null && !this.resumptionToken.isEmpty()){
				map.put("resumptionToken",this.resumptionToken);
			}
			this.inputStream = OAIResponse.getVerbInfo("ListRecords",map,this.getUrl());
			if(this.inputStream!=null){
				return true;
			}
			Document emptyDocument = OAIResponse.getVerbResponse("ListRecords",map,this.getUrl());
			this.inputStream = OAIUtils.getInputStreamOfDocument(OAIUtils.getResponseError(emptyDocument,"badArgument"));
		}catch(Exception e){
			LOG.error("Params are NOT right in URL: ",e.getCause());
		}
		return false;
	}
	
	public static Document getListRecordsLogic(Map<String, String> arguments,String requestURL) throws Exception {
		Document doc = OAIResponse.getVerbResponse("ListRecords",arguments,requestURL);
		if(doc.getElementsByTagName("error")!=null && doc.getElementsByTagName("error").getLength()>0){
			return doc;
		}else{
			Node verbNode = doc.createElementNS(OAIResponse.NAMESPACE, "ListRecords");
			String metadataPrefix = arguments.get("metadataPrefix");
			String resumptionToken = arguments.get("resumptionToken");
			boolean error = false;
			boolean changes = false;
			if(!(arguments==null || arguments.isEmpty()) && arguments.get("metadataPrefix")!=null && (arguments.get("from")==null && arguments.get("until")==null && arguments.get("set")==null)){
				MetadataFormat metadataFormat = MetadataFormat.getMetadataFormat(arguments.get("metadataPrefix"));
				Iterator<Ese> esesIterator = DAOFactory.instance().getEseDAO().getEsesByArguments(null,null,metadataFormat,null,0,OAIResponse.LIMIT_PER_RESPONSE).iterator();
				HashSet<Ese> tempSet = new HashSet<Ese>();
				Integer counter = OAIResponse.LIMIT_PER_RESPONSE;
				int index = 0;
				int setNumber = -1;
				int length = 0;
				while(esesIterator.hasNext() && counter>0){
					Ese eseTemp = esesIterator.next();
					if(eseTemp.getMetadataFormat().equals(MetadataFormat.getMetadataFormat(metadataPrefix))){
						tempSet.add(eseTemp);						
						Node recordNode = OAIUtils.getRecordOfEse(tempSet,null,metadataPrefix,doc);
						setNumber++;
						tempSet.clear();
						if(recordNode!=null){
							length = recordNode.getChildNodes().getLength();
							for(index=0;index<length && counter>0;index++,counter--){
								verbNode.appendChild(recordNode.getFirstChild());
								changes = true;
							}
						}
					}
				}
				if(esesIterator.hasNext() || index<length){
					doc.getDocumentElement().appendChild(verbNode);
					doc = OAIUtils.buildResumptionToken(doc, arguments,setNumber+OAIUtils.SPECIAL_KEY+index);
				}
			}else if(metadataPrefix!=null && !metadataPrefix.isEmpty()){
				Date fromDate = null;
				Date untilDate = null;
				String from = arguments.get("from");
				try {
					if(from!=null){
						fromDate = OAIUtils.parseStringToISO8601Date(from);
					}else{
						fromDate = OAIUtils.parseStringToISO8601Date("0001-01-01T00:00:00Z");
					}
				} catch (Exception ex) {
					LOG.error("Error trying to parse '"+from+"' dates, using default: "+ex.getCause());
					return OAIUtils.getResponseError(doc,"badArgument");
				}
				String until = arguments.get("until");
				try {
					if(until!=null){
						untilDate = OAIUtils.parseStringToISO8601Date(until);
					}else{
						untilDate = OAIUtils.parseStringToISO8601Date("9999-12-31T23:59:59Z");
					}
				} catch (Exception ex) {
					LOG.error("Error trying to parse '"+until+"' dates, using default: "+ex.getCause());
					return OAIUtils.getResponseError(doc,"badArgument");
				}
				if(arguments.get("resumptionToken")==null || arguments.get("resumptionToken").isEmpty()){
					MetadataFormat metadataFormat = MetadataFormat.getMetadataFormat(metadataPrefix);
					List<Ese> eses = null;
					String set = null;
					set = arguments.get("set");
					if(metadataFormat!=null){
						eses = DAOFactory.instance().getEseDAO().getEsesByArguments(fromDate,untilDate,metadataFormat,set,0,OAIResponse.LIMIT_PER_RESPONSE);
					}
					if(eses!=null && !eses.isEmpty()){
						doc = listRecordsLogicEngine(doc,eses,arguments,metadataFormat,metadataPrefix,fromDate,untilDate,verbNode);
						NodeList listListRecords = doc.getElementsByTagName("ListRecords");
						if(listListRecords.getLength()==1){
							if(listListRecords.item(0)!=null && listListRecords.item(0).getLastChild()!=null && (listListRecords.item(0).getLastChild().getNodeName().contains("record") || (listListRecords.item(0).getLastChild().getPreviousSibling()!=null && listListRecords.item(0).getLastChild().getPreviousSibling().getNodeName().contains("record")) )){
								changes = true;
							}
						}
					}else{
						eses = DAOFactory.instance().getEseDAO().getEsesByArguments(fromDate,untilDate,metadataFormat,set+":",0,OAIResponse.LIMIT_PER_RESPONSE);
						if(eses!=null && !eses.isEmpty()){
							doc = listRecordsLogicEngine(doc,eses,arguments,metadataFormat,metadataPrefix,fromDate,untilDate,verbNode);
							NodeList listListRecords = doc.getElementsByTagName("ListRecords");
							if(listListRecords.getLength()==1){
								if(listListRecords.item(0).getLastChild()!=null && listListRecords.item(0).getLastChild().getPreviousSibling()!=null && listListRecords.item(0).getLastChild().getPreviousSibling().getNodeName().contains("record")){
									changes = true;
								}
							}
						}else{
							return OAIUtils.getResponseError(doc,"noRecordsMatch");
						}
					}
				}
			}else if(!(resumptionToken==null || resumptionToken.isEmpty())){
				doc = OAIResponse.resumptionTokenLogic("ListRecords",resumptionToken,arguments,doc);
				NodeList elements = doc.getElementsByTagName("error");
				if(elements.getLength()>0){
					error = true;
				}
			}else{
				doc = OAIUtils.getResponseError(doc,"badArgument");
				error = true;
			}
			if(!error && changes){
				doc.getDocumentElement().appendChild(verbNode);
			}else if(!changes && (resumptionToken==null || resumptionToken.isEmpty())){
				doc = OAIUtils.getResponseError(doc,"noRecordsMatch");
			}
		}
		return doc;
	}
	
	private static Document listRecordsLogicEngine(Document doc,List<Ese> eses,Map<String, String> arguments, MetadataFormat metadataFormat,String metadataPrefix,Date fromDate,Date untilDate,Node verbNode) throws SAXException, IOException, ParserConfigurationException, ParseException {
		int esesCounter = -1;
		int recordsCounter = 0;
		int esesRecordsCounter = 0;
		Iterator<Ese> iteratorEses = eses.iterator();
		int length = 0;
		int i = 0;
		while(iteratorEses.hasNext() && recordsCounter<OAIResponse.LIMIT_PER_RESPONSE){
			esesCounter++;
			Set<Ese> tempSet = new HashSet<Ese>();
			Ese tempEse = iteratorEses.next();
			Node recordsNode = null;
			if(tempEse.getMetadataFormat().equals(metadataFormat)){
				tempSet.add(tempEse);
				recordsNode = OAIUtils.getRecordOfEse(tempSet,null,metadataPrefix,doc);
			}
			if(recordsNode == null){
				length = 0;
			}else{
				length = recordsNode.getChildNodes().getLength();
			}
			
			for(i=length,esesRecordsCounter = 0;i>0 && recordsCounter<OAIResponse.LIMIT_PER_RESPONSE;i--,recordsCounter++,esesRecordsCounter++){
				verbNode.appendChild(recordsNode.getFirstChild());
			}
		}
		if(recordsCounter>0){
			doc.getDocumentElement().appendChild(verbNode);
		}
		if(recordsCounter==OAIResponse.LIMIT_PER_RESPONSE || iteratorEses.hasNext()){
			if(length>esesRecordsCounter){
				String set = arguments.get("set");
				arguments = new HashMap<String, String>();
				if(fromDate.after(OAIUtils.parseStringToISO8601Date("0001-01-01T00:00:00Z"))){
					arguments.put("from",OAIUtils.parseDateToISO8601(fromDate));
				}
				if(untilDate.before(OAIUtils.parseStringToISO8601Date("9999-12-31T23:59:59Z"))){
					arguments.put("until",OAIUtils.parseDateToISO8601(untilDate));
				}
				arguments.put("set",set);
				arguments.put("metadataPrefix", metadataFormat.toString());
				doc = OAIUtils.buildResumptionToken(doc,arguments,esesCounter+OAIUtils.SPECIAL_KEY+esesRecordsCounter);
			}
		}
		return doc;
	}

	public static Document buildListRecordsResponse(List<Ese> sets,Document doc, Map<String, String> arguments) throws DOMException, ParserConfigurationException, ParseException, SAXException, IOException, XMLStreamException {
		Node verbNode = doc.createElementNS(OAIResponse.NAMESPACE, "ListRecords");
		String resumptionToken = arguments.get("resumptionToken");
		String[] params = resumptionToken.split("/");
		String[] limits = null;
		Integer limit = 0;
		Integer setsCounter = null; //It controls the first result of DDBB
		try{
			if(params[4].contains(OAIUtils.SPECIAL_KEY)){
				limits = params[4].split(OAIUtils.SPECIAL_KEY);
			}
			limit = new Integer(limits[1]);
			setsCounter = new Integer(limits[0]);
		}catch(Exception e){
			LOG.error("Invalid resumptionToken, can NOT parse: "+limits[1]+" to Integer: "+e.getCause());
			return OAIUtils.getResponseError(doc,"badResumptionToken");
		}
		if(sets!=null && !sets.isEmpty()){
			Iterator<Ese> iteratorEses = sets.iterator();
			Node recordNode = null;
			int i = 0;
			int recordCounter = setsCounter-1;
			int setsWhereCounter = setsCounter;
			int length = 0;
			setsCounter = 0; //First time it has to be 0 because it count the number of records saved
			while(setsCounter < OAIResponse.LIMIT_PER_RESPONSE && iteratorEses.hasNext()){
				recordCounter++;
				Set<Ese> tempSet = new HashSet<Ese>();
				Ese eseTemp = iteratorEses.next();
				String metadataPrefix = params[3]; //metadataPrefix
				if(eseTemp.getMetadataFormat().equals(MetadataFormat.getMetadataFormat(metadataPrefix)))
				tempSet.add(eseTemp);
				recordNode = OAIUtils.getRecordOfEse(tempSet,null,metadataPrefix,doc);
				if(recordNode==null){
					length = 0;
				}else{
					length = recordNode.getChildNodes().getLength();
				}
				for(i=length;i>0 && setsCounter<OAIResponse.LIMIT_PER_RESPONSE;i--,setsWhereCounter++){
					Node childNode = recordNode.getFirstChild();
					verbNode.appendChild(childNode);
					if(limit<=0){
						setsCounter++;
					}else{
						verbNode.removeChild(childNode);
						limit--;
					}
				}
				if(setsCounter < OAIResponse.LIMIT_PER_RESPONSE && iteratorEses.hasNext()){
					setsWhereCounter=0; //In the next loop, first time has to be 0
				}
			}
			if(recordNode!=null || setsCounter>0 || iteratorEses.hasNext()){
				doc.getDocumentElement().appendChild(verbNode);
				if(setsCounter==OAIResponse.LIMIT_PER_RESPONSE){
					if(i>0){
						doc = OAIUtils.buildResumptionToken(doc,arguments,recordCounter+OAIUtils.SPECIAL_KEY+(length-i));
					}
				}
			}else{
				return OAIUtils.getResponseError(doc,"noRecordsMatch");
			}
		}else{
			if(!(resumptionToken!=null && !resumptionToken.isEmpty() && resumptionToken.length()>5)){//number of / to be split
				return OAIUtils.getResponseError(doc,"badResumptionToken");
			}
			return OAIUtils.getResponseError(doc,"noRecordsMatch");
		}
		return doc;
	}
}
