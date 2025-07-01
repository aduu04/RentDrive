package com.adityasharma.rentdrive.repository;

import com.adityasharma.rentdrive.entity.Booking;
import com.adityasharma.rentdrive.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Used for customer to view their bookings
    List<Booking> findByCustomer(User customer);

    // Used to check if a car is booked before deleting
    boolean existsByCarId(Long carId);
}
