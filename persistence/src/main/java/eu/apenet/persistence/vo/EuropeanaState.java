package eu.apenet.persistence.vo;

public enum EuropeanaState {
	NOT_CONVERTED("not_converted"), CONVERTED("converted"), DELIVERED("delivered"), HARVESTED("harvested");

	private String name;

	private EuropeanaState(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public static EuropeanaState getEuropeanaState(String name) {
		for (EuropeanaState europeanaState : EuropeanaState.values()) {
			if (europeanaState.name.equals(name))
				return europeanaState;
		}
		return null;
	}
}
