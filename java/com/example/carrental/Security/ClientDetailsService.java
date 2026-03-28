package com.example.carrental.Security;

import com.example.carrental.Models.client.ClientModel;
import com.example.carrental.Repositories.client.ClientRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class ClientDetailsService implements UserDetailsService {
    private final ClientRepository clientRepository;

    public ClientDetailsService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        ClientModel client = clientRepository.findByEmailAddress(email)
                .orElseThrow(() -> new UsernameNotFoundException("Client not found with email: " + email));

        return new ClientPrincipal(client);
    }
}