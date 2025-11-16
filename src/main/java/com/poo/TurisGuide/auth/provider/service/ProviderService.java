package com.poo.TurisGuide.auth.provider.service;

import com.poo.TurisGuide.auth.provider.model.ProviderModel;
import com.poo.TurisGuide.auth.provider.repository.ProviderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderService {

    @Autowired
    private ProviderRepository providerRepository;

    public boolean existsByCnpj(String cnpj) {
        return providerRepository.existsByCnpj(cnpj);
    }

    @Transactional
    public ProviderModel saveProvider(ProviderModel providerModel){
        return providerRepository.save(providerModel);
    }
    @Transactional
    public List<ProviderModel> getAllProviders(){
        return providerRepository.findAll();
    }

    public ProviderModel findByCnpj(String cnpj) {
        return (ProviderModel) providerRepository.findByCnpj(cnpj);
    }

    /*
    @Transactional
    public  ProviderModel getProviderId(String cnpj){

    }
    */
}
