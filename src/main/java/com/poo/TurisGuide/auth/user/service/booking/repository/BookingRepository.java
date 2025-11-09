package com.poo.TurisGuide.auth.user.service.booking.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poo.TurisGuide.auth.user.model.UserModel;
import com.poo.TurisGuide.auth.user.service.booking.model.BookingModel;

@Repository
public interface BookingRepository extends JpaRepository<BookingModel, UUID> {
    List<BookingModel> findByCheckInBetween(LocalDate start, LocalDate end);
    List<BookingModel> findByUser(UserModel user);
}
