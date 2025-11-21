package com.poo.TurisGuide.catalog.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.poo.TurisGuide.catalog.model.ListingModel;
import com.poo.TurisGuide.catalog.repository.ListingRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ListingService {
    private final ListingRepository listingRepository;
    
    @Transactional
    public ListingModel saveListing(ListingModel listingModel) {
        listingModel = listingRepository.save(listingModel);
        return listingModel;
    }

    public List<ListingModel> getAllListing() {
        return listingRepository.findAll();
    }

    @Transactional
    public ListingModel updateListing(ListingModel listingModel, long listingId) {
        ListingModel existing = listingRepository.findById(listingId)
        .orElseThrow(() -> new IllegalArgumentException("Listing not found: " + listingId));

        existing.setDescricao(listingModel.getDescricao());
        existing.setLocalizacao(listingModel.getLocalizacao());
        existing.setTitulo(listingModel.getTitulo());
        existing.setValor(listingModel.getValor());
        
        // Atualiza o prestador (objeto) em vez do ID
        if (listingModel.getPrestador() != null) {
            existing.setPrestador(listingModel.getPrestador());
        }

        // Nota: Alterar o 'tipo' (classe) de uma entidade existente é complexo no JPA/Hibernate.
        // Geralmente não se altera o tipo de um registro na tabela Single Table via update simples.
        // Ignoramos setTipo() aqui.

        return listingRepository.save(existing);
    }

    @Transactional
    public void deleteListing(long listingId) {
        listingRepository.deleteById(listingId);
    }
}
