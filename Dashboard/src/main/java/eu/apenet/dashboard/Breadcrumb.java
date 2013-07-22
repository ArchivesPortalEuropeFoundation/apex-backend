package eu.apenet.dashboard;


public class Breadcrumb {
	private String href;
	private String place;
	
	public Breadcrumb() {}
	
	public Breadcrumb(String href,String place){
		this.href = href;
		this.place = place;
	}
	
	public void setHref(String href) {
		this.href = href;
	}
	public String getHref() {
		return href;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getPlace() {
		return place;
	}
	
	@Override
	public String toString(){
		return this.place+"::"+this.href;
	}
}
