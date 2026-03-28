package com.example.carrental.Security;

import com.example.carrental.Models.admin.AdminModel;
import com.example.carrental.Repositories.admin.AdminRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    public AdminDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AdminModel admin = adminRepository.findByEmailAddress(email)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with email: " + email));

        return new AdminPrincipal(admin);
    }
}