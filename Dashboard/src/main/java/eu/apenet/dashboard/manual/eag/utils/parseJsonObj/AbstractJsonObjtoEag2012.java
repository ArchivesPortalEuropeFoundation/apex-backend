package eu.apenet.dashboard.manual.eag.utils.parseJsonObj;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import eu.apenet.dashboard.manual.eag.Eag2012;

/**
 * Abstract class for objects {@link  JSONObject} JSONObject to {@link Eag2012>} Eag2012
 */
public abstract class AbstractJsonObjtoEag2012 {
	private final Logger log2 = Logger.getLogger(getClass());

	/**
	 * Method for replace if exists special string to return a correct string
	 * @param field {@link String} Field
	 * @return {@link String} Field
	 */
	public String replaceIfExistsSpecialReturnString(String field){
		this.log2.debug("Method start: \"replaceIfExistsSpecialReturnString\"");
		if(field!=null && !field.isEmpty()){
			while(field.contains(Eag2012.SPECIAL_RETURN_STRING_N)){
					field = field.replace(Eag2012.SPECIAL_RETURN_STRING_N,"\n");
				}				while(field.contains(Eag2012.SPECIAL_RETURN_STRING_R)){
			field = field.replace(Eag2012.SPECIAL_RETURN_STRING_R,"\r");
			}
			while(field.contains(Eag2012.SPECIAL_RETURN_APOSTROPHE)){
				field = field.replace(Eag2012.SPECIAL_RETURN_APOSTROPHE,"\'");
			}
		}
		this.log2.debug("End method: \"replaceIfExistsSpecialReturnString\"");
		return field;
	}

	/**
	 * Method for decodes the string and returns
	 * @param escapeString {@link String} the escapeString
	 * @return {@link String}
	 */
	public String unescapeJsonString(final String escapeString) {
		this.log2.debug("Method start: \"unescapeJsonString\"");
		String unescapeString = escapeString;	
		try {
			unescapeString = URLDecoder.decode(escapeString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log2.error("No date decode possible.");
		}	
		this.log2.debug("End method: \"unescapeJsonString\"");
		return StringEscapeUtils.unescapeHtml(unescapeString);
	}

	/**
	 * Method that checks if it have to create or fill it with data the {@link Map<String, Map<String, Map<String, List<List<String>>>>>} 'Map<String, Map<String, Map<String, List<List<String>>>>>'
	 * @param mapMapMapList {@link Map<String, Map<String, Map<String, List<List<String>>>>>}
	 * @param listMapMapList {@link List<Map<String,Map<String,Map<String,List<List<String>>>>>>}
	 * @param i {@link int} position of the object {@link JSONObject} JSONObject in the Table
	 * @return {@link Map<String, Map<String, Map<String, List<List<String>>>>>> mapMapMapList} numMapMapMapList
	 */
	public Map<String, Map<String, Map<String, List<List<String>>>>> createMapMapMapList(Map<String, Map<String, Map<String, List<List<String>>>>> mapMapMapList,List<Map<String,Map<String,Map<String,List<List<String>>>>>> listMapMapList, int i){
		this.log2.debug("Method start: \"createMapMapMapList\"");
		if(listMapMapList.size()>i && listMapMapList.get(i)!=null){
			mapMapMapList = listMapMapList.get(i);	
		}else{
			mapMapMapList = new HashMap<String, Map<String, Map<String, List<List<String>>>>>();
		 }
		this.log2.debug("End method: \"createMapMapMapList\"");
		return mapMapMapList;
	}

	/**
	 * Method that checks if it have to create or fill it with data the {@link Map<String, Map<String, Map<String, List<String>>>} 'Map<String, Map<String, Map<String, List<String>>>'
	 * @param numMapMapMapList {@link Map<String, Map<String, Map<String, List<String>>>>}
	 * @param DataValueListMapMapListS <List<Map<String,Map<String,Map<String,List<String>>>>>}
	 * @param i {@link  int} position of the object {@link JSONObject} JSONObject in the Table
	 * @return numMapMapMapList {@link Map<String, Map<String, Map<String, List<String>>>>}
	 */
	public Map<String, Map<String, Map<String, List<String>>>> createMapMapMapList2(Map<String, Map<String, Map<String, List<String>>>> numMapMapMapList,List<Map<String,Map<String,Map<String,List<String>>>>> DataValueListMapMapListS,int i){
		this.log2.debug("Method start: \"createMapMapMapList2\"");
		if(DataValueListMapMapListS.size()>i && DataValueListMapMapListS.get(i)!=null){
			 numMapMapMapList = DataValueListMapMapListS.get(i);
		 }else{
			 numMapMapMapList = new HashMap<String, Map<String, Map<String, List<String>>>>();
		 }
		this.log2.debug("End method: \"createMapMapMapList2\"");
		return numMapMapMapList;
	}

	/**
	 * Method that checks if it have to create or fill it with data the {@link Map<String, Map<String, List<List<String>>>>} 'Map<String, Map<String, List<List<String>>>>'
	 * @param MapMapMapList {@link Map<String, Map<String, Map<String, List<List<String>>>>>}
	 * @param MapMapList {@link Map<String, Map<String, List<List<String>>>>}
	 * @param key {@link String} Constants for TABs indexes
	 * @return MapMapList {@link Map<String, Map<String, List<List<String>>>>}
	 */
	public  Map<String, Map<String, List<List<String>>>> createMapMapList_list(Map<String, Map<String, Map<String, List<List<String>>>>> MapMapMapList,Map<String, Map<String, List<List<String>>>> MapMapList, String key ){			
		this.log2.debug("Method start: \"createMapMapList_list\"");
		if(MapMapMapList.size()>0 && MapMapMapList.get(key)!=null){
			 MapMapList= MapMapMapList.get(key);
		 }else{
			 MapMapList = new HashMap<String, Map<String, List<List<String>>>>();
		 }	
		this.log2.debug("End method: \"createMapMapList_list\"");
		return MapMapList;			
	}

	/**
	 * Method that checks if it have to create or fill it with data the {@link Map<String, Map<String, List<String>>>} 'Map<String, Map<String, List<String>>>'
	 * @param ListMapList {@link List<Map<String, Map<String, List<String>>>>}
	 * @param MapMapList {@link Map<String, Map<String, List<String>>>}
	 * @param numMapMapList {@link int} position of the object {@link JSONObject} JSONObject in the Table
	 * @return MapMapList {@link Map<String, Map<String, List<String>>>}
	 */
	public  Map<String, Map<String, List<String>>> createMapMapList(List<Map<String, Map<String, List<String>>>> ListMapList,Map<String, Map<String, List<String>>> MapMapList,int numMapMapList){	
		this.log2.debug("Method start: \"createMapMapList\"");
		if(ListMapList.size()>numMapMapList && ListMapList.get(numMapMapList)!=null){ 
			MapMapList = ListMapList.get(numMapMapList);
		}else{
			MapMapList = new HashMap<String, Map<String, List<String>>>();
		}	
		this.log2.debug("End method: \"createMapMapList\"");
		return MapMapList;			
	}

	/**
	 * Method that checks if it have to create or fill it with data the {@link Map<String, Map<String, List<String>>>} 'Map<String, Map<String, List<String>>>'
	 * @param MapMapMapList {@link Map<String, Map<String, Map<String, List<String>>>>}
	 * @param mapMapList {@link Map<String, Map<String, List<String>>>}
	 * @param key {@link String} , Constant for TABs indexes.
	 * @param i {@link int} position of the object {@link JSONObject} JSONObject int the Table
	 * @returnmapMapList {@link Map<String, Map<String, List<String>>>}
	 */
	public  Map<String, Map<String, List<String>>> createMapMapList2(Map<String, Map<String, Map<String, List<String>>>> MapMapMapList,Map<String, Map<String, List<String>>> mapMapList,String key,int i){			
		this.log2.debug("Method start: \"createMapMapList2\"");
		if(MapMapMapList.size()>i && MapMapMapList.get(key)!=null){
			 mapMapList = MapMapMapList.get(key);
		 }else{
			 mapMapList = new HashMap<String, Map<String, List<String>>>();
		 }	
		this.log2.debug("End method: \"createMapMapList2\"");
		return mapMapList;			
	}

	/**
	 * Method that checks if it have to create or fill it with data the {@link Map<String, List<List<String>>} 'Map<String, List<List<String>>'
	 * @param MapMapList {@link Map<String, Map<String, List<List<String>>>>}
	 * @param MapList {@link Map<String, List<List<String>>>}
	 * @param key {@link String} Constant for section indexes
	 * @return MapList {@link Map<String, List<List<String>>>}
	 */
	public  Map<String, List<List<String>>> createMapList_list(Map<String, Map<String, List<List<String>>>> MapMapList, Map<String, List<List<String>>> MapList,String key){
		this.log2.debug("Method start: \"createMapList_list\"");
		if(MapMapList.size()>0 && MapMapList.get(key)!=null){
			 MapList = MapMapList.get(key);
		 }else{
			 MapList = new HashMap<String, List<List<String>>>();
		 }
		this.log2.debug("End method: \"createMapList_list\"");
		 return MapList;
	}

	/**
	 * Method that checks if it have to create or fill it with data the {@link Map<String, List<String>>>} 'Map<String, List<String>'
	 * @param listList {@link List<Map<String,List<String>>}
	 * @param mapList {@link Map<String, List<String>>}
	 * @param z {@link int} position of the object {@link JSONObject} JSONObject int the Table
	 * @return mapList {@link Map<String, List<String>>}
	 */
	public Map<String, List<String>> createMapList(List<Map<String,List<String>>> listList,Map<String, List<String>> mapList,int z){
		this.log2.debug("Method start: \"createMapList\"");
		if(listList.size()>z && listList.get(z)!=null){
			mapList = listList.get(z);
		}else{
			mapList = new HashMap<String, List<String>>();
		}	
		this.log2.debug("End method: \"createMapList\"");
		return mapList;
	}

	/**
	 * Method that checks if it have to create or fill it with data the {@link Map<String, List<String>>>} 'Map<String, List<String>'
	 * @param mapMapList {@link Map<String, Map<String, List<String>>>}
	 * @param mapList {@link Map<String, List<String>>}
	 * @param key {@link String} Constant for TABs indexes
	 * @param i {@link int} position of the {@link JSONObject} object JSONObject in the Table
	 * @return mapList {@link Map<String, List<String>>}
	 */
	public Map<String, List<String>> createMapList(Map<String, Map<String, List<String>>> mapMapList,Map<String, List<String>> mapList, String key,int i){
		this.log2.debug("Method start: \"createMapList\"");
		if(mapMapList.size()>i && mapMapList.get(key)!=null){
			mapList = mapMapList.get(key);
		}else{
			mapList = new HashMap<String, List<String>>();
		}	
		this.log2.debug("End method: \"createMapList\"");
		return mapList;
	}

	/**
	 * Method that checks if it have to create or fill it with data the {@link Map<String, List<String>} 'Map<String, List<String>'
	 * @param mapMapList {@link Map<String, Map<String, List<String>>>}
	 * @param mapList {@link Map<String, List<String>>}
	 * @param key {@link String} Constant for TABs indexes
	 * @return mapList {@link Map<String, List<String>>}
	 */
	public Map<String, List<String>> createMapListTemp(Map<String, Map<String, List<String>>> mapMapList,Map<String, List<String>> mapList, String key){
		this.log2.debug("Method start: \"createMapListTemp\"");
		if(mapMapList.size()>0 && mapMapList.get(key)!=null){
			mapList = mapMapList.get(key);
		}else{
			mapList = new HashMap<String, List<String>>();
		}	
		this.log2.debug("End method: \"createMapListTemp\"");
		return mapList;
	}

	/**
	 * Method that checks if it have to create or fill it with data {@link List<List<String>>} 'List<List<String>>'
	 * @param MapList {@link Map<String, List<List<String>>>}
	 * @param List {@link List<List<String>>}
	 * @param key {@link String} Constant for subsection indexes
	 * @return List {@link List<List<String>>}
	 */ 
	public List<List<String>> createList_list(Map<String, List<List<String>>> MapList,List<List<String>> List,String key){
		this.log2.debug("Method start: \"createList_list\"");
		if(MapList.size()>0 && MapList.get(key)!=null){
			 List= MapList.get(key);
		 }else{
			 List = new ArrayList<List<String>>();
		 }
		this.log2.debug("End method: \"createList_list\"");
		return List;
	}

	/**
	 * Method that checks if it have to create or fill it with data the {@link List<String>} 'List<String>'
	 * @param List_list {@link List<List<String>>}
	 * @param List {@link List<String>}
	 * @param i {@link int} position of the object {@link JSONObject} JSONObject int the Table
	 * @return  List {@link List<String>}
	 */
	public List<String> createList2(List<List<String>> List_list,List<String> List, int i){
		this.log2.debug("Method start: \"createList2\"");
		if(List_list.size()>i && List_list.get(i)!=null){
			List = List_list.get(i);
		 }else{
			 List = new ArrayList<String>();
		 }
		this.log2.debug("End method: \"createList2\"");
		return List;
	}

	/**
	 * Method that checks if it have to create or fill it with data the {@link List<String>} 'List<String>'
	 * @param mapList {@link Map<String, List<String>>}
	 * @param list {@link List<String>}
	 * @param key {@link String} Constant for TABs indexes
	 * @param i {@link int} position of the object {@link JSONObject} JSONObject int the Table
	 * @return List {@link List<String>}
	 */
	public List<String> createList(Map<String, List<String>> mapList,List<String> list, String key,int i){
		this.log2.debug("Method start: \"createList\"");
		if((mapList.size()>i|| !mapList.isEmpty()) && mapList.get(key)!=null){
			list = mapList.get(key);
		}else{
			list = new ArrayList<String>();
		}
		this.log2.debug("End method: \"createList\"");
		return list;
	}

	/**
	 * Method that checks if it have to create or fill it with data  {@link Map<String, String>} 'Map<String, String>'
	 * @param listString {@link List<Map<String, String>>}
	 * @param mapString {@link Map<String, String>}
	 * @param i {@link int} position of the object {@link JSONObject} JSONObject in the Table
	 * @return mapString {@link Map<String, String>}
	 */
	public Map<String, String> createListString(List<Map<String, String>> listString,Map<String, String> mapString,int i){
		this.log2.debug("Method start: \"createListString\"");
		if(listString.size()>i && listString.get(i)!=null){
			mapString = listString.get(i);
		}else{
			mapString = new HashMap<String, String>();
		}
		this.log2.debug("End method: \"createListString\"");
		return mapString;
	}

	/**
	 * Method that checks if it have to create if the object is null and fill it with data the {@link List<Map<String,List<String>>>} 'List<Map<String,List<String>>>'
	 * @param listListData {@link List<Map<String,List<String>>>}
	 * @param keyTab {@link String} Constant for TABs indexes
	 * @param value {@link String} value of the object
	 * @param i {@link int}position of the object {@link JSONObject} JSONObject in the Table
	 * @param numMapList {@link int} position of the object {@link JSONObject} JSONObject in the Table on 'Map<String, List<String>>'
	 * @param numList {@link int} position of the object {@link JSONObject} JSONObject in the Table on 'List<String>'
	 * @return listListData {@link List<Map<String,List<String>>>}
	 */
	public List<Map<String,List<String>>> createData(List<Map<String,List<String>>> listListData, String keyTab,String value,int i,int numMapList,int numList){
		this.log2.debug("Method start: \"createData\"");
		if(listListData==null){
				listListData = new ArrayList<Map<String,List<String>>>();
			}
			Map<String, List<String>> mapListData = null;
			mapListData=createMapList(listListData,mapListData,numMapList);				
			List<String> listData = null;				
			listData = createList(mapListData,listData,keyTab,numList);
			listData.add(replaceIfExistsSpecialReturnString(value));
			mapListData.put(keyTab, listData);
		    if(listListData.size() > i){
		    	listListData.set(i,mapListData);
		    }else{
		    	listListData.add(mapListData);
		    }
		    this.log2.debug("End method: \"createData\"");
		return listListData;
	}

	/**
	 * Method that checks if it have to create if the object is null<br> 
	 * and fill it with data the {@link List<Map<String, Map<String, List<String>>>>} 'List<Map<String, Map<String, List<String>>>>'
	 * @param descriptiveNotePValue {@link List<Map<String, Map<String, List<String>>>>}
	 * @param key {@link String} Constant for TABs indexes
	 * @param key2 {@link String} Constant for section indexes
	 * @param value {@link String} value of the object
	 * @param i {@link int} position of the object {@link JSONObject} JSONObject in the Table
	 * @param nListMapList {@link int} position of the object {@link JSONObject} JSONObject in the Table on 'List<Map<String, Map<String, List<String>>>>'
	 * @param nMapMapList {@link int} position of the object {@link JSONObject} JSONObject in the Table on 'Map<String, Map<String, List<String>>>'
	 * @param nMapList {@link int} position of the object {@link JSONObject} JSONObject in the Table on 'Map<String, List<String>>'
	 * @return descriptiveNotePValue {@link List<Map<String, Map<String, List<String>>>>}
	 */
	public List<Map<String, Map<String, List<String>>>> createListMapList(List<Map<String, Map<String, List<String>>>> descriptiveNotePValue, String key,String key2,String value,int i,int nListMapList, int nMapMapList, int nMapList){
		this.log2.debug("Method start: \"createListMapList\"");
		if(descriptiveNotePValue==null){
			 descriptiveNotePValue = new ArrayList<Map<String, Map<String, List<String>>>>();
		 }
		 Map<String, Map<String, List<String>>> descriptiveNoteMapMapList = null;			
		 descriptiveNoteMapMapList = createMapMapList(descriptiveNotePValue,descriptiveNoteMapMapList,nListMapList);			 
		 Map<String, List<String>> descriptiveNoteMapList = null;
		 descriptiveNoteMapList = createMapList(descriptiveNoteMapMapList,descriptiveNoteMapList,key,nMapMapList);			
		 List<String> descriptiveNoteList = null;
		 descriptiveNoteList = createList(descriptiveNoteMapList,descriptiveNoteList,key2,nMapList);			
		 descriptiveNoteList.add(replaceIfExistsSpecialReturnString(value));
		 descriptiveNoteMapList.put(key2,descriptiveNoteList);
		 descriptiveNoteMapMapList.put(key,descriptiveNoteMapList);
		 if(descriptiveNotePValue.size()>i){
			 descriptiveNotePValue.set(i,descriptiveNoteMapMapList);
		 }else{
			 descriptiveNotePValue.add(descriptiveNoteMapMapList);
		 }
		 this.log2.debug("End method: \"createListMapList\"");
		 return descriptiveNotePValue;
	}

	/**
	 * Method that checks if it have to create if the object is null<br> 
	 * and fill it with data the {@link List<Map<String, Map<String, List<String>>>>} 'List<Map<String, Map<String, List<String>>>>'
	 * @param listMapList {@link List<Map<String, Map<String, List<String>>>>}
	 * @param key {@link String} Constant for TABs indexes
	 * @param key2 {@link String} Constant for section indexes
	 * @param i {@link int} position of the object JSONObject in the Table
	 * @param nLML {@link int} position of the object {@link JSONObject} JSONObject in the Table on 'List<Map<String, Map<String, List<String>>>>'
	 * @param nM {@link int} position of the object {@link JSONObject} JSONObject in the Table on 'Map<String,List<String>>'
	 * @param nL {@link int} position of the object {@link JSONObject} JSONObject in the Table on 'List<String>'
	 * @return {@link List<Map<String, Map<String, List<String>>>>}
	 */
	public  List<Map<String, Map<String, List<String>>>> createListMapListAcces(List<Map<String, Map<String, List<String>>>> listMapList, String key, String key2, int i, int nLML, int nM,int nL){
		this.log2.debug("Method start: \"createListMapListAcces\"");
		if(listMapList==null){
			 listMapList = new ArrayList<Map<String, Map<String, List<String>>>>();
		 }
		 Map<String, Map<String, List<String>>> mapMapList = null;
		 mapMapList = createMapMapList(listMapList,mapMapList,nLML);
		 Map<String,List<String>> mapList = null;
		 mapList = createMapList(mapMapList,mapList,key,nM);			 
		 List<String> listEmail = null;
		 listEmail = createList(mapList,listEmail,key2,nL);
		 listEmail.add("");
		 mapList.put(key2,listEmail);
		 mapMapList.put(key, mapList);
		 if(listMapList.size()>i){
			 listMapList.set(i,mapMapList);
		 }else{
			 listMapList.add(mapMapList);
		 }
		 this.log2.debug("End method: \"createListMapListAcces\"");
		 return listMapList;
	}

	/**
	 * Method that checks if it have to create if the object is null<br> 
	 * and fill it with data the {@link List<Map<String,Map<String,Map<String,List<String>>>>>} 'List<Map<String,Map<String,Map<String,List<String>>>>>'
	 * @param DataValueListMapMapListS {@link List<Map<String,Map<String,Map<String,List<String>>>>>}
	 * @param key {@link String} Constant for TABs indexes
	 * @param key2 {@link String} Constant for section indexes
	 * @param key3 {@link String} Constant for subsection indexes
	 * @param value {@link String} value of the object
	 * @param i {@link int>, position of the object {@link JSONObject} JSONObject in the Table
	 * @param numMapMapMapList {@link int} position of the object {@link JSONObject} JSONObject in the Table on 'Map<String, Map<String, Map<String, List<String>>>>'
	 * @param numMapMapList {@link int} position of the object {@link JSONObject} JSONObject in the Table on 'Map<String, Map<String, List<String>>>'
	 * @param numMapList {@link int} position of the object  {@link JSONObject} JSONObject in the Table on ' Map<String, List<String>>'
	 * @param numList {@link int} position of the object {@link JSONObject} JSONObject in the Table on 'List<String>'
	 * @return DataValueListMapMapListS {@link List<Map<String,Map<String,Map<String,List<String>>>>>}
	 */
	public List<Map<String,Map<String,Map<String,List<String>>>>> createDataListMapMapListS(List<Map<String,Map<String,Map<String,List<String>>>>> DataValueListMapMapListS,String key,String key2,String key3,String value,int i,int numMapMapMapList,int numMapMapList,int numMapList,int numList ){
		this.log2.debug("Method start: \"createDataListMapMapListS\"");
		if(DataValueListMapMapListS==null){
			 DataValueListMapMapListS = new ArrayList<Map<String,Map<String,Map<String,List<String>>>>>();
		 }
		 Map<String, Map<String, Map<String, List<String>>>> MapMapMapList = null;			 
		 MapMapMapList = createMapMapMapList2(MapMapMapList,DataValueListMapMapListS,numMapMapMapList);			 
		 Map<String, Map<String, List<String>>> mapMapList = null;
		 mapMapList = createMapMapList2(MapMapMapList,mapMapList,key,numMapMapList);			 
		 Map<String, List<String>> mapList = null;
		 mapList = createMapList(mapMapList,mapList,key2,numMapList);			
		 List<String> nums = null;
		 nums = createList(mapList,nums,key3,numList);			
		 nums.add(replaceIfExistsSpecialReturnString(value));			 
		 mapList.put(key3,nums);
		 mapMapList.put(key2,mapList);
		 MapMapMapList.put(key,mapMapList);
		 if(DataValueListMapMapListS.size()>i){
			 DataValueListMapMapListS.set(i,MapMapMapList);
		 }else{
			 DataValueListMapMapListS.add(MapMapMapList);
		 }
		 this.log2.debug("End method: \"createDataListMapMapListS\"");
		 return DataValueListMapMapListS;
	}

	/**
	 * Method that checks if it have to create if the object is null<br> 
	 * and fill it with data the {@link List<Map<String,Map<String,Map<String,List<List<String>>>>>>} 'List<Map<String,Map<String,Map<String,List<List<String>>>>>>'
	 * @param dateValueListMapMapList {@link List<Map<String,Map<String,Map<String,List<List<String>>>>>>}
	 * @param key {@link String} Constant for TABs indexes
	 * @param key2  {@link String} Constant for section indexes
	 * @param key3  {@link String} Constant for subsection indexes
	 * @param value {@link String} value of the object
	 * @param i {@link int} position of the object {@link JSONObject} JSONObject in the Table
	 * @param numList {@link int} position of the object {@link JSONObject} JSONObject in the Table on 'List<String>'
	 * @return dateValueListMapMapList {@link List<Map<String,Map<String,Map<String,List<List<String>>>>>>}
	 */
	public List<Map<String,Map<String,Map<String,List<List<String>>>>>> createDataListMapMapList(List<Map<String,Map<String,Map<String,List<List<String>>>>>> dateValueListMapMapList,String key,String key2,String key3,String value,int i,int numList){			
		this.log2.debug("Method start: \"createDataListMapMapList\"");
		if(dateValueListMapMapList==null){
			 dateValueListMapMapList = new ArrayList<Map<String,Map<String,Map<String,List<List<String>>>>>>();
		 }
		 Map<String, Map<String, Map<String, List<List<String>>>>> dateMapMapMapList = null;			
		 dateMapMapMapList = createMapMapMapList(dateMapMapMapList,dateValueListMapMapList,i);
		 Map<String, Map<String, List<List<String>>>> dateMapMapList = null;
		 dateMapMapList = createMapMapList_list(dateMapMapMapList,dateMapMapList,key); 
		 Map<String, List<List<String>>> datesMapList = null;
		 datesMapList = createMapList_list(dateMapMapList,datesMapList,key2);			
		 List<List<String>> datesList = null;
		 datesList= createList_list(datesMapList,datesList,key3);			 
		 List<String> dates = null;
		 dates = createList2(datesList,dates,numList);			 
		 String stringWithoutBreaks = replaceIfExistsSpecialReturnString(value);
		 if (stringWithoutBreaks.indexOf("%5C") > -1){
			 String escapeString = unescapeJsonString(stringWithoutBreaks);
			 dates.add(escapeString);
		 }else{
			 dates.add(stringWithoutBreaks);
		 }
		 if(datesList.size()>0){
			 datesList.set(0, dates);
		 }else{
			 datesList.add(dates);
		 }
		 datesMapList.put(key3,datesList);
		 dateMapMapList.put(key2, datesMapList);
		 dateMapMapMapList.put(key, dateMapMapList);
		 if(dateValueListMapMapList.size()>i){
			 dateValueListMapMapList.set(i, dateMapMapMapList);
		 }else{
			 dateValueListMapMapList.add(dateMapMapMapList);
		 }		
		 this.log2.debug("End method: \"createDataListMapMapList\"");
		 return dateValueListMapMapList;
	}

	/**
	 * Method that checks if it have to create if the object is null<br> 
	 * and fill it with data the {@link List<Map<String, Map<String, Map<String, List<List<String>>>>>>} 'List<Map<String, Map<String, Map<String, List<List<String>>>>>>'
	 * @param yearValue {@link List<Map<String, Map<String, Map<String, List<List<String>>>>>>}
	 * @param key {@link String} Constant for TABs indexes
	 * @param key2 {@link String} Constant for section indexes
	 * @param key3 {@link String} Constant for subsection indexes
	 * @param value {@link String} value of the object
	 * @param i {@link int} position of the object {@link JSONObject} JSONObject in the Table
	 * @return yearValue {@link List<Map<String, Map<String, Map<String, List<List<String>>>>>>}
	 */
	public List<Map<String, Map<String, Map<String, List<List<String>>>>>> createDataListMapMapList_identity(List<Map<String, Map<String, Map<String, List<List<String>>>>>> yearValue,String key,String key2,String key3,String value, int i){
		this.log2.debug("Method start: \"createDataListMapMapList_identity\"");
		if(yearValue==null){
			yearValue = new ArrayList<Map<String, Map<String, Map<String, List<List<String>>>>>>();
		}			
		Map<String, Map<String, Map<String, List<List<String>>>>> mapMapMApList = null;
		mapMapMApList = createMapMapMapList(mapMapMApList,yearValue,0);			
		Map<String, Map<String, List<List<String>>>> yearMapMap = null;
		yearMapMap = createMapMapList_list(mapMapMApList,yearMapMap,key);
		Map<String, List<List<String>>> mapList = null;
		mapList = createMapList_list(yearMapMap,mapList,key2);
		List<List<String>> list_list = null;
		list_list = createList_list(mapList,list_list,key3);
		List<String> list = null;		
		if(list_list.size()>0 && list_list.size()>(i-1)){
			list = list_list.get(i-1);
		}else{
			list = new ArrayList<String>();
		}
		String stringWithoutBreaks = replaceIfExistsSpecialReturnString(value);
		if (stringWithoutBreaks.indexOf("%5C") > -1){
			String escapeString = unescapeJsonString(stringWithoutBreaks);
			list.add(escapeString);
		}else{
			list.add(stringWithoutBreaks);
		}		
		if(list_list.size()>0 && list_list.size()>(i-1)){
			list_list.set((i-1),list);
		}else{
			list_list.add(list);
		}
		mapList.put(key3,list_list);
		yearMapMap.put(key2, mapList);
		mapMapMApList.put(key,yearMapMap);
		if(yearValue.size()>0){
			yearValue.set(0,mapMapMApList);
		}else{
			yearValue.add(mapMapMApList);
		}
		this.log2.debug("End method: \"createDataListMapMapList_identity\"");
		return yearValue;
	}

	/**
	 * Method that checks if it have to create if the object is null and fill it with data the {@link List<String>} 'List<String>'
	 * @param restorationList {@link List<String>}
	 * @param value {@link String} value of the object
	 * @param i {@link int} position of the object {@link JSONObject}  JSONObject in the Table
	 * @return restorationList {@link List<String>}
	 */
	public List<String> createDataListAcces(List<String> restorationList,String value,int i){
		this.log2.debug("Method start: \"createDataListAcces\"");
		if(restorationList == null){
		    restorationList = new ArrayList<String>();
		 }
		 if(restorationList.size()>i){
			 restorationList.set(i,replaceIfExistsSpecialReturnString(value));
		 }else{
		   restorationList.add(replaceIfExistsSpecialReturnString(value));
		 }
		 this.log2.debug("End method: \"createDataListAcces\"");
		 return restorationList;
	}

	/**
	 * Method that checks if it have to create if the object is null and fill it with data the {@link List<List<String>>} 'List<List<String>>'
	 * @param dataList {@link List<List<String>>}
	 * @param value {@link String} value of the object
	 * @param i {@link int>} position of the object {@link JSONObject} JSONObject in the Table
	 * @return dataList {@link List<List<String>>}
	 */
	public  List<List<String>> createDataList(List<List<String>> dataList,String value, int i){
		this.log2.debug("Method start: \"createDataList\"");
		if(dataList==null){
			 dataList = new ArrayList<List<String>>();
		 }
		 List<String> dataL = null;			
		 if(dataList.size()>i){
			 dataL = dataList.get(i);
		 }else{
			 dataL = new ArrayList<String>();
		 }
		 dataL.add(replaceIfExistsSpecialReturnString(value));
		 if(dataList.size()>i){
			 dataList.set(i, dataL);
		 }else{
			 dataList.add(dataL);
		 }
		 this.log2.debug("End method: \"createDataList\"");
		 return dataList;
	 }

	/**
	 * Method that checks if it have to create if the object is null and fill it with data the {@link List<Map<String, String>>} 'List<Map<String, String>>'
	 * @param listString {@link List<Map<String, String>>}
	 * @param key {@link String} Constant for TABs indexes
	 * @param value {@link String} value of the object
	 * @param i {@link int} position of the object {@link JSONObject} JSONObject in the Table
	 * @param numMapString {@link int} position of the object {@link JSONObject} JSONObject in the Table on 'Map<String, String>'
	 * @return listString {@link List<Map<String, String>>}
	 */
	public List<Map<String, String>> createDataListString(List<Map<String, String>> listString,String key,String value,int i, int numMapString){			
		this.log2.debug("Method start: \"createDataListString\"");
		if(listString == null){
			listString = new ArrayList<Map<String, String>>();
		}
		Map<String, String> mapString = null;
		mapString = createListString(listString,mapString,numMapString);			
		mapString.put(key,replaceIfExistsSpecialReturnString(value));
		if(listString.size()>i){
			listString.set(i,mapString);
		}else{
			listString.add(mapString);
		}	
		this.log2.debug("End method: \"createDataListString\"");
		return listString;
	}

	/**
	 * Method that checks if it have to create if the object is null and fill it with data the {@link List<Map<String, List<String>>>} 'List<Map<String, List<String>>>'
	 * @param listList {@link List<Map<String, List<String>>>}
	 * @param key {@link String} Constant for TABs indexes
	 * @param value {@link String} value of the object
	 * @param x {@link int} position of the object {@link JSONObject>} JSONObject in the Table
	 * @param nML {@link int} position of the object {@link JSONObject}JSONObject in the Table on 'Map<String, List<String>>'
	 * @param nL {@link int} position of the object {@link JSONObject} JSONObject in the Table on 'List<String>'
	 * @return listList {@link List<Map<String, List<String>>>}
	 */
	public List<Map<String, List<String>>> createDataListList(List<Map<String, List<String>>> listList,String key, String value,int x, int nML,int nL){			
		this.log2.debug("Method start: \"createDataListList\"");
		if(listList==null){
			listList = new ArrayList<Map<String,List<String>>>();
		}
		Map<String, List<String>> mapList = null;			
		mapList = createMapList(listList,mapList,nML);
		List<String> listFax = null;
		listFax = createList(mapList,listFax,key,nL);
		listFax.add(replaceIfExistsSpecialReturnString(value));
		mapList.put(key,listFax);
		if(listList.size()>x){
			listList.set(x,mapList);
		}else{
			listList.add(mapList);
		}
		this.log2.debug("End method: \"createDataListList\"");
	return listList;
	}

	/**
	 * Method that checks if it have to create if the object is null and fill it with data the <List<Map<String, List<String>>>> 'List<Map<String, List<String>>>'
	 * @param listList {@link List<Map<String, List<String>>>}
	 * @param key {@link String} Constant for TABs indexes
	 * @param value {@link String} value of the object
	 * @param x {@link int} position of the object {@link JSONObject} JSONObject in the Table
	 * @param nLL {@link int} position of the object {@link JSONObject} JSONObject in the Table on 'Map<String, List<String>>'
	 * @return listList {@link List<Map<String, List<String>>>}
	 */
	public List<Map<String, List<String>>> createDataListList2(List<Map<String, List<String>>> listList,String key,List<String> value,int x,int nLL){			
		this.log2.debug("Method start: \"createDataListList2\"");
		if(listList==null){
			listList = new ArrayList<Map<String, List<String>>>();
		}
		Map<String, List<String>> tempListMap2 = null;
		if(listList.size()>nLL){
        	tempListMap2 = listList.get(nLL);
        }else{
        	tempListMap2 = new HashMap<String, List<String>>();
        }
        tempListMap2.put(key,value);
        if(listList.size()>x){
        	listList.set(x,tempListMap2);
        }else{
        	listList.add(tempListMap2);
        }
        this.log2.debug("End method: \"createDataListList2\"");
		return listList;
	}	
}
