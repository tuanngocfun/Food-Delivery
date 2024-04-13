package featureaz.foodzy.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Builder;

/**
 * Data transfer object for items within an order.
 * This class is used to hold the details about each ordered item,
 * such as the dish name and the quantity ordered.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItem {
    @Getter @Setter
    private String dishName;

    @Getter @Setter
    private Integer quantity;
}
