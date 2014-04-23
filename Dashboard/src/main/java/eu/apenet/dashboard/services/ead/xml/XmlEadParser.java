package eu.apenet.dashboard.services.ead.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import eu.apenet.commons.solr.SolrFields;
import eu.apenet.commons.solr.SolrValues;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.ead.publish.EADCounts;
import eu.apenet.dashboard.services.ead.publish.LevelInfo;
import eu.apenet.dashboard.services.ead.publish.PublishData;
import eu.apenet.dashboard.services.ead.publish.EadSolrPublisher;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

public class XmlEadParser extends AbstractParser {
	private static Logger LOG = Logger.getLogger(XmlEadParser.class);
	public static final String UTF_8 = "utf-8";
	public static final QName EAD = new QName(APENET_EAD, "ead");
	private static final QName UNITTITLE = new QName(APENET_EAD, "unittitle");
	private static final QName TITLEPROPER = new QName(APENET_EAD, "titleproper");



    public static void parseEad(Ead ead) throws Exception {
        EadContent oldEadContent = ead.getEadContent();
        if (oldEadContent == null){
        	parse(ead, null);
        }
	}

    public static long parseEadAndPublish(Ead ead) throws Exception {
        EadContent oldEadContent = ead.getEadContent();
        if (oldEadContent == null){
        	return parse(ead, new EadSolrPublisher(ead));
        }
        return 0l;
    }

	private static long parse(Ead ead, EadSolrPublisher solrPublisher) throws Exception {
		int cOrderId = 0;
		EadContent eadContent = new EadContent();
        if (solrPublisher != null)
			eadContent.setVisible(true);

		FileInputStream fileInputStream = null;
		List<LevelInfo> upperLevels = new ArrayList<LevelInfo>();
		ArchivalInstitution ai = ead.getArchivalInstitution();
		Map<String, Object> fullHierarchy = new HashMap<String, Object>();

        if(ead instanceof FindingAid) {
            eadContent.setFaId(ead.getId());
        } else if(ead instanceof HoldingsGuide) {
            eadContent.setHgId(ead.getId());
        } else if(ead instanceof SourceGuide) {
            eadContent.setSgId(ead.getId());
        }

        eadContent.setEadid(ead.getEadid());
        upperLevels.add(new LevelInfo(ead.getId()));
        String initialFilePath = ead.getPathApenetead();
        fileInputStream = getFileInputStream(ead.getPathApenetead());


		String eadid = eadContent.getEadid();
		if (solrPublisher != null) {
			List<ArchivalInstitution> ais = new ArrayList<ArchivalInstitution>();
			while (ai != null) {
				ais.add(ai);
				ai = ai.getParent();
			}
			int depth = 0;
			for (int i = ais.size() - 1; i >= 0; i--) {
				ArchivalInstitution currentAi = ais.get(i);
				String id = SolrValues.AI_PREFIX + currentAi.getAiId();
				String newFacetField = currentAi.getAiname();
				if (currentAi.isGroup()) {
					newFacetField += EadSolrPublisher.COLON + SolrValues.TYPE_GROUP;
				} else {
					newFacetField += EadSolrPublisher.COLON + SolrValues.TYPE_LEAF;
				}
				newFacetField += EadSolrPublisher.COLON + id;
				fullHierarchy.put(SolrFields.AI_DYNAMIC + depth + SolrFields.DYNAMIC_STRING_SUFFIX, newFacetField);
				fullHierarchy.put(SolrFields.AI_DYNAMIC_ID + depth + SolrFields.DYNAMIC_STRING_SUFFIX, id);
				depth++;
			}

		}
		XMLStreamReader xmlReader = getXMLReader(fileInputStream);
		StringWriter stringWriter = new StringWriter();
		XMLStreamWriterHolder xmlWriterHolder = new XMLStreamWriterHolder(XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter));
		QName lastElement = null;
		EADCounts eadCounts = new EADCounts();
		boolean noCLevelFound = true;
		Long ecId = null;
		try {
			JpaUtil.beginDatabaseTransaction();
			for (int event = xmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlReader.next()) {
				if (event == XMLStreamConstants.START_ELEMENT) {
					QName elementName = xmlReader.getName();
					lastElement = xmlReader.getName();
					if (EAD.equals(elementName)) {
						xmlWriterHolder.writeEAD();
					} else if (XmlCLevelParser.CLEVEL.equals(elementName)) {
						if (noCLevelFound){
							noCLevelFound = false;
							xmlWriterHolder.close();
							eadContent.setXml(stringWriter.toString());
							JpaUtil.getEntityManager().persist(eadContent);
							stringWriter.close();
							stringWriter = null;
							
                            if(solrPublisher != null){
                    			PublishData publishData = new PublishData();
                    			publishData.setXml(eadContent.getXml());
                				publishData.setId(ead.getId().longValue());
                				publishData.setUpperLevelUnittitles(upperLevels);
                				publishData.setFullHierarchy(fullHierarchy);
                				publishData.setArchdesc(true);
                				eadCounts.addNumberOfDAOs(solrPublisher.parseHeader(eadContent, publishData));
                            }
							ecId  = eadContent.getEcId();
							eadContent = null;
						}						
						eadCounts.addEadCounts(XmlCLevelParser.parse(xmlReader, ecId, null, cOrderId++, ead, solrPublisher, upperLevels, fullHierarchy));
					} else {
						xmlWriterHolder.writeStartElement(xmlReader);
					}
				} else if (event == XMLStreamConstants.END_ELEMENT) {
					xmlWriterHolder.closeElement();
				} else if (event == XMLStreamConstants.CHARACTERS) {
					if (UNITTITLE.equals(lastElement)) {
						if (eadContent != null && eadContent.getUnittitle() == null) {
							eadContent.setUnittitle(limitTitle(xmlReader.getText()));
						}
						lastElement = null;

					} else if (TITLEPROPER.equals(lastElement)) {
						if (eadContent != null && eadContent.getTitleproper() == null) {
							eadContent.setTitleproper(limitTitle(xmlReader.getText()));
						}
						lastElement = null;

					}
					xmlWriterHolder.writeCharacters(xmlReader);
				} else if (event == XMLStreamConstants.CDATA) {
					xmlWriterHolder.writeCData(xmlReader);
				}
			}
			xmlReader.close();
			fileInputStream.close();
			
			if (noCLevelFound){
				noCLevelFound = false;
				xmlWriterHolder.close();
				eadContent.setXml(stringWriter.toString());
				JpaUtil.getEntityManager().persist(eadContent);
				stringWriter = null;
                if(solrPublisher != null){
        			PublishData publishData = new PublishData();
        			publishData.setXml(eadContent.getXml());
    				publishData.setId(ead.getId().longValue());
    				publishData.setUpperLevelUnittitles(upperLevels);
    				publishData.setFullHierarchy(fullHierarchy);
    				publishData.setArchdesc(true);
    				eadCounts.addNumberOfDAOs(solrPublisher.parseHeader(eadContent, publishData));
                }
				ecId  = eadContent.getEcId();
				eadContent = null;
			}									
			
			
			if (solrPublisher != null) {
				solrPublisher.commitAll(eadCounts);
			}
			JpaUtil.commitDatabaseTransaction();
		
		} catch (Exception de) {
			if ((initialFilePath!=null) &&(initialFilePath.contains(APEnetUtilities.FILESEPARATOR))){
                LOG.error("Something has happened in parse method: " + de.getMessage(),de);
			}
			JpaUtil.rollbackDatabaseTransaction();
			if (solrPublisher != null) {
				LOG.error(eadid + ": rollback:", de);
				solrPublisher.rollback();
			}
			throw de;
		}
		if (solrPublisher != null) {
			return solrPublisher.getSolrTime();
		}else {
			return 0;
		}
	}

	private static FileInputStream getFileInputStream(String path) throws FileNotFoundException, XMLStreamException {
		File file = new File(APEnetUtilities.getConfig().getRepoDirPath() + path);
		return new FileInputStream(file);
	}

	private static XMLStreamReader getXMLReader(FileInputStream fileInputStream) throws FileNotFoundException,
			XMLStreamException {
		XMLInputFactory inputFactory = (XMLInputFactory) XMLInputFactory.newInstance();
		return (XMLStreamReader) inputFactory.createXMLStreamReader(fileInputStream, UTF_8);
	}


	protected static class IndexData {
		private long startIndex = -1;
		private long endIndex = -1;
		private long currentIndex = -1;

		public IndexData(long startIndex, long endIndex) {
			this.startIndex = startIndex;
			this.endIndex = endIndex;

		}

		public long getCurrentIndex() {
			if (currentIndex == -1) {
				this.currentIndex = startIndex;
			} else if (currentIndex < (endIndex - 1)) {
				currentIndex++;
			}
			return currentIndex;
		}

	}

}
