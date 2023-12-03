package ir.map.g221.guisocialnetwork.utils.generictypes;

import ir.map.g221.guisocialnetwork.exceptions.functionexceptions.FunctionFailureException;
import ir.map.g221.guisocialnetwork.exceptions.functionexceptions.InjectionFailureException;
import ir.map.g221.guisocialnetwork.exceptions.functionexceptions.UnivalentFailureException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Bijection<TA, TB> {
    private final Map<TA, TB> domToCodom;
    private final Map<TB, TA> codomToDom;

    public Bijection() {
        domToCodom = new HashMap<>();
        codomToDom = new HashMap<>();
    }

    public Bijection(Iterable<Pair<TA, TB>> pairs) {
        domToCodom = new HashMap<>();
        codomToDom = new HashMap<>();
        putPairs(pairs);
    }

    public Bijection<TB, TA> inverseFunction() {
        return new Bijection<>(
                domToCodom.entrySet().stream()
                        .map(entry -> Pair.of(
                                entry.getValue(),
                                entry.getKey()))
                        .toList()
        );
    }

    public Set<TA> getDomain() {
        return domToCodom.keySet();
    }

    public Set<TB> getCodomain() {
        return codomToDom.keySet();
    }

    /**
     * Extends the function by adding a new pair (x, f(x)).
     * @param x the domain element.
     * @param fx the codomain element (image of x or f(x)).
     * @throws FunctionFailureException if either x or f(x) already exists in the domain or codomain respectively.
     */
    public void putPair(TA x, TB fx) throws FunctionFailureException {
        if (domToCodom.containsKey(x) && codomToDom.containsKey(fx)) {
            return;
        }

        if (domToCodom.containsKey(x)) {
            throw new UnivalentFailureException();
        }
        if (codomToDom.containsKey(fx)) {
            throw new InjectionFailureException();
        }

        domToCodom.put(x, fx);
        codomToDom.put(fx, x);
    }

    public void putInversePair(TB fx, TA x) throws FunctionFailureException {
        putPair(x, fx);
    }

    public void putPairs(Iterable<Pair<TA, TB>> pairs) throws FunctionFailureException {
        pairs.forEach(pair -> this.putPair(pair.getFirst(), pair.getSecond()));
    }

    public boolean removePairOfX(TA x) throws FunctionFailureException {
        if (!domToCodom.containsKey(x)) {
            return false;
        }

        TB y = imageOf(x);
        domToCodom.remove(x);
        codomToDom.remove(y);

        return true;
    }

    public boolean removePairOfY(TB y) throws FunctionFailureException {
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

    public void clear() {
        domToCodom.clear();
        codomToDom.clear();
    }
}
