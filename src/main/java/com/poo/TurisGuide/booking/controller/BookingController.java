package com.poo.TurisGuide.booking.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poo.TurisGuide.auth.user.model.UserModel;
import com.poo.TurisGuide.auth.user.repository.UserRepository;
import com.poo.TurisGuide.booking.dto.BookingDTO;
import com.poo.TurisGuide.booking.model.BookingModel;
import com.poo.TurisGuide.booking.service.BookingService;
import com.poo.TurisGuide.catalog.model.ListingModel;
import com.poo.TurisGuide.catalog.repository.ListingRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import jakarta.validation.Valid;
import lombok.Data;

@RestController
@Data
@RequestMapping("/booking")
public class BookingController {
    private final BookingService bookingService;
    private final UserRepository userRepository;
    private final ListingRepository listingRepository;

    @PostMapping
    public ResponseEntity<BookingModel> createBooking(@RequestBody @Valid BookingDTO bookingDTO){
        var newBooking = new BookingModel();

        
        UserModel user = userRepository.findById(Objects.requireNonNull(bookingDTO.userId()))
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        ListingModel listing = listingRepository.findById(Objects.requireNonNull(bookingDTO.listingId()))
                .orElseThrow(() -> new RuntimeException("Listing not found"));
        
        BeanUtils.copyProperties(bookingDTO, newBooking);

        newBooking.setListing(listing);
        newBooking.setUser(user);

        newBooking = bookingService.createBooking(newBooking);

        return ResponseEntity.ok(newBooking);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> deleteBooking(@PathVariable UUID bookingId) {
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<BookingDTO>> getBookingById(@PathVariable UUID userId) {
        var user = userRepository.findById(Objects.requireNonNull(userId))
            .orElseThrow(() -> new RuntimeException()); 
        return ResponseEntity.ok(bookingService.findByUser(user));
    }
}
