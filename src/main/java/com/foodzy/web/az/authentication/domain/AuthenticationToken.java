package com.foodzy.web.az.authentication.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foodzy.web.az.core.partner.domain.Partner;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author Ngoc
 * @since 1.0.0
 */
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationToken implements Serializable {

//    @Getter
//    @Setter
//    @JsonProperty("account")
//    private Account account;

    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    @JsonProperty("authority")
    private Authority authority;

    @Getter
    @Setter
    @JsonProperty("partner")
    private Partner partner;

    @Getter
    @JsonProperty("userAccessToken")
    private String userAccessToken;

    @Getter
    @JsonProperty("userAccessTokenArena")
    private String userAccessTokenArena;

    @Getter
    @JsonProperty("userUuid")
    private UUID userUuid;
}
