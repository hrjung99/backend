package swyp.swyp6_team7.global.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import swyp.swyp6_team7.location.domain.LocationType;
import swyp.swyp6_team7.location.service.LocationService;

import java.io.InputStream;

@Component
@ConditionalOnProperty(name = "app.data-loader.enabled", havingValue = "true", matchIfMissing = true)
public class DataLoader implements CommandLineRunner {
    @Autowired
    private LocationService locationService;

    @Override
    public void run(String... args) throws Exception {
        // 모든 로케이션 데이터를 한 번에 로드합니다.
        locationService.loadAllLocations();
    }
}
