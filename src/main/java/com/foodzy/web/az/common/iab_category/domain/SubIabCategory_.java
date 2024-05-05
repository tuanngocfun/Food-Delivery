package com.foodzy.web.az.common.iab_category.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * @author Ngoc
 * @since 1.0.0
 */
@StaticMetamodel(SubIabCategory.class)
public class SubIabCategory_ {

    public static volatile SingularAttribute<SubIabCategory, String> code;
    public static volatile SingularAttribute<SubIabCategory, IabCategory> iabCategory;
    public static volatile SingularAttribute<SubIabCategory, String> name;
}
