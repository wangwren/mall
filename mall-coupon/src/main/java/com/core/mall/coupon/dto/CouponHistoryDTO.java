package com.core.mall.coupon.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 优惠券领取历史记录
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Data
public class CouponHistoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;

	private Long couponId;

	private Long memberId;

	private String memberNickName;

	private Integer getType;

	private Date createTime;

	private Integer useType;

	private Date useTime;

	private Long orderId;

	private Long orderSn;


}
