package com.poo.TurisGuide.auth.user.service.booking.service;

import org.springframework.stereotype.Service;

import com.poo.TurisGuide.auth.user.service.booking.model.BookingModel;
import com.poo.TurisGuide.auth.user.service.booking.repository.BookingRepository;

import jakarta.transaction.Transactional;
import lombok.Data;

@Service
@Data
public class BookingService {
    
    private final BookingRepository bookingRepository;

    @Transactional
    public BookingModel createBooking(BookingModel booking) {
        return bookingRepository.save(booking);
    }

    @Transactional
    public void deleteBooking(BookingModel booking) {
        booking = bookingRepository.findById(booking.getId()).orElseThrow(() -> new RuntimeException());
        bookingRepository.delete(booking);
    }
}
