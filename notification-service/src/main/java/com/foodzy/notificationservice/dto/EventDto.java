package com.foodzy.notificationservice.dto;

import java.util.UUID;
import java.util.Objects;

/**
 * Data transfer object representing an event related to orders.
 *
 * This record encapsulates details about an event, ensuring data consistency
 * and immutability.
 * 
 * @author Ngoc
 * @version 1.0.0
 * @since 1.0.0
 */
public record EventDto(UUID orderId, Integer userId, String message) {

    /**
     * Constructs a new EventDto with the specified details. Validates input to ensure
     * that no null values are accepted as valid arguments.
     *
     * @param orderId the UUID of the order associated with this event
     * @param userId the ID of the user associated with this event
     * @param message the message describing the event
     * @throws IllegalArgumentException if any argument is null or message is empty
     */
    public EventDto {
        if (Objects.isNull(orderId)) {
            throw new IllegalArgumentException("Order ID cannot be null.");
        }
        if (Objects.isNull(userId)) {
            throw new IllegalArgumentException("User ID cannot be null.");
        }
        if (Objects.isNull(message) || message.isBlank()) {
            throw new IllegalArgumentException("Message cannot be blank.");
        }
    }

    @Override
    public String toString() {
        return String.format("EventDto[Order ID=%s, User ID=%d, Message='%s']", orderId, userId, message);
    }
}
