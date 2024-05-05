package com.foodzy.web.az.common.iab_category.service;


import com.foodzy.web.az.common.iab_category.domain.SubIabCategory;
import com.foodzy.web.az.common.iab_category.repository.SubIabCategoryRepository;
import com.foodzy.web.az.core.exception.ItemNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Ngoc
 * @since 1.0.0
 */
@Service
public class SubIabCategoryService {

    private static final Logger logger = LoggerFactory.getLogger(SubIabCategoryService.class);

    @Autowired
    private SubIabCategoryRepository repository;

    @Transactional(readOnly = true)
    public Iterable<SubIabCategory> query(Specification<SubIabCategory> specification) {
        return this.repository.findAll(specification);
    }

    @Transactional(readOnly = true)
    public Page<SubIabCategory> query(Specification<SubIabCategory> specification, Pageable pageable) {
        return this.repository.findAll(specification, pageable);
    }

    @Transactional(readOnly = true)
    public SubIabCategory query(String code) throws IllegalArgumentException {
        if (StringUtils.isBlank(code))
            throw new IllegalArgumentException("Failed to query Sub IAB Category (reason: invalid code).");
        return this.repository.findById(code).orElseThrow(() -> new ItemNotFoundException("Sub IAB category not found."));
    }
}
