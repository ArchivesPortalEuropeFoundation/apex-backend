package eu.apenet.dashboard.indexing;

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

import eu.apenet.persistence.vo.*;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import eu.apenet.commons.solr.SolrFields;
import eu.apenet.commons.solr.SolrValues;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.EadContentDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;

public class EADParser extends AbstractParser {
	private static Logger LOG = Logger.getLogger(EADParser.class);
	public static final String UTF_8 = "utf-8";
	public static final QName EAD = new QName(APENET_EAD, "ead");
	private static final QName UNITTITLE = new QName(APENET_EAD, "unittitle");
	private static final QName TITLEPROPER = new QName(APENET_EAD, "titleproper");

    @Deprecated
	public static void parseHoldingGuide(HoldingsGuide holdingsGuide) throws Exception {
		parseEad(holdingsGuide);
	}

    @Deprecated
	public static void parseFindingAid(FindingAid findingAid) throws Exception {
		parseEad(findingAid);
	}

    public static void parseEad(Ead ead) throws Exception {
		parse(ead, null);
	}

    @Deprecated
	public static void parseHoldingGuideAndIndex(HoldingsGuide holdingsGuide) throws Exception {
		parseEadAndIndex(holdingsGuide);
	}

    @Deprecated
	public static void parseFindingAidAndIndex(FindingAid findingAid) throws Exception {
		parseEadAndIndex(findingAid);
	}

    public static void parseEadAndIndex(Ead ead) throws Exception {
        parse(ead, new Indexer(ead));
    }

	private static void parse(Ead ead, Indexer indexer) throws Exception {
		long startTime = System.currentTimeMillis();
		EadContentDAO dao = DAOFactory.instance().getEadContentDAO();
		int cOrderId = 0;
		EadContent eadContent = new EadContent();

        EadContent oldEadContent = ead.getEadContent();
        if (oldEadContent != null)
            dao.delete(oldEadContent);

        if (indexer != null)
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
		if (indexer != null) {
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
					newFacetField += Indexer.COLON + SolrValues.TYPE_GROUP;
				} else {
					newFacetField += Indexer.COLON + SolrValues.TYPE_LEAF;
				}
				newFacetField += Indexer.COLON + id;
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
			HibernateUtil.beginDatabaseTransaction();
			for (int event = xmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = xmlReader.next()) {
				if (event == XMLStreamConstants.START_ELEMENT) {
					QName elementName = xmlReader.getName();
					lastElement = xmlReader.getName();
					if (EAD.equals(elementName)) {
						xmlWriterHolder.writeEAD();
					} else if (CLevelParser.CLEVEL.equals(elementName)) {
						if (noCLevelFound){
							noCLevelFound = false;
							xmlWriterHolder.close();
							eadContent.setXml(stringWriter.toString());
							HibernateUtil.getDatabaseSession().save(eadContent);
							stringWriter = null;
                            if(indexer != null)
                            	eadCounts.addNumberOfDAOs(indexer.parseHeader(eadContent));
							ecId  = eadContent.getEcId();
							eadContent = null;
						}						
						eadCounts.addEadCounts(CLevelParser.parse(xmlReader, ecId, null, cOrderId++, indexer, upperLevels, fullHierarchy));
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
				HibernateUtil.getDatabaseSession().save(eadContent);
				stringWriter = null;
                if(indexer != null)
                	eadCounts.addNumberOfDAOs(indexer.parseHeader(eadContent));
				ecId  = eadContent.getEcId();
				eadContent = null;
			}									
			
			
			if (indexer != null) {
				indexer.commitAll(eadCounts);
			}
			HibernateUtil.commitDatabaseTransaction();
			if (indexer != null && ead instanceof HoldingsGuide) {
				ContentUtils.addLinkHGtoAL((HoldingsGuide)ead);
			}
			LOG.info(eadid + ": Total time: " + (System.currentTimeMillis() - startTime));
			
		} catch (Exception de) {
			if ((initialFilePath!=null) &&(initialFilePath.contains(APEnetUtilities.FILESEPARATOR))){
                LOG.error("Something has happened in parse method: " +de);
                LOG.error("Starting Parse method rollback... - but we will not move the file back to TMP since it should stay in REPO always");
			}
			
			HibernateUtil.rollbackDatabaseTransaction();
			HibernateUtil.closeDatabaseSession();
			if (indexer != null) {
				LOG.error(eadid + ": rollback:", de);
				indexer.rollback();
			}
			throw de;
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
