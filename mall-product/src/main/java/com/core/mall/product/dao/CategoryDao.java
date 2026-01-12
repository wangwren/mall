package com.core.mall.product.dao;

import com.core.mall.common.dao.BaseDao;
import com.core.mall.product.entity.CategoryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-26
 */
@Mapper
public interface CategoryDao extends BaseDao<CategoryEntity> {
	
}