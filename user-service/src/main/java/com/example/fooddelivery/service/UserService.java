package com.example.fooddelivery.service;

import com.example.fooddelivery.dto.CreateUserRequest;
import com.example.fooddelivery.dto.UpdateUserRequest;
import com.example.fooddelivery.dto.UserDto;
import com.example.fooddelivery.entity.PasswordCredential;
import com.example.fooddelivery.entity.User;
import com.example.fooddelivery.entity.UserProfile;
import com.example.fooddelivery.exception.AppException;
import com.example.fooddelivery.exception.ErrorCode;
import com.example.fooddelivery.repository.PasswordCredentialRepository;
import com.example.fooddelivery.repository.UserProfileRepository;
import com.example.fooddelivery.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordCredentialRepository passwordCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuditLogger auditLogger;

    @Autowired
    public UserService(UserRepository userRepository, UserProfileRepository userProfileRepository,
                       PasswordCredentialRepository passwordCredentialRepository, @Lazy PasswordEncoder passwordEncoder,
                       CustomAuditLogger auditLogger) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.passwordCredentialRepository = passwordCredentialRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditLogger = auditLogger;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Integer id) {
        return mapToUserDto(getUserEntityById(id));
    }

    public UserDto getUserByEmail(String email) {
        return mapToUserDto(getUserEntityByEmail(email));
    }

    @Transactional
    public UserDto createUser(CreateUserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setAdditionalRoles(request.getAdditionalRoles());

        PasswordCredential passwordCredential = new PasswordCredential();
        passwordCredential.setPassword(passwordEncoder.encode(request.getPassword()));
        passwordCredential.setAccountNonExpired(true);
        passwordCredential.setAccountNonLocked(true);
        passwordCredential.setCredentialsNonExpired(true);
        passwordCredential.setEnabled(!request.isRequireAccountActivation()); // Enable account based on account activation setting
        passwordCredential.setLastPasswordResetDate(new Date());
        passwordCredential.setUser(user);

        User savedUser = userRepository.save(user);
        passwordCredentialRepository.save(passwordCredential);

        UserProfile userProfile = new UserProfile();
        userProfile.setFirstname(request.getFirstname());
        userProfile.setLastname(request.getLastname());
        userProfile.setUser(savedUser);

        userProfileRepository.save(userProfile);

        auditLogger.logSecurityEvent("User created: " + user.getEmail());

        return mapToUserDto(savedUser);
    }

    @Transactional
    public UserDto updateUser(Integer id, UpdateUserRequest request) {
        User user = getUserEntityById(id);
        UserProfile userProfile = user.getUserProfile();

        userProfile.setFirstname(request.getFirstname());
        userProfile.setLastname(request.getLastname());

        userProfileRepository.save(userProfile);

        user.setRole(request.getRoles().stream().findFirst().orElse(user.getRole()));
        user.setAdditionalRoles(request.getRoles());

        User updatedUser = userRepository.save(user);

        auditLogger.logSecurityEvent("User updated: " + user.getEmail());

        return mapToUserDto(updatedUser);
    }

    public void deleteUser(Integer id) {
        User user = getUserEntityById(id);
        user.getPasswordCredential().setEnabled(false); // Soft deletion
        userRepository.save(user);
        auditLogger.logSecurityEvent("User deactivated: " + user.getEmail());
    }

    public void changePassword(Integer id, String newPassword) {
        User user = getUserEntityById(id);
        PasswordCredential passwordCredential = user.getPasswordCredential();
        passwordCredential.setPassword(passwordEncoder.encode(newPassword));
        passwordCredential.setLastPasswordResetDate(new Date());
        passwordCredential.setCredentialsNonExpired(true);
        passwordCredentialRepository.save(passwordCredential);
        auditLogger.logSecurityEvent("Password changed for user: " + user.getEmail());
    }

    public void lockUser(Integer id) {
        User user = getUserEntityById(id);
        PasswordCredential passwordCredential = user.getPasswordCredential();
        passwordCredential.setAccountNonLocked(false);
        passwordCredentialRepository.save(passwordCredential);
        auditLogger.logSecurityEvent("User locked: " + user.getEmail());
    }

    public void unlockUser(Integer id) {
        User user = getUserEntityById(id);
        PasswordCredential passwordCredential = user.getPasswordCredential();
        passwordCredential.setAccountNonLocked(true);
        passwordCredentialRepository.save(passwordCredential);
        auditLogger.logSecurityEvent("User unlocked: " + user.getEmail());
    }

    public void expireAccount(Integer id) {
        User user = getUserEntityById(id);
        PasswordCredential passwordCredential = user.getPasswordCredential();
        passwordCredential.setAccountNonExpired(false);
        passwordCredentialRepository.save(passwordCredential);
        auditLogger.logSecurityEvent("User account expired: " + user.getEmail());
    }

    public void unexpireAccount(Integer id) {
        User user = getUserEntityById(id);
        PasswordCredential passwordCredential = user.getPasswordCredential();
        passwordCredential.setAccountNonExpired(true);
        passwordCredentialRepository.save(passwordCredential);
        auditLogger.logSecurityEvent("User account unexpired: " + user.getEmail());
    }

    public void activateAccount(Integer id) {
        User user = getUserEntityById(id);
        PasswordCredential passwordCredential = user.getPasswordCredential();
        passwordCredential.setEnabled(true);
        passwordCredentialRepository.save(passwordCredential);
        auditLogger.logSecurityEvent("User account activated: " + user.getEmail());
    }

    private User getUserEntityById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_FOUND, "User not found with id: " + id));
    }

    private User getUserEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_FOUND, "User not found with email: " + email));
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        return userDto;
    }
}