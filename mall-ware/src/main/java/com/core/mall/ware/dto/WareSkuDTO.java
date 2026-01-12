package com.core.mall.ware.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 商品库存
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Data
public class WareSkuDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;

	private Long skuId;

	private Long wareId;

	private Integer stock;

	private String skuName;

	private Integer stockLocked;


}
