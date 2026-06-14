package com.movie.bookingSystem.dto;

import lombok.Data;
import java.util.List;

@Data
public class BookingRequest {
    private Long movieId;
    private List<String> seatNumbers;
}