package featureaz.foodzy.orderservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

/**
 * Represents a user in the system, encapsulating essential user information.
 * This record serves as an immutable data carrier for user properties.
 *
 * @author Ngoc
 * @version 1.0.0
 * @since 1.0.0
 */
@Entity
@Table(name = "users")
public record User(
    @Id
    @Column(name = "user_id", nullable = false)
    @Getter Integer id,

    @Column(name = "username", nullable = false, unique = true, columnDefinition = "TEXT")
    @Getter String username,

    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    @Getter String password,

    @Column(name = "address", columnDefinition = "TEXT")
    @Getter String address,

    @Column(name = "phone_number", columnDefinition = "TEXT")
    @Getter String phoneNumber
) {
    public User(Integer id, String username, String password, String address, String phoneNumber) {
        if (id == null || username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            address == null || address.trim().isEmpty() ||
            phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("All fields must be non-null and non-empty.");
        }
        this.id = id;
        this.username = username;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}