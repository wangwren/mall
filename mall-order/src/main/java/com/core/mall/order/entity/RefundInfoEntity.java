package com.core.mall.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 退款信息
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Data
@TableName("oms_refund_info")
public class RefundInfoEntity {

    /**
     * id
     */
	private Long id;
    /**
     * 退款的订单
     */
	private Long orderReturnId;
    /**
     * 退款金额
     */
	private BigDecimal refund;
    /**
     * 退款交易流水号
     */
	private String refundSn;
    /**
     * 退款状态
     */
	private Integer refundStatus;
    /**
     * 退款渠道[1-支付宝，2-微信，3-银联，4-汇款]
     */
	private Integer refundChannel;
    /**
     * 退款备注/内容
     */
	private String refundContent;
}