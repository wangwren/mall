package com.core.mall.member.dao;

import com.core.mall.common.dao.BaseDao;
import com.core.mall.member.entity.MemberEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Mapper
public interface MemberDao extends BaseDao<MemberEntity> {
	
}