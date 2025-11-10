package com.poo.TurisGuide.auth.provider.model;

import com.poo.TurisGuide.auth.provider.dtos.ProviderRegisterDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "provider")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ProviderModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @Column(unique = true)
    private String cnpj;

    private String email;
    private String password;
    private String services;
    private LocalDate dob;
    private LocalDate creationDate;

    public ProviderModel(ProviderRegisterDto data) {
        this.name = data.name();
        this.cnpj = data.cnpj();
        this.email = data.email();
        this.services = data.services();
        this.password = data.password();
        this.creationDate = LocalDate.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_PROVIDER"));
    }

    @Override
    public String getUsername() {
        return this.cnpj;
    }

    @Override
    public String getPassword() {
        return this.password;
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

    public String getCnpj() {
        return this.cnpj;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
