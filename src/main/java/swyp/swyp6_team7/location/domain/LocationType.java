package swyp.swyp6_team7.location.domain;

public enum LocationType {
    DOMESTIC("국내"), INTERNATIONAL("해외");

    private final String description;

    LocationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static LocationType fromString(String value) {
        for (LocationType type : LocationType.values()) {
            if (type.getDescription().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid location type: " + value);
    }
}
