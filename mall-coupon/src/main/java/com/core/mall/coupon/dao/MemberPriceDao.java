package com.core.mall.coupon.dao;

import com.core.mall.common.dao.BaseDao;
import com.core.mall.coupon.entity.MemberPriceEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品会员价格
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Mapper
public interface MemberPriceDao extends BaseDao<MemberPriceEntity> {
	
}