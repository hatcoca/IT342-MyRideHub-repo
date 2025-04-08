package com.myridehub.MyRideHub.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myridehub.MyRideHub.model.Booking;
import com.myridehub.MyRideHub.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        return booking.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{name}")
    public ResponseEntity<List<Booking>> getBookingsByCustomerName(@PathVariable String name) {
        return ResponseEntity.ok(bookingService.getBookingsByCustomerName(name));
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<Booking>> getBookingsByVehicleId(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(bookingService.getBookingsByVehicleId(vehicleId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Booking>> getBookingsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(bookingService.getBookingsByStatus(status));
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        try {
            if (booking.getCustomerName() == null || booking.getVehicle() == null || booking.getVehicle().getId() == null) {
                return ResponseEntity.badRequest().body("Missing required fields");
            }
            return ResponseEntity.ok(bookingService.createBooking(booking));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating booking: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long id, @RequestBody Map<String, String> statusMap) {
        String status = statusMap.getOrDefault("status", "").trim();
        if (status.isEmpty()) {
            return ResponseEntity.badRequest().body("Status cannot be empty");
        }

        return bookingService.getBookingById(id)
                .map(existingBooking -> ResponseEntity.ok(bookingService.updateBookingStatus(id, status)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        if (booking.isPresent()) {
            bookingService.deleteBooking(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}