package com.poo.TurisGuide.auth.user.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDTO(
    UUID id,
    @NotBlank String passwordHash,
    @NotBlank String firstName,
    String lastName,
    @NotBlank String address,
    @NotBlank @Valid @Email String email,
    @JsonFormat(pattern = "dd/MM/yyyy") LocalDate DOB
) {}