package com.core.mall.coupon.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 秒杀活动场次
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Data
public class SeckillSessionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;

	private String name;

	private Date startTime;

	private Date endTime;

	private Integer status;

	private Date createTime;


}
