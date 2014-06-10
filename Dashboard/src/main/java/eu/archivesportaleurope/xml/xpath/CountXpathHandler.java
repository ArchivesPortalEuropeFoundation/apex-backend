package eu.archivesportaleurope.xml.xpath;

import javax.xml.stream.XMLStreamReader;


public class CountXpathHandler extends AbstractXpathHandler {
	private int count = 0; 
	

	public CountXpathHandler(String defaultNamespace, String[] xpathQueryArray) {
		super(defaultNamespace, xpathQueryArray);
	}





	
	@Override
	protected void processExactStartElementMatch(XMLStreamReader xmlReader){
		count++;
	}






	public int getCount() {
		return count;
	}






	@Override
	protected void clear() {
	}
	



	
}
