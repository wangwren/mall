package com.core.mall.product.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 品牌
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-26
 */
@Data
public class BrandDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long brandId;

	private String name;

	private String logo;

	private String descript;

	private Integer showStatus;

	private String firstLetter;

	private Integer sort;


}
