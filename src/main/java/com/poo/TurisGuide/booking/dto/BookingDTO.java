package com.poo.TurisGuide.booking.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;


public record BookingDTO(
    UUID userId,
    UUID listingId,
    @JsonFormat(pattern = "dd/MM/yyyy") LocalDate checkIn,
    @JsonFormat(pattern = "dd/MM/yyyy") LocalDate checkOut,
    int guests,
    String paymentMethod
) {
    
}
