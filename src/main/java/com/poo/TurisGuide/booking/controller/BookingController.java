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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.Data;

@RestController
@Data
@RequestMapping("/booking")
@Tag(name = "Reservas", description = "Gerenciamento de reservas e agendamentos")
@SecurityRequirement(name = "bearerAuth")
public class BookingController {
    private final BookingService bookingService;
    private final UserRepository userRepository;
    private final ListingRepository listingRepository;

    @PostMapping
    @Operation(summary = "Criar reserva", 
               description = "Cria uma nova reserva para um usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reserva criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Usuário ou listing não encontrado"),
        @ApiResponse(responseCode = "401", description = "Token inválido")
    })
    public ResponseEntity<BookingModel> createBooking(
            @Parameter(description = "Dados da reserva") 
            @RequestBody @Valid BookingDTO bookingDTO){
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
    @Operation(summary = "Deletar reserva", 
               description = "Remove uma reserva do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Reserva não encontrada"),
        @ApiResponse(responseCode = "401", description = "Token inválido")
    })
    public ResponseEntity<Void> deleteBooking(
            @Parameter(description = "ID da reserva") 
            @PathVariable UUID bookingId) {
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Buscar reservas do usuário", 
               description = "Lista todas as reservas de um usuário específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reservas encontradas"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "401", description = "Token inválido")
    })
    public ResponseEntity<List<BookingDTO>> getBookingById(
            @Parameter(description = "ID do usuário") 
            @PathVariable UUID userId) {
        var user = userRepository.findById(Objects.requireNonNull(userId))
            .orElseThrow(() -> new RuntimeException()); 
        return ResponseEntity.ok(bookingService.findByUser(user));
    }
}
