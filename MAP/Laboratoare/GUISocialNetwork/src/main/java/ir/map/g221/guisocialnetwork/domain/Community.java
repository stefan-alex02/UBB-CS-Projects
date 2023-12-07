package ir.map.g221.guisocialnetwork.domain;

import ir.map.g221.guisocialnetwork.business.CommunityHandler;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.utils.graphs.ConnectedComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Community {
    private final ConnectedComponent<User> component;
    private final Optional<List<User>> longestPath;

    private Community(ConnectedComponent<User> component) {
        this.component = component;
        longestPath = Optional.empty();
    }

    private Community(ConnectedComponent<User> component, List<User> longestPath) {
        this.component = component;
        this.longestPath = Optional.of(longestPath);
    }

    public static Community of(ConnectedComponent<User> component) {
        return new Community(component);
    }

    public static Community of(ConnectedComponent<User> component, List<User> longestPath) {
        return new Community(component, longestPath);
    }

    public List<User> getFriendshipPath() {
        return longestPath.orElseThrow();
    }

    public int size() {
        return component.size();
    }

    public boolean isEmpty() {
        return component.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Community community = (Community) o;
        return Objects.equals(component, community.component);
    }

    @Override
    public int hashCode() {
        return Objects.hash(component);
    }

    @Override
    public String toString() {
        return (size() > 1 ? "Community:" : "Single user:") + "\n" +
                component.getVerticesData().stream()
                    .map(user -> user.toString(component))
                    .collect(Collectors.joining("\n"));
    }
}
