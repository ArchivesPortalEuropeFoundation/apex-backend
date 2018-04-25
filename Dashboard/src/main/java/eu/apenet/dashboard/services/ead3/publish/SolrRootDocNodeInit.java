/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3.publish;

import eu.apenet.commons.solr.Ead3SolrFields;
import eu.apenet.persistence.vo.CLevel;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author kaisar
 */
public class SolrRootDocNodeInit {

    private SolrDocNode docNode;
    private Set<CLevel> cLevels;
    private Map<String, Long> unitIdvsIdMap;

    public SolrRootDocNodeInit(SolrDocNode root, Set<CLevel> levels) {
        this.docNode = root;
        this.cLevels = levels;
        this.unitIdvsIdMap = levels.stream().collect(Collectors.toMap(CLevel::getUnitid, CLevel::getId));
        this.processTree(this.docNode);

    }

    public SolrDocNode getProcessedNode() {
        return this.docNode;
    }

    private void processTree(SolrDocNode root) {
        SolrDocNode currentNode = root.getChild();
        if (currentNode != null) {
            processTree(currentNode);
        }
        currentNode = root.getSibling();
        if (currentNode != null) {
            processTree(currentNode);
        }
        processNode(root);

    }

    private void processNode(SolrDocNode node) {
        Long idValue = unitIdvsIdMap.get(node.getDataElement(Ead3SolrFields.UNIT_ID).toString());
        if (null != idValue) {
            node.setDataElement(Ead3SolrFields.ID, "C" + idValue);
        }
//        this.cLevels.stream().filter((cl) -> (StringUtils.equals(cl.getUnitid(), node.getDataElement(Ead3SolrFields.UNIT_ID).toString()))).forEachOrdered((cl) -> {
//            node.setDataElement(Ead3SolrFields.ID, "C" + cl.getId());
//        });
    }
}
