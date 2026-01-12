package com.core.mall.order.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * 退款信息
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Data
public class RefundInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;

	private Long orderReturnId;

	private BigDecimal refund;

	private String refundSn;

	private Integer refundStatus;

	private Integer refundChannel;

	private String refundContent;


}
