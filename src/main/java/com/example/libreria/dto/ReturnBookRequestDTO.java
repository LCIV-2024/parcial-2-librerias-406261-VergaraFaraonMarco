package com.example.libreria.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnBookRequestDTO {
    
    @NotNull(message = "La fecha de devolución es obligatoria")
    private LocalDate returnDate;
}

