package com.example.fooddelivery.config.authorization;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    // Customer permissions
    CUSTOMER_READ_PROFILE("customer:read_profile"),
    CUSTOMER_EDIT_PROFILE("customer:edit_profile"),
    CUSTOMER_PLACE_ORDER("customer:place_order"),
    CUSTOMER_CANCEL_ORDER("customer:cancel_order"),
    CUSTOMER_ADD_PAYMENT_METHOD("customer:add_payment"),
    CUSTOMER_RATE_REVIEW("customer:rate_review"),
    CUSTOMER_TRACK_DELIVERY("customer:track_delivery"),
    
    // Delivery personnel permissions
    DELIVERY_READ_PROFILE("delivery:read_profile"),
    DELIVERY_EDIT_PROFILE("delivery:edit_profile"),
    DELIVERY_ACCEPT_ORDER("delivery:accept_order"),
    DELIVERY_UPDATE_STATUS("delivery:update_status"),
    DELIVERY_MANAGE_AVAILABILITY("delivery:manage_availability"),
    
    // Restaurant owner permissions
    RESTAURANT_MANAGE_PROFILE("restaurant:manage_profile"),
    RESTAURANT_MANAGE_MENU("restaurant:manage_menu"),
    RESTAURANT_MANAGE_ORDERS("restaurant:manage_orders"),
    RESTAURANT_VIEW_ANALYTICS("restaurant:view_analytics"),
    
    // Admin permissions
    ADMIN_MANAGE_USERS("admin:manage_users"),
    ADMIN_MANAGE_ORDERS("admin:manage_orders"),
    ADMIN_MANAGE_PROMOTIONS("admin:manage_promotions"),
    ADMIN_VIEW_REPORTS("admin:view_reports");

    @Getter
    private final String permission;
}
