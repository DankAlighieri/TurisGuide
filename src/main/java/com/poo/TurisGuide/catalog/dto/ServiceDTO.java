package com.poo.TurisGuide.catalog.dto;

import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ServiceDTO(
    @NotBlank String nome,
    @NotBlank String descricao,
    @NotBlank String tipo,
    @NotNull @DecimalMin(value = "0.01") Double preco,
    @NotNull @Min(1) Integer capacidade,
    @NotBlank String localizacao,
    String endereco,
    String imagem,
    String comodidades,
    @NotNull UUID provedorId
) {
}

