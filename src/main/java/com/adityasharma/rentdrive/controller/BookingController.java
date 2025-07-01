package com.adityasharma.rentdrive.controller;

import com.adityasharma.rentdrive.dto.BookingRequest;
import com.adityasharma.rentdrive.entity.Booking;
import com.adityasharma.rentdrive.entity.BookingStatus;
import com.adityasharma.rentdrive.entity.Car;
import com.adityasharma.rentdrive.entity.User;
import com.adityasharma.rentdrive.service.BookingService;
import com.adityasharma.rentdrive.service.CarService;
import com.adityasharma.rentdrive.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class BookingController {

    private final BookingService bookingService;
    private final CarService carService;
    private final UserService userService;

    // 🟢 Show booking form for selected car
    @GetMapping("/customer/book-car/{carId}")
    public String bookCarForm(@PathVariable Long carId, Model model) {
        Optional<Car> car = carService.getCarById(carId);
        if (car.isPresent()) {
            BookingRequest bookingRequest = new BookingRequest();
            bookingRequest.setCarId(carId);
            model.addAttribute("car", car.get());
            model.addAttribute("bookingRequest", bookingRequest);
            return "customer/book_car";
        }
        return "redirect:/customer/dashboard";
    }

    // 🟢 Book car (POST)
    @PostMapping("/customer/book-car")
    public String bookCar(@ModelAttribute BookingRequest bookingRequest, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        Car car = carService.getCarById(bookingRequest.getCarId()).orElseThrow();

        Booking booking = Booking.builder()
                .car(car)
                .customer(user)
                .startDate(bookingRequest.getStartDate())
                .endDate(bookingRequest.getEndDate())
                .status(BookingStatus.PENDING)  // ✅ Default status
                .build();

        bookingService.createBooking(booking);
        return "redirect:/customer/my-bookings";
    }

    // 🟡 View customer bookings
    @GetMapping("/customer/my-bookings")
    public String viewMyBookings(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        model.addAttribute("bookings", bookingService.getBookingsByCustomer(user));
        return "customer/view_bookings";
    }

    // 🔴 Cancel customer booking
    @GetMapping("/customer/cancel-booking/{id}")
    public String cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return "redirect:/customer/my-bookings";
    }

    // 🟡 View all bookings (admin)
    @GetMapping("/admin/view-bookings")
    public String viewAllBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "admin/view_bookings";
    }

    // ✅ Approve booking
    @PostMapping("/admin/approve-booking/{id}")
    public String approveBooking(@PathVariable Long id) {
        bookingService.approveBooking(id);
        return "redirect:/admin/view-bookings";
    }

    // ❌ Reject booking
    @PostMapping("/admin/reject-booking/{id}")
    public String rejectBooking(@PathVariable Long id) {
        bookingService.rejectBooking(id);
        return "redirect:/admin/view-bookings";
    }
    
 // Admin cancel booking
    @GetMapping("/admin/cancel-booking/{id}")
    public String cancelBookingByAdmin(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return "redirect:/admin/view-bookings";
    }

}
