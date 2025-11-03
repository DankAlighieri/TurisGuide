package com.poo.TurisGuide.catalog.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poo.TurisGuide.catalog.dto.ListingDTO;
import com.poo.TurisGuide.catalog.model.ListingModel;
import com.poo.TurisGuide.catalog.service.ListingService;
import com.rabbitmq.client.RpcClient.Response;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/listings")
public class ListingController {

    final ListingService listingService;

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
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        e.printStackTrace(); // Para ver o erro completo no log
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                           .body("Erro interno: " + e.getMessage());
    }
}
