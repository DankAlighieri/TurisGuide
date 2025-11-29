package com.poo.TurisGuide.auth.provider.dtos;

import com.poo.TurisGuide.auth.provider.model.ProviderModel;

import java.util.UUID;

public record LoginResponseProviderDto(String token, ProviderData provider) {

    public record ProviderData(
            UUID id,
            String name,
            String cnpj,
            String email,
            String services
    ) {
        public static ProviderData fromModel(ProviderModel model) {
            return new ProviderData(
                    model.getId(),
                    model.getName(),
                    model.getCnpj(),
                    model.getEmail(),
                    model.getServices()
            );
        }
    }
}
