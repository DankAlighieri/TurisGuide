package com.poo.TurisGuide.auth.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Cria tabelas separadas unidas por ID
@Table(name = "users_base")
@Data
public abstract class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected UUID id;

    @Column(unique = true, nullable = false)
    protected String login;
    
    @Column(nullable = false)
    protected String password;
    
    protected String email;
    
    protected String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role != null) {
            return List.of(new SimpleGrantedAuthority("ROLE_" + this.role));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return login;
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
