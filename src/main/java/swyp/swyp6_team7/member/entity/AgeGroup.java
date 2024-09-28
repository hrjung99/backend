package swyp.swyp6_team7.member.entity;

public enum AgeGroup{
    TEEN("10대"), // 10대
    TWENTY("20대"), // 20대
    THIRTY("30대"), // 30대
    FORTY("40대"),  // 40대
    FIFTY_PLUS("50대 이상"); // 50대 이상

    private final String value;

    AgeGroup(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AgeGroup fromValue(String value) {
        for (AgeGroup ageGroup : AgeGroup.values()) {
            if (ageGroup.getValue().equals(value)) {
                return ageGroup;
            }
        }
        throw new IllegalArgumentException("Invalid age group provided.");
    }
}
