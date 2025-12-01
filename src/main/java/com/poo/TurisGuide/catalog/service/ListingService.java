package com.poo.TurisGuide.catalog.service;

import java.util.List;
import java.util.UUID;

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

    public List<ListingModel> getListingsByProvider(UUID providerId) {
        return listingRepository.findByPrestadorId(providerId);
    }

    public List<ListingModel> getAllListings() {
        return listingRepository.findAll();
    }

    @Transactional
    public ListingModel updateListing(ListingModel listingModel, UUID listingId) {
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

        return listingRepository.save(existing);
    }

    @Transactional
    public void deleteListing(UUID listingId) {
        listingRepository.deleteById(listingId);
    }
}
