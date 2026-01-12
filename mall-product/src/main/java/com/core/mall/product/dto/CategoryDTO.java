package com.core.mall.product.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 商品三级分类
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-26
 */
@Data
public class CategoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long catId;

	private String name;

	private Long parentCid;

	private Integer catLevel;

	private Integer showStatus;

	private Integer sort;

	private String icon;

	private String productUnit;

	private Integer productCount;

	/**
	 * 当前分类下的所有子类
	 */
	private List<CategoryDTO> childrens;


}
