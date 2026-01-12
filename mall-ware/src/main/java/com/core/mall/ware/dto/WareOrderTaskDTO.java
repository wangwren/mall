package com.core.mall.ware.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 库存工作单
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Data
public class WareOrderTaskDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;

	private Long orderId;

	private String orderSn;

	private String consignee;

	private String consigneeTel;

	private String deliveryAddress;

	private String orderComment;

	private Integer paymentWay;

	private Integer taskStatus;

	private String orderBody;

	private String trackingNo;

	private Date createTime;

	private Long wareId;

	private String taskComment;


}
