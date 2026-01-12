package com.core.mall.order.dao;

import com.core.mall.common.dao.BaseDao;
import com.core.mall.order.entity.OrderItemEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Mapper
public interface OrderItemDao extends BaseDao<OrderItemEntity> {
	
}