package featureaz.foodzy.orderservice.service;

import featureaz.foodzy.orderservice.dto.EventDto;
import featureaz.foodzy.orderservice.entity.Order;
import featureaz.foodzy.orderservice.entity.Status;
import featureaz.foodzy.orderservice.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaTemplate<String, EventDto> kafkaTemplate;


    public void createNewOrder(Order newOrder) {
        orderRepository.save(newOrder);
    }

    public List<Order> getOrdersByUserId(Integer userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Order> getAllOrdersForShipper() {
        return orderRepository.findByUseDeliveryTrueAndStatus(Status.DELIVERY);
    }

    public List<Order> getActiveOrdersForVendor() {
        return orderRepository.findByStatusNot(Status.DONE);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void updateOrderStatus(UUID orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order toBeUpdatedOrder = optionalOrder.get();
            toBeUpdatedOrder.setStatus(getNewStatusForOrder(toBeUpdatedOrder.getStatus(), toBeUpdatedOrder.getUseDelivery()));
            orderRepository.save(toBeUpdatedOrder);

            //    notifications
            EventDto newEvent = EventDto.builder()
                    .orderId(toBeUpdatedOrder.getId())
                    .userId(toBeUpdatedOrder.getUserId())
                    .message("Order: ..." + orderId.toString().substring(orderId.toString().length() - 6) + " -> current status: " + toBeUpdatedOrder.getStatus().name().toLowerCase())
                    .build();

            kafkaTemplate.send("notifications", newEvent);
        } else {
            throw new EntityNotFoundException("Entity with ID " + orderId + " not found");
        }
    }

    public Status getNewStatusForOrder(Status previousStatus, Boolean useDelivery) {
        return switch (previousStatus) {
            case NEW -> Status.COOKING;
            case COOKING -> {
                if (useDelivery) yield Status.DELIVERY;
                yield Status.DONE;
            }
            case DELIVERY -> Status.DONE;
            default -> throw new IllegalArgumentException("Unknown previousStatus: " + previousStatus);
        };
    }

    public void setShipperIdForOrder(Integer shipperId, UUID orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order toBeUpdatedOrder = optionalOrder.get();
            toBeUpdatedOrder.setShipperId(shipperId);
            orderRepository.save(toBeUpdatedOrder);
        } else {
            throw new EntityNotFoundException("Entity with ID " + orderId + " not found");
        }
    }
    
}