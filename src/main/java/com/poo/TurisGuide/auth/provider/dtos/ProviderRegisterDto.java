package com.poo.TurisGuide.auth.provider.dtos;

public record ProviderRegisterDto(String name, String cnpj, String email,String password, String services) {
}
