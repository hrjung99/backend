package swyp.swyp6_team7.location.reader;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import swyp.swyp6_team7.location.domain.Location;
import swyp.swyp6_team7.location.domain.LocationType;
import swyp.swyp6_team7.location.parser.CityParser;
import swyp.swyp6_team7.location.parser.Parser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvReader <T>{

    private final ResourceLoader resourceLoader;

    public CsvReader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public List<T> readByLine(InputStream inputStream, Parser<T> parser, LocationType locationType) throws IOException {
        List<T> items = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                items.add(parser.parse(line, locationType));
            }
        }
        return items;
    }
}
