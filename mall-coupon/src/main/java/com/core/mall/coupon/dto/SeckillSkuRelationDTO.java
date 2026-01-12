package com.core.mall.coupon.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * 秒杀活动商品关联
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Data
public class SeckillSkuRelationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;

	private Long promotionId;

	private Long promotionSessionId;

	private Long skuId;

	private BigDecimal seckillPrice;

	private BigDecimal seckillCount;

	private BigDecimal seckillLimit;

	private Integer seckillSort;


}
