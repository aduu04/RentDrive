package com.adityasharma.rentdrive.controller;

import com.adityasharma.rentdrive.dto.RegisterRequest;
import com.adityasharma.rentdrive.entity.User;
import com.adityasharma.rentdrive.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequest request, Model model) {
        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .role(request.getRole())
                .build();

        userService.registerUser(user);
        model.addAttribute("success", "Registration successful. Please login.");
        return "auth/login";
    }

    @GetMapping("/")
    public String homePage() {
        return "index";  
    }

    // Redirect after successful login
//    @GetMapping("/dashboard")
//    public String dashboardRedirect(Authentication authentication) {
//        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
//            return "redirect:/admin/dashboard";
//        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER"))) {
//            return "redirect:/customer/dashboard";
//        }
//        return "redirect:/login?error";
//    }
}
