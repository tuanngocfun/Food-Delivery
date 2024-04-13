package featureaz.foodzy.orderservice.client;

import featureaz.foodzy.orderservice.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/users/{userId}")
    User getUserInfo(@PathVariable("userId") Integer userId);
}
