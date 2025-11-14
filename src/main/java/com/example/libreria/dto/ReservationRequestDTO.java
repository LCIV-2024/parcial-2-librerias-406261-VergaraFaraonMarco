package com.example.libreria.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDTO {
    
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;
    
    @NotNull(message = "El ID externo del libro es obligatorio")
    private Long bookExternalId;
    
    @NotNull(message = "Los días de alquiler son obligatorios")
    @Positive(message = "Los días de alquiler deben ser positivos")
    private Integer rentalDays;
    
    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate startDate;
}

