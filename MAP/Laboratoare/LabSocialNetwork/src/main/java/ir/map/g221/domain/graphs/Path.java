package ir.map.g221.domain.graphs;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Path<TNode extends Node<TNode>> implements Comparable<Path<TNode>> {
    private List<TNode> path;

    public Path() {
        path = new ArrayList<>();
    }

    public void addToBeginning(TNode node) {
        path.add(0, node);
    }

    public List<TNode> getPath() {
        return path;
    }

    public void setPath(List<TNode> path) {
        this.path = path;
    }

    public int length() {
        return path.size() - 1;
    }

    @Override
    public int compareTo(@NotNull Path<TNode> o) {
        return length() - o.length();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        path.forEach(node -> {
            str.append(node.toString());
            if (path.indexOf(node) < path.size() - 1) {
                str.append(" ->\n");
            }
        });
        return str.toString();
    }
}
