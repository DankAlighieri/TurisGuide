package com.poo.TurisGuide.auth.user.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.poo.TurisGuide.auth.user.roles.UserRole;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDTO(
    @NotBlank String login,
    @NotBlank String password,
    @NotBlank String firstName,
    String lastName,
    @NotBlank String address,
    @NotBlank @Valid @Email String email,
    @JsonFormat(pattern = "dd/MM/yyyy") LocalDate DOB,
    @NotNull UserRole role
) {}