package com.poo.TurisGuide.auth.user.service.booking.dto;

import java.time.LocalDate;
import java.util.UUID;


public record BookingDTO(
    UUID userId,
    Long listingId,
    LocalDate checkIn,
    LocalDate checkOut
) {
    
}
