package org.example.practic.domain;

import java.util.ArrayList;
import java.util.List;

public class RouteFinder {
    private List<RouteDTO> routes;
    private List<TravelDTO> travels;
    private String train;

    public List<RouteDTO> findRoutes(String startCity, String endCity, List<TravelDTO> travelDTOs) {
        routes = new ArrayList<>();
        travels = travelDTOs;

        // Start the backtracking from each travelDTO in the start city
        for (TravelDTO travelDTO : getTravelsInCity(startCity)) {
            train = travelDTO.getTrainId();
            traverse(travelDTO, endCity, new ArrayList<>());
        }

        return routes;
    }

    private void traverse(TravelDTO currentTravel, String endCity, List<TravelDTO> currentPath) {
        currentPath.add(currentTravel);

        if (currentTravel.getDestinationCityId().equals(endCity)) {
            if (currentPath.stream().allMatch(t -> t.getTrainId().equals(
                    train
            ))) {
                routes.add(new RouteDTO(new ArrayList<>(currentPath)));
            }
        } else {
            List<TravelDTO> nextTravels = getNextTravels(currentTravel, currentPath);
            for (TravelDTO nextTravel : nextTravels) {
                traverse(nextTravel, endCity, currentPath);
            }
        }

        currentPath.remove(currentPath.size() - 1);
    }

    private List<TravelDTO> getNextTravels(TravelDTO currentTravel, List<TravelDTO> currentPath) {
        List<TravelDTO> nextTravels = new ArrayList<>();

        for (TravelDTO travel : getTravelsInCity(currentTravel.getDestinationCityId())) {
            if (!currentPath.contains(travel)) {
                nextTravels.add(travel);
            }
        }

        return nextTravels;
    }

    private List<TravelDTO> getTravelsInCity(String city) {
        List<TravelDTO> travelsInCity = new ArrayList<>();
        for (TravelDTO travel : travels) {
            if (travel.getDepartureCityId().equals(city)) {
                travelsInCity.add(travel);
            }
        }
        return travelsInCity;
    }
}
