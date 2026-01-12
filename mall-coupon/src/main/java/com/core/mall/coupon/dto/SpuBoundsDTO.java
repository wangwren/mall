package com.core.mall.coupon.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * 商品spu积分设置
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Data
public class SpuBoundsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;

	private Long spuId;

	private BigDecimal growBounds;

	private BigDecimal buyBounds;

	private Integer work;


}
