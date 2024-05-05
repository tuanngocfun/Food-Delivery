package com.foodzy.web.az.common.iab_category.domain;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Ngoc
 * @since 1.0.0
 */
public class IabCategorySpecs {

    public static Specification<IabCategory> filterByBlacklisted(Boolean filterBlacklisted) {
        if (Objects.isNull(filterBlacklisted))
            return emptyAnd();
        return (root, query, cb) -> cb.equal(root.get(IabCategory_.blacklisted), filterBlacklisted);
    }

    public static Specification<IabCategory> filterByCode(String filterCode) {
        if (StringUtils.isBlank(filterCode))
            return emptyAnd();
        return (root, query, cb) -> cb.equal(cb.lower(root.get(IabCategory_.code)), filterCode.toLowerCase());
    }

    public static Specification<IabCategory> filterByNamePattern(String filterNamePattern) {
        return Optional.ofNullable(filterNamePattern)
                .map(namePattern -> {
                    String trimmedNamePattern = namePattern.replaceAll("^\\s+", "");
                    if (StringUtils.isEmpty(trimmedNamePattern))
                        return emptyAnd();
                    return (Specification<IabCategory>) (root, query, cb) -> cb.like(
                            cb.lower(root.get(IabCategory_.name)),
                            String.format("%%%s%%", trimmedNamePattern).toLowerCase());
                })
                .orElse(emptyAnd());
    }

    private static Specification<IabCategory> emptyAnd() {
        return (root, query, cb) -> cb.and();
    }
}
