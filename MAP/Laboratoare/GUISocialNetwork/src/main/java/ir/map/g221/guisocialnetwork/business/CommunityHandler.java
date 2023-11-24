package ir.map.g221.guisocialnetwork.business;

import ir.map.g221.guisocialnetwork.domain.Community;
import ir.map.g221.guisocialnetwork.domain.entities.Friendship;
import ir.map.g221.guisocialnetwork.domain.entities.User;
import ir.map.g221.guisocialnetwork.persistence.Repository;
import ir.map.g221.guisocialnetwork.utils.events.Event;
import ir.map.g221.guisocialnetwork.utils.events.FriendshipChangeEvent;
import ir.map.g221.guisocialnetwork.utils.events.UserChangeEvent;
import ir.map.g221.guisocialnetwork.utils.generictypes.UnorderedPair;
import ir.map.g221.guisocialnetwork.utils.graphs.UndirectedGraph;
import ir.map.g221.guisocialnetwork.utils.observer.Observer;

import java.util.List;

public class CommunityHandler implements Observer {
    private UndirectedGraph<User> graph = null;
    private final Repository<Long, User> userRepository;
    private final Repository<UnorderedPair<Long, Long>, Friendship> friendshipRepository;

    public CommunityHandler(Repository<Long, User> userRepository,
                            Repository<UnorderedPair<Long, Long>, Friendship> friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    UndirectedGraph<User> getGeneratedGraph() {
        if (graph == null) {
            graph = UndirectedGraph.ofEmpty();
            userRepository.findAll().forEach(user -> graph.addVertex(user));
            friendshipRepository.findAll().forEach(friendship ->
                    graph.addEdge(friendship.getFirstUser(), friendship.getSecondUser()));
        }
        return graph;
    }

    public Community getCommunityOfUser(User user) {
        return Community.of(getGeneratedGraph().getComponentOf(user).orElseThrow());
    }

    public List<Community> calculateCommunities() {
        return getGeneratedGraph().getComponents().stream()
                .map(Community::of)
                .toList();
    }

    public Community mostSociableCommunity() {
        var pair = getGeneratedGraph().getComponentWithLongestPath();
        return Community.of(pair.getFirst(), pair.getSecond());
    }

    @Override
    public void update(Event event) {
        switch(event.getEventType()) {
            case USER :
                handleUserChangeEvent((UserChangeEvent) event);
                break;
            case FRIENDSHIP:
                handleFriendshipChangeEvent((FriendshipChangeEvent) event);
                break;
            default:;
        }
    }

    private void handleFriendshipChangeEvent(FriendshipChangeEvent friendshipChangeEvent) {
        switch (friendshipChangeEvent.getChangeEventType()) {
            case ADD:
                getGeneratedGraph().addEdge(
                        friendshipChangeEvent.getNewData().getFirstUser(),
                        friendshipChangeEvent.getNewData().getSecondUser());
                break;
            case DELETE:
                getGeneratedGraph().removeEdge(
                        friendshipChangeEvent.getOldData().getFirstUser(),
                        friendshipChangeEvent.getOldData().getSecondUser());
        }
    }

    private void handleUserChangeEvent(UserChangeEvent userChangeEvent) {
        switch (userChangeEvent.getChangeEventType()) {
            case ADD:
                graph = null;
                break;
            case DELETE:
                getGeneratedGraph().removeVertex(userChangeEvent.getOldData());
                break;
            case UPDATE:
                getGeneratedGraph().updateVertex(userChangeEvent.getOldData(), userChangeEvent.getNewData());
                break;
            default:;
        }
    }
}
