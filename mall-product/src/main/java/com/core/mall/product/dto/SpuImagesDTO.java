package com.core.mall.product.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * spu图片
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-26
 */
@Data
public class SpuImagesDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	private Long id;

	private Long spuId;

	private String imgName;

	private String imgUrl;

	private Integer imgSort;

	private Integer defaultImg;


}
