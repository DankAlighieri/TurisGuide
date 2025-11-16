package com.poo.TurisGuide.auth.provider.dtos;

import jakarta.validation.constraints.NotBlank;

public record ProviderDTO(@NotBlank String cnpj,@NotBlank String password) {
}
