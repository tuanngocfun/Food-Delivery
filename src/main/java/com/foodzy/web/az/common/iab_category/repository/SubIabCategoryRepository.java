package com.foodzy.web.az.common.iab_category.repository;

import com.foodzy.web.az.common.iab_category.domain.SubIabCategory;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * @author Ngoc
 * @since 1.0.0
 */
@Repository
public interface SubIabCategoryRepository extends
        PagingAndSortingRepository<SubIabCategory, String>,
        JpaSpecificationExecutor<SubIabCategory>,
        Serializable {

}
