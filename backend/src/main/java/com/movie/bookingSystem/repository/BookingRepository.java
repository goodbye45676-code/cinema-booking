package com.movie.bookingSystem.repository;

import com.movie.bookingSystem.model.Booking;
import com.movie.bookingSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
}
