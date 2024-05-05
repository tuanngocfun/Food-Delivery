package com.foodzy.web.az.common.iab_category.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Ngoc
 * @since 1.0.0
 */
@Entity
@Table
public class SubIabCategory {

    @ManyToOne
    @JoinColumn(name = "iab_category_code", nullable = false, updatable = false)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private IabCategory iabCategory;

    @Id
    @Column(name = "code", columnDefinition = "TEXT", nullable = false)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private String code;

    @Column(name = "name", columnDefinition = "TEXT")
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private String name;
}
