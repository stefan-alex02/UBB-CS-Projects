package ir.map.g221.guisocialnetwork.utils.generictypes;

import ir.map.g221.guisocialnetwork.exceptions.functionexceptions.FunctionFailureException;
import ir.map.g221.guisocialnetwork.exceptions.functionexceptions.InjectionFailureException;
import ir.map.g221.guisocialnetwork.exceptions.functionexceptions.UnivalentFailureException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Bijection<TA, TB> {
    private final Map<TA, TB> domToCodom;
    private final Map<TB, TA> codomToDom;

    /**
     * Bijection empty constructor.
     */
    public Bijection() {
        domToCodom = new HashMap<>();
        codomToDom = new HashMap<>();
    }

    /**
     * Bijection constructor that receives a set of pairs (given pairs must guarantee a bijection).
     * @param pairs the set of pairs to be added to the bijection
     */
    public Bijection(Iterable<Pair<TA, TB>> pairs) {
        domToCodom = new HashMap<>();
        codomToDom = new HashMap<>();
        putPairs(pairs);
    }

    /**
     * Creates and returns a bijection which represents the inverse of current bijection
     * (e.g. if current bijection is defined as {@code f : A -> B}, then its inverse will be
     * defined as {@code f^(-1) : B -> A} such that {@code f^(-1)(f(x)) = x (for any x in A)}, or
     * {@code f(f^(-1)(y)) = y (for any y in B)}
     * @return the inverse bijection function
     */
    public Bijection<TB, TA> inverseFunction() {
        return new Bijection<>(
                domToCodom.entrySet().stream()
                        .map(entry -> Pair.of(
                                entry.getValue(),
                                entry.getKey()))
                        .toList()
        );
    }

    /**
     * Gets the domain of the bijection (the set of all arguments).
     * @return the domain
     */
    public Set<TA> getDomain() {
        return domToCodom.keySet();
    }

    /**
     * Gets the codomain of the bijection (the set of all images).
     * @return the codomain
     */
    public Set<TB> getCodomain() {
        return codomToDom.keySet();
    }

    /**
     * Extends the function by adding a new pair (x, y).
     * @param x the domain element.
     * @param y the codomain element (image of x or f(x)).
     * @throws FunctionFailureException if either x or y already exists in the domain or codomain respectively.
     */
    public void putPair(TA x, TB y) throws FunctionFailureException {
        if (domToCodom.containsKey(x) && codomToDom.containsKey(y)) {
            return;
        }

        if (domToCodom.containsKey(x)) {
            throw new UnivalentFailureException();
        }
        if (codomToDom.containsKey(y)) {
            throw new InjectionFailureException();
        }

        domToCodom.put(x, y);
        codomToDom.put(y, x);
    }

    /**
     * Adds multiple pairs (x, y) to the bijection.
     * @param pairs the set of pairs (all of them must be completely new)
     * @throws FunctionFailureException if any of the elements of any given pairs already
     * exists in the bijection
     */
    public void putPairs(Iterable<Pair<TA, TB>> pairs) throws FunctionFailureException {
        pairs.forEach(pair -> this.putPair(pair.getFirst(), pair.getSecond()));
    }

    /**
     * Removes the corresponding Y value for given X, if it exists.
     * @param x the X for which the matching pair (X,Y) is to be removed.
     * @return true if the bijection had such a pair, false otherwise
     */
    public boolean removePairOfX(TA x) {
        if (!domToCodom.containsKey(x)) {
            return false;
        }

        TB y = imageOf(x);
        domToCodom.remove(x);
        codomToDom.remove(y);

        return true;
    }

    /**
     * Removes the corresponding X value for given Y, if it exists.
     * @param y the Y for which the matching pair (X,Y) is to be removed.
     * @return true if the bijection had such a pair, false otherwise
     */
    public boolean removePairOfY(TB y) {
        if (!codomToDom.containsKey(y)) {
            return false;
        }

        TA x = preimageOf(y);
        codomToDom.remove(y);
        domToCodom.remove(x);

        return true;
    }

    /**
     * Gets the corresponding image Y of given element X in the bijection.
     * @param domainElem the element X whose associated Y is to be returned
     * @return the value to which the specified X is mapped, or
     *      {@code null} if this bijection contains no mapping for X
     */
    public TB imageOf(TA domainElem) {
        return domToCodom.get(domainElem);
    }

    /**
     * Gets the corresponding pre-image X of given element Y in the bijection.
     * @param codomainElem the element Y whose associated X is to be returned
     * @return the value to which the specified Y is mapped, or
     *      {@code null} if this bijection contains no mapping for Y
     */
    public TA preimageOf(TB codomainElem) {
        return codomToDom.get(codomainElem);
    }

    /**
     * Clears the entire content of bijection function.
     */
    public void clear() {
        domToCodom.clear();
        codomToDom.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bijection<?, ?> bijection = (Bijection<?, ?>) o;
        return Objects.equals(domToCodom, bijection.domToCodom) && Objects.equals(codomToDom, bijection.codomToDom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domToCodom, codomToDom);
    }
}
