package eu.apenet.solr.stopwords;


public class Stopword {

	private static final String SEPARATOR = "|";
	private String value;
	private String valueSource;
	private String language;
	private String description;
	private boolean forbidden;
	private String forbiddenSource;
	private String forbiddenReason;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescription() {
		if (description == null){
			return "";
		}
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isForbidden() {
		return forbidden;
	}
	public void setForbidden(boolean forbidden) {
		this.forbidden = forbidden;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getValueSource() {
		return valueSource;
	}
	public void setValueSource(String valueSource) {
		this.valueSource = valueSource;
	}
	public String getForbiddenSource() {
		return forbiddenSource;
	}
	public void setForbiddenSource(String forbiddenSource) {
		this.forbiddenSource = forbiddenSource;
	}
	
	
	
	public String getForbiddenReason() {
		return forbiddenReason;
	}
	public void setForbiddenReason(String forbiddenReason) {
		this.forbiddenReason = forbiddenReason;
	}
	public void merge(Stopword stopword){
		if (stopword.isForbidden()){
			if (forbiddenSource == null){
				forbiddenSource = stopword.getForbiddenSource();
				forbiddenReason = stopword.getForbiddenReason();
			}else {
				if (!contains(forbiddenSource, stopword.getForbiddenSource())){
					forbiddenSource += 	SEPARATOR + stopword.getForbiddenSource();
					forbiddenReason += 	SEPARATOR + stopword.getForbiddenReason();
				}				
				
			}
			forbidden = stopword.isForbidden();
		}else {
			if (valueSource == null){
				valueSource = stopword.getValueSource();
			}else if (!contains(valueSource, stopword.getValueSource())){
				valueSource += SEPARATOR + stopword.getValueSource();
			}
			
			if (!contains(language, stopword.getLanguage()) && !contains(valueSource,stopword.getDescription())){
				description = getDescription() + SEPARATOR + stopword.getDescription();
			}
		}
		if (!contains(language, stopword.getLanguage())){
			language += SEPARATOR + stopword.getLanguage();
		}
	}
	private boolean contains(String values, String value){
		boolean found = false;
		String[] splitted = values.split("\\|");
		for (int i=0; !found && i < splitted.length;i++){
			found = splitted[i].equals(value);
		}
		return found;
	}
}
