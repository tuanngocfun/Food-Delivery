package com.example.fooddelivery.dto;

import com.example.fooddelivery.config.authorization.Role;

import lombok.Data;

@Data
public class UserDto {
    private Integer id;
    private String email;
    private Role role;
}
