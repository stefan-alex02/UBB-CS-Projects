package ro.mpp2024.persistence.word;

import ro.mpp2024.domain.Word;
import ro.mpp2024.persistence.Repository;

import java.util.Collection;

public interface WordRepository extends Repository<Word, String> {
    Collection<Word> chooseWords(int n);
}
