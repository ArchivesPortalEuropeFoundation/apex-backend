package eu.apenet.persistence.vo;

public enum QueuingState {
	NO("no"), READY("read"), BUSY("busy"), ERROR("error");

	private String name;

	private QueuingState(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public static QueuingState getQueuingState(String name) {
		for (QueuingState queuingState : QueuingState.values()) {
			if (queuingState.name.equals(name))
				return queuingState;
		}
		return null;
	}
}
