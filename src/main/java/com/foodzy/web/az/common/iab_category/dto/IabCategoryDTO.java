package com.foodzy.web.az.common.iab_category.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ngoc
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IabCategoryDTO {

    @Getter
    @Setter
    @JsonProperty(required = true, value = "code")
    private String code;

    @Getter
    @Setter
    @JsonProperty("name")
    private String name;
}
