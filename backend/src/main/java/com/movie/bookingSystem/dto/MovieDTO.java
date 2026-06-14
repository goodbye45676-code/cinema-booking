package com.movie.bookingSystem.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MovieDTO {
    private Long id;
    private String title;
    private String imageUrl;
    private String location;
    private Integer totalSeats;
    private Integer availableSeats;
    private BigDecimal price;
    private LocalDateTime showTime;
    private String description;
}
