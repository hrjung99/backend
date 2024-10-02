package swyp.swyp6_team7.location.reader;

import org.springframework.stereotype.Component;
import swyp.swyp6_team7.location.domain.CityType;
import swyp.swyp6_team7.location.parser.Parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvReader <T>{

    public List<T> readByLine(String filename, Parser<T> parser, CityType cityType) throws IOException {
        List<T> result = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = reader.readLine()) != null) {
            try {
                result.add(parser.parse(line, cityType));
            } catch (Exception e) {
                System.out.printf("파싱 중 문제가 생겨 이 라인은 넘어갑니다. 파일 내용: %s\n", line);
            }
        }

        reader.close();
        return result;
    }
}
