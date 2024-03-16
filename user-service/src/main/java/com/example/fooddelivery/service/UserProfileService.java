package com.example.fooddelivery.service;

import org.springframework.stereotype.Service;

import com.example.fooddelivery.dto.UserProfileDto;
import com.example.fooddelivery.entity.UserProfile;
import com.example.fooddelivery.exception.AppException;
import com.example.fooddelivery.exception.ErrorCode;
import com.example.fooddelivery.repository.UserProfileRepository;

@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public UserProfileDto getUserProfileById(Integer id) {
        return userProfileRepository.findById(id)
                .map(this::mapToUserProfileDto)
                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_FOUND, "User profile not found with id: " + id));
    }

    private UserProfileDto mapToUserProfileDto(UserProfile userProfile) {
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setId(userProfile.getId());
        userProfileDto.setFirstname(userProfile.getFirstname());
        userProfileDto.setLastname(userProfile.getLastname());
        return userProfileDto;
    }
}
