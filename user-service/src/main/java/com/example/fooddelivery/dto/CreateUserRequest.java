package com.example.fooddelivery.dto;

import java.util.Set;
import com.example.fooddelivery.config.authorization.Role;
import lombok.Data;

@Data
public class CreateUserRequest {
    private String email;
    private String password;
    private Role role;
    private String firstname;
    private String lastname;
    private Set<Role> additionalRoles; // Directly part of the class now
    private boolean requireAccountActivation; // Directly part of the class now
}
