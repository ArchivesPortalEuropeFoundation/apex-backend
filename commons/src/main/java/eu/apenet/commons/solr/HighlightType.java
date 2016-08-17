package eu.apenet.commons.solr;

public enum HighlightType {
    DEFAULT("default"), UNITID("unitid");
    private String name;

    private HighlightType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static HighlightType getHighlightType(String type) {
        if (UNITID.toString().equalsIgnoreCase(type)) {
            return UNITID;
        }
        return DEFAULT;
    }

}
