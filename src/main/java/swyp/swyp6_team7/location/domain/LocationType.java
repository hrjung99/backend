package swyp.swyp6_team7.location.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum LocationType {
    DOMESTIC("국내"),
    INTERNATIONAL("해외"),
    UNKNOWN("알 수 없음");

    private final String description;

    public static LocationType fromString(String value) {
        return Arrays.stream(values())
                .filter(e -> e.description.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원되지 않는 LocationType입니다: " + value));
    }

    @Override
    public String toString() {
        return description;
    }
}
