package eu.apenet.dashboard.services.ead.xml.stream.mets;

import eu.apenet.dashboard.services.ead.xml.stream.mets.xpath.MetsFile;

public class DaoInfo{

	private MetsFile thumbnail;
	private MetsFile reference;
	private String label;
	public MetsFile getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(MetsFile thumbnail) {
		this.thumbnail = thumbnail;
	}
	public MetsFile getReference() {
		return reference;
	}
	public void setReference(MetsFile reference) {
		this.reference = reference;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
}
