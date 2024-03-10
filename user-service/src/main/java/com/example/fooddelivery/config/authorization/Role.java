package com.example.fooddelivery.config.authorization;

import java.util.Set;

import static com.example.fooddelivery.config.authorization.Permission.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
        CUSTOMER(
                Set.of(
                        CUSTOMER_READ_PROFILE,
                        CUSTOMER_EDIT_PROFILE,
                        CUSTOMER_PLACE_ORDER,
                        CUSTOMER_CANCEL_ORDER,
                        CUSTOMER_ADD_PAYMENT_METHOD,
                        CUSTOMER_RATE_REVIEW,
                        CUSTOMER_TRACK_DELIVERY
                )),
        DELIVERY_PERSONNEL(
                Set.of(
                        DELIVERY_READ_PROFILE,
                        DELIVERY_EDIT_PROFILE,
                        DELIVERY_ACCEPT_ORDER,
                        DELIVERY_UPDATE_STATUS,
                        DELIVERY_MANAGE_AVAILABILITY
                )),
        RESTAURANT_OWNER(
                Set.of(
                        RESTAURANT_MANAGE_PROFILE,
                        RESTAURANT_MANAGE_MENU,
                        RESTAURANT_MANAGE_ORDERS,
                        RESTAURANT_VIEW_ANALYTICS
                )),
        ADMIN(
                Set.of(
                        ADMIN_MANAGE_USERS,
                        ADMIN_MANAGE_ORDERS,
                        ADMIN_MANAGE_PROMOTIONS,
                        ADMIN_VIEW_REPORTS,
                        // Admin inherits all other permissions
                        CUSTOMER_READ_PROFILE,
                        CUSTOMER_EDIT_PROFILE,
                        DELIVERY_READ_PROFILE,
                        DELIVERY_EDIT_PROFILE,
                        RESTAURANT_MANAGE_PROFILE,
                        RESTAURANT_MANAGE_MENU
                ));

        @Getter
        private final Set<Permission> permissions;

        public List<SimpleGrantedAuthority> getAuthorities() {
                var authorities = getPermissions()
                        .stream()
                        .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                        .collect(Collectors.toList());
                authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
                return authorities;
        }
}
