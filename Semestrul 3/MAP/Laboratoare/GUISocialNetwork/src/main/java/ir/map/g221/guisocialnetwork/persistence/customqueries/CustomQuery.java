package ir.map.g221.guisocialnetwork.persistence.customqueries;

import ir.map.g221.guisocialnetwork.domain.entities.Entity;
import org.intellij.lang.annotations.Language;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class CustomQuery<ID, E extends Entity<ID>> {
    @Language("SQL")
    protected String queryString;

    protected Object[] settings;

    CustomQuery(final Object... settings) {
        this.settings = settings;
    }

    public String getQueryString() {
        return queryString;
    }

    public abstract void fillStatement(PreparedStatement statement) throws SQLException;
}
