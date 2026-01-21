package com.core.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.core.mall.common.page.PageData;
import com.core.mall.common.service.impl.CrudServiceImpl;
import com.core.mall.product.dao.AttrGroupDao;
import com.core.mall.product.dto.AttrGroupDTO;
import com.core.mall.product.entity.AttrGroupEntity;
import com.core.mall.product.service.AttrGroupService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * 属性分组
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-26
 */
@Service
public class AttrGroupServiceImpl extends CrudServiceImpl<AttrGroupDao, AttrGroupEntity, AttrGroupDTO> implements AttrGroupService {

    @Override
    public QueryWrapper<AttrGroupEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");
        String categoryId = (String) params.get("categoryId");

        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        if (StrUtil.isNotBlank(categoryId)) {
            Long categoryIdLong = Long.parseLong(categoryId);
            wrapper.eq("catelog_id", categoryIdLong);
        }

        return wrapper;
    }
}