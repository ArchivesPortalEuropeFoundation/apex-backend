package eu.apenet.commons.solr.facet;

public class ListFacetSettings {

    private static final String SEPARATOR = ":";
    public final static int DEFAULT_FACET_VALUE_LIMIT = 11;
    private final FacetType facet;
    private int limit = DEFAULT_FACET_VALUE_LIMIT;
    private Boolean expanded = true;
    private boolean oneValueAllowed = false;
    private Integer mincount = null;

    public ListFacetSettings(String inputString) {
        String[] values = inputString.split(SEPARATOR);
        String facetName = values[0];
        String limit = values[1];
        String expanded = values[2];
        facet = FacetType.getFacetByName(facetName);
        this.limit = Integer.parseInt(limit);
        this.expanded = Boolean.parseBoolean(expanded);

    }

    public ListFacetSettings(FacetType facet, Integer mincount) {
        this.facet = facet;
        this.mincount = mincount;
    }

    public ListFacetSettings(FacetType facet, boolean oneValueAllowed, Integer mincount) {
        this.facet = facet;
        this.oneValueAllowed = oneValueAllowed;
        this.mincount = mincount;
    }

    public ListFacetSettings(FacetType facet, boolean oneValueAllowed, Integer mincount, Integer limit) {
        this.facet = facet;
        this.oneValueAllowed = oneValueAllowed;
        this.mincount = mincount;
        this.limit = limit;
    }

    public ListFacetSettings(FacetType facet) {
        this.facet = facet;
    }

    public ListFacetSettings(FacetType facet, boolean oneValueAllowed) {
        this.facet = facet;
        this.oneValueAllowed = oneValueAllowed;
    }

    public FacetType getFacetType() {
        return facet;
    }

    public int getLimit() {
        return limit;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public boolean isOneValueAllowed() {
        return oneValueAllowed;
    }

    public Integer getMincount() {
        return mincount;
    }

    @Override
    public String toString() {
        return facet.getName() + SEPARATOR + limit + SEPARATOR + expanded;
    }

}
