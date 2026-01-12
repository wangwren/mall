package com.core.mall.ware.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * 采购单明细
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Data
public class PurchaseDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;

	private Long purchaseId;

	private Long skuId;

	private Integer skuNum;

	private BigDecimal skuPrice;

	private Long wareId;

	private Integer status;


}
