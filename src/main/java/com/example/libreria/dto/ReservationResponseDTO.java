package com.example.libreria.dto;

import com.example.libreria.model.Reservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDTO {
    
    private Long id;
    private Long userId;
    private String userName;
    private Long bookExternalId;
    private String bookTitle;
    private Integer rentalDays;
    private LocalDate startDate;
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
    private BigDecimal dailyRate;
    private BigDecimal totalFee;
    private BigDecimal lateFee;
    private Reservation.ReservationStatus status;
    private LocalDateTime createdAt;
}

