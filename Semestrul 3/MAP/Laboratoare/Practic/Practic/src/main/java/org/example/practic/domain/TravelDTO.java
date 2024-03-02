package org.example.practic.domain;

public class TravelDTO {
    private final String trainId;
    private final String departureCityId;
    private final String destinationCityId;
    private Boolean visited;

    private TravelDTO(String trainId, String departureCityId, String destinationCityId, Boolean visited) {
        this.trainId = trainId;
        this.departureCityId = departureCityId;
        this.destinationCityId = destinationCityId;
        this.visited = visited;
    }

    public static TravelDTO fromTrainStation(TrainStation trainStation) {
        return new TravelDTO(trainStation.getID().getFirst(),
                trainStation.getID().getSecond(),
                trainStation.getID().getThird(),
                false);
    }

    public String getTrainId() {
        return trainId;
    }

    public String getDepartureCityId() {
        return departureCityId;
    }

    public String getDestinationCityId() {
        return destinationCityId;
    }

    public Boolean getVisited() {
        return visited;
    }

    public void setVisited(Boolean visited) {
        this.visited = visited;
    }
}
