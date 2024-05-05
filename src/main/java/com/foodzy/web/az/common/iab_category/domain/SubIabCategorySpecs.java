package com.foodzy.web.az.common.iab_category.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

/**
 * @author Ngoc
 * @since 1.0.0
 */
public class SubIabCategorySpecs {

    public static Specification<SubIabCategory> filterByIabCategoryCode(String filterIabCategoryCode) {
        if (StringUtils.isBlank(filterIabCategoryCode))
            return emptyAnd();
        return (root, query, cb) -> cb.equal(root.get(SubIabCategory_.iabCategory).get(IabCategory_.code), filterIabCategoryCode);
    }

    public static Specification<SubIabCategory> filterByCode(String filterCode) {
        if (StringUtils.isBlank(filterCode))
            return emptyAnd();
        return (root, query, cb) -> cb.equal(cb.lower(root.get(SubIabCategory_.code)), filterCode.toLowerCase());
    }

    public static Specification<SubIabCategory> filterByNamePattern(String filterNamePattern) {
        return Optional.ofNullable(filterNamePattern)
                .map(namePattern -> {
                    String trimmedNamePattern = namePattern.replaceAll("^\\s+", "");
                    if (StringUtils.isEmpty(trimmedNamePattern))
                        return emptyAnd();
                    return (Specification<SubIabCategory>) (root, query, cb) -> cb.like(
                            cb.lower(root.get(SubIabCategory_.name)),
                            String.format("%%%s%%", trimmedNamePattern).toLowerCase());
                })
                .orElse(emptyAnd());
    }

    private static Specification<SubIabCategory> emptyAnd() {
        return (root, query, cb) -> cb.and();
    }
}
