package com.adityasharma.rentdrive.dto;

import com.adityasharma.rentdrive.entity.User;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private User.Role role;
}
