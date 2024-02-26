package org.example.practic.business;

import org.example.practic.domain.City;
import org.example.practic.persistence.Repository;
import org.example.practic.utils.ObjectTransformer;

import java.util.List;

public class CityService {
    private final Repository<String, City> cityRepository;

    public CityService(Repository<String, City> cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<City> getCities() {
        return ObjectTransformer.iterableToList(cityRepository.findAll());
    }

    public List<String> getCityNames() {
        return getCities().stream().map(Object::toString).toList();
    }

    public City getCityById(String id) {
        return cityRepository.findOne(id).orElseThrow();
    }
}
