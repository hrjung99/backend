package swyp.swyp6_team7.location.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.location.repository.LocationRepository;
import swyp.swyp6_team7.travel.repository.TravelRepository;

@Transactional
@Service
public class LocationInitService {

    @PersistenceContext
    private EntityManager entityManager;

    private final LocationRepository locationRepository;
    private final TravelRepository travelRepository;

    public LocationInitService(LocationRepository locationRepository, TravelRepository travelRepository) {
        this.locationRepository = locationRepository;
        this.travelRepository = travelRepository;
    }

    @PostConstruct
    public void init() {
        travelRepository.deleteAll();
        locationRepository.deleteAll();
    }


}

