package com.foodzy.web.az.authentication.domain;

public enum Authority {
    // Core advertising roles
    ADVERTISING_MANAGER,
    CAMPAIGN_PLANNER,
    CONTENT_CREATOR,
    ADVERTISER,
    AFFILIATE,
    AGENCY,
    ANALYST,
    USER,
    // Support and Operational Roles
    PUBLISHER,
    SALES,
    TRADER,
    // High-Level Oversight Roles
    MEDIA_BUYING_SPECIALIST,
    MEDIA_MANAGER,
    //Technical and Security Roles
    SUPER_USER,
    SYSTEM_ADMIN,
    // Partner and External Collaboration Roles
    PARTNER_ADMIN,
    PARTNER_USER,
    //Customizable and Specialized Roles
    EVENT_COORDINATOR;

    public String getRole() {
        return String.format("ROLE_%s", this.name());
    }
}
