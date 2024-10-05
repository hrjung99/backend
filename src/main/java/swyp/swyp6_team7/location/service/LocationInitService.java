package swyp.swyp6_team7.location.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.location.domain.LocationType;
import swyp.swyp6_team7.location.repository.LocationRepository;
import swyp.swyp6_team7.travel.repository.TravelRepository;

import java.io.IOException;

@Transactional
@Service
public class LocationInitService {

    @PersistenceContext
    private EntityManager entityManager;
    private final LocationRepository locationRepository;
    private final LocationService locationService;

    public LocationInitService(LocationRepository locationRepository, LocationService locationService) {
        this.locationRepository = locationRepository;
        this.locationService = locationService;
    }


    @PostConstruct
    public void init() {
        if (locationRepository.count() == 0) {
            try {
                // CSV 파일에서 도시 정보를 읽어와서 Location 테이블에 삽입
                locationService.importCities("korea_cities.csv", LocationType.DOMESTIC);
                locationService.importCities("foreign_cities.csv", LocationType.INTERNATIONAL);
            } catch (IOException e) {
                // 에러 핸들링 로직 추가
                e.printStackTrace();
            }
        }
    }


}

