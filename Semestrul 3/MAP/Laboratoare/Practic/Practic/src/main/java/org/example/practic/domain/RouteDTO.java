package org.example.practic.domain;

import org.example.practic.factory.BuildContainer;

import java.util.List;

public class RouteDTO {
    private final List<TravelDTO> stations;

    public RouteDTO(List<TravelDTO> stations) {
        this.stations = stations;
    }

    public List<TravelDTO> getStations() {
        return stations;
    }

    public String getTrainId() {
        return stations.get(0).getTrainId();
    }

    public String toString(BuildContainer container) {
        return container.getCityService().getCityById(stations.get(0).getDepartureCityId()) + " -- " +
                String.join("->", stations.stream()
                .map(station -> station.getTrainId() +
                        " -> " +
                        container.getCityService().getCityById(station.getDestinationCityId()))
                        .toList());
    }
}
