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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/listings")
@Tag(name = "Listagens", description = "Gerenciamento de hotéis, restaurantes e atividades")
public class ListingController {

    private final ListingService listingService;

    @PostMapping
    @Operation(summary = "Criar listing", 
               description = "Cria um novo listing (requer permissão de PROVIDER ou ADMIN)")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Listing criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Token inválido"),
        @ApiResponse(responseCode = "403", description = "Permissão negada")
    })
    public ResponseEntity<ListingModel> saveListing(
            @Parameter(description = "Dados do listing") 
            @RequestBody @Valid ListingDTO listingDTO) {
        ListingModel listingModel = new ListingModel();
        
        // mapeando os atributos da requisição para o model
        BeanUtils.copyProperties(listingDTO, listingModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.listingService.saveListing(listingModel));
    } 

    @GetMapping
    @Operation(summary = "Listar todos os listings", 
               description = "Retorna uma lista de todos os listings disponíveis")
    @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
    public ResponseEntity<List<ListingModel>> getListings() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.listingService.getAllListing());
    }
    
    @PutMapping("/{listingId}")
    @Operation(summary = "Atualizar listing", 
               description = "Atualiza um listing existente")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listing atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Listing não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Token inválido")
    })
    public ResponseEntity<ListingModel> updateListing(
            @Parameter(description = "Dados atualizados do listing") 
            @RequestBody @Valid ListingDTO listingDTO, 
            @Parameter(description = "ID do listing") 
            @PathVariable long listingId){
        ListingModel updatedListing = new ListingModel();

        BeanUtils.copyProperties(listingDTO, updatedListing);
        updatedListing.setId(listingId);

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
