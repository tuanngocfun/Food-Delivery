package featureaz.foodzy.orderservice.entity;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NaturalId;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Order implementation for the order service.
 *
 * @author Ngoc
 * @version 1.0.0
 * @since 1.0.0
 */
@Entity(name = "orders")
@Table(name = "orders")
@Cacheable(false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @Column(name = "order_id", columnDefinition = "BINARY(16)", nullable = false)
    @Getter
    private UUID id;

    @Column(name = "user_id")
    @Getter
    @Setter
    private Integer userId;

    @Column(name = "shipper_id")
    @Getter
    @Setter
    private Integer shipperId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Getter
    @Setter
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    @Getter
    @Setter
    private PaymentType paymentType;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    @Getter
    @Setter
    private Date createdAt;

    @Column(name = "image_url", columnDefinition = "TEXT")
    @Getter
    @Setter
    private String imageUrl;

    @Column(name = "total_price")
    @Getter
    @Setter
    private Integer totalPrice;

    @Column(name = "use_delivery", columnDefinition = "BOOLEAN")
    @Getter
    @Setter
    private Boolean useDelivery;

    /**
     * Constructs a new Order with given identifiers.
     *
     * @param id the UUID of the order
     */
    public Order(UUID id) {
        setId(id); // Set the ID using the setter for additional encapsulation and checks.
        this.createdAt = new Date();
    }

    public void setId(UUID id) {
        if (id == null || StringUtils.isBlank(id.toString())) {
            throw new IllegalArgumentException("Order ID cannot be null or empty.");
        }
        this.id = id;
    }

}
/**
 * Payment types available for an order.
 */
public enum PaymentType {
    CASH,
    MOMO
}

/**
 * Status types available for an order.
 */
public enum Status {
    NEW,
    COOKING,
    DELIVERY,
    DONE
}