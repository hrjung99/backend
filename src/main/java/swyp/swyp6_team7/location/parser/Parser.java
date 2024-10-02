package swyp.swyp6_team7.location.parser;

import swyp.swyp6_team7.location.domain.CityType;

public interface Parser<T> {
    T parse(String line, CityType cityType);
}