package ir.map.g221.guisocialnetwork.domain;

import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.utils.graphs.GraphComponent;
import ir.map.g221.guisocialnetwork.utils.graphs.Path;

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
        return (size() > 1 ? "Community:" : "Single user:") + "\n" + graphComponent.getNodes().stream()
                .map(User::toString)
                .collect(Collectors.joining("\n"));
    }
}
