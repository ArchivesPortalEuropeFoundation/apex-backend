package eu.apenet.dashboard.ead2ese;

public class SelectItem implements Comparable<SelectItem> {
	private final static int LESS = -1;
	private final static int GREATER = 1;
	private String content;
	private String value;
	
	public SelectItem (String valueAndContent){
		this.value= valueAndContent;
		this.content = valueAndContent;
		
	}
	
	public SelectItem(String value, String content){
		this.value= value;
		this.content = content;
		
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return content;
	}
	@Override
	public int compareTo(SelectItem other) {
		if (other == null){
			return GREATER;
		}
		if (content == null){
			return LESS;
		}
		if (other.content == null){
			return GREATER;
		}
		return content.compareTo(other.content);
	}
	
}
