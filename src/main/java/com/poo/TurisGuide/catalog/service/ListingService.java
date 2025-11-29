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

    public List<ListingModel> getListingsByProviderId(java.util.UUID providerId) {
        return listingRepository.findByProvedorId(providerId);
    }

    @Transactional
    public ListingModel updateListing(ListingModel listingModel,long listingId) {
        ListingModel existing = listingRepository.findById(listingId)
        .orElseThrow(() -> new IllegalArgumentException("Listing not found: " + listingId));

        existing.setDescricao(listingModel.getDescricao());
        existing.setIdPrestador(listingModel.getIdPrestador());
        existing.setLocalizacao(listingModel.getLocalizacao());
        existing.setTipo(listingModel.getTipo());
        existing.setTitulo(listingModel.getTitulo());
        existing.setValor(listingModel.getValor());

        return listingRepository.save(existing);
    }

    @Transactional
    public void deleteListing(long listingId) {
        listingRepository.deleteById(listingId);
    }
}
