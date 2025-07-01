package com.adityasharma.rentdrive.service;

import com.adityasharma.rentdrive.entity.Booking;
import com.adityasharma.rentdrive.entity.BookingStatus;
import com.adityasharma.rentdrive.entity.Car;
import com.adityasharma.rentdrive.entity.User;
import com.adityasharma.rentdrive.repository.BookingRepository;
import com.adityasharma.rentdrive.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;

    public Booking createBooking(Booking booking) {
        long days = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
        double cost = days * booking.getCar().getRentPerDay();
        booking.setTotalCost(cost);

        // ✅ Set status as PENDING
        booking.setStatus(BookingStatus.PENDING);

        // ❌ Do NOT mark car as unavailable yet — wait for admin approval
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByCustomer(User customer) {
        return bookingRepository.findByCustomer(customer);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public void cancelBooking(Long id) {
        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        bookingOpt.ifPresent(booking -> {
            // If booking was confirmed, mark car available again
            if (booking.getStatus() == BookingStatus.CONFIRMED) {
                booking.getCar().setAvailable(true);
                carRepository.save(booking.getCar());
            }
            bookingRepository.delete(booking);
        });
    }

    // ✅ Approve Booking
    public boolean approveBooking(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setStatus(BookingStatus.CONFIRMED);
            booking.getCar().setAvailable(false);
            carRepository.save(booking.getCar());
            bookingRepository.save(booking);
            return true;
        }
        return false;
    }

    // ✅ Reject Booking
    public boolean rejectBooking(Long bookingId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            bookingRepository.deleteById(bookingId);
            return true;
        }
        return false;
    }
}
