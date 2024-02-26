package ir.map.g221.guisocialnetwork.persistence;

public enum EntityTable {
    USER("users");

    private final String tableName;

    EntityTable(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

}
