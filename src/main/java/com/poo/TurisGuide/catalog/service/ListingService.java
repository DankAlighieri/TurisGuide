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
}
