package featureaz.foodzy.orderservice.entity;

import lombok.*;
import jakarta.persistence.*;

import java.util.UUID;

/**
 * OrderDish entity representing the relationship between orders and dishes.
 *
 * @author Ngoc
 * @version 1.0.0
 * @since 1.0.0
 */
@Entity(name = "OrderDish")
@Table(name = "orders_dishes")
@Cacheable(false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderDish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @Getter
    private Integer id;

    @Column(name = "order_id", columnDefinition = "BINARY(16)", nullable = false)
    @Getter
    @Setter
    private UUID orderId;

    @Column(name = "dish_name", columnDefinition = "TEXT")
    @Getter
    @Setter
    private String dishName;

    @Column(name = "quantity")
    @Getter
    @Setter
    private Integer quantity;

    /**
     * Constructs a new OrderDish associated with an order and dish details.
     *
     * @param orderId   the UUID of the order this dish is associated with
     * @param dishName  the name of the dish
     * @param quantity  the quantity of the dish ordered
     */
    public OrderDish(UUID orderId, String dishName, Integer quantity) {
        setOrderId(orderId);
        setDishName(dishName);
        setQuantity(quantity);
    }

    public void setOrderId(UUID orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null.");
        }
        this.orderId = orderId;
    }

    public void setDishName(String dishName) {
        if (dishName == null || dishName.trim().isEmpty()) {
            throw new IllegalArgumentException("Dish name cannot be blank.");
        }
        this.dishName = dishName;
    }

    public void setQuantity(Integer quantity) {
        if (quantity == null || quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }
        this.quantity = quantity;
    }
}
