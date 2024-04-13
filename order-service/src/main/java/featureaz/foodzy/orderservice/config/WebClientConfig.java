package featureaz.foodzy.orderservice.config;

import featureaz.foodzy.orderservice.client.UserServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {
    @Autowired
    private LoadBalancedExchangeFilterFunction filterFunction;


    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://user-service")
                .filter(filterFunction)
                .build();
    }

    @Bean
    public UserServiceClient userServiceClient() {
        HttpServiceProxyFactory httpServiceProxyFactory
                = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(webClient()))
                .build();
        return httpServiceProxyFactory.createClient(UserServiceClient.class);
    }
}