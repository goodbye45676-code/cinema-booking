package com.movie.bookingSystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "movie_id" , nullable = false)
    private Movie movie;

    @Column(name = "seat_numbers")
    private String seatNumbers;

    @Column(name = "total_price" , nullable = false , precision = 10 , scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "booking_data")
    private LocalDateTime bookingDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    public enum Status{
        PAID , PENDING , CANCELLED
    }

}
