package com.poo.TurisGuide.catalog.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poo.TurisGuide.catalog.dto.ListingDTO;
import com.poo.TurisGuide.catalog.model.ListingModel;
import com.poo.TurisGuide.catalog.service.ListingService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/listings")
public class ListingController {

    private final ListingService listingService;

    @PostMapping
    public ResponseEntity<ListingModel> saveListing(@RequestBody @Valid ListingDTO listingDTO) {
        ListingModel listingModel = new ListingModel();
        
        // mapeando os atributos da requisição para o model
        BeanUtils.copyProperties(listingDTO, listingModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.listingService.saveListing(listingModel));
    } 

    @GetMapping
    public ResponseEntity<List<ListingModel>> getListings() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.listingService.getAllListing());
    }
    
    @PutMapping("/{listingId}")
    public ResponseEntity<ListingModel> updateListing(@RequestBody @Valid ListingDTO listingDTO, @PathVariable long listingId){
        ListingModel updatedListing = new ListingModel();

        BeanUtils.copyProperties(listingDTO, updatedListing);
//        updatedListing.setId(listingId);

        updatedListing = this.listingService.updateListing(updatedListing, listingId);

        return ResponseEntity.status(HttpStatus.OK).body(updatedListing);
    }

    @DeleteMapping("/{listingId}")
    public ResponseEntity<String> deleteListing(@PathVariable long listingId){
        listingService.deleteListing(listingId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        e.printStackTrace(); // Para ver o erro completo no log
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                           .body("Erro interno: " + e.getMessage());
    }
}
