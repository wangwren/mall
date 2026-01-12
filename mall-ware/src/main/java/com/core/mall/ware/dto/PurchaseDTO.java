package com.core.mall.ware.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * 采购信息
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Data
public class PurchaseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;

	private Long assigneeId;

	private String assigneeName;

	private String phone;

	private Integer priority;

	private Integer status;

	private Long wareId;

	private BigDecimal amount;

	private Date createTime;

	private Date updateTime;


}
