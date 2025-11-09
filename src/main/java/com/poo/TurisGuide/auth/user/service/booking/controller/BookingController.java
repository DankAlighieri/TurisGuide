package com.poo.TurisGuide.auth.user.service.booking.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poo.TurisGuide.auth.user.model.UserModel;
import com.poo.TurisGuide.auth.user.repository.UserRepository;
import com.poo.TurisGuide.auth.user.service.booking.dto.BookingDTO;
import com.poo.TurisGuide.auth.user.service.booking.model.BookingModel;
import com.poo.TurisGuide.auth.user.service.booking.service.BookingService;

import java.util.Objects;

import jakarta.validation.Valid;
import lombok.Data;

@RestController
@Data
@RequestMapping("/booking")
public class BookingController {
    private final BookingService bookingService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<BookingModel> createBooking(@RequestBody @Valid BookingDTO bookingDTO){
        var newBooking = new BookingModel();

        UserModel user = userRepository.findById(Objects.requireNonNull(bookingDTO.userId()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        BeanUtils.copyProperties(bookingDTO, newBooking);

        newBooking = bookingService.createBooking(newBooking);

        return ResponseEntity.ok(newBooking);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBooking(@RequestBody @Valid BookingDTO bookingDTO) {
        var booking = new BookingModel();

        BeanUtils.copyProperties(bookingDTO, booking);

        bookingService.deleteBooking(booking);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
