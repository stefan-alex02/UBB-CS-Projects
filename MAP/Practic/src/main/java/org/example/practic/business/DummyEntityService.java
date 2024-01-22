package org.example.practic.business;

import org.example.practic.domain.DummyEntity;
import org.example.practic.exceptions.NonexistentUsernameException;
import org.example.practic.persistence.paging.Page;
import org.example.practic.persistence.paging.Pageable;
import org.example.practic.persistence.pagingrepos.DummyEntityDBPagingRepository;
import org.example.practic.utils.events.DummyEntityEvent;
import org.example.practic.utils.observer.Observable;
import org.example.practic.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class DummyEntityService implements Observable<DummyEntityEvent> {
    private final DummyEntityDBPagingRepository dummyEntityRepository;
    private final List<Observer<DummyEntityEvent>> observerList;

    public DummyEntityService(DummyEntityDBPagingRepository dummyEntityRepository) {
        this.dummyEntityRepository = dummyEntityRepository;
        observerList = new ArrayList<>();
    }

    public DummyEntity getEntityById(Long id) {
        return dummyEntityRepository.findOne(id)
                .orElseThrow(() -> new NonexistentUsernameException("Given ID does not exist"));
    }

    public Page<DummyEntity> getDummies(Pageable pageable) {
        return dummyEntityRepository.findAll(pageable);
    }

    public Long getNoOfDummies() {
        return StreamSupport.stream(dummyEntityRepository.findAll().spliterator(), false)
                .count();
    }

    @Override
    public void addObserver(Observer<DummyEntityEvent> observer) {
        observerList.add(observer);
    }

    @Override
    public void removeObserver(Observer<DummyEntityEvent> observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifyObservers(DummyEntityEvent event) {
        observerList.forEach(observer -> observer.update(event));
    }
}
