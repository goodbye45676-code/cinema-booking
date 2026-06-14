package com.movie.bookingSystem.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.movie.bookingSystem.model.Booking;
import com.movie.bookingSystem.model.Ticket;
import com.movie.bookingSystem.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket generateTicket(Booking booking) {
        String qrContent = "BOOKING-ID:" + booking.getId()
                + "|USER:" + booking.getUser().getEmail()
                + "|MOVIE:" + booking.getMovie().getTitle()
                + "|SEATS:" + booking.getSeatNumbers();

        String qrBase64 = generateQrCodeBase64(qrContent);

        Ticket ticket = new Ticket();
        ticket.setBooking(booking);
        ticket.setQrCode(qrBase64);

        return ticketRepository.save(ticket);
    }

    public Ticket getTicketByBookingId(Long bookingId) {
        return ticketRepository.findByBookingId(bookingId).orElse(null);
    }

    private String generateQrCodeBase64(String text) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 250, 250);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);

            return "data:image/png;base64,"
                    + Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }
}