package org.example.practic.domain;

import java.util.Objects;

public abstract class Entity<ID> {
    protected ID ID;

    public Entity(ID ID) {
        this.ID = ID;
    }

    public ID getID() {
        return ID;
    }

    public void setID(ID ID) {
        this.ID = ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(ID, entity.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
}
