package com.core.mall.order.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * 支付信息表
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Data
public class PaymentInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;

	private String orderSn;

	private Long orderId;

	private String alipayTradeNo;

	private BigDecimal totalAmount;

	private String subject;

	private String paymentStatus;

	private Date createTime;

	private Date confirmTime;

	private String callbackContent;

	private Date callbackTime;


}
