package com.core.mall.product.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import java.math.BigDecimal;

/**
 * spu信息
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-26
 */
@Data
public class SpuInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;

	private String spuName;

	private String spuDescription;

	private Long catalogId;

	private Long brandId;

	private BigDecimal weight;

	private Integer publishStatus;

	private Date createTime;

	private Date updateTime;


}
