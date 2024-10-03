package swyp.swyp6_team7.location.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swyp.swyp6_team7.location.domain.City;
import swyp.swyp6_team7.location.domain.CityType;
import swyp.swyp6_team7.location.repository.CityRepository;
import swyp.swyp6_team7.travel.repository.TravelRepository;

@Transactional
@Service
public class CityInitService{

    @PersistenceContext
    private EntityManager entityManager;

    private final CityRepository cityRepository;
    private final TravelRepository travelRepository;

    public CityInitService(CityRepository cityRepository, TravelRepository travelRepository) {
        this.cityRepository = cityRepository;
        this.travelRepository = travelRepository;
    }

    @PostConstruct
    public void init() {
        travelRepository.deleteAll();
        cityRepository.deleteAll();
    }


}

