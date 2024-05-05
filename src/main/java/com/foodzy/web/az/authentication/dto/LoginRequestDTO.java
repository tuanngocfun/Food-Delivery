package com.foodzy.web.az.authentication.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequestDTO {

    @Getter
    @Setter
    @JsonProperty(required = true, value = "partner")
    private String partner;

    @Getter
    @Setter
    @JsonProperty(required = true, value = "password")
    private String password;

    @Getter
    @Setter
    @JsonProperty(required = true, value = "username")
    private String username;
}
