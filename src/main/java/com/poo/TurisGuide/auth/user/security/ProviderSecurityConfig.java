package com.poo.TurisGuide.auth.provider.infra.security;

import com.poo.TurisGuide.auth.provider.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ProviderSecurityConfig {

    @Autowired
    private ProviderService providerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean(name = "providerAuthenticationManager")
    public AuthenticationManager providerAuthenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(providerService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setHideUserNotFoundExceptions(false);

        return new ProviderManager(authenticationProvider);
    }
}
