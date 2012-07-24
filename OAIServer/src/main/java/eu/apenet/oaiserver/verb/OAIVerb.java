package eu.apenet.oaiserver.verb;

public abstract class OAIVerb {
	private String url;
	
	public void setUrl(String url){
		this.url = url;
	}
	protected String getUrl(){
		return this.url;
	}
	protected abstract boolean execute();
}
