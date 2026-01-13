package com.core.mall.product.service;

import com.core.mall.common.page.PageData;
import com.core.mall.common.service.CrudService;
import com.core.mall.product.dto.CategoryDTO;
import com.core.mall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-26
 */
public interface CategoryService extends CrudService<CategoryEntity, CategoryDTO> {

    List<CategoryDTO> pageWithTree(Map<String, Object> params);

    void deleteBatch(Long[] ids);
}