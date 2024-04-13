package featureaz.foodzy.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.util.UUID;

/**
 * Data transfer object for events related to order processing.
 * This class is used to encapsulate details about order events.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventDto {
    @Getter @Setter
    private UUID orderId;

    @Getter @Setter
    private Integer userId;

    @Getter @Setter
    private String message;
}
