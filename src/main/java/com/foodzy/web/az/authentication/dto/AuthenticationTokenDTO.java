package com.foodzy.web.az.authentication.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foodzy.web.az.authentication.domain.Authority;
import com.foodzy.web.az.core.partner.dto.PartnerDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Ngoc
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationTokenDTO {

    @Getter
    @Setter
    @JsonProperty("authority")
    private Authority authority;

    @Getter
    @Setter
    @JsonProperty(required = true, value = "partner")
    private PartnerDTO partner;

    @Getter
    @Setter
    @JsonProperty(required = true, value = "userAccessToken")
    private String userAccessToken;

    @Getter
    @Setter
    @JsonProperty(required = true, value = "userUuid")
    private UUID userUuid;
}
