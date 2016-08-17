package eu.apenet.commons.utils;

public enum SecurityLevel {
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high");
    private String level;

    SecurityLevel(String level) {
        this.level = level;
    }

    public static SecurityLevel getLevel(String levelString) {
        for (SecurityLevel level : SecurityLevel.values()) {
            if (level.toString().equalsIgnoreCase(levelString)) {
                return level;
            }
        }
        return HIGH;
    }

    public boolean isAtLeastMedium() {
        return this.equals(MEDIUM) || this.equals(HIGH);
    }

    public boolean isAtLeastHigh() {
        return this.equals(HIGH);
    }

    @Override
    public String toString() {
        return level;
    }

}
