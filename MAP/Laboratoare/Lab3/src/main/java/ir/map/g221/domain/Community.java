package ir.map.g221.domain;

import ir.map.g221.domain.entities.User;
import ir.map.g221.domain.graphs.GraphComponent;
import ir.map.g221.domain.graphs.Path;

import java.util.stream.Collectors;

public class Community {
    private final GraphComponent<User> graphComponent;

    public Community(GraphComponent<User> graphComponent) {
        this.graphComponent = graphComponent;
    }

    public Path<User> getFriendshipPath() {
        return graphComponent.getLongestPath();
    }

    public int size() {
        return graphComponent.size();
    }

    public boolean isEmpty() {
        return graphComponent.isEmpty();
    }

    @Override
    public String toString() {
        return "Community:\n" + graphComponent.getNodes().stream()
                .map(User::toString)
                .collect(Collectors.joining("\n"));
    }
}
