package com.core.mall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.core.mall.common.service.impl.CrudServiceImpl;
import com.core.mall.coupon.dao.SpuBoundsDao;
import com.core.mall.coupon.dto.SpuBoundsDTO;
import com.core.mall.coupon.entity.SpuBoundsEntity;
import com.core.mall.coupon.service.SpuBoundsService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 商品spu积分设置
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Service
public class SpuBoundsServiceImpl extends CrudServiceImpl<SpuBoundsDao, SpuBoundsEntity, SpuBoundsDTO> implements SpuBoundsService {

    @Override
    public QueryWrapper<SpuBoundsEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<SpuBoundsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}