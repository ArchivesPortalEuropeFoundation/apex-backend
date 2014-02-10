package eu.apenet.commons.solr;

public enum EadHighlightType {
	DEFAULT("default"), UNITID("unitid");
	private String name;
	private EadHighlightType (String name){
		this.name = name;
	}
	@Override
	public String toString() {
		return name;
	}
	public static EadHighlightType getHighlightType(String type){
		if (UNITID.toString().equalsIgnoreCase(type)){
			return UNITID;
		}
		return DEFAULT;
	}
	
}
