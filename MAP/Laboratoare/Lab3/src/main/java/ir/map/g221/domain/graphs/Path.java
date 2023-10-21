package ir.map.g221.domain.graphs;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Path<TNode extends Node<TNode>> implements Comparable<Path<TNode>> {
    private final List<TNode> path;

    public Path() {
        path = new ArrayList<>();
    }

    public void addToBeginning(TNode node) {
        path.add(0, node);
    }

    public void appendPath(Path<TNode> path) {
        this.path.addAll(path.path);
    }

    public List<TNode> getPath() {
        return path;
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
        for (var node: path) {
            str.append(node.toString());
            if (path.indexOf(node) < path.size() - 1) {
                str.append(" ->\n");
            }
        }
        return str.toString();
    }
}
