package com.core.mall.member.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * 会员等级
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Data
public class MemberLevelDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;

	private String name;

	private Integer growthPoint;

	private Integer defaultStatus;

	private BigDecimal freeFreightPoint;

	private Integer commentGrowthPoint;

	private Integer priviledgeFreeFreight;

	private Integer priviledgeMemberPrice;

	private Integer priviledgeBirthday;

	private String note;


}
