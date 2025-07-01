package com.adityasharma.rentdrive.service;

import com.adityasharma.rentdrive.entity.Car;
import com.adityasharma.rentdrive.repository.BookingRepository;
import com.adityasharma.rentdrive.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final BookingRepository bookingRepository;

    public Car addCar(Car car) {
        car.setAvailable(true);
        return carRepository.save(car);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public List<Car> getAvailableCars() {
        return carRepository.findByAvailableTrue();
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    /**
     * Deletes a car only if it has no associated bookings.
     * @param id the car ID
     * @return true if deleted, false if it has existing bookings
     */
    public boolean deleteCar(Long id) {
        if (bookingRepository.existsByCarId(id)) {
            return false; // Car has existing bookings, do not delete
        }
        carRepository.deleteById(id);
        return true;
    }

    public void updateAvailability(Long id, boolean available) {
        carRepository.findById(id).ifPresent(car -> {
            car.setAvailable(available);
            carRepository.save(car);
        });
    }
}