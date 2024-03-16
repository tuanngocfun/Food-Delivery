package com.example.fooddelivery.controller;

import com.example.fooddelivery.dto.CreateUserRequest;
import com.example.fooddelivery.dto.UpdateUserRequest;
import com.example.fooddelivery.dto.UserDto;
import com.example.fooddelivery.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserRequest request) {
        UserDto createdUserDto = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Integer id, @RequestBody UpdateUserRequest request) {
        UserDto updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable Integer id, @RequestBody String newPassword) {
        userService.changePassword(id, newPassword);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/lock")
    public ResponseEntity<Void> lockUser(@PathVariable Integer id) {
        userService.lockUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/unlock")
    public ResponseEntity<Void> unlockUser(@PathVariable Integer id) {
        userService.unlockUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/expire")
    public ResponseEntity<Void> expireAccount(@PathVariable Integer id) {
        userService.expireAccount(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/unexpire")
    public ResponseEntity<Void> unexpireAccount(@PathVariable Integer id) {
        userService.unexpireAccount(id);
        return ResponseEntity.noContent().build();
    }
}