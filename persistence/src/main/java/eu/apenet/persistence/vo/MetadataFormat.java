package eu.apenet.persistence.vo;

public enum MetadataFormat {
	EDM("edm");;

	private String name;

	private MetadataFormat(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public static MetadataFormat getMetadataFormat(String name) {
		for (MetadataFormat metadataFormat : MetadataFormat.values()) {
			if (metadataFormat.name.equals(name))
				return metadataFormat;
		}
		return null;
	}

}
