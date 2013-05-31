package eu.apenet.dashboard.services.ead.publish.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eu.apenet.dashboard.services.ead.LinkingService;
import eu.apenet.dashboard.services.ead.publish.EADCounts;
import eu.apenet.dashboard.services.ead.publish.LevelInfo;
import eu.apenet.dashboard.services.ead.publish.PublishData;
import eu.apenet.dashboard.services.ead.publish.SolrPublisher;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;



public class DatabaseCLevelPublisher {


	public static EADCounts publish(CLevel clevel, Long eadContentId,
			Ead ead, SolrPublisher solrPublisher, List<LevelInfo> upperLevelUnittitles, Map<String, Object> fullHierarchy)
			throws Exception {
		CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
		List<LevelInfo> unittitles = new ArrayList<LevelInfo>();
		unittitles.addAll(upperLevelUnittitles);
		EADCounts eadCounts = new EADCounts();
		PublishData publishData = new PublishData();
		publishData.setXml(clevel.getXml());
		publishData.setClId(clevel.getClId());
		publishData.setParentId(clevel.getParentClId());
		publishData.setLeaf(clevel.isLeaf());
		publishData.setUpperLevelUnittitles(upperLevelUnittitles);
		publishData.setFullHierarchy(fullHierarchy);
		publishData.setOrderId(clevel.getOrderId());
		eadCounts.addClevel(solrPublisher.parseCLevel(publishData));
		unittitles.add(new LevelInfo(clevel.getClId(),clevel.getOrderId(), clevel.getUnittitle()));	
		int childOrderId = 0;
		List<CLevel> clevels = clevelDAO.findChildCLevels(clevel.getClId(), childOrderId, 1);
		while (clevels.size() > 0) {
			eadCounts.addEadCounts(DatabaseCLevelPublisher.publish(clevels.get(0),eadContentId,ead, solrPublisher, unittitles, fullHierarchy));
			childOrderId++;
			if (clevel.getHrefEadid() != null){
				LinkingService.linkWithoutCommit(ead, clevel);
			}
			clevels = clevelDAO.findChildCLevels(clevel.getClId(), childOrderId, 1);
		}
//		if (eadCounts.getNumberOfUnits() > 1){
//		System.out.println("C:" +eadCounts.getNumberOfUnits());
//		}
		return eadCounts;

	}
}
