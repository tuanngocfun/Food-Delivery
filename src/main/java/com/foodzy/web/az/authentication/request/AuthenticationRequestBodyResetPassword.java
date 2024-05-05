package com.foodzy.web.az.authentication.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationRequestBodyResetPassword {

    @Getter
    @JsonProperty("email")
    private String email;
}
