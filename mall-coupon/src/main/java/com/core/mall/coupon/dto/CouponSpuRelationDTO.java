package com.core.mall.coupon.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 优惠券与产品关联
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Data
public class CouponSpuRelationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;

	private Long couponId;

	private Long spuId;

	private String spuName;


}
