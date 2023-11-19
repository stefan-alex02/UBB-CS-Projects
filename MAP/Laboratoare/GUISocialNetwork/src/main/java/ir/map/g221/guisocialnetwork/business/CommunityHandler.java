package ir.map.g221.guisocialnetwork.business;

import ir.map.g221.guisocialnetwork.domain.Community;
import ir.map.g221.guisocialnetwork.domain.entities.Friendship;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.persistence.Repository;
import ir.map.g221.guisocialnetwork.utils.events.FriendshipChangeEvent;
import ir.map.g221.guisocialnetwork.utils.events.UserChangeEvent;
import ir.map.g221.guisocialnetwork.utils.generictypes.ObjectTransformer;
import ir.map.g221.guisocialnetwork.utils.generictypes.UnorderedPair;
import ir.map.g221.guisocialnetwork.utils.graphs.Edge;
import ir.map.g221.guisocialnetwork.utils.graphs.UndirectedGraph;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CommunityHandler {
    private UndirectedGraph<User> graph = null;
    private final Repository<Long, User> userRepository;
    private final Repository<UnorderedPair<Long, Long>, Friendship> friendshipRepository;

    public CommunityHandler(Repository<Long, User> userRepository,
                            Repository<UnorderedPair<Long, Long>, Friendship> friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    private User getGraphUser(User user) {
        return graph.getNodes().stream()
                .filter(Predicate.isEqual(user))
                .findFirst()
                .orElseThrow();
    }

    private Edge<User> getGraphEdge(User user1, User user2) {
        return graph.getEdges().stream()
                .filter(Predicate.isEqual(Edge.of(getGraphUser(user1), getGraphUser(user2))))
                .findFirst()
                .orElseThrow();
    }

    /**
     *
     * @return true if the graph was not generated before, false otherwise.
     */
    private void generateGraph() {
        graph = new UndirectedGraph<>(
                ObjectTransformer.iterableToSet(userRepository.findAll()),
                ObjectTransformer.iterableToCollection(friendshipRepository.findAll()).stream()
                        .map(fr -> Edge.of(fr.getFirstUser(),fr.getSecondUser()))
                        .collect(Collectors.toSet())
        );
    }

    public void generateGraphIfNull() {
        if (graph == null) {
            generateGraph();
        }
    }

    public Community getCommunityOfUser(User user) {
        generateGraphIfNull();
        return new Community(graph.componentOf(user).orElseThrow());
    }

    public List<Community> calculateCommunities() {
        generateGraphIfNull();
        return graph.getComponents().stream()
                .map(Community::new)
                .toList();
    }

    public Community mostSociableCommunity() {
        generateGraphIfNull();
        return new Community(graph.getComponentWithLongestPath());
    }

    public void update(UserChangeEvent event) {
        switch(event.getEventType()) {
            case ADD:
                graph = null;
                break;
            case DELETE:
                if (graph != null) {
                    graph.forceRemoveNode(getGraphUser(event.getOldData()));
                }
                else {
                    generateGraph();
                }
                break;
            case UPDATE:
                if (graph != null) {
                    graph.updateNode(getGraphUser(event.getOldData()), event.getNewData());
                }
                else {
                    generateGraph();
                }
                break;
            default:;
        }
    }

    public void update(FriendshipChangeEvent event) {
        switch(event.getEventType()) {
            case ADD:
                if (graph != null) {
                    graph.forceAddEdge(Edge.of(
                            event.getNewData().getFirstUser(),
                            event.getNewData().getSecondUser())
                    );
                }
                else {
                    generateGraph();
                }
                break;
            case DELETE:
                if (graph != null) {
                    graph.forceRemoveEdge(getGraphEdge(
                            event.getOldData().getFirstUser(),
                            event.getOldData().getSecondUser())
                    );
                }
                else {
                    generateGraph();
                }
                break;
            default:;
        }
    }
}
