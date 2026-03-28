package com.example.parkupjavafx.services;

import com.example.parkupjavafx.models.User;
import com.example.parkupjavafx.repositories.UserRepository;

public class AuthService {

    private final UserRepository userRepository = new UserRepository();

    // Static variable to keep the user logged in across different screens
    private static User currentUser;

    public boolean login(String email, String password) {
        User user = userRepository.authenticate(email, password);
        if (user != null) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public boolean register(String name, String email, String password) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return false;
        }
        if (userRepository.emailExists(email)) {
            return false;
        }
        return userRepository.register(name, email, password);
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
    }
}