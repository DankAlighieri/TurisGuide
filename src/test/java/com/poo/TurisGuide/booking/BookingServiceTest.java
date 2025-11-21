package com.poo.TurisGuide.booking;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.poo.TurisGuide.booking.model.BookingModel;
import com.poo.TurisGuide.booking.repository.BookingRepository;
import com.poo.TurisGuide.booking.service.BookingService;
import com.poo.TurisGuide.catalog.model.Hospedagem;
import com.poo.TurisGuide.payment.strategy.PaymentStrategy;
import com.poo.TurisGuide.payment.strategy.PixPayment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private Map<String, PaymentStrategy> paymentStrategies;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void deveProcessarPagamentoPixECriarReserva() {
        // Arrange
        BookingModel booking = new BookingModel();
        Hospedagem hospedagem = new Hospedagem();
        hospedagem.setValor(100.0);
        booking.setListing(hospedagem);

        // Mock da estratégia PIX
        PaymentStrategy pixStrategy = mock(PixPayment.class);
        when(pixStrategy.processarPagamento(100.0)).thenReturn(true);
        
        // Configura o Map para retornar a estratégia quando a chave for "PIX"
        when(paymentStrategies.get("PIX")).thenReturn(pixStrategy);
        
        // Mock do salvamento
        when(bookingRepository.save(any(BookingModel.class))).thenReturn(booking);

        // Act
        BookingModel result = bookingService.createBooking(booking, "PIX");

        // Assert
        assertNotNull(result);
        // Verifica se o método de pagamento foi chamado com o valor correto
        verify(pixStrategy).processarPagamento(100.0);
        // Verifica se salvou no banco
        verify(bookingRepository).save(booking);
    }

    @Test
    void deveLancarErroSePagamentoFalhar() {
        // Arrange
        BookingModel booking = new BookingModel();
        Hospedagem hospedagem = new Hospedagem();
        hospedagem.setValor(200.0);
        booking.setListing(hospedagem);

        PaymentStrategy pixStrategy = mock(PixPayment.class);
        when(pixStrategy.processarPagamento(200.0)).thenReturn(false); // Pagamento falhou
        
        when(paymentStrategies.get("PIX")).thenReturn(pixStrategy);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            bookingService.createBooking(booking, "PIX");
        });

        // Garante que NÃO salvou no banco
        verify(bookingRepository, never()).save(any());
    }
}
