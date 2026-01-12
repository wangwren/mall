package com.core.mall.coupon.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 首页轮播广告
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Data
public class HomeAdvDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;

	private String name;

	private String pic;

	private Date startTime;

	private Date endTime;

	private Integer status;

	private Integer clickCount;

	private String url;

	private String note;

	private Integer sort;

	private Long publisherId;

	private Long authId;


}
