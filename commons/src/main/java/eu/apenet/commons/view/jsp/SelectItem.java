package eu.apenet.commons.view.jsp;

public class SelectItem implements Comparable<SelectItem> {

    private final static int LESS = -1;
    private final static int GREATER = 1;
    private String content;
    private String value;

    public SelectItem(String valueAndContent) {
        this.value = valueAndContent;
        this.content = valueAndContent;

    }

    public SelectItem(String value, String content) {
        this.value = value;
        this.content = content;

    }

    public SelectItem(Object value, String content) {
        this.value = value.toString();
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
        if (other == null) {
            return GREATER;
        }
        if (content == null) {
            return LESS;
        }
        if (other.content == null) {
            return GREATER;
        }
        return content.compareTo(other.content);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            SelectItem otherItem = (SelectItem) obj;
            if (value != null && otherItem.getValue() != null) {
                return this.value.equals(otherItem.getValue());
            }
        }
        return false;
    }

}
