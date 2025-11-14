package com.poo.TurisGuide.booking.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para criação de reserva")
public record BookingDTO(
    @Schema(description = "ID do usuário", example = "550e8400-e29b-41d4-a716-446655440000")
    @NotNull UUID userId,
    
    @Schema(description = "ID do listing", example = "103")
    @NotNull Long listingId,
    
    @Schema(description = "Data de check-in", example = "15/12/2025")
    @JsonFormat(pattern = "dd/MM/yyyy") LocalDate checkIn,
    
    @Schema(description = "Data de check-out", example = "20/12/2025")
    @JsonFormat(pattern = "dd/MM/yyyy") LocalDate checkOut
) {
    
}
