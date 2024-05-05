package com.foodzy.web.az.authentication.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailStatus {

    @Getter
    @Setter
    @JsonProperty("adminRole")
    private boolean adminRole;

    @Getter
    @Setter
    @JsonProperty("rawStatus")
    private int rawStatus;

    @Getter
    @Setter
    @JsonProperty("samePartner")
    private boolean samePartner;

    @Getter
    @Setter
    @JsonProperty("ssoRegistered")
    private boolean ssoRegistered;

    @Getter
    @Setter
    @JsonProperty("xpoActivated")
    private boolean xpoActivated;

    @Getter
    @Setter
    @JsonProperty("xpoRegistered")
    private boolean xpoRegistered;

    @Getter
    @Setter
    @JsonProperty("xpoInactive")
    private boolean xpoInactive;

    @Getter
    @Setter
    @JsonProperty("owner")
    private boolean owner;
}
