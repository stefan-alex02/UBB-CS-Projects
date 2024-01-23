package org.example.practic.domain;

import org.example.practic.utils.ThreeTuple;

public class TrainStation extends Entity<ThreeTuple<String, String, String>> {
    public TrainStation(ThreeTuple<String, String, String> ID) {
        super(ID);
    }
}
