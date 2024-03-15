package com.example.fooddelivery.service;

import com.example.fooddelivery.entity.User;
import com.example.fooddelivery.exception.AppException;
import com.example.fooddelivery.exception.ErrorCode;
import com.example.fooddelivery.repository.UserRepository;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_FOUND, "User not found with id: " + id));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_FOUND, "User not found with email: " + email));
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.enableAccount();
        user.unexpireAccount();
        user.unlockAccount();
        user.unexpireCredentials();
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        User existingUser;
        try {
            existingUser = getUserById(user.getId());
        } catch (AppException e) {
            if (e.getError().getCode() == ErrorCode.ITEM_NOT_FOUND) {
                throw new AppException(ErrorCode.ITEM_NOT_FOUND, "User not found with id: " + user.getId());
            } else {
                throw e;
            }
        }

        existingUser.setFirstname(user.getFirstname());
        existingUser.setLastname(user.getLastname());
        existingUser.setEmail(user.getEmail());
        return userRepository.save(existingUser);
    }

    public void deleteUser(Integer id) {
        User user = getUserById(id);
        user.disableAccount();
        userRepository.save(user);
    }

    public void changePassword(Integer id, String newPassword) {
        User user = getUserById(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.updatePasswordResetDate();
        user.unexpireCredentials();
        userRepository.save(user);
    }

    public void lockUser(Integer id) {
        User user = getUserById(id);
        user.lockAccount();
        userRepository.save(user);
    }

    public void unlockUser(Integer id) {
        User user = getUserById(id);
        user.unlockAccount();
        userRepository.save(user);
    }

    public void expireAccount(Integer id) {
        User user = getUserById(id);
        user.expireAccount();
        userRepository.save(user);
    }

    public void unexpireAccount(Integer id) {
        User user = getUserById(id);
        user.unexpireAccount();
        userRepository.save(user);
    }
}