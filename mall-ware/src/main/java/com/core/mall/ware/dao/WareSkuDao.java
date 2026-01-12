package com.core.mall.ware.dao;

import com.core.mall.common.dao.BaseDao;
import com.core.mall.ware.entity.WareSkuEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Mapper
public interface WareSkuDao extends BaseDao<WareSkuEntity> {
	
}