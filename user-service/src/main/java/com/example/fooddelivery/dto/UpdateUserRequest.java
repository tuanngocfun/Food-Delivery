package com.example.fooddelivery.dto;

import com.example.fooddelivery.config.authorization.Role;
import java.util.Set;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private String firstname;
    private String lastname;
    private Set<Role> roles; // For updating user roles
}
