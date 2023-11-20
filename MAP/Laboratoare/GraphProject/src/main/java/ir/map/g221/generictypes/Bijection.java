package ir.map.g221.generictypes;

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
        addPairs(pairs);
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
    public void addPair(TA x, TB fx) throws FunctionFailureException {
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

    public void addInversePair(TB fx, TA x) throws FunctionFailureException {
        addPair(x, fx);
    }

    public void addPairs(Iterable<Pair<TA, TB>> pairs) throws FunctionFailureException {
        pairs.forEach(pair -> this.addPair(pair.getFirst(), pair.getSecond()));
    }

    public TB imageOf(TA domainElem) {
        return domToCodom.get(domainElem);
    }

    public TA preimageOf(TB codomainElem) {
        return codomToDom.get(codomainElem);
    }
}
