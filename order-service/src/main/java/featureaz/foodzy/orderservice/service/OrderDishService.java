package featureaz.foodzy.orderservice.service;

import featureaz.foodzy.orderservice.entity.OrderDish;
import featureaz.foodzy.orderservice.repository.OrderDishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderDishService {
    @Autowired
    OrderDishRepository orderDishRepository;

    public void createNewOrderDish(OrderDish newOrderDish) {
        orderDishRepository.save(newOrderDish);
    }

    public List<OrderDish> getOrderItemsByOrderId(UUID orderId) {
        return orderDishRepository.findByOrderId(orderId);
    }
}