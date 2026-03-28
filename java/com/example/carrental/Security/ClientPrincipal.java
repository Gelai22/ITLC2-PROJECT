package com.example.carrental.Security;

import com.example.carrental.Models.client.ClientModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class ClientPrincipal implements UserDetails {

    // 🛠️ FIX 1: Hold a reference to the actual ClientModel object
    private final ClientModel client;

    public ClientPrincipal(ClientModel client) {
        this.client = client;
    }

    public ClientModel getClient() {
        return client;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Clients typically have a single role, or none if you use the ClientModel directly
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENT"));
    }

    @Override
    public String getPassword() {
        return client.getPassword();
    }

    @Override
    public String getUsername() {
        return client.getEmailAddress();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}