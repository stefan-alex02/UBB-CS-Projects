package org.example.practic.business;

import org.example.practic.domain.RouteDTO;
import org.example.practic.domain.RouteFinder;
import org.example.practic.domain.TrainStation;
import org.example.practic.domain.TravelDTO;
import org.example.practic.persistence.Repository;
import org.example.practic.utils.ThreeTuple;

import java.util.List;
import java.util.stream.StreamSupport;

public class TrainStationService {
    private final Repository<ThreeTuple<String, String, String>, TrainStation> trainStationRepository;

    public TrainStationService(Repository<ThreeTuple<String, String, String>, TrainStation> trainStationRepository) {
        this.trainStationRepository = trainStationRepository;
    }

    public List<TravelDTO> getTravelDTOs() {
        return StreamSupport.stream(trainStationRepository.findAll().spliterator(), false)
                .map(TravelDTO::fromTrainStation).toList();
    }

    public List<RouteDTO> calculateRoutes(String departureCity, String destinationCity) {
        RouteFinder routeFinder = new RouteFinder();
        return routeFinder.findRoutes(departureCity, destinationCity, getTravelDTOs()
                );
    }
}
