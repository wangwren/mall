package com.core.mall.order.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 订单操作历史记录
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Data
public class OrderOperateHistoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;

	private Long orderId;

	private String operateMan;

	private Date createTime;

	private Integer orderStatus;

	private String note;


}
