package com.poo.TurisGuide.catalog.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poo.TurisGuide.catalog.model.ListingModel;

@Repository
public interface ListingRepository extends JpaRepository<ListingModel, Long> {

}