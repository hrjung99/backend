package swyp.swyp6_team7.location.parser;

import swyp.swyp6_team7.location.domain.LocationType;

public interface Parser<T> {
    T parse(String line, LocationType locationType);
}