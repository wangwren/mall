package com.core.mall.product.service;

import com.core.mall.common.service.CrudService;
import com.core.mall.product.dto.BrandDTO;
import com.core.mall.product.entity.BrandEntity;

import java.util.List;

/**
 * 品牌
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-26
 */
public interface BrandService extends CrudService<BrandEntity, BrandDTO> {

    List<BrandDTO> getAll();

}