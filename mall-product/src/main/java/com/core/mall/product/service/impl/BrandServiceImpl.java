package com.core.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.core.mall.common.service.impl.CrudServiceImpl;
import com.core.mall.common.utils.ConvertUtils;
import com.core.mall.product.dao.BrandDao;
import com.core.mall.product.dto.BrandDTO;
import com.core.mall.product.entity.BrandEntity;
import com.core.mall.product.service.BrandService;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 品牌
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-26
 */
@Service
public class BrandServiceImpl extends CrudServiceImpl<BrandDao, BrandEntity, BrandDTO> implements BrandService {

    @Autowired
    private BrandDao brandDao;

    @Override
    public QueryWrapper<BrandEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<BrandEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override
    public List<BrandDTO> getAll() {
        List<BrandEntity> brandEntities = brandDao.selectList(null);
        return ConvertUtils.sourceToTarget(brandEntities, BrandDTO.class);
    }


}