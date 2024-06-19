package ro.mpp2024.domain;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@jakarta.persistence.Entity
@Table(name="words")
public class Word implements Entity<String> {
    private String word;

    public Word(String word) {
        this.word = word;
    }

    public Word() {
    }

    @Override
    public void setId(String s) {
        this.word = s;
    }

    @Id
    @Override
    public String getId() {
        return word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return Objects.equals(word, word1.word);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(word);
    }
}
