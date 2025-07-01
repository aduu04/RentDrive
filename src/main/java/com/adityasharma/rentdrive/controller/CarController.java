package com.adityasharma.rentdrive.controller;

import com.adityasharma.rentdrive.entity.Car;
import com.adityasharma.rentdrive.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class CarController {

    private final CarService carService;

    // ✅ Store uploads in a fixed location
    private static final String UPLOAD_DIR = System.getProperty("user.home") + File.separator + "rentdrive-uploads";

    // ✅ Admin dashboard
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model, @RequestParam(value = "error", required = false) String error) {
        model.addAttribute("cars", carService.getAllCars());
        if ("booked".equals(error)) {
            model.addAttribute("deleteError", "Cannot delete car. It has existing bookings.");
        }
        return "admin/dashboard";
    }

    // ✅ Customer dashboard
    @GetMapping("/customer/dashboard")
    public String customerDashboard(Model model) {
        model.addAttribute("cars", carService.getAvailableCars());
        return "customer/dashboard";
    }

    // ✅ Show add car form
    @GetMapping("/admin/add-car")
    public String addCarPage(Model model) {
        model.addAttribute("car", new Car());
        return "admin/add_car";
    }

    // ✅ Handle car form submission
    @PostMapping("/admin/add-car")
    public String addCar(@ModelAttribute Car car, @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

            // Create uploads dir if it doesn't exist
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Save uploaded image
            File file = new File(uploadDir, filename);
            imageFile.transferTo(file);

            // Set image path
            car.setImageUrl("/uploads/" + filename);
        }

        carService.addCar(car);
        return "redirect:/admin/dashboard";
    }

    // ✅ Attempt car deletion
    @GetMapping("/admin/delete-car/{id}")
    public String deleteCar(@PathVariable Long id) {
        boolean deleted = carService.deleteCar(id);
        if (!deleted) {
            return "redirect:/admin/dashboard?error=booked";
        }
        return "redirect:/admin/dashboard";
    }
}
