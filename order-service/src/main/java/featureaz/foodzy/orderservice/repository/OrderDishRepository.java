package featureaz.foodzy.orderservice.repository;

import featureaz.foodzy.orderservice.entity.OrderDish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Repository for managing {@link OrderDish} entities.
 * Extends the {@link JpaRepository} for basic CRUD operations,
 * {@link JpaSpecificationExecutor} for dynamic query construction, and
 * {@link PagingAndSortingRepository} for pagination and sorting capabilities.
 *
 * @author Ngoc
 * @since 1.0.0
 */
public interface OrderDishRepository extends
        PagingAndSortingRepository<OrderDish, UUID>,
        JpaSpecificationExecutor<OrderDish>,
        Serializable {

    List<OrderDish> findByOrderId(UUID orderId);
}
