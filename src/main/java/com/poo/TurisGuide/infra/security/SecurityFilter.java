package com.poo.TurisGuide.infra.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.poo.TurisGuide.auth.user.repository.UserRepository;
import com.poo.TurisGuide.auth.provider.repository.ProviderRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter{
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProviderRepository providerRepository;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, 
        @NonNull HttpServletResponse response, 
        @NonNull  FilterChain filterChain) throws ServletException, IOException {
        
            var token = recoverToken(request);
            if (token != null) {
                var identifier = tokenService.validateToken(token);
                if (!identifier.isEmpty()){
                    UserDetails userDetails = null;
                    
                    // Tenta buscar como User (por login)
                    userDetails = userRepository.findByLogin(identifier);
                    
                    // Se não encontrar, tenta como Provider (por cnpj)
                    if (userDetails == null) {
                        userDetails = providerRepository.findByCnpj(identifier);
                    }
                    
                    // Se encontrou (User ou Provider), autentica
                    if (userDetails != null) {
                        var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
            filterChain.doFilter(request, response);
    }
    
    private String recoverToken(HttpServletRequest req) {
        var authHeader = req.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}