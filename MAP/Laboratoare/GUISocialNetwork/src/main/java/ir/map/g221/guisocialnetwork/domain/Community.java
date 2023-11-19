package ir.map.g221.guisocialnetwork.domain;

import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.utils.graphs.GraphComponent;
import ir.map.g221.guisocialnetwork.utils.graphs.Path;

import java.util.Objects;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Community community = (Community) o;
        return Objects.equals(graphComponent, community.graphComponent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(graphComponent);
    }

    @Override
    public String toString() {
        return (size() > 1 ? "Community:" : "Single user:") + "\n" + graphComponent.getNodes().stream()
                .map(User::toString)
                .collect(Collectors.joining("\n"));
    }
}
