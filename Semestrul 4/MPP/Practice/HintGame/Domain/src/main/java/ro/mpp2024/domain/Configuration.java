package ro.mpp2024.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Check;

import java.io.Serializable;
import java.util.Objects;

@jakarta.persistence.Entity
@Table(name = "configurations")
@Check(constraints = "row BETWEEN 1 AND 4 AND column BETWEEN 1 AND 4")
public class Configuration implements Entity<Integer> {
    private Integer id;
    private Integer row;
    private Integer column;
    private String hint;

    public Configuration(Integer id,
                         @Min(1) @Max(4) Integer row,
                         @Min(1) @Max(4) Integer column,
                         String hint) {
        this.id = id;
        this.row = row;
        this.column = column;
        this.hint = hint;
    }

    public Configuration() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer integer) {
        this.id = integer;
    }

    @Min(1)
    @Max(4)
    @NotNull
    @Column
    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    @Min(1)
    @Max(4)
    @NotNull
    @Column
    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    @Column
    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "id=" + id +
                ", row=" + row +
                ", column=" + column +
                ", hint='" + hint + '\'' +
                '}';
    }
}

