package eu.apenet.dashboard.infraestructure;

import java.util.HashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Jara Alvarez
 * 23rd February 2011
 */

//This class contains an items list of the one country archival landscape
//The item is also another class with an specified structure
public class ArchivalLandscapeStructure {
	
	private final Logger log = Logger.getLogger(getClass());
	private List <ArchivalLandscapeNode> alNodes = new ArrayList<ArchivalLandscapeNode>();
	private ArchivalLandscapeNode alNode;
	private int nodeId=0;
		
	public List <ArchivalLandscapeNode> archivalLandscapeStructure(NodeList cList){
				
		for (int i=0;i< cList.getLength();i++)
			this.getStructuredList(cList.item(i), null, null, null);
	        		
        return alNodes;
	}
	
	private void getStructuredList(Node node, Boolean is_group, String parent_name, String parent_internal_al_id) {
		
		NodeList listDid;
		NamedNodeMap attributes;
		Node attribute;
		Map<String, String> names = new HashMap<String, String>();	
		Map<String, Boolean> primaryName = new HashMap<String,Boolean>();
		Boolean stored=false;
		String internal_al_id = null;
		Boolean primary = false;
	
		try {
	        attributes = node.getAttributes();
	        attribute = attributes.getNamedItem("level");	        
	               
	        internal_al_id = attributes.getNamedItem("id").getNodeValue();	        
	        
	        //It's a group 
	        if ((attribute.getNodeValue().equals("series")))        	
	        {   
	        	if (node.getParentNode().getAttributes().getNamedItem("level").getNodeValue().equals("fonds"))
	        		parent_internal_al_id = null;
	        	else
	        		parent_internal_al_id = node.getParentNode().getAttributes().getNamedItem("id").getNodeValue();
	        	listDid = node.getChildNodes();	
	            for (int i = 0; i < listDid.getLength(); i++) 
	            {
	            	if (listDid.item(i).getNodeName() == "did") 
	                { 
	            		primary= false;
	            		NodeList listUnitTitle = listDid.item(i).getChildNodes();
	                    for (int j = 0; j < listUnitTitle.getLength(); j++) 
	                    {  	                    	
	                    	//Languages
	                    	if (listUnitTitle.item(j).hasAttributes()){
		                    	attributes = listUnitTitle.item(j).getAttributes();
		                        attribute = attributes.getNamedItem("type");
		                    	names.put(attribute.getNodeValue(), listUnitTitle.item(j).getTextContent().trim());
			                    if ((listUnitTitle.item(j).getNodeType() == Node.ELEMENT_NODE)&&(!primary))
			                    {
			                    	primary = true;
			                		primaryName.put(attribute.getNodeValue(), primary);
			                    }		                    	int it=nodeId;
		                    	while (it -1 >= 0){
		                     		if (alNodes.get(it-1).getInternal_al_id().contains(internal_al_id))
		                     			stored=true;
		                     		it--;
		                    	}	                    	
	                    	}
	                    }                    
	                    if (!stored){ 
	                    	if (parent_name==null)
	                    		alNode = new ArchivalLandscapeNode(nodeId,names,true,null, internal_al_id, parent_internal_al_id, primaryName);
	                    	else 
	                    		alNode = new ArchivalLandscapeNode(nodeId,names,true,parent_name, internal_al_id, parent_internal_al_id, primaryName);
		                   	alNodes.add(alNode);                   	
		                   	parent_name = listDid.item(i).getTextContent().trim();
		                   	nodeId++;
	                    }
	                }
	                else if (listDid.item(i).getNodeName() == "c")
	                {
	                	attributes = listDid.item(i).getAttributes();
	                	attribute = attributes.getNamedItem("level");
	                	if ((attribute.getNodeValue().equals("series")))
	                		getStructuredList(listDid.item(i), true, parent_name, parent_internal_al_id);
	                	else if ((attribute.getNodeValue().equals("file")))
	                		getStructuredList(listDid.item(i),false, parent_name, parent_internal_al_id);                			
	                }
	            }             
	        }
	        //It's an institution
	        else if ((attribute.getNodeValue().equals("file"))) 
	        {    	 
	        	if (node.getParentNode().getAttributes().getNamedItem("level").getNodeValue().equals("fonds"))
	        		parent_internal_al_id = null;
	        	else
	        		parent_internal_al_id = node.getParentNode().getAttributes().getNamedItem("id").getNodeValue();
	        	listDid= node.getChildNodes();
	        	for (int l = 0; l < listDid.getLength(); l++) 
	            {
	        		if (listDid.item(l).getNodeName() == "did") 
	                { 
	        			primary = false;
		                 NodeList listUnitTitle = listDid.item(l).getChildNodes();
		                 for (int j = 0; j < listUnitTitle.getLength(); j++) 
		                 {   
		                	//Languages
		                	if ((listUnitTitle.item(j).hasAttributes()) && (listUnitTitle.item(j).getNodeName().equals("unittitle"))){
			                 	attributes = listUnitTitle.item(j).getAttributes();
			                    attribute = attributes.getNamedItem("type");
			                    names.put(attribute.getNodeValue(), listUnitTitle.item(j).getTextContent().trim());
			                    if ((listUnitTitle.item(j).getNodeType() == Node.ELEMENT_NODE)&&(!primary))
			                    {
			                    	primary = true;
			                		primaryName.put(attribute.getNodeValue(), primary);
			                    }
			                 	int it=nodeId;
			                 	while (it -1 >= 0){
			                 		if (alNodes.get(it-1).getInternal_al_id().contains(internal_al_id))
			                 			stored=true;
			                 		it--;
			                 	}
			                 }
		                 }
			             if (!stored){ 
			                  alNode = new ArchivalLandscapeNode(nodeId,names,false,parent_name, internal_al_id, parent_internal_al_id, primaryName);
				              alNodes.add(alNode);
				              nodeId++; 	
			            }
	                }	        		
	            }
	        }
		}catch(Exception e){
			log.error("ArchivalLandscapeStructure: It has happened some errors during the archival landscape structure building process" + e);
		}
}
	
	

}
