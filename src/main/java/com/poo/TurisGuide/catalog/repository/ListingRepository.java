package com.poo.TurisGuide.catalog.repository;


import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poo.TurisGuide.catalog.model.ListingModel;

@Repository
public interface ListingRepository extends JpaRepository<ListingModel, UUID> {
    List<ListingModel> findByPrestadorId(UUID prestadorId);
}