package com.example.libreria.controller;

import com.example.libreria.dto.ReservationRequestDTO;
import com.example.libreria.dto.ReservationResponseDTO;
import com.example.libreria.dto.ReturnBookRequestDTO;
import com.example.libreria.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    
    private final ReservationService reservationService;
    
    @PostMapping
    public ResponseEntity<ReservationResponseDTO> createReservation(
            @Valid @RequestBody ReservationRequestDTO requestDTO) {
        ReservationResponseDTO reservation = reservationService.createReservation(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservationById(@PathVariable Long id) {
        ReservationResponseDTO reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }
    
    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
        List<ReservationResponseDTO> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByUserId(@PathVariable Long userId) {
        List<ReservationResponseDTO> reservations = reservationService.getReservationsByUserId(userId);
        return ResponseEntity.ok(reservations);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<ReservationResponseDTO>> getActiveReservations() {
        List<ReservationResponseDTO> reservations = reservationService.getActiveReservations();
        return ResponseEntity.ok(reservations);
    }
    
    @GetMapping("/overdue")
    public ResponseEntity<List<ReservationResponseDTO>> getOverdueReservations() {
        List<ReservationResponseDTO> reservations = reservationService.getOverdueReservations();
        return ResponseEntity.ok(reservations);
    }
    
    @PostMapping("/{id}/return")
    public ResponseEntity<ReservationResponseDTO> returnBook(
            @PathVariable Long id,
            @Valid @RequestBody ReturnBookRequestDTO returnRequest) {
        ReservationResponseDTO reservation = reservationService.returnBook(id, returnRequest);
        return ResponseEntity.ok(reservation);
    }
}

