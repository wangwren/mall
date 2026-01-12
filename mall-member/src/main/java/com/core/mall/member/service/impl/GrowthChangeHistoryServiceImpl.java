package com.core.mall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.core.mall.common.service.impl.CrudServiceImpl;
import com.core.mall.member.dao.GrowthChangeHistoryDao;
import com.core.mall.member.dto.GrowthChangeHistoryDTO;
import com.core.mall.member.entity.GrowthChangeHistoryEntity;
import com.core.mall.member.service.GrowthChangeHistoryService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 成长值变化历史记录
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@Service
public class GrowthChangeHistoryServiceImpl extends CrudServiceImpl<GrowthChangeHistoryDao, GrowthChangeHistoryEntity, GrowthChangeHistoryDTO> implements GrowthChangeHistoryService {

    @Override
    public QueryWrapper<GrowthChangeHistoryEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<GrowthChangeHistoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(id), "id", id);

        return wrapper;
    }


}