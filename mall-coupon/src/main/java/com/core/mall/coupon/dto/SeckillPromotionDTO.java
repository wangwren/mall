package com.core.mall.coupon.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 秒杀活动
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Data
public class SeckillPromotionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;

	private String title;

	private Date startTime;

	private Date endTime;

	private Integer status;

	private Date createTime;

	private Long userId;


}
