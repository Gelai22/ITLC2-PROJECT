package com.example.carrental.Repositories.client;

import com.example.carrental.Models.client.ClientModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientModel, Long> {

    Optional<ClientModel> findByEmailAddress(String emailAddress);
}