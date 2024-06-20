package ro.mpp2024.domain;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@jakarta.persistence.Entity
@Table(name="users")
public class User implements Entity<Integer> {
    private int id;
    private String alias;

    public User(int id, String alias) {
        this.id = id;
        this.alias = alias;
    }

    public User() {
    }

    @Override
    public void setId(Integer integer) {
        this.id = integer;
    }

    @Id
    @GeneratedValue(strategy=IDENTITY)
    @Override
    public Integer getId() {
        return id;
    }

    @Column
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
