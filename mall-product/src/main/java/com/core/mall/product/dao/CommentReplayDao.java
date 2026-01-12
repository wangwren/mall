package com.core.mall.product.dao;

import com.core.mall.common.dao.BaseDao;
import com.core.mall.product.entity.CommentReplayEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价回复关系
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-26
 */
@Mapper
public interface CommentReplayDao extends BaseDao<CommentReplayEntity> {
	
}