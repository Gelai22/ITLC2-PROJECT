package com.example.carrental.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ClientDetailsService clientDetailsService;
    private final AdminDetailsService adminDetailsService;
    private final AuthenticationHandler successHandler;
    private final AuthenticationFailedHandler failureHandler;

    public SecurityConfig(ClientDetailsService clientDetailsService, AdminDetailsService adminDetailsService,
                          AuthenticationHandler successHandler, AuthenticationFailedHandler failureHandler) {
        this.clientDetailsService = clientDetailsService;
        this.adminDetailsService = adminDetailsService;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager adminAuthenticationManager() {
        DaoAuthenticationProvider adminProvider = new DaoAuthenticationProvider();
        adminProvider.setUserDetailsService(adminDetailsService);
        adminProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(Collections.singletonList(adminProvider));
    }

    @Bean
    @Primary
    public AuthenticationManager clientAuthenticationManager() {
        DaoAuthenticationProvider clientProvider = new DaoAuthenticationProvider();
        clientProvider.setUserDetailsService(clientDetailsService);
        clientProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(Collections.singletonList(clientProvider));
    }

    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/admin/**")
                .authenticationManager(adminAuthenticationManager())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/login", "/admin/register").permitAll()
                        .anyRequest().hasRole("ADMIN")
                )
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessUrl("/admin/login?logout")
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/admin/login?error"))
                );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")
                .authenticationManager(clientAuthenticationManager())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login", "/register", "/admin/login", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/dashboard", "/reservation/add", "/car/**", "/maintenance/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> response.sendRedirect("/"))
                );

        return http.build();
    }
}