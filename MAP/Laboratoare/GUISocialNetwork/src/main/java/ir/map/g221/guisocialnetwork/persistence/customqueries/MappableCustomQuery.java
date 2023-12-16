package ir.map.g221.guisocialnetwork.persistence.customqueries;

import ir.map.g221.guisocialnetwork.domain.entities.Entity;

public abstract class MappableCustomQuery<ID, E extends Entity<ID>, RE> extends CustomQuery<ID, E> {
    public MappableCustomQuery(Object[] settings) {
        super(settings);
    }

    /**
     * A mapper method that transforms the given entity E obtained from the result set,
     * into a RE type object.
     * @param entity the given entity of type E
     * @return the mapped entity of type RE
     */
    public abstract RE map(E entity);
}
