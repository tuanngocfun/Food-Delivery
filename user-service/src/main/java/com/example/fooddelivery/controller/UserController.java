package com.example.fooddelivery.controller;

import com.example.fooddelivery.entity.User;
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
    public ResponseEntity<List<User>> getAllUsers() {
        // Implement logic to fetch all users
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
        user.setId(id);
        return ResponseEntity.ok(userService.updateUser(user));
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