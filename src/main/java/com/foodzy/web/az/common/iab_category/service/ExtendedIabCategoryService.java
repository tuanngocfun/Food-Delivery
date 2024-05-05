package com.foodzy.web.az.common.iab_category.service;

import com.foodzy.web.az.common.iab_category.domain.IabCategory;
import com.foodzy.web.az.common.iab_category.repository.IabCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Extends IAB Category service with bulk update capabilities.
 * @author Ngoc
 * @since 1.0.0
 */
@Service
public class ExtendedIabCategoryService extends IabCategoryService {

    private static final Logger logger = LoggerFactory.getLogger(ExtendedIabCategoryService.class);

    @Autowired
    private IabCategoryRepository repository;

    /**
     * Update multiple IAB categories in a single transaction.
     * @param iabCategories List of IAB categories to be updated.
     */
    @Transactional
    public void updateMultiple(List<IabCategory> iabCategories) {
        iabCategories.forEach(category -> {
            if (category.getCode() != null && repository.existsById(category.getCode())) {
                logger.info("Updating IAB Category: " + category.getCode());
                repository.save(category);
            } else {
                logger.error("Failed to update IAB Category: " + category.getCode() + " (not found)");
            }
        });
    }
}
