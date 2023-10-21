package ir.map.g221.domain.graphs;

import java.util.Set;

public interface Node<T extends Node<T>> {
    Set<T> getNeighbours();
    Integer getDegree();
    String toString();
}
