package com.core.mall.member.controller;

import com.core.mall.common.page.PageData;
import com.core.mall.common.utils.Result;
import com.core.mall.common.validator.AssertUtils;
import com.core.mall.common.validator.ValidatorUtils;
import com.core.mall.common.validator.group.AddGroup;
import com.core.mall.common.validator.group.DefaultGroup;
import com.core.mall.common.validator.group.UpdateGroup;
import com.core.mall.member.dto.MemberStatisticsInfoDTO;
import com.core.mall.member.service.MemberStatisticsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 会员统计信息
 *
 * @author wangweiren wangwren0906@gmail.com
 * @since 1.0.0 2025-12-29
 */
@RestController
@RequestMapping("member/memberstatisticsinfo")
public class MemberStatisticsInfoController {
    @Autowired
    private MemberStatisticsInfoService memberStatisticsInfoService;

    @GetMapping("page")
    public Result<PageData<MemberStatisticsInfoDTO>> page(@Parameter(hidden = true) @RequestParam Map<String, Object> params){
        PageData<MemberStatisticsInfoDTO> page = memberStatisticsInfoService.page(params);

        return new Result<PageData<MemberStatisticsInfoDTO>>().ok(page);
    }

    @GetMapping("{id}")
    public Result<MemberStatisticsInfoDTO> get(@PathVariable("id") Long id){
        MemberStatisticsInfoDTO data = memberStatisticsInfoService.get(id);

        return new Result<MemberStatisticsInfoDTO>().ok(data);
    }

    @PostMapping
    public Result save(@RequestBody MemberStatisticsInfoDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        memberStatisticsInfoService.save(dto);

        return new Result();
    }

    @PutMapping
    public Result update(@RequestBody MemberStatisticsInfoDTO dto){
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);

        memberStatisticsInfoService.update(dto);

        return new Result();
    }

    @DeleteMapping
    public Result delete(@RequestBody Long[] ids){
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");

        memberStatisticsInfoService.delete(ids);

        return new Result();
    }


}
