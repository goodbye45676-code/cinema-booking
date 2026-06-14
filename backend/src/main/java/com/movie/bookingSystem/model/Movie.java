package com.movie.bookingSystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movies")
@Data
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , length = 150)
    private String title;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(length = 150)
    private String location;

    @Column(name = "total_seats" , nullable = false)
    private Integer totalSeats;

    @Column(name = "available_seats" , nullable = false)
    private Integer availableSeats;

    @Column(nullable = false , precision =  10 , scale = 2)
    private BigDecimal price;

    @Column(name = "Show_time")
    private LocalDateTime showTime;

    @Column(columnDefinition = "TEXT")
    private String description;
}
