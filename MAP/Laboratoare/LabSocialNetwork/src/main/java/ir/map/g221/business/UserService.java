package ir.map.g221.business;

import ir.map.g221.domain.generaltypes.ObjectTransformer;
import ir.map.g221.domain.generaltypes.UnorderedPair;
import ir.map.g221.domain.graphs.Edge;
import ir.map.g221.domain.graphs.UndirectedGraph;
import ir.map.g221.domain.Community;
import ir.map.g221.exceptions.NotFoundException;
import ir.map.g221.exceptions.ValidationException;
import ir.map.g221.persistence.Repository;
import ir.map.g221.domain.entities.Friendship;
import ir.map.g221.domain.entities.User;

import java.util.List;

public class UserService {
    private final Repository<UnorderedPair<Long, Long>, Friendship> friendshipRepository;
    private final Repository<Long, User> userRepository;

    public UserService(Repository<Long, User> userRepository,
                       Repository<UnorderedPair<Long, Long>, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    public void addUser(String firstName, String lastName) throws ValidationException {
        userRepository.save(new User(firstName, lastName));
    }

    public User getUser(Long id) throws NotFoundException {
        return userRepository
                .findOne(id)
                .orElseThrow(() ->
                    new NotFoundException("User could not be found.")
                );
    }

    public void removeUser(Long id) throws NotFoundException {
//        userRepository.findOne(id).ifPresentOrElse(user ->
//                ObjectTransformer.iterableToCollection(friendshipRepository.findAll())
//                        .stream()
//                        .filter(friendship -> friendship.hasUser(user))
//                        .forEach(friendship -> friendshipRepository.delete(friendship.getId()))
//        , () -> {
//            throw new NotFoundException("User could not be found.");
//        });
        userRepository.delete(id).orElseThrow(() -> new NotFoundException("User could not be found."));
    }

    public List<Community> calculateCommunities() {
        return createGraphFromUsers()
                .getAllComponents().stream()
                .map(Community::new)
                .toList();
    }

    public Community mostSociableCommunity() {
        return new Community(createGraphFromUsers().getComponentWithLongestPath());
    }

    private UndirectedGraph<User> createGraphFromUsers() {
        UndirectedGraph<User> graph = new UndirectedGraph<>(
                ObjectTransformer.iterableToSet(userRepository.findAll())
        );

        graph.tryAddEdges(ObjectTransformer.iterableToCollection(friendshipRepository.findAll()).stream()
                .map(fr -> Edge.of(fr.getFirstUser(),fr.getSecondUser()))
                .toList()
        );
        return graph;
    }
}
