package com.poo.TurisGuide.auth.provider.service;

import com.poo.TurisGuide.auth.provider.model.ProviderModel;
import com.poo.TurisGuide.auth.provider.repository.ProviderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProviderService implements UserDetailsService {

    @Autowired
    private ProviderRepository providerRepository;

    public boolean existsByCnpj(String cnpj) {
        return providerRepository.existsByCnpj(cnpj);
    }

    @Transactional
    public ProviderModel saveProvider(ProviderModel providerModel){
        return providerRepository.save(providerModel);
    }

    @Override
    public UserDetails loadUserByUsername(String cnpj) throws UsernameNotFoundException {
        UserDetails provider = providerRepository.findByCnpj(cnpj);
        if (provider == null) {
            throw new UsernameNotFoundException("Provider not found with CNPJ: " + cnpj);
        }
        return provider;
    }
    @Transactional
    public List<ProviderModel> getAllProviders(){
        return providerRepository.findAll();
    }
}
