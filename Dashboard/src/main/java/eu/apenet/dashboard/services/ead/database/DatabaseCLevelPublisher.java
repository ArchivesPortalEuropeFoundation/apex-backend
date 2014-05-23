package eu.apenet.dashboard.services.ead.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import eu.apenet.dashboard.services.ead.LinkingService;
import eu.apenet.dashboard.services.ead.publish.EADCounts;
import eu.apenet.dashboard.services.ead.publish.LevelInfo;
import eu.apenet.dashboard.services.ead.publish.PublishData;
import eu.apenet.dashboard.services.ead.publish.SolrPublisher;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;



public class DatabaseCLevelPublisher {
	private static final int NUMBER_OF_CLEVEL_ONCE = 1;

	public static EADCounts publish(CLevel clevel, Long eadContentId,
			Ead ead, SolrPublisher solrPublisher, List<LevelInfo> upperLevelUnittitles, Map<String, Object> fullHierarchy, Set<String> unitids,EadDatabaseSaver eadDatabaseSaver)
			throws Exception {
		CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
		List<LevelInfo> unittitles = new ArrayList<LevelInfo>();
		unittitles.addAll(upperLevelUnittitles);
		EADCounts eadCounts = new EADCounts();
		PublishData publishData = new PublishData();
		publishData.setXml(clevel.getXml());
		publishData.setId(clevel.getClId());
		publishData.setParentId(clevel.getParentClId());
		publishData.setLeaf(clevel.isLeaf());
		publishData.setUpperLevelUnittitles(upperLevelUnittitles);
		publishData.setFullHierarchy(fullHierarchy);
		if (StringUtils.isNotBlank(clevel.getUnitid())){
			if (unitids.contains(clevel.getUnitid())){
				publishData.setDuplicateUnitid(true);
				clevel.setDuplicateUnitid(true);
				eadDatabaseSaver.update(clevel);
			}else {
				unitids.add(clevel.getUnitid());
			}
		}

		if (publishData.getParentId() == null) {
			publishData.setOrderId(clevel.getOrderId()+1);
		}else {
			publishData.setOrderId(clevel.getOrderId());
		}
		eadCounts.addClevel(solrPublisher.parseCLevel(publishData));
		if ((ead instanceof SourceGuide || ead instanceof HoldingsGuide) && clevel.getHrefEadid() != null){
			eadDatabaseSaver.insert(LinkingService.getNewLink(ead, clevel));
		}
		unittitles.add(new LevelInfo(clevel.getClId(),clevel.getOrderId(), clevel.getUnittitle()));	
		int childOrderId = 0;
		List<CLevel> clevels = clevelDAO.findChildCLevels(clevel.getClId(), childOrderId, NUMBER_OF_CLEVEL_ONCE);
		while (clevels.size() > 0) {
			eadCounts.addEadCounts(DatabaseCLevelPublisher.publish(clevels.get(0),eadContentId,ead, solrPublisher, unittitles, fullHierarchy, unitids,eadDatabaseSaver));
			childOrderId++;
			clevels = clevelDAO.findChildCLevels(clevel.getClId(), childOrderId, NUMBER_OF_CLEVEL_ONCE);
		}
		return eadCounts;

	}
}
