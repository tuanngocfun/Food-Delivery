package featureaz.foodzy.orderservice.util;

import featureaz.foodzy.orderservice.dto.OrderItem;
import featureaz.foodzy.orderservice.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private UUID id;

    private Integer userId;

    private Integer totalPrice;

    private String imageUrl;

    private List<OrderItem> orderItems;

    private Status status;

    private Boolean useDelivery;

    private Integer shipperId;

    private String userAddress;

    private String userName;

    private String phoneNumber;
}