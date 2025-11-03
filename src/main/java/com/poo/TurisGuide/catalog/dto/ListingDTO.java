package com.poo.TurisGuide.catalog.dto;

import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ListingDTO (
        @NotBlank String titulo, 
        @NotBlank String descricao, 
        @NotBlank String localizacao, 
        @NotBlank String tipo, 
        @NotNull UUID idPrestador, 
        @NotNull @DecimalMin(value = "0.01") double valor
    ) 
    {}