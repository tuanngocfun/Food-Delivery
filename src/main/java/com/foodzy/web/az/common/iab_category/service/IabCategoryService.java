package com.foodzy.web.az.common.iab_category.service;


import com.foodzy.web.az.common.iab_category.domain.IabCategory;
import com.foodzy.web.az.common.iab_category.repository.IabCategoryRepository;
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
public class IabCategoryService {

    private static final Logger logger = LoggerFactory.getLogger(IabCategoryService.class);

    @Autowired
    private IabCategoryRepository repository;

    @Transactional(readOnly = true)
    public Iterable<IabCategory> query(Specification<IabCategory> specification) {
        return this.repository.findAll(specification);
    }

    @Transactional(readOnly = true)
    public Page<IabCategory> query(Specification<IabCategory> specification, Pageable pageable) {
        return this.repository.findAll(specification, pageable);
    }

    @Transactional(readOnly = true)
    public boolean existsById(String code) throws IllegalArgumentException {
        if (StringUtils.isBlank(code))
            throw new IllegalArgumentException("Failed to check IAB category existence (reason: invalid code).");
        return this.repository.existsById(code);
    }

    @Transactional(readOnly = true)
    public IabCategory query(String code) throws IllegalArgumentException {
        if (StringUtils.isBlank(code))
            throw new IllegalArgumentException("Failed to query IAB Category (reason: invalid code).");
        return this.repository.findById(code).orElseThrow(() -> new ItemNotFoundException("IAB category not found."));
    }
}
