package com.poo.TurisGuide.booking.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.poo.TurisGuide.auth.user.model.UserModel;
import com.poo.TurisGuide.booking.model.BookingModel;
import com.poo.TurisGuide.booking.repository.BookingRepository;
import com.poo.TurisGuide.payment.strategy.PaymentStrategy;

import jakarta.transaction.Transactional;
import lombok.Data;

@Service
@Data
public class BookingService {
    
    private final BookingRepository bookingRepository;
    // Injeta todas as estratégias disponíveis num Map
    private final Map<String, PaymentStrategy> paymentStrategies;

    @Transactional
    public BookingModel createBooking(BookingModel booking, String metodoPagamento) {
        // 1. Identificar estratégia
        PaymentStrategy strategy = paymentStrategies.get(metodoPagamento);
        if (strategy == null) {
            throw new IllegalArgumentException("Método de pagamento inválido");
        }

        // 2. Processar pagamento
        boolean pago = strategy.processarPagamento(booking.getListing().getValor());
        
        if (!pago) {
            throw new RuntimeException("Falha no pagamento");
        }

        // 3. Salvar reserva
        return bookingRepository.save(Objects.requireNonNull(booking));
    }

    @Transactional
    public void deleteBooking(UUID bookingId) {
        bookingRepository.findById(Objects.requireNonNull(bookingId));
    }

    public List<BookingModel> findByUser(UserModel user){

        return bookingRepository.findByUser(user);
    }
}
