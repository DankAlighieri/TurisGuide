package com.poo.TurisGuide.catalog.dto;

import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ListingDTO (
        @NotBlank String titulo, 
        @NotBlank String descricao, 
        @NotBlank String localizacao, 
        @NotBlank String tipo, 
        @NotNull @Positive Double valor,
        UUID idPrestador
    ) 
    {}