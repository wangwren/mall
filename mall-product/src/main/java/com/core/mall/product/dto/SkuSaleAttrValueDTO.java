package com.core.mall.product.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * sku销售属性&值
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-26
 */
@Data
public class SkuSaleAttrValueDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;

	private Long skuId;

	private Long attrId;

	private String attrName;

	private String attrValue;

	private Integer attrSort;


}
