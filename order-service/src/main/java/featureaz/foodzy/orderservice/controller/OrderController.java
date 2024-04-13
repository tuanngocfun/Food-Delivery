package featureaz.foodzy.orderservice.controller;

import featureaz.foodzy.orderservice.client.UserServiceClient;
import featureaz.foodzy.orderservice.dto.OrderItem;
import featureaz.foodzy.orderservice.entity.*;
import featureaz.foodzy.orderservice.service.OrderDishService;
import featureaz.foodzy.orderservice.service.OrderService;
import featureaz.foodzy.orderservice.util.OrderRequest;
import featureaz.foodzy.orderservice.util.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {
    private final OrderService orderService;

    private final OrderDishService orderDishService;

    private final ModelMapper mapper;

    private final UserServiceClient userServiceClient;


    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<Order> allOrders = orderService.getAllOrders();
        var result = getDishesItemsForDishes(allOrders);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/active")
    public ResponseEntity<List<OrderResponse>> getActiveOrders() {
        List<Order> activeOrders = orderService.getActiveOrdersForVendor();
        var result = getDishesItemsForDishes(activeOrders);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/delivery")
    public ResponseEntity<List<OrderResponse>> getAllDeliveryOrders() {
        List<Order> allDeliveryOrders = orderService.getAllOrdersForShipper();
        var result = getDishesItemsForDishes(allDeliveryOrders);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/user-service")
    public User testWithUserServiceClient() {
        return userServiceClient.getUserInfo(2);
    }

    @GetMapping("/orders/users/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersForUser(@PathVariable Integer userId) {
        List<Order> ordersByUserId = orderService.getOrdersByUserId(userId);

        var result = getDishesItemsForDishes(ordersByUserId);
        return ResponseEntity.ok(result);
    }


    @PostMapping
    public ResponseEntity<String> createNewOrder(@RequestBody OrderRequest request) {

        UUID orderId = UUID.randomUUID();

        var newOrder = Order.builder()
                .id(orderId)
                .userId(request.getUserId())
                .status(Status.NEW)
                .createdAt(new Date())
                .imageUrl(request.getImageUrl())
                .totalPrice(request.getTotalPrice())
                .paymentType(PaymentType.valueOf(request.getPaymentType()))
                .useDelivery(request.getUseDelivery())
                .build();

        for (OrderItem orderItem : request.getOrderItems()) {
            OrderDish orderDish = OrderDish.builder()
                    .orderId(newOrder.getId())
                    .dishName(orderItem.getDishName())
                    .quantity(orderItem.getQuantity())
                    .build();

            orderDishService.createNewOrderDish(orderDish);
        }

        orderService.createNewOrder(newOrder);

        return ResponseEntity.ok("create order successfully");
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable UUID orderId) {
        orderService.updateOrderStatus(orderId);
        return ResponseEntity.ok("update order status successfully");
    }

    @PutMapping("/{orderId}/{shipperId}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable UUID orderId, @PathVariable Integer shipperId) {
        orderService.setShipperIdForOrder(shipperId, orderId);
        return ResponseEntity.ok("update order status successfully");
    }


    private List<OrderResponse> getDishesItemsForDishes(List<Order> orders) {
        List<OrderResponse> result = new ArrayList<OrderResponse>();

        for (Order order : orders) {
            UUID orderId = order.getId();
            List<OrderDish> orderDishes = orderDishService.getOrderItemsByOrderId(orderId);

            List<OrderItem> orderItems = orderDishes.stream()
                    .map(orderDish -> mapper.map(orderDish, OrderItem.class))
                    .toList();


            OrderResponse orderResponse = OrderResponse.builder()
                    .id(order.getId())
                    .userId(order.getUserId())
                    .shipperId(order.getShipperId())
                    .orderItems(orderItems)
                    .imageUrl(order.getImageUrl())
                    .totalPrice(order.getTotalPrice())
                    .status(order.getStatus())
                    .useDelivery(order.getUseDelivery())
                    .build();

            var userInfo = userServiceClient.getUserInfo(orderResponse.getUserId());
            System.out.println(userInfo);
            String userAddress = userInfo.address();
            String userPhoneNumber = userInfo.phoneNumber();
            String userName = userInfo.username();
            orderResponse.setUserAddress(userAddress);
            orderResponse.setPhoneNumber(userPhoneNumber);
            orderResponse.setUserName(userName);

            result.add(orderResponse);
        }
        return result;
    }
}