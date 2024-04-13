package featureaz.foodzy.orderservice.repository;

import featureaz.foodzy.orderservice.entity.Order;
import featureaz.foodzy.orderservice.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing {@link Order} entities.
 * Extends the {@link JpaRepository} for basic CRUD operations,
 * {@link JpaSpecificationExecutor} for dynamic query construction, and
 * {@link PagingAndSortingRepository} for pagination and sorting capabilities.
 *
 * @author Ngoc
 * @since 1.0.0
 */
public interface OrderRepository extends
        PagingAndSortingRepository<Order, UUID>,
        JpaSpecificationExecutor<Order>,
        Serializable {

    List<Order> findByUserIdOrderByCreatedAtDesc(Integer userId);

    List<Order> findByUseDeliveryTrueAndStatus(Status status);

    List<Order> findByStatusNot(Status status);

    Optional<Order> findById(UUID orderId);
}
