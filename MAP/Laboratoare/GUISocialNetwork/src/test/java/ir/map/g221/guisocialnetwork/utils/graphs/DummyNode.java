package ir.map.g221.guisocialnetwork.utils.graphs;

import ir.map.g221.guisocialnetwork.exceptions.graphs.InvalidEdgeException;

import java.util.HashSet;
import java.util.Set;

public class DummyNode implements Node<DummyNode> {
    public Set<DummyNode> neighbours;
    private final int code;

    {
        neighbours = new HashSet<>();
    }

    DummyNode(int code) {
        this.code = code;
    }

    @Override
    public boolean hasNeighbour(DummyNode neighbour) {
        return neighbours.contains(neighbour);
    }

    @Override
    public Set<DummyNode> getNeighbours() {
        return neighbours;
    }

    @Override
    public Integer getDegree() {
        return neighbours.size();
    }

    @Override
    public void pairWith(DummyNode neighbour) {
        this.neighbours.add(neighbour);
        neighbour.neighbours.add(this);
    }

    @Override
    public void unpairWith(DummyNode neighbour) throws InvalidEdgeException {
        if (!hasNeighbour(neighbour) || !neighbour.hasNeighbour(this)) {
            throw new InvalidEdgeException("The edge to be removed does not exist.");
        }
        this.neighbours.remove(neighbour);
        neighbour.neighbours.remove(this);
    }

    @Override
    public String indexToString() {
        return Integer.toString(code);
    }
}
