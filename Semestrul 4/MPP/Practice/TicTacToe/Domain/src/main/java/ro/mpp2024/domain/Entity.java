package ro.mpp2024.domain;

public interface Entity<ID> {
    /**
     * Sets the id
     * @param id the id
     */
    void setId(ID id);

    /**
     * Gets the id
     * @return the id
     */
    ID getId();
}
