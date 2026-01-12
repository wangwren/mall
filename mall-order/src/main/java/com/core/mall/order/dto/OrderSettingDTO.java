package com.core.mall.order.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 订单配置信息
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Data
public class OrderSettingDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;

	private Integer flashOrderOvertime;

	private Integer normalOrderOvertime;

	private Integer confirmOvertime;

	private Integer finishOvertime;

	private Integer commentOvertime;

	private Integer memberLevel;


}
