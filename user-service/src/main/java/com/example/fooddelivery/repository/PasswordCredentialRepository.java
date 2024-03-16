package com.example.fooddelivery.repository;

import com.example.fooddelivery.entity.PasswordCredential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordCredentialRepository extends JpaRepository<PasswordCredential, Integer> {
}