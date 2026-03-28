package com.example.carrental.Services.client;

import com.example.carrental.Models.client.ClientModel;
import com.example.carrental.Repositories.client.ClientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientAuthService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientAuthService(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ClientModel registerNewClient(ClientModel client) {
        String hashedPassword = passwordEncoder.encode(client.getPassword());
        client.setPassword(hashedPassword);

        return clientRepository.save(client);
    }
}