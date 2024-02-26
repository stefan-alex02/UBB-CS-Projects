package org.example.modelpractic.business;

import org.example.modelpractic.domain.Person;
import org.example.modelpractic.exceptions.NonexistentUsernameException;
import org.example.modelpractic.persistence.Repository;

import java.util.stream.StreamSupport;

public class PersonService {
    private final Repository<Long, Person> personRepository;

    public PersonService(Repository<Long, Person> personRepository) {
        this.personRepository = personRepository;
    }

    public Person getPersonByUsername(String username) throws NonexistentUsernameException {
        return StreamSupport.stream(personRepository.findAll().spliterator(), false)
                .filter(p -> p.getUsername().equals(username)).findFirst()
                .orElseThrow(() -> new NonexistentUsernameException("There is no person with given username"));
    }
}
