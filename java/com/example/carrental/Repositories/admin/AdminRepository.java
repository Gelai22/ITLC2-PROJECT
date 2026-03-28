package com.example.carrental.Repositories.admin;

import com.example.carrental.Models.admin.AdminModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AdminModel, Long> {
    Optional<AdminModel> findByEmailAddress(String emailAddress);
}