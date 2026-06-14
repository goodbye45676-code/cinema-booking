package com.movie.bookingSystem.service;




import com.movie.bookingSystem.dto.BookingRequest;
import com.movie.bookingSystem.dto.BookingResponse;
import com.movie.bookingSystem.model.Booking;
import com.movie.bookingSystem.model.Movie;
import com.movie.bookingSystem.model.Ticket;
import com.movie.bookingSystem.model.User;
import com.movie.bookingSystem.repository.BookingRepository;
import com.movie.bookingSystem.repository.MovieRepository;
import com.movie.bookingSystem.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final TicketService ticketService;

    public BookingService(BookingRepository bookingRepository,
                          MovieRepository movieRepository,
                          UserRepository userRepository,
                          TicketService ticketService) {
        this.bookingRepository = bookingRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.ticketService = ticketService;
    }

    @Transactional
    public BookingResponse createBooking(String userEmail, BookingRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        int requestedSeats = request.getSeatNumbers().size();

        if (requestedSeats == 0) {
            throw new RuntimeException("No seats selected");
        }

        if (movie.getAvailableSeats() < requestedSeats) {
            throw new RuntimeException("Not enough available seats. Only "
                    + movie.getAvailableSeats() + " left");
        }

        movie.setAvailableSeats(movie.getAvailableSeats() - requestedSeats);
        movieRepository.save(movie);

        BigDecimal total = movie.getPrice().multiply(BigDecimal.valueOf(requestedSeats));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setMovie(movie);
        booking.setSeatNumbers(String.join(",", request.getSeatNumbers()));
        booking.setTotalPrice(total);
        booking.setStatus(Booking.Status.PAID);

        booking = bookingRepository.save(booking);
        bookingRepository.flush(); // << يضمن أن الـ id مكتوب فعلياً في DB قبل استخدامه

        Ticket ticket = ticketService.generateTicket(booking);

        return mapToResponse(booking, ticket);
    }

    public List<BookingResponse> getUserBookings(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return bookingRepository.findByUser(user).stream()
                .map(b -> {
                    Ticket ticket = ticketService.getTicketByBookingId(b.getId());
                    return mapToResponse(b, ticket);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelBooking(String userEmail, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Not authorized to cancel this booking");
        }

        if (booking.getStatus() == Booking.Status.CANCELLED) {
            throw new RuntimeException("Booking already cancelled");
        }

        // إعادة المقاعد المتاحة
        Movie movie = booking.getMovie();
        int seatCount = booking.getSeatNumbers().split(",").length;
        movie.setAvailableSeats(movie.getAvailableSeats() + seatCount);
        movieRepository.save(movie);

        booking.setStatus(Booking.Status.CANCELLED);
        bookingRepository.save(booking);
    }

    private BookingResponse mapToResponse(Booking booking, Ticket ticket) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setMovieTitle(booking.getMovie().getTitle());
        response.setMovieLocation(booking.getMovie().getLocation());
        response.setShowTime(booking.getMovie().getShowTime());
        response.setSeatNumbers(booking.getSeatNumbers());
        response.setTotalPrice(booking.getTotalPrice());
        response.setStatus(booking.getStatus().name());
        response.setBookingDate(booking.getBookingDate());
        response.setQrCode(ticket != null ? ticket.getQrCode() : null);
        return response;
    }
}