package com.myridehub.MyRideHub.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.myridehub.MyRideHub.model.Booking;
import com.myridehub.MyRideHub.repository.BookingRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getBookingsByCustomerName(String customerName) {
        return bookingRepository.findByCustomerName(customerName);
    }

    public List<Booking> getBookingsByVehicleId(Long vehicleId) {
        return bookingRepository.findByVehicleId(vehicleId);
    }

    public List<Booking> getBookingsByStatus(String status) {
        return bookingRepository.findByStatus(status);
    }

    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking updateBookingStatus(Long id, String status) {
        Booking booking = bookingRepository.findById(id).orElseThrow();
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}