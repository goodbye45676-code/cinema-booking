package com.movie.bookingSystem.controller;


import com.movie.bookingSystem.dto.BookingRequest;
import com.movie.bookingSystem.dto.BookingResponse;
import com.movie.bookingSystem.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // إنشاء حجز جديد (يشمل الدفع المحاكى + توليد التذكرة)
    @PostMapping
    public ResponseEntity<?> createBooking(Authentication authentication,
                                           @RequestBody BookingRequest request) {
        try {
            String email = authentication.getName();
            BookingResponse response = bookingService.createBooking(email, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // عرض حجوزات المستخدم الحالي
    @GetMapping("/my")
    public ResponseEntity<List<BookingResponse>> getMyBookings(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(bookingService.getUserBookings(email));
    }

    // إلغاء حجز
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelBooking(Authentication authentication, @PathVariable Long id) {
        try {
            String email = authentication.getName();
            bookingService.cancelBooking(email, id);
            return ResponseEntity.ok("Booking cancelled successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
