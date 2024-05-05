package com.foodzy.web.az.common.iab_category.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * @author Ngoc
 * @since 1.0.0
 */
@Entity
@Table
public class IabCategory {

    @Id
    @Column(name = "code", columnDefinition = "TEXT")
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private String code;

    @Column(name = "blacklisted", columnDefinition = "BOOLEAN", nullable = false)
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Boolean blacklisted;

    @Column(name = "name", columnDefinition = "TEXT")
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private String name;

    @OneToMany(mappedBy = "iabCategory")
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private List<SubIabCategory> subIabCategories;
}
