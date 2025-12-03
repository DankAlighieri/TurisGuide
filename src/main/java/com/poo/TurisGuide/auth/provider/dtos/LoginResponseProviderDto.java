package com.poo.TurisGuide.auth.provider.dtos;

import java.util.UUID;

public record LoginResponseProviderDto(String token, UUID providerId) {
}
