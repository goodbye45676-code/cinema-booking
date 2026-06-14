package com.movie.bookingSystem.repository;

import com.movie.bookingSystem.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByBookingId(Long bookingId);
}
