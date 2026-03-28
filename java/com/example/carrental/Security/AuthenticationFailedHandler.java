package com.example.carrental.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationFailedHandler implements AuthenticationFailureHandler {

    private static final String CLIENT_LOGIN_URL = "/login?error";
    private static final String ADMIN_LOGIN_URL = "/admin/login?error";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String referrer = request.getHeader("referer");

        if (referrer != null && referrer.contains("/admin/login")) {
            response.sendRedirect(ADMIN_LOGIN_URL);
        } else {
            response.sendRedirect(CLIENT_LOGIN_URL);
        }
    }
}