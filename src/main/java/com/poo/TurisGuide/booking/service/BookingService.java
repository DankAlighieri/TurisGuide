package com.poo.TurisGuide.booking.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.poo.TurisGuide.auth.user.model.UserModel;
import com.poo.TurisGuide.booking.dto.BookingDTO;
import com.poo.TurisGuide.booking.model.BookingModel;
import com.poo.TurisGuide.booking.repository.BookingRepository;

import jakarta.transaction.Transactional;
import lombok.Data;

@Service
@Data
public class BookingService {
    
    private final BookingRepository bookingRepository;

    @Transactional
    public BookingModel createBooking(BookingModel booking) {
        return bookingRepository.save(Objects.requireNonNull(booking));
    }

    @Transactional
    public void deleteBooking(UUID bookingId) {
        bookingRepository.findById(Objects.requireNonNull(bookingId));
    }

    public List<BookingDTO> findByUser(UserModel user){

        return bookingRepository.findByUser(user);
    }
}
