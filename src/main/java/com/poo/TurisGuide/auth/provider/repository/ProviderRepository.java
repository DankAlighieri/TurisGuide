package com.poo.TurisGuide.auth.provider.repository;

import com.poo.TurisGuide.auth.provider.model.ProviderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProviderRepository extends JpaRepository<ProviderModel, UUID> {
    UserDetails findByCnpj(String cnpj);
    boolean existsByCnpj(String cnpj);
}
