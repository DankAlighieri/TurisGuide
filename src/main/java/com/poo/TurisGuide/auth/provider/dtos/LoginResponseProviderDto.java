package com.poo.TurisGuide.auth.provider.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginResponseProviderDto(String token) {
}
