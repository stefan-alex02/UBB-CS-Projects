package org.example.zboruri.business;

import org.example.zboruri.domain.Client;
import org.example.zboruri.exceptions.NonexistentUsernameException;
import org.example.zboruri.persistence.Repository;

public class ClientService {
    private Repository<String, Client> clientRepository;

    public ClientService(Repository<String, Client> clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client getClientByUsername(String username) {
        return clientRepository.findOne(username)
                .orElseThrow(() -> new NonexistentUsernameException("No client with username \"" +
                        username + "\" exists."));
    }
}
