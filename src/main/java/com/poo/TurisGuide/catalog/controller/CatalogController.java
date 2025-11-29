package com.poo.TurisGuide.catalog.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.poo.TurisGuide.catalog.dto.ServiceDTO;
import com.poo.TurisGuide.catalog.model.ListingModel;
import com.poo.TurisGuide.catalog.service.ListingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/catalog")
@Tag(name = "Catálogo de Serviços", description = "API para gerenciamento de serviços turísticos por prestadores")
public class CatalogController {

    private final ListingService listingService;

    @Operation(summary = "Criar novo serviço", description = "Cadastra um novo serviço turístico no catálogo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Serviço criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping
    public ResponseEntity<?> createService(@RequestBody @Valid ServiceDTO serviceDTO) {
        try {
            System.out.println("=== CADASTRO DE SERVIÇO ===");
            System.out.println("Tipo: " + serviceDTO.tipo());
            System.out.println("Nome: " + serviceDTO.nome());
            System.out.println("Provedor ID: " + serviceDTO.provedorId());

            ListingModel listingModel = new ListingModel();
            BeanUtils.copyProperties(serviceDTO, listingModel);

            // Mapear campos adicionais se necessário
            listingModel.setIdPrestador(serviceDTO.provedorId());

            ListingModel savedListing = listingService.saveListing(listingModel);

            System.out.println("✅ Serviço cadastrado com ID: " + savedListing.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(savedListing);
        } catch (Exception e) {
            System.out.println("❌ Erro ao cadastrar serviço: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cadastrar serviço: " + e.getMessage());
        }
    }

    @Operation(summary = "Listar serviços do prestador", description = "Retorna todos os serviços de um prestador específico")
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<?> getServicesByProvider(@PathVariable UUID providerId) {
        try {
            System.out.println("=== BUSCAR SERVIÇOS DO PRESTADOR ===");
            System.out.println("Provider ID: " + providerId);

            List<ListingModel> services = listingService.getListingsByProviderId(providerId);

            System.out.println("✅ Encontrados " + services.size() + " serviços");

            return ResponseEntity.ok(services);
        } catch (Exception e) {
            System.out.println("❌ Erro ao buscar serviços: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar serviços: " + e.getMessage());
        }
    }

    @Operation(summary = "Deletar serviço", description = "Remove um serviço do catálogo")
    @DeleteMapping("/{serviceId}")
    public ResponseEntity<?> deleteService(@PathVariable Long serviceId) {
        try {
            System.out.println("=== DELETAR SERVIÇO ===");
            System.out.println("Service ID: " + serviceId);

            listingService.deleteListing(serviceId);

            System.out.println("✅ Serviço deletado com sucesso");

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("❌ Erro ao deletar serviço: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar serviço: " + e.getMessage());
        }
    }

    @Operation(summary = "Listar todos os serviços", description = "Retorna todos os serviços cadastrados no catálogo")
    @GetMapping
    public ResponseEntity<?> getAllServices() {
        try {
            List<ListingModel> services = listingService.getAllListing();
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            System.out.println("❌ Erro ao listar serviços: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar serviços: " + e.getMessage());
        }
    }
}

