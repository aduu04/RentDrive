package com.adityasharma.rentdrive.repository;

import com.adityasharma.rentdrive.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByAvailableTrue();
}
