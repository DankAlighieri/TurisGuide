/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.poo.TurisGuide.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                    // Endpoints públicos de autenticação
                    .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/auth/register").permitAll() // Mudar em prod!!!!!

                    // Recursos estáticos (HTML, CSS, JS, imagens, etc.)
                    .requestMatchers("/", "/index.html", "/login.html", "/cadastro.html").permitAll()
                    .requestMatchers("/cadastro-servico.html", "/provedor-dashboard.html").permitAll()
                    .requestMatchers("/busca.html", "/reservar.html", "/minhas-reservas.html").permitAll()
                    .requestMatchers("/pacotes.html", "/reservar-pacote.html").permitAll()
                    .requestMatchers("/editar-servico.html", "/gerenciar-reservas.html").permitAll()
                    .requestMatchers("/usuario-dashboard.html").permitAll()
                    .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**").permitAll()
                    

                    // Provider endpoints
                    .requestMatchers(HttpMethod.POST, "/provider/register").permitAll()
                    .requestMatchers(HttpMethod.POST, "/provider/login").permitAll()

                    // Swagger endpoints
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/api-docs/**").permitAll()
                    .requestMatchers("/swagger-ui.html").permitAll()

                    .requestMatchers(HttpMethod.POST, "/booking").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.GET, "/booking/{userId}").hasAnyRole("USER", "ADMIN")


                    .requestMatchers(HttpMethod.POST, "/listings").permitAll()                   
                    .requestMatchers(HttpMethod.POST, "/listings").hasAnyRole("PRESTADOR", "ADMIN")                    
                    // Alterado para permitAll para que a busca (busca.html) funcione sem login
                    .requestMatchers(HttpMethod.GET, "/listings/{providerId}").permitAll()
                    
                    .anyRequest().authenticated()
                )
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
