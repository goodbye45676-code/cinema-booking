package com.movie.bookingSystem.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookingResponse {
    private Long id;
    private String movieTitle;
    private String movieLocation;
    private LocalDateTime showTime;
    private String seatNumbers;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime bookingDate;
    private String qrCode;
}
