package com.movie.bookingSystem.model;



import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Data
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    @Column(name = "qr_code", columnDefinition = "LONGTEXT")
    private String qrCode;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt = LocalDateTime.now();
}