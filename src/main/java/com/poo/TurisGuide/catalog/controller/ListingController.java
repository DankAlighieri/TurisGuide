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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/listings")
@Tag(name = "Listagens", description = "Gerenciamento de listagens de serviços turísticos")
@SecurityRequirement(name = "bearerAuth")
public class ListingController {

    private final ListingService listingService;

    @Operation(summary = "Criar nova listagem", description = "Cria uma nova listagem de serviço turístico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Listagem criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<ListingModel> saveListing(@RequestBody @Valid ListingDTO listingDTO) {
        ListingModel listingModel = new ListingModel();
        
        // mapeando os atributos da requisição para o model
        BeanUtils.copyProperties(listingDTO, listingModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.listingService.saveListing(listingModel));
    } 

    @Operation(summary = "Listar todas as listagens", description = "Retorna todas as listagens cadastradas")
    @ApiResponse(responseCode = "200", description = "Lista de listagens retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<ListingModel>> getListings() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.listingService.getAllListing());
    }
    
    @Operation(summary = "Atualizar listagem", description = "Atualiza uma listagem existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listagem atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Listagem não encontrada")
    })
    @PutMapping("/{listingId}")
    public ResponseEntity<ListingModel> updateListing(@RequestBody @Valid ListingDTO listingDTO, @PathVariable long listingId){
        ListingModel updatedListing = new ListingModel();

        BeanUtils.copyProperties(listingDTO, updatedListing);
        updatedListing.setId(listingId);

        updatedListing = this.listingService.updateListing(updatedListing, listingId);

        return ResponseEntity.status(HttpStatus.OK).body(updatedListing);
    }

    @Operation(summary = "Deletar listagem", description = "Remove uma listagem do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listagem deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Listagem não encontrada")
    })
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
