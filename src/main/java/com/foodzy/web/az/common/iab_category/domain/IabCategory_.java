package com.foodzy.web.az.common.iab_category.domain;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * @author Ngoc
 * @since 1.0.0
 */
@StaticMetamodel(IabCategory.class)
public class IabCategory_ {

    public static volatile SingularAttribute<IabCategory, Boolean> blacklisted;
    public static volatile SingularAttribute<IabCategory, String> code;
    public static volatile SingularAttribute<IabCategory, String> name;
    public static volatile ListAttribute<IabCategory, SubIabCategory> subIabCategories;
}
