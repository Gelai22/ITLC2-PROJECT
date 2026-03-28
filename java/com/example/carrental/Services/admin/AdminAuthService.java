package com.example.carrental.Services.admin;

import com.example.carrental.Models.admin.AdminModel;
import com.example.carrental.Repositories.admin.AdminRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminAuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.registration.system-key}")
    private String systemKey;

    public AdminAuthService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean validateSystemKey(String providedKey) {
        return systemKey.equals(providedKey);
    }

    @Transactional
    public AdminModel registerNewAdmin(AdminModel admin) {
        String hashedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(hashedPassword);

        return adminRepository.save(admin);
    }
}