package com.core.mall.product.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * spu信息介绍
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-26
 */
@Data
public class SpuInfoDescDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long spuId;

	private String decript;


}
